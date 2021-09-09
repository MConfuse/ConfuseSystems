package de.confuse.util;

import static de.confuse.util.ArrayUtils.*;

public class StringUtils
{
	public static int findFirstMatchingPosition(String input, char ignore, char val)
	{
		char[] in = input.toCharArray();
		char[] lastChars = new char[] {};

		for (char c : in)
		{
			if (lastChars.length > 0 && lastChars[lastChars.length - 1] != ignore && c == val)
			{
				return lastChars.length;
			}
			else
			{
				lastChars = addElementToArray(lastChars, c);
			}

		}

		return -1;
	}

}
