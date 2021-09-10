package de.confuse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import de.confuse.confFile.ConfFileField;
import de.confuse.confFile.ConfFileReader;
import de.confuse.confFile.ConfFileWriter;
import de.confuse.util.ArrayUtils;
import de.confuse.version.Version;
import static de.confuse.util.ArrayUtils.*;
import static de.confuse.util.StringUtils.*;

public class MainTest {

	public MainTest()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
//		String pathname = "test.conff";
		String pathname = "C:/Users/MrCon/git/ConfuseSystems/ConfuseSystems/test.conff";
		File file = new File(pathname);
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line;
			String prevLine = reader.readLine(); // always contains the line above the current one
			Object[] layers = new StringBuilder[0]; // array of StringBuilders, for each bracketLayer one 

			int bracketLayer = -1; // Represents the layer of bracket the program is currently in, -1 = none/default

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
					layers = addElementToArray(layers, new StringBuilder());
					continue;
				}
				else if (line.trim().startsWith("}"))
				{
					bracketLayer--;
					layers = removeElementFromArray(layers, layers[layers.length - 1]);
					continue;
				}

				/* Actual reading of the line */
				if (bracketLayer >= 0)
				{
					StringBuilder builder = (StringBuilder) layers[bracketLayer];
					builder.append(line.trim());

					String temp = line.trim();
					String name = null; // non null
					String comment = null; // null able
					String[] values = new String[1]; // default only one slot

					/* Normal value, format: 'Name: "Value"' */
					if ((name = temp.split(" ")[0]).endsWith(":"))
					{
						// finds the first non marked quotation mark
						int pos = findFirstMatchingPosition(temp.substring(temp.indexOf("\"") + 1), '\\', '"')
								+ temp.indexOf("\"") + 1;

						// findFirstMatchingPosition returns -1 which is why I'm not including the + 1
						if (pos == temp.indexOf("\""))
							pos = temp.length() - 1;

						values[0] = temp.substring(temp.indexOf("\"") + 1, pos);
						if (temp.length() > pos + 1)
						{
							comment = temp.substring(pos).trim();
						}

						System.out.println(name + " | " + values[0] + " | " + comment);
					}
					else if (!(name = temp.split(" ")[0]).endsWith(":"))
					{
						System.out.println("Array!");
						String arrayStr = "";
						int start = findFirstMatchingPosition(temp, 'a', '[') + 1;
						int end = findFirstMatchingPosition(temp.substring(start + 1), '\'', ']');
						System.out.println(temp.substring(start));
						System.out.println(start + " | " + end);
					}

				}

//				System.out.println(layers.length + ": " + line);
				prevLine = line;
			}
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
//		System.out.println(ArrayUtils.containsConsecutiveValues(new Object[] {"\", "}, "\\".substring(0, 1), '"', ",", " "));
//		String[] arr = {"a", "b", "c"};
//		System.out.println(Arrays.toString(ArrayUtils.addElementToArray(arr, "d")));

//		System.out.println(Arrays.toString(ArrayUtils.splitStringToArray("13. 11. 1998 war ein Guter Tag... Dies das Ananas.", ". ")));
//		System.out.println(ArrayUtils.containsConsecutiveValues(Arrays.asList('a', 'b', 'e', 'a', 'b', 'c'), 'a', 'b', 'c'));

//		ConfFileWriter writer = new ConfFileWriter(new File("yeeeee"), true);
//
//		ConfFileField field = new ConfFileField("Test");
//		field.put("heck", "yea");
//		field.put("fuck", "no");
//		ConfFileField field2 = new ConfFileField("Scam");
//		field2.put("menolike", "scammers");
//		writer.addFields(field, field2);
//		try
//		{
//			writer.writeFile();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		try
//		{
//			ConfFileReader reader = new ConfFileReader(new File("yeeeee.conff"));
//			field = reader.getField("Scam");
//			System.out.println(field.getValue("menolie"));
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
