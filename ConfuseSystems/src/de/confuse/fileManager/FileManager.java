package de.confuse.fileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A File Manager used to easily access and save Files!
 * 
 * @version 1.4
 * @author Confuse/Confuse#5117
 * https://github.com/MConfuse/FileManagerAPI
 */
public class FileManager {

	public static final String FILE_TYPE = ".cfg";
	private final String version = "v1.4";
	/**
	 * <strong>!!Note!!</strong><br>
	 * Currently experimental and not optimized in the slightest :D<br>
	 * <br>
	 * When true the {@link FileManager} will complete file loading and saving tasks
	 * asynchronously. <br>
	 * The methods that are affected by this are: <br>
	 * - {@link #loadFiles()}<br>
	 * - {@link #saveFiles()}<br>
	 * <br>
	 * The {@link CompletableFuture} Library is used if the asynchronous mode is
	 * chosen.
	 */
	private final boolean async;

	// Files in here will be used to create directories
	private List<File> directories = new ArrayList<File>();
	// Files need to be added here to keep track of the Files
	private List<CustomFile> files = new ArrayList<CustomFile>();

	/**
	 * Creates a new Instance of the {@link FileManager}.
	 * 
	 * @param async <br>
	 *              <strong>!!Note!!</strong><br>
	 *              Currently experimental and not optimized in the slightest :D<br>
	 *              <br>
	 *              Whether or not saving and loading operations should be completed
	 *              using asynchronous threads. Using the asynchronous option can
	 *              shorten loading and file creation times but will complicate the
	 *              stack traces of errors!<br>
	 *              The {@link CompletableFuture} Library is used if the
	 *              asynchronous mode is chosen.
	 */
	public FileManager(boolean async)
	{
		this.async = async;
		System.out.println("File Manager " + version + " initialized!");
	}

	/**
	 * Creates directories using the specified File paths.
	 * 
	 * @param files Array of Files to be added.
	 * 
	 * @see #addDirectories(List)
	 */
	public void addDirectories(File... files)
	{
		for (File file : files)
			directories.add(file);

		makeDirs();
	}

	/**
	 * Creates directories using the specified File paths.
	 * 
	 * @param files List {@link File}.
	 * 
	 * @see #addDirectories(File...)
	 */
	public void addDirectories(List<File> files)
	{
		directories.addAll(files);
		makeDirs();
	}

	/**
	 * Adds the Custom files to the FileManager so we can load the Files using the
	 * {@link #loadFiles()} Method. This requires the
	 * {@link #addDirectories(File...)} method to have completed it's work or you
	 * having already created the needed directories
	 * 
	 * @param cFiles Array of {@link CustomFile}s.
	 */
	public void addFiles(CustomFile... cFiles)
	{
		for (CustomFile file : cFiles)
			files.add(file);
	}

	/**
	 * Adds the Custom files to the FileManager so we can load the Files using the
	 * {@link #loadFiles()} Method. This requires the
	 * {@link #addDirectories(File...)} method to have completed it's work or you
	 * having already created the needed directories
	 * 
	 * @param cFiles List of {@link CustomFile}s.
	 */
	public void addFiles(List<CustomFile> cFiles)
	{
		files.addAll(cFiles);
	}

