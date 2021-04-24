package de.confuse.confFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Depending on which constructor of this Class you use, it will have different
 * properties and use cases.<br>
 * <br>
 * <strong>#1 - The {@link #ConfFileField(String)} Constructor:</strong><br>
 * This constructor represents a field. Within the .CONFF formatting, a field is
 * given a name upon creation and contains all values associated with this name.
 * Values can be manually added to this {@link ConfFileField} using the
 * {@link #put(String, String)} method.<br>
 * Add this instance to a {@link ConfFileWriter} to use and add the values added
 * to this {@link ConfFileField} to/in the {@link ConfFileWriter}. Alternatively
 * the values can be manually retrieved using the {@link #getFormattedField()}
 * method, which will a fully formatted {@link ConfFileField} which can be put
 * into a {@link ConfFileReader} again, to read the data written into this
 * Field.<br>
 * <br>
 * <strong>Note:</strong><br>
 * Will set the {@link #systemField} boolean to false. <br>
 * <br>
 * <strong>#2 - The {@link #ConfFileField(String, String)}
 * Constructor:</strong><br>
 * This constructor is usually used when the {@link ConfFileReader} is reading a
 * new {@link File}. This {@link ConfFileField} is then assigned a name with
 * which it can be retrieved again using the
 * {@link ConfFileReader#getField(String)} method.<br>
 * From here a specific value out of this {@link ConfFileField} can be retrieved
 * by it's name ("key") using the {@link #getValue(String)} method<br>
 * <strong>Note:</strong><br>
 * Will set the {@link #systemField} boolean to false.<br>
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
public class ConfFileField {

	/**
	 * True when this Instance was created by a {@link ConfFileReader}, false if
	 * otherwise. Depending on which value this boolean is, some methods are
	 * unavailable and will only return <code>null</code>, information about the
	 * required boolean value is in each of the methods descriptions in the
	 * "<strong>Note</strong>" section.
	 */
	public final boolean systemField;

	/* --- By user created for use in the ConfFileWriter --- */
	/** The name of this field */
	private final String name;
	/** All {@link ConfResult}s currently contained within this field */
	private List<ConfResult> content = new ArrayList<ConfResult>();

	/* --- Created by ConfFileReader while reading data --- */
	/** A {@link List} containing all {@link ConfResult} */
	private List<ConfResult> values = new ArrayList<ConfResult>();

	/**
	 * Adds a new Field to the File. <br>
	 * Fields are an essential part of this System, they contain the actual
	 * values.<br>
	 * <br>
	 * <strong>Note:</strong><br>
	 * - Having characters like the following ones within the key or value might
	 * cause the retrieval to fail or not return the correct values: "" , { }<br>
	 * - Will set the {@link #systemField} boolean to <strong>false</strong>.
	 * 
	 * @param name Name of the Field
	 */
	public ConfFileField(final String name)
	{
		this.systemField = false;
		this.name = name;
	}

	/**
	 * Will take the input values in the .CONFF format and put them into a
	 * {@link ConcurrentHashMap} for easy data retrieval using the
	 * {@link #getValue(String)} method.<br>
	 * <br>
	 * <strong>Note:</strong><br>
	 * Will set the {@link #systemField} boolean to <strong>true</strong>.
	 * 
	 * @param name   The name of this field
	 * @param values A String in the .CONFF format
	 */
	public ConfFileField(final String name, String values)
	{
		this.systemField = true;
		this.name = name;
		String[] valueArray = values.split(",");

		/*
		 * If there are more than at least one value it will split them up and add them
		 * to the List
		 */
		if (valueArray.length > 1)
			for (String string : valueArray)
			{
				String key = string.substring(string.indexOf("\"") + 1, string.lastIndexOf("\""));
				String val = string.substring(string.lastIndexOf("\"") + 1);

				this.values.add(new ConfResult(key, val));
			}
		else if (!values.equals("")) // Only one value
		{
			String key = values.substring(values.indexOf("\"") + 1, values.lastIndexOf("\""));
			String val = values.substring(values.lastIndexOf("\"") + 1);

			this.values.add(new ConfResult(key, val));
		}
		else // no value
			;

	}

	/**
	 * This method is used to easily add new values with their specific key.<br>
	 * This will also return this {@link ConfFileField}s Instance in case you want
	 * to add something else in one line.<br>
	 * <br>
	 * Note:<br>
	 * - Having characters like the following ones within the key or value might
	 * cause the retrieval to fail or not return the correct values: "" , { }<br>
	 * - Requires the {@link #systemField} boolean to be <strong>false</strong>,
	 * else it will return null.
	 * 
	 * @param key   The key to retrieve the assigned value
	 * @param value The actual value that can be obtained using the assigned key
	 * @return This {@link ConfFileField}s instance
	 */
	public ConfFileField put(String key, String value)
	{
		content.add(new ConfResult(key, value));

		return this;
	}

	/**
	 * Method used by the {@link ConfFileWriter} to write the final file. Pretty
	 * much useless to the normal user.<br>
	 * <br>
	 * <strong>Note:</strong><br>
	 * Requires the {@link #systemField} boolean to be <strong>false</strong>, else
	 * it will return null.
	 * 
	 * @return String with the ConfFile formatting | Null
	 */
	public String getFormattedField()
	{
		if (systemField)
			return null;

		StringBuilder builder = new StringBuilder();

		// Creates the actual content of the field
		int index = 1;
		for (ConfResult result : content)
		{
			builder.append(result.getFormattedField() + (index == content.size() ? "" : ","));
			index++;
		}

		return name + "{" + builder.toString() + "};\r";
	}

	/**
	 * Returns a value using the values key. This method is case sensitive!<br>
	 * <br>
	 * <strong>Note:</strong><br>
	 * Requires the {@link #systemField} boolean to be <strong>true</strong>, else
	 * it will return null.
	 * 
	 * @param key Key (name) of the value
	 * @return String containing the value | Null
	 */
	public String getValue(String key)
	{
		if (!systemField)
			return null;

		for (ConfResult result : values)
			if (result.getKey().equals(key))
				return result.getValue();

		return null;
	}

	/**
	 * Returns all saved {@link #values} in a {@link ConcurrentHashMap}.<br>
	 * <br>
	 * <strong>Note:</strong><br>
	 * Requires the {@link #systemField} boolean to be <strong>true</strong>, else
	 * it will return null.
	 * 
	 * @return a {@link List} containing all stored keys and values | Null
	 */
	public List<ConfResult> getValues()
	{
		if (systemField)
			return values;
		else
			return null;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

}
