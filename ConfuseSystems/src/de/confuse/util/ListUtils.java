package de.confuse.util;

import java.util.List;

public class ListUtils
{
	/**
	 * Does what the name suggests
	 * 
	 * @param list The list to convert
	 * @return a new Array identical to the list.
	 */
	public static String[] stringListToArray(List<String> list)
	{
		String[] out = new String[list.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = list.get(i);

		return out;
	}

	/**
	 * Does what the name suggests
	 * 
	 * @param list The list to convert
	 * @return a new Array identical to the list.
	 */
	public static Object[] objectListToArray(List<?> list)
	{
		Object[] out = new Object[list.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = list.get(i);
		
		return out;
	}

}
