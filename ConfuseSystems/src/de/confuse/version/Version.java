package de.confuse.version;

public class Version
{
	/** The name of the Program */
	private final String name;
	/** The Software version, dependent on iteration */
	private final int version;
	/**
	 * The Build of the Software, meaning the times the Software has been compiled
	 * and tested/shipped
	 */
	private final int build;
	/**
	 * The minor patch of the Software, meaning every change that is not big enough
	 * to increase the {@link #version}.
	 */
	private final int patch;

	public Version(final String name, final int version, final int build, final int patch)
	{
		this.name = name;
		this.version = version;
		this.build = build;
		this.patch = patch;
	}

	public String getFullVersion()
	{
		return "v" + version + " b" + build + " p" + patch;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the version
	 */
	public int getVersion()
	{
		return version;
	}

	/**
	 * @return the build
	 */
	public int getBuild()
	{
		return build;
	}

	/**
	 * @return the patch
	 */
	public int getPatch()
	{
		return patch;
	}

}
