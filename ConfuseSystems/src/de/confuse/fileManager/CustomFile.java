package de.confuse.fileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * CustomFiles for easy File handling.
 * 
 * @version 1.5
 * @author Confuse/Confuse#5117
 *         https://github.com/MConfuse/ConfuseSystems
 */
public abstract class CustomFile
{
	private final File file;
	private final String name;
	private final boolean load;

	protected BufferedReader reader;
	protected PrintWriter writer;

	public CustomFile(final String name, final String filePath, final boolean loadOnStart)
	{
		this.name = name;
		this.load = loadOnStart;

		file = new File(filePath, name + FileManager.FILE_TYPE);

		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		else if (load)
		{
			try
			{
				loadFile();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

	}

	public abstract void loadFile() throws IOException;

	public abstract void saveFile() throws IOException;

	/**
	 * Creates a new {@link BufferedReader} using the {@link CustomFile}s
	 * {@link #file}path.<br>
	 * When the {@link BufferedReader} has been created, it can be retrieved by
	 * calling the {@link #reader} field.<br>
	 * <br>
	 * <strong>Note:</strong><br>
	 * You have to manually close the created {@link #reader} object, otherwise
	 * memory leaks can occur.
	 * 
	 * @return The {@link BufferedReader} or null, if the file was not found.
	 */
	protected BufferedReader createReader()
	{
		if (reader != null)
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				System.err.println("[CustomFile Error] > Closing the old BufferedReader failed!");
				e.printStackTrace();
			}

		try
		{
			return reader = new BufferedReader(new FileReader(file));
		}
		catch (FileNotFoundException e)
		{
			System.err.println("[CustomFile Error] > The file could not be found! Returning null!");
			System.err.println("[CustomFile Error] > Stacktrace:");
			System.err.println("[CustomFile Error] > File: " + file.getAbsolutePath() + " | Loaded on init: " + load);
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Creates a new {@link PrintWriter} using the {@link CustomFile}s
	 * {@link #file}path.<br>
	 * When the {@link PrintWriter} has been created, it can be retrieved by
	 * calling the {@link #writer} field.<br>
	 * <br>
	 * <strong>Note:</strong><br>
	 * You have to manually close the created {@link #writer} object, otherwise
	 * memory leaks can occur.
	 * 
	 * @return The {@link PrintWriter} or null, if the {@link FileWriter}
	 *         encountered any error.
	 */
	protected PrintWriter createWriter()
	{
		if (writer != null)
			writer.close();

		try
		{
			return writer = new PrintWriter(new FileWriter(file));
		}
		catch (IOException e)
		{
			System.err.println("[CustomFile Error] > Creating the new PrintWriter failed!");
			System.err.println("[CustomFile Error] > Stacktrace:");
			System.err.println("[CustomFile Error] > File: " + file.getAbsolutePath() + " | Loaded on init: " + load);
			e.printStackTrace();
			return null;
		}

	}

	public final File getFile()
	{ return this.file; }

	public final String getName()
	{ return this.name; }

}
