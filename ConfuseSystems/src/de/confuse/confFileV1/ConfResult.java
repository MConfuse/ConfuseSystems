package de.confuse.confFileV1;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A class that's usually created by the {@link ConfFileField} but can also be
 * created manually for special use cases. <br>
 * This class represents one specific value and it's key and is completely
 * unique. An instance of this class cannot be retrieved directly, only the
 * value associated with it can be retrieved from the {@link ConfFileField} it
 * was created by, using the {@link ConfFileField#getValue(String)} method.<br>
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
public class ConfResult {

	/** Represents the name/key of this Field */
	private final String key;
	/** Contains the value for this specific {@link #key} */
	private final String value;

	/**
	 * @param key   The key/name for the {@link #value}
	 * @param value The final value for this {@link #key}
	 */
	public ConfResult(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	/**
	 * @return a formatted version of the {@link #key} and {@link #value}
	 */
	public String getFormattedField()
	{
		return "\"" + key + "\"" + value;
	}

	public String getKey()
	{
		return key;
	}

	public String getValue()
	{
		return value;
	}

}
