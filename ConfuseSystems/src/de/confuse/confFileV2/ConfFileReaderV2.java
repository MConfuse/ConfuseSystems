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
import java.util.Iterator;
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
		Object[] layers = new ConfFileFieldV2[0]; // array of ConfFileFields, for each bracketLayer one

		/*
		 * Represents the layer of bracket the program is currently in, -1 =
		 * none/default.
		 * 
		 * TODO: Currently only the first bracket layer works properly, recursion is not
		 * my strong suite yet, lOl!
		 */
		int bracketLayer = -1;

		while ((line = reader.readLine()) != null)
		{
			if (line.trim().startsWith("#") || line.trim().isEmpty())
				continue;
			System.out.println(" # " + line);

			int braceOpen = findFirstMatchingPosition(line, '\\', '{');
			int braceClose = findFirstMatchingPosition(line, '\\', '}');

			/*
			 * (Usually) true when the given line is a ConfFieldV2 in inline format, still
			 * lots of possible false positives, I think.
			 */
			if (braceOpen != -1 && braceClose != -1)
			{
				// differentiate between old and new inline formatting
				System.out.println(line);
				int nameEnd = findFirstMatchingPosition(line, '\\', ':');
				if (nameEnd == -1) // old confF format
					continue;

				//

				String fieldName;
				if (line.startsWith("Field:"))
					fieldName = line.substring(0, nameEnd);
				else
					fieldName = line.substring(0, braceOpen - 1);
				ConfFileFieldV2 field = new ConfFileFieldV2(fieldName, true);
//				System.out.println(name);

				String temp = line.trim();

				int totalBracketsOpen = amountOfMatches(temp, "(");
//				int totalBracketsOpen = temp.split("\\(").length;
				int totalBracketsClosed = amountOfMatches(temp, ")");
//				int totalBracketsClosed = temp.split("\\)").length;
				int totalValues = 0;
				int valueOffset = 0; // for each value + 1
				int valueStart = 0, valueEnd = 0;

				/*
				 * Checks if there is a unclosed value, if there is it will remove the last
				 * statement in the hopes that this will result in a less crashing way, lOl
				 */
				if (totalBracketsOpen > totalBracketsClosed)
					totalValues = totalBracketsClosed;
				else if (totalBracketsOpen < totalBracketsClosed)
					totalValues = totalBracketsClosed;
				else
					totalValues = totalBracketsClosed; // can be opened too, doesn't matter

				/* Cycles through all available values */
				for (int i = 0; i < totalValues; i++)
				{
					valueStart = findFirstMatchingPosition(temp, '\\', '(', valueOffset);
					valueEnd = findFirstMatchingPosition(temp, '\\', ')', valueOffset) - 1;
					String valueStr = temp.substring(valueStart, valueEnd);
					valueOffset++;
					System.out.println(valueStr);

					String name = null;
					String[] values = new String[0];
					String comment = null;

					/* Normal value, format: 'Name: "Value" # Comment' */
					if ((name = valueStr.split(" ")[0]).endsWith(":"))
					{
						name = name.substring(0, name.length() - 1); // removing the : from the back
						// finds the first non marked quotation mark
						int pos = findFirstMatchingPosition(valueStr.substring(valueStr.indexOf("\"") + 1), '\\', '"')
								+ valueStr.indexOf("\"");

						// findFirstMatchingPosition returns -1 which is why I'm not including the + 1
						if (pos == valueStr.indexOf("\""))
							pos = valueStr.length() - 1;

						values = addElementToArray(values, ConfFileValueV2
								.replaceEscapeMarkers(valueStr.substring(valueStr.indexOf("\"") + 1, pos)));

						// comment extraction
						if (valueStr.length() > pos + 1 && findFirstMatchingPosition(valueStr, '\\', '#') != -1)
							comment = valueStr.substring(pos + 1).trim();

						field.put(new ConfFileValueV2(name, comment, values));
//						System.out.println(name + " | " + values[0] + " | " + comment);
					}
					else if (!(name = valueStr.split(" ")[0]).endsWith(":"))
					/*
					 * Array value, format: 'Name ["test", "test with \"another test\" here"] #
					 * Comment'
					 */
					{
						int arrStart = findFirstMatchingPosition(valueStr, 'a', '[');
						int arrEnd = findFirstMatchingPosition(valueStr.substring(arrStart + 1), '\\', ']') + arrStart;
						String arrStr = valueStr.substring(arrStart, arrEnd);
//						System.out.println(arrStr);

						int totalQuotations = arrStr.split("\"").length;
						int ignoredQuotations = amountOfMatches(arrStr, "\\\"");
						int offset = 0;
						int valStart = 0;
						int valEnd = 0;

//						System.out.println(arrStr + " | " + totalQuotations + " | " + ignoredQuotations);
						/*
						 * loops through the total amount of quotation marks, (total - ignored) / 2 -> /
						 * "value" -> 2 per value
						 */
						for (int k = 0; k < (totalQuotations - ignoredQuotations) / 2; k++)
						{
							/*
							 * finding the first and last index of the value using
							 * '"' and ignoring every '\"'
							 */
							valStart = findFirstMatchingPosition(arrStr, '\\', '"', offset);
							valEnd = findFirstMatchingPosition(arrStr, '\\', '"', offset + 1) - 1;
							offset += 2;

							String value = ConfFileValueV2.replaceEscapeMarkers(arrStr.substring(valStart, valEnd));
							// Adding the found value into the array
							values = addElementToArray(values, value);
						}

						// Comment
						int totalLength = arrStart + arrStr.length() + ("	".length() * bracketLayer) + 2;
						if (totalLength < valueStr.length())
							comment = valueStr.substring(totalLength);

						field.put(name, comment, values);
						System.out.println("Values: " + Arrays.toString(values));
//						printArray(values);
					}

				}

				fields.add(field);
				continue;
			}

			/*
			 * Bracket layering logic, every line that starts with { or } is either
			 * increasing or decreasing the bracketLayer.
			 */
			if (line.trim().startsWith("{"))
			{
				bracketLayer++;
				layers = addElementToArray(layers, new ConfFileFieldV2(prevLine, false));
				continue;
			}
			else if (line.trim().startsWith("}"))
			{
				bracketLayer--;
				if (bracketLayer >= 0) // Adding the nested field into the the field before
					((ConfFileFieldV2) layers[bracketLayer]).put((ConfFileFieldV2) layers[bracketLayer + 1]);
				else
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

				/* Adding a new Field within the current field. */
				if (temp.startsWith("Field:"))
				{
					prevLine = temp.substring("Field: ".length());
					continue;
				}
				/* Normal value, format: 'Name: "Value" # Comment' */
				else if ((name = temp.split(" ")[0]).endsWith(":"))
				{
					name = name.substring(0, name.length() - 1); // removing the : from the back
					// finds the first non marked quotation mark
					int pos = findFirstMatchingPosition(temp.substring(temp.indexOf("\"") + 1), '\\', '"')
							+ temp.indexOf("\"");

					// findFirstMatchingPosition returns -1 which is why I'm not including the + 1
					if (pos == temp.indexOf("\""))
						pos = temp.length() - 1;

					values = addElementToArray(values,
							ConfFileValueV2.replaceEscapeMarkers(temp.substring(temp.indexOf("\"") + 1, pos)));
					if (temp.length() > pos + 1 && findFirstMatchingPosition(temp, '\\', '#') != -1)
						comment = temp.substring(pos + 1).trim();

					field.put(name, comment, values);
//					System.out.println(name + " | " + values[0] + " | " + comment);
				}
				else if (!(name = temp.split(" ")[0]).endsWith(":"))
				/*
				 * Array value, format: 'Name ["test", "test with \"another test\" here"] #
				 * Comment'
				 */
				{
//					System.out.println("Array!");
					int arrStart = findFirstMatchingPosition(temp, 'a', '[');
					int arrEnd = findFirstMatchingPosition(temp.substring(arrStart + 1), '\\', ']') + arrStart;
					String arrStr = temp.substring(arrStart, arrEnd);
//						System.out.println(arrStr);

					int totalQuotations = arrStr.split("\"").length;
					int ignoredQuotations = amountOfMatches(arrStr, "\\\"");
					int offset = 0;
					int valStart = 0;
					int valEnd = 0;

//					System.out.println(arrStr + " | " + totalQuotations + " | " + ignoredQuotations);
					/*
					 * loops through the total amount of quotation marks, (total - ignored) / 2 -> /
					 * "value" -> 2 per value
					 */
					for (int i = 0; i < (totalQuotations - ignoredQuotations) / 2; i++)
					{
						/*
						 * finding the first and last index of the value using
						 * '"' and ignoring every '\"'
						 */
						valStart = findFirstMatchingPosition(arrStr, '\\', '"', offset);
						valEnd = findFirstMatchingPosition(arrStr, '\\', '"', offset + 1) - 1;
						offset += 2;

						String value = ConfFileValueV2.replaceEscapeMarkers(arrStr.substring(valStart, valEnd));
						// Adding the found value into the array
						values = addElementToArray(values, value);
					}

					// Comment
					int totalLength = arrStart + arrStr.length() + ("	".length() * bracketLayer) + 2;
					if (totalLength < line.length())
						comment = line.substring(totalLength);

					field.put(name, comment, values);
//					printArray(values);
				}

			}

//				System.out.println(layers.length + ": " + line);
			prevLine = line;
		}

		reader.close();

		for (ConfFileFieldV2 field : fields)
		{
//			System.out.println(field.getName() + ": " + Arrays.toString(field.getValues().toArray()));
		}

		ConfFileFieldV2 field = getField("test");
		ConfFileFieldV2 nested2 = field.getField("test").getField("nestingTest");
		System.out.println(nested2.getValue("nesting"));
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

	public List<ConfFileFieldV2> getFields()
	{ return this.fields; }

}
