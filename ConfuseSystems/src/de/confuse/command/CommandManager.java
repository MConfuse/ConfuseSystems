package de.confuse.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Commands for managing Settings and other stuff on a text basis.
 * 
 * @version 1.1
 * @author Confuse/xXConfusedJenni#5117
 */
public class CommandManager {

	private List<Command> commands = new ArrayList<Command>();

	/**
	 * Constructor, just make sure to only create one and refer to the created one to make this functional.
	 * 
	 */
	public CommandManager()
	{
		System.out.println("Created a new Command Manager!");
	}

	/**
	 * Adds Commands as an Array
	 * 
	 * @param cmd Array of {@link Command}s
	 */
	public void addCommand(Command... cmd)
	{
		for (Command c : cmd)
		{
			commands.add(c);
		}

	}

	/**
	 * Adds Commands from a List
	 * 
	 * @param cmd List of {@link Command}s
	 */
	public void addCommands(List<Command> cmd)
	{
		commands.addAll(cmd);
	}

	/**
	 * Calls a command using a retrieved Command
	 * 
	 * @param c    The {@link Command} Object
	 * @param args Arguments parsed to the {@link Command}
	 * @throws Exception command failed
	 */
	public void callCommand(Command c, String[] args) throws Exception
	{
		for (Command cmd : commands)
		{
			if (cmd.equals(c))
			{
				cmd.onCommand(cmd.getName(), args, "Called using absolute command");
				return;
			}

		}

		System.out.println("Command was not found!");
	}

	/**
	 * Calls a command using the commands name.
	 * 
	 * @param name Name of the {@link Command}
	 * @param args Arguments parsed to the {@link Command}
	 * @throws Exception Command failed
	 */
	public void callCommand(String name, String[] args) throws Exception
	{
		for (Command cmd : commands)
		{
			if (cmd.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
			{
				cmd.onCommand(cmd.getName(), args, "Called using absolute Name");
				return;
			}

		}

		System.out.println("Command was not found!");
	}

	/**
	 * Calls a command using a whole String as input. First argument (In example
	 * 'name/alias') needs to be the commands alias or name.
	 * 
	 * Format: WarpDrive (name/alias) toggle (arg0) overDrive (arg1) on (arg2)
	 * 
	 * @param input A String out of the Console/Chat
	 * @throws Exception Command failed
	 */
	public void callCommand(String input) throws Exception
	{
		String[] inArgs = input.split(" ");
		String command = inArgs[0].toLowerCase();
		String args = input.substring(command.length()).trim();

		for (Command cmd : commands)
		{
			for (String al : cmd.getAlias())
			{
				if (al.toLowerCase().equalsIgnoreCase(command))
				{
					cmd.onCommand(command, args.split(" "), input);
					return;
				}

			}

		}

		for (Command cmd : commands)
		{
			if (cmd.getName().toLowerCase().equalsIgnoreCase(command))
			{
				cmd.onCommand(command, args.split(" "), input);
				return;
			}

		}

		System.out.println("Command was not found!");
	}

	/**
	 * Returns true if alias matches a command. Intended to be called by the Chat
	 * Listener.
	 * 
	 * @param alias Alias of the {@link Command}
	 * @return Boolean whether or not the alias is a Command
	 */
	public boolean isCommand(String alias)
	{
		for (Command cmd : commands)
		{
			for (String al : cmd.getAlias())
			{
				if (al.toLowerCase().equalsIgnoreCase(alias.toLowerCase()))
				{
					return true;
				}

			}

		}

		return false;
	}

	/**
	 * Returns the List of loaded Commands
	 * 
	 * @return List of all {@link Command}s
	 */
	public List<Command> getCommands()
	{
		return commands;
	}

	/**
	 * Retrieves a Command with matching alias from the added commands
	 * 
	 * @param alias Alias of the {@link Command}
	 * @return A Command with matching alias {@link Command} | null
	 */
	public Command getCommandByAlias(String alias)
	{
		for (Command cmd : commands)
		{
			for (String al : cmd.getAlias())
			{
				if (al.toLowerCase().equalsIgnoreCase(alias.toLowerCase()))
				{
					return cmd;
				}

			}

		}

		return null;
	}

	/**
	 * Retrieves a Command with matching name from the added commands.
	 * 
	 * @param name Name of the {@link Command}
	 * @return A {@link Command} matching the name | null
	 */
	public Command getCommandByName(String name)
	{
		for (Command cmd : commands)
		{
			if (cmd.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
			{
				return cmd;
			}

		}

		return null;
	}

}
