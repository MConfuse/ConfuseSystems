package de.confuse.util;

public class Position
{
	private double x;
	private double y;

	public Position(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public void setPosition(int x, int y)
	{
		setPosition((double) x, (double) y);
	}

	/**
	 * @return the x
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * @return the x
	 */
	public int geInttX()
	{
		return (int) x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x)
	{
		this.x = x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY()
	{
		return y;
	}

	/**
	 * @return the y
	 */
	public int getIntY()
	{
		return (int) y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y)
	{
		this.y = y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y)
	{
		this.y = y;
	}

}
