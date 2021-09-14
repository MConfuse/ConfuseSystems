package de.confuse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import de.confuse.confFileV1.ConfFileField;
import de.confuse.confFileV2.ConfFileReaderV2;
import de.confuse.confFileV2.ConfFileValueV2;
import de.confuse.confFileV2.ConfFileWriterV2;
import de.confuse.util.ArrayUtils;
import de.confuse.version.Version;
import static de.confuse.util.ArrayUtils.*;
import static de.confuse.util.StringUtils.*;

public class MainTest
{

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
			ConfFileReaderV2 reader = new ConfFileReaderV2(file);

			ConfFileWriterV2 writer = new ConfFileWriterV2(new File("testWrite.conff"));
			writer.addFields(reader.getFields());
			writer.writeFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
//		try
//		{
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//
//			String line;
//			String prevLine = reader.readLine(); // always contains the line above the current one
//			Object[] layers = new StringBuilder[0]; // array of StringBuilders, for each bracketLayer one
//
//			/*
//			 * Represents the layer of bracket the program is currently in,
//			 * -1 = none/default.
//			 * 
//			 * TODO: Currently only the first bracket layer works properly, recursion is not
//			 * my strong suite yet, lOl!
//			 */
//			int bracketLayer = -1;
//
//			while ((line = reader.readLine()) != null)
//			{
//				if (line.trim().startsWith("#"))
//					continue;
//				/*
//				 * Bracket layering logic, every line that starts with { or } is either
//				 * increasing or decreasing the bracketLayer.
//				 */
//				if (line.trim().startsWith("{"))
//				{
//					bracketLayer++;
//					layers = addElementToArray(layers, new StringBuilder());
//					continue;
//				}
//				else if (line.trim().startsWith("}"))
//				{
//					bracketLayer--;
//					layers = removeElementFromArray(layers, layers[layers.length - 1]);
//					continue;
//				}
//
//				/* Actual reading of the line */
//				if (bracketLayer >= 0)
//				{
//					StringBuilder builder = (StringBuilder) layers[bracketLayer];
//					builder.append(line.trim());
//
//					String temp = line.trim();
//					String name = null; // non null
//					String comment = null; // null able
//					String[] values = new String[0]; // default only one slot
//
//					/* Normal value, format: 'Name: "Value"' */
//					if ((name = temp.split(" ")[0]).endsWith(":"))
//					{
//						// finds the first non marked quotation mark
//						int pos = findFirstMatchingPosition(temp.substring(temp.indexOf("\"") + 1), '\\', '"')
//								+ temp.indexOf("\"");
//
//						// findFirstMatchingPosition returns -1 which is why I'm not including the + 1
//						if (pos == temp.indexOf("\""))
//							pos = temp.length() - 1;
//
//						values = addElementToArray(values, temp.substring(temp.indexOf("\"") + 1, pos));
//						if (temp.length() > pos + 1)
//						{
//							comment = temp.substring(pos).trim();
//						}
//
//						System.out.println(name + " | " + values[0] + " | " + comment);
//					}
//					else if (!(name = temp.split(" ")[0]).endsWith(":"))
//						/* Array value, format: 'Name ["test", "test with \"another test\" here"] */
//					{
//						System.out.println("Array!");
//						int arrStart = findFirstMatchingPosition(temp, 'a', '[');
//						int arrEnd = findFirstMatchingPosition(temp.substring(arrStart + 1), '\\', ']') + arrStart;
//						String arrStr = temp.substring(arrStart, arrEnd);
////						System.out.println(arrStr);
//
//						int totalQuotations = arrStr.split("\"").length;
//						int ignoredQuotations = amountOfMatches(arrStr, "\\\"");
//						int offset = 0;
//
//						System.out.println(arrStr + " | " + totalQuotations + " | " + ignoredQuotations);
//						// loops through the total amount of quotation marks, (total - ignored) / 2 ->
//						// "value" -> 2 per value
//						for (int i = 0; i < (totalQuotations - ignoredQuotations) / 2; i++)
//						{
//							/* finding the first and last index of the value using '"' and ignoring every '\"' */
//							int valStart = findFirstMatchingPosition(arrStr, '\\', '"', offset);
//							int valEnd = findFirstMatchingPosition(arrStr, '\\', '"', offset + 1) - 1;
//							offset += 2;
//
//							// Adding the found value into the array
//							values = addElementToArray(values, arrStr.substring(valStart, valEnd));
//						}
//
//						printArray(values);
//					}
//
//				}
//
////				System.out.println(layers.length + ": " + line);
//				prevLine = line;
//			}
//
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}

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
