package de.confuse.confFileV2;

import static de.confuse.util.ArrayUtils.addElementToArray;
import static de.confuse.util.ArrayUtils.printArray;
import static de.confuse.util.ArrayUtils.removeElementFromArray;
import static de.confuse.util.StringUtils.amountOfMatches;
import static de.confuse.util.StringUtils.findFirstMatchingPosition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfFileReaderV2
{
	private final List<ConfFileFieldV2> fields = new ArrayList<ConfFileFieldV2>();

	public ConfFileReaderV2(File file) throws IOException
	{
		readFileConentent(file);
	}

	private void readFileConentent(File file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String line;
		String prevLine = reader.readLine(); // always contains the line above the current one
		Object[] layers = new ConfFileFieldV2[0]; // array of StringBuilders, for each bracketLayer one

		/*
		 * Represents the layer of bracket the program is currently in,
		 * -1 = none/default.
		 * 
		 * TODO: Currently only the first bracket layer works properly, recursion is not
		 * my strong suite yet, lOl!
		 */
		int bracketLayer = -1;

		while ((line = reader.readLine()) != null)
		{
			if (line.trim().startsWith("#"))
				continue;
			/*
			 * Bracket layering logic, every line that starts with { or } is either
			 * increasing or decreasing the bracketLayer.
			 */
			if (line.trim().startsWith("{"))
			{
				bracketLayer++;
				layers = addElementToArray(layers, new ConfFileFieldV2(prevLine));
				continue;
			}
			else if (line.trim().startsWith("}"))
			{
				bracketLayer--;
				fields.add((ConfFileFieldV2) layers[layers.length - 1]);
				layers = removeElementFromArray(layers, layers[layers.length - 1]);
				continue;
			}

			/* Actual reading of the line */
			if (bracketLayer >= 0)
			{
				ConfFileFieldV2 field = (ConfFileFieldV2) layers[bracketLayer];

				String temp = line.trim();
				String name = null; // non null
				String comment = null; // null able
				String[] values = new String[0]; // default only one slot

				/* Normal value, format: 'Name: "Value"' */
				if ((name = temp.split(" ")[0]).endsWith(":"))
				{
					name = name.substring(0, name.length() - 1); // removing the : from the back
					// finds the first non marked quotation mark
					int pos = findFirstMatchingPosition(temp.substring(temp.indexOf("\"") + 1), '\\', '"')
							+ temp.indexOf("\"");

					// findFirstMatchingPosition returns -1 which is why I'm not including the + 1
					if (pos == temp.indexOf("\""))
						pos = temp.length() - 1;

					values = addElementToArray(values, temp.substring(temp.indexOf("\"") + 1, pos));
					if (temp.length() > pos + 1)
					{
						comment = temp.substring(pos + 1).trim();
					}

					field.put(name, comment, values);
					System.out.println(name + " | " + values[0] + " | " + comment);
				}
				else if (!(name = temp.split(" ")[0]).endsWith(":"))
				/* Array value, format: 'Name ["test", "test with \"another test\" here"] */
				{
					System.out.println("Array!");
					name = name.substring(0, name.length() - 1); // removing the : from the back
					int arrStart = findFirstMatchingPosition(temp, 'a', '[');
					int arrEnd = findFirstMatchingPosition(temp.substring(arrStart + 1), '\\', ']') + arrStart;
					String arrStr = temp.substring(arrStart, arrEnd);
//						System.out.println(arrStr);

					int totalQuotations = arrStr.split("\"").length;
					int ignoredQuotations = amountOfMatches(arrStr, "\\\"");
					int offset = 0;

					System.out.println(arrStr + " | " + totalQuotations + " | " + ignoredQuotations);
					// loops through the total amount of quotation marks, (total - ignored) / 2 ->
					// "value" -> 2 per value
					for (int i = 0; i < (totalQuotations - ignoredQuotations) / 2; i++)
					{
						/*
						 * finding the first and last index of the value using
						 * '"' and ignoring every '\"'
						 */
						int valStart = findFirstMatchingPosition(arrStr, '\\', '"', offset);
						int valEnd = findFirstMatchingPosition(arrStr, '\\', '"', offset + 1) - 1;
						offset += 2;

						// Adding the found value into the array
						values = addElementToArray(values, arrStr.substring(valStart, valEnd));
					}

					field.put(name, comment, values);
					printArray(values);
				}

			}

//				System.out.println(layers.length + ": " + line);
			prevLine = line;
		}

		for (ConfFileFieldV2 field : fields)
		{
			System.out.println(field.getName() + ": " + Arrays.toString(field.getValues().toArray()));
		}

		ConfFileFieldV2 field = getField("test");
		System.out.println("Value: " + field.getValueRaw("val1").getComment());
	}

	/**
	 * Searches through all to the {@link #fields} list added
	 * {@link ConfFileFieldV2}s and returns the one with the matching name.<br>
	 * <strong>This is case sensitive!!</strong>
	 * 
	 * @param name The name to search for.
	 * @return The {@link ConfFileFieldV2} with the matching name or null
	 */
	public ConfFileFieldV2 getField(String name)
	{
		for (ConfFileFieldV2 field : this.fields)
			if (field.getName().equals(name))
				return field;

		return null;
	}

}
