package de.confuse.confFileV2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfFileFieldV2
{
	/**
	 * The name of this field. Used to retrieve it from the
	 * {@link ConfFileReaderV2}.
	 */
	private final String name;
	/**
	 * Contains all {@link ConfFileValueV2}s that are currently available.
	 */
	private final List<ConfFileValueV2> values;

	/**
	 * The {@link ConfFileFieldV2} is one of the base elements of the whole
	 * ConfF-System as it contains a {@link List} of {@link ConfFileValueV2}s which
	 * store the actual data from the {@link File} read by the
	 * {@link ConfFileReaderV2}.<br>
	 * This class and, in this case, this Object is a bridge between the raw
	 * data read from the file and the actual data as the {@link ConfFileReaderV2}
	 * uses it to split the raw data into usable chunks of data which can the be
	 * accessed using the access methods like {@link #getValue(String)} for direct
	 * access or just using {@link #getValues()} and iterating over all of the
	 * {@link #values}.
	 * 
	 * @param name   The name of this Field
	 * @param values A nullable {@link List} of {@link ConfFileValueV2}s
	 */
	public ConfFileFieldV2(final String name, List<ConfFileValueV2> values)
	{
		this.name = name;
		if (values != null)
			this.values = values;
		else
			this.values = new ArrayList<ConfFileValueV2>();
	}

	/**
	 * Instantiates a new {@link ConfFileFieldV2}. This method is the short form of
	 * {@link #ConfFileFieldV2(String, List)} and fills the List parameter with
	 * null.<br>
	 * <br>
	 * The {@link ConfFileFieldV2} is one of the base elements of the whole
	 * ConfF-System as it contains a {@link List} of {@link ConfFileValueV2}s which
	 * store the actual data from the {@link File} read by the
	 * {@link ConfFileReaderV2}.<br>
	 * This class and, in this case, this Object is a bridge between the raw
	 * data read from the file and the actual data as the {@link ConfFileReaderV2}
	 * uses it to split the raw data into usable chunks of data which can the be
	 * accessed using the access methods like {@link #getValue(String)} for direct
	 * access or just using {@link #getValues()} and iterating over all of the
	 * {@link #values}.
	 * 
	 * @param name The name of this Field
	 * @see #ConfFileFieldV2(String, List)
	 */
	public ConfFileFieldV2(String name)
	{
		this(name, null);
	}

	/**
	 * Loops through all {@link ConfFileValueV2}s currently in the {@link #values}
	 * list and retrieves the first {@link ConfFileValueV2}, if there is one, that
	 * matches given key (<strong>case sensitive!</strong>).
	 * 
	 * @param key The key (name) to search for.
	 * @return The first matching {@link ConfFileValueV2} or null
	 */
	public ConfFileValueV2 getValueRaw(String key)
	{
		for (ConfFileValueV2 val : this.values)
			if (val.getKey().equals(key))
				return val;

		return null;
	}

	/**
	 * Loops through all {@link ConfFileValueV2}s currently in the {@link #values}
	 * list and retrieves the first {@link ConfFileValueV2}, if there is one, that
	 * matches given key (<strong>case sensitive!</strong>).
	 * 
	 * @param key The key (name) to search for.
	 * @return The first matching {@link ConfFileValueV2}s value or null
	 * @see ConfFileValueV2#getValue()
	 * @see #getValues(String)
	 */
	public String getValue(String key)
	{
		for (ConfFileValueV2 val : this.values)
			if (val.getKey().equals(key))
				return val.getValue();

		return null;
	}

	/**
	 * Loops through all {@link ConfFileValueV2}s currently in the {@link #values}
	 * list and retrieves the first {@link ConfFileValueV2}, if there is one, that
	 * matches given key (<strong>case sensitive!</strong>).
	 * 
	 * @param key The key (name) to search for.
	 * @return The first matching {@link ConfFileValueV2}s values or null
	 * @see ConfFileValueV2#getValues()
	 * @see #getValue(String)
	 */
	public String[] getValues(String key)
	{
		for (ConfFileValueV2 val : this.values)
			if (val.getKey().equals(key))
				return val.getValues();

		return null;
	}

	/**
	 * Adds the given {@link ConfFileValueV2} to the {@link #values} list.
	 * 
	 * @param val The {@link ConfFileValueV2} to add.
	 * @see #put(String, CharSequence, String...)
	 * @see #put(String, String...)
	 */
	public ConfFileFieldV2 put(ConfFileValueV2 val)
	{
		this.values.add(val);
		return this;
	}

	/**
	 * Creates and adds a {@link ConfFileValueV2} with the given values to the
	 * {@link #values} list.
	 * 
	 * @param key    The key (name) to search for.
	 * @param values The Values to add to the {@link ConfFileValueV2} field.
	 * @see #put(ConfFileValueV2)
	 * @see #put(String, CharSequence, String...)
	 */
	public ConfFileFieldV2 put(String key, String... values)
	{
		return put(new ConfFileValueV2(key, null, values));
	}

	/**
	 * Creates and adds a {@link ConfFileValueV2} with the given values to the
	 * {@link #values} list.
	 * 
	 * @param key     The key (name) to search for.
	 * @param comment The comment that should be attached to the end of the line.
	 * @param values  The Values to add to the {@link ConfFileValueV2} field.
	 * @see #put(ConfFileValueV2)
	 * @see #put(String, String...)
	 */
	public ConfFileFieldV2 put(String key, CharSequence comment, String... values)
	{
		return put(new ConfFileValueV2(key, String.valueOf(comment), values));
	}

	/**
	 * @return the {@link ConfFileFieldV2}s name
	 */
	public String getName()
	{ return this.name; }

	/**
	 * @return all of the values
	 */
	public List<ConfFileValueV2> getValues()
	{ return values; }

}