	/**
	 * Loads all manually added Files.
	 * 
	 * @see #addFiles(CustomFile...)
	 */
	public void loadFiles()
	{
		if (async)
		{
			for (CustomFile file : files)
			{
				CompletableFuture.supplyAsync(() ->
				{
					try
					{
						file.loadFile();
						return true;
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return false;
					}

				});
			}
		}
		else
		{
			for (CustomFile file : files)
			{
				try
				{
					file.loadFile();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}

		}

	}

	/**
	 * Saves all the data to the previously added Files.
	 * 
	 * @see #addFiles(CustomFile...)
	 */
	public void saveFiles()
	{
		if (async)
		{
			for (CustomFile file : files)
			{
				CompletableFuture.supplyAsync(() ->
				{
					try
					{
						file.saveFile();
						return true;
					}
					catch (IOException e)
					{
						e.printStackTrace();
						return false;
					}
				});
			}
		}
		else
		{
			for (CustomFile file : files)
			{
				try
				{
					file.saveFile();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}

		}

	}

	/**
	 * Returns a new File with the given path and name. Usually used for
	 * Configurations or similar Files that are created/loaded during runtime.
	 * 
	 * @param filePath String containing the path to the File.
	 * @param name     String that names the File.
	 * @return new File
	 */
	public File createTempFile(String filePath, String name)
	{
		return new File(filePath, name + FILE_TYPE);
	}

	/**
	 * Returns a List containing all currently added Directories. Only works for
	 * directories added with the {@link #addDirectories(File...)} method!
	 * 
	 * @return List {@link File}
	 */
	public List<File> getDirectories()
	{
		return directories;
	}

	/**
	 * Returns the directory with the specific name. Only works for directories
	 * added with the {@link #addDirectories(File...)} method!
	 * 
	 * @param name Name of the directory.
	 * @return {@link File} | null
	 */
	public File getDirectory(String name)
	{
		for (File file : directories)
			if (file.getName().equalsIgnoreCase(name))
				return file;

		return null;
	}

	/**
	 * Returns the directory with the position in the List. Only works for
	 * directories added with the {@link #addDirectories(File...)} method!
	 * 
	 * @param index Position of the File in the List. (Should be the order they were
	 *              added in).
	 * @return {@link File} | null
	 */
	public File getDirectory(int index)
	{
		return directories.get(index);
	}

	/**
	 * Retrieves a CustomFile using the Class it's from (F. ex.: TestFile.class).
	 * Only searches the manually added Files!
	 * 
	 * @param clazz Class of the {@link CustomFile}.
	 * @return CustomFile | null
	 */
	public CustomFile getCustomFile(final Class<? extends CustomFile> clazz)
	{
		for (CustomFile file : files)
			if (file.getClass() == clazz)
				return file;

		return null;
	}

	/**
	 * Retrieves a CustomFile using the Files name. Only searches the manually added
	 * Files!
	 * 
	 * @param name Name of the {@link CustomFile}.
	 * @return CustomFile | null
	 */
	public CustomFile getCustomFile(String name)
	{
		for (CustomFile file : files)
			if (file.getName().equalsIgnoreCase(name))
				return file;

		return null;
	}

	/**
	 * Returns a List containing all Custom Files added to the ArrayList.
	 * 
	 * @return List {@link CustomFile}
	 */
	public List<CustomFile> getCustomFiles()
	{
		return files;
	}

	/**
	 * Searches for a File in a specific location. Returns a List containing all the
	 * Files containing the name (Ignoring capitalization).
	 * 
	 * @param filePath Path to the directory.
	 * @param name String that the Filename has to contain.
	 * @return List {@link File}
	 */
	public List<File> getFiles(String filePath, String name)
	{
		// All files in the filePath
		File[] folderFiles = new File(filePath).listFiles();
		List<File> files = new ArrayList<File>();
		name = name.toLowerCase();
		
		for (File file : folderFiles)
			if (file.getName().toLowerCase().contains(name))
				files.add(file);

		return files;
	}

	/**
	 * Returns a List containing all Files inside the Folder.
	 * 
	 * NOTE: Only files will be retrieved, Folders will be ignored.
	 * 
	 * @param filePath Path to the directory
	 * @return List {@link File}
	 */
	public List<File> getFiles(String filePath)
	{
		// All files in the filePath
		File[] folderFiles = new File(filePath).listFiles();
		List<File> files = new ArrayList<File>();

		for (File file : folderFiles)
			if (file.isFile())
				files.add(file);

		return files;
	}

	/**
	 * Retrieves the first File out of the target directory matching the name
	 * (Ignoring capitalization).
	 * 
	 * @param filePath Path to the directory.
	 * @param name The Files name.
	 * @return File | null
	 */
	public File getFile(String filePath, String name)
	{
		// All files in the filePath
		File[] folderFiles = new File(filePath).listFiles();

		for (File file : folderFiles)
			if (file.isFile() && file.getName().substring(0, file.getName().lastIndexOf(".")).equalsIgnoreCase(name))
				return file;

		return null;
	}

	/**
	 * Creates the directories added to the {@link #directories} List in case that
	 * they have not been created yet.
	 */
	private void makeDirs()
	{
		for (File file : directories)
			if (!file.exists())
				file.mkdir();
	}

}
