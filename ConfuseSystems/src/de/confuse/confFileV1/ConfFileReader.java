package de.confuse.confFileV1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A new type of File reader specifically for the .CONFF formatting. The base
 * functions of this class can be compared to a .JSON reader. <br>
 * <br>
 * When creating a .CONFF file using the {@link ConfFileWriter} you need to add
 * {@link ConfFileField}s in which you specify 3 strings: <br>
 * A name, a key and a value. <br>
 * <br>
 * To use this class correctly and retrieve values out of the written files you
 * have to first create a new instance of this class, feeding it with either the
 * input data or the file containing the data. This will automatically save the
 * contents of the file locally in the instance of this class. <br>
 * To retrieve fields you need to first specify which {@link ConfFileField} you
 * would like to retrieve using the {@link #getField(String)} method, where the
 * input {@link String} is the fields name you specified in the
 * {@link ConfFileField}s Constructor. This is case sensitive so keep that in
 * mind! <br>
 * After having retrieved the Field ({@link ConfResult}) make sure to insert a
 * != null check just to be sure to not run into a {@link NullPointerException}!
 * <br>
 * Now you can search within the retrieved {@link ConfFileField} for a specific key
 * using the {@link ConfFileField#getValue(String)} method. This key is also case
 * sensitive! If the key was found it will return the assigned value in form of
 * a String. <br>
 * <br>
 * <strong>--- Important: ---</strong><br>
 * This {@link ConfFileReader} Supports the following File Versions: <br>
 * 1.0 - 1.1 <br>
 * Newer Versions File Versions might not be readable anymore! <br>
 * <br>
 * Note: Having characters like the following ones within the key or value might
 * cause the retrieval to fail or not return the correct values: "" , { } <br>
 * Also note that in case you clean this instance up without having saved all
 * the needed data to your program you will have to reread the entire file using
 * a new instance of this class!<br>
 * <br>
 * <strong>Version 1.2 Patch notes:</strong><br>
 * - Refactored the whole class to fit the changes made to the
 * {@link ConfResult} and {@link ConfFileField} classes.<br>
 * Main Changes include:<br>
 * -- {@link ConfFileField}s are now replacing the {@link ConfResult} using an
 * overloaded Constructor, all old {@link ConfResult} methods are now included
 * in the {@link ConfFileField} and only function if they were created using the
 * old {@link ConfResult} constructor. The {@link ConfResult} class is now an
 * easy way of storing the keys and values that were previously stored inside a
 * {@link ConcurrentHashMap}.
 * 
 * @version 1.2
 * @author Confuse/Confuse#5117
 *
 */
public class ConfFileReader {

	/** A list containing all results of the input file */
	private List<ConfFileField> result = new ArrayList<ConfFileField>();
	/**
	 * The Version of the {@link ConfFileWriter} that wrote this File, depending on
	 * the Version this reader might not be able to read the content
	 */
	public static double FILE_VERSION = -1D;

	/**
	 * Creates a new instance of this class. Instead of a File that should be read,
	 * this constructor takes a direct string using the .CONFF formatting and splits
	 * it up like usual. This method is intended for special use cases. <br>
	 * <br>
	 * --- Important: --- <br>
	 * With this Constructor you have to set the {@link #FILE_VERSION} manually using
	 * the setter!
	 * 
	 * @param content A {@link String} following the .CONFF formatting
	 * @param version The version of the formatting
	 * @throws Exception Thrown when the {@link #FILE_VERSION} was not specified
	 */
	public ConfFileReader(String content, double version) throws Exception
	{
		FILE_VERSION = version;
		readContent(content);
	}

	/**
	 * Creates a new instance of this class. You can input any file here but keep in
	 * mind this class can only work when the file follows the .CONFF formatting.
	 * 
	 * @param file The {@link File} which is read
	 * @throws Exception This Constructor throws an Exception because of two
	 *                   reasons: <br>
	 *                   Either you did not Specify the {@link #FILE_VERSION} using
	 *                   the {@link #setFileVersion(double)} or there was an error
	 *                   with the specified {@link File}
	 */
	public ConfFileReader(File file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		boolean isHeader = true;
		String line;

		while ((line = reader.readLine()) != null)
		{
			// Checks if the read line is the first line of the File
			if (isHeader)
			{
				isHeader = false;

				// If there is a Version set the Version variable
				if (line.startsWith("ConfFileVersion:"))
				{
					// Retrieves the Version
					String version = line.substring(line.indexOf(":") + 2, line.indexOf("C", 1) - 3);
					FILE_VERSION = Double.parseDouble(version);

					continue;
				}

			}

			readContent(line);
		}

		reader.close();
	}

	/**
	 * Creates the {@link ConfResult}s using the input. <br>
	 * Note: This method only works with correctly formatted Strings.
	 * 
	 * @param paramContent The String that contains the ConfField/s.
	 */
	private void readContent(String paramContent) throws IOException
	{
		if (FILE_VERSION == -1D)
			throw new IOException("File Version was not specified!");

		if (FILE_VERSION < 2)
		{
			String[] lineSplit = paramContent.split(";");

			for (String string : lineSplit)
			{
				// If it is a comment, skip it
				if (string.startsWith("#"))
					continue;

				// Splitting up the different values
				result.add(new ConfFileField(string.substring(0, string.indexOf("{")),
						string.substring(string.indexOf("{") + 1, string.lastIndexOf("}"))));
			}

			return;
		}

		throw new IOException("Unsupported Version! This reader only supports versions 0.1 to 1.9! Input Version: " + FILE_VERSION);
	}

	/**
	 * Returns a {@link ConfFileField} by the fields name.
	 * 
	 * @param name Name of the Field (Case sensitive!)
	 * @return {@link ConfResult} | Null
	 */
	public ConfFileField getField(String name)
	{
		for (ConfFileField field : result)
			if (field.getName().equals(name))
				return field;

		return null;
	}

	public List<ConfFileField> getResult()
	{
		return result;
	}
	
	public List<ConfFileField> getFields()
	{
		return result;
	}

	public double getFileVersion()
	{
		return FILE_VERSION;
	}

	public void setFileVersion(double fileVersion)
	{
		FILE_VERSION = fileVersion;
	}

}
