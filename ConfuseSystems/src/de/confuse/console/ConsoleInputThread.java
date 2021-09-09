package de.confuse.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.confuse.command.Command;
import de.confuse.console.commands.ExitCommand;
import de.confuse.util.ArrayUtils;

public class ConsoleInputThread extends ArrayUtils implements Runnable
{
	public static ConsoleInputThread instance;
	private static boolean running = true;
	private static BufferedReader reader;
	private static Thread thread;

	private final List<IConsoleInput> interfaces = new ArrayList<IConsoleInput>();
	private final List<Command> commands = new ArrayList<Command>();

	public ConsoleInputThread(List<IConsoleInput> interfaces, List<Command> commands)
	{
		instance = this;
		if (interfaces != null)
			this.interfaces.addAll(interfaces);
		if (commands != null)
			this.commands.addAll(commands);

		this.commands.add(new ExitCommand());
		thread = new Thread(this, "ConsoleReader-Thread");
		thread.start();
	}

	@Override
	public void run()
	{
		reader = new BufferedReader(new InputStreamReader(System.in));
		String line = "";

		while (running)
			try
			{
				if ((line = reader.readLine()) != null)
				{
					for (IConsoleInput in : interfaces)
						in.consoleInput(line);

					String[] args = line.split(" ");

					if (args.length > 0)
						for (Command cmd : commands)
							if (cmd != null)
								if (containsValueIgnoreCase(cmd.getAlias(), args[0])
										|| cmd.getName().equalsIgnoreCase(args[0]))
									try
									{
										cmd.onCommand(args[0], args, line);
										break;
									}
									catch (Exception e)
									{
										Logger.error("Error during execution of the Command!");
										Logger.error(cmd.getSyntax());
										e.printStackTrace();
									}

					for (IConsoleInput in : interfaces)
						in.unhandledInput(line);
				}

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

	}

	public static void shutdown() throws IOException
	{
		running = false;
		reader.close();
	}

	/**
	 * @return the interfaces
	 */
	public List<IConsoleInput> getInterfaces()
	{
		return interfaces;
	}

	public void addInterfaces(IConsoleInput... input)
	{
		if (input != null && !containsValue(input, null))
			interfaces.addAll(Arrays.asList(input));
	}

	/**
	 * @return the commands
	 */
	public List<Command> getCommands()
	{
		return commands;
	}

	public void addCommands(Command... cmd)
	{
		if (cmd != null && !containsValue(cmd, null))
			commands.addAll(Arrays.asList(cmd));
	}

}
