package de.confuse.console;

public class Logger
{

	public static String consoleName = "System";

	public static void log(String text)
	{
		info(text);
	}

	/**
	 * Formats the given String with the given Objects.<br>
	 * %a - floating point (except BigDecimal) -> Returns Hex output of floating
	 * point number.<br>
	 * %b - Any type -> "true" if non-null, "false" if null<br>
	 * %c - character -> Unicode character<br>
	 * %d - integer (incl. byte, short, int, long, bigint) -> Decimal Integer<br>
	 * %e - floating point -> decimal number in scientific notation<br>
	 * %f - floating point -> decimal number<br>
	 * %g - floating point -> decimal number, possibly in scientific notation
	 * depending on the precision and value.<br>
	 * %h - any type -> Hex String of value from hashCode() method.<br>
	 * %n - none -> Platform-specific line separator.<br>
	 * %o - integer (incl. byte, short, int, long, bigint) -> Octal number<br>
	 * %s - any type -> String value<br>
	 * %t - Date/Time (incl. long, Calendar, Date and TemporalAccessor) -> %t is the
	 * prefix for Date/Time conversions. More formatting flags are needed after
	 * this. See Date/Time conversion below.<br>
	 * %x - integer (incl. byte, short, int, long, bigint) -> Hex string.<br>
	 * 
	 * @param string  The String to format
	 * @param objects The Objects to place into the
	 * @return the formatted String
	 */
	public static String format(String string, Object... objects)
	{
		return String.format(string, objects);
	}

	public static void loading(String text)
	{
		System.out.println("[" + consoleName + " Info] > Loading " + text);
	}

	public static void loaded(String text)
	{
		System.out.println("[" + consoleName + " Info] > Loaded " + text);
	}

	public static void saving(String text)
	{
		System.out.println("[" + consoleName + " Info] > Saving " + text);
	}

	public static void saved(String text)
	{
		System.out.println("[" + consoleName + " Info] > Saved " + text);
	}

	public static void info(String text)
	{
		System.out.println("[" + consoleName + " Info] > " + text);
	}

	public static void error(String text)
	{
		System.out.println("[" + consoleName + " Error] > " + text);
	}
	
	public static void warning(String text)
	{
		System.out.println("[" + consoleName + " Warning] > " + text);
	}

	public static void downloading(String text)
	{
		System.out.println("[" + consoleName + " Info] > Downloading " + text);
	}

	public static void downloaded(String text)
	{
		System.out.println("[" + consoleName + " Info] > Downloaded " + text);
	}

	public static void creating(String text)
	{
		System.out.println("[" + consoleName + " Info] > Creating " + text);
	}

	public static void created(String text)
	{
		System.out.println("[" + consoleName + " Info] > Created " + text);
	}

	public static void spacer()
	{
		System.out.println("[" + consoleName + " Info] > ------------------");
	}

	public static void spacer(String text)
	{
		System.out.println("[" + consoleName + " Info] > --------- " + text + " ---------");
	}

}
