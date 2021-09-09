package de.confuse.console.commands;

import de.confuse.command.Command;
import de.confuse.console.ConsoleInputThread;
import de.confuse.console.Logger;

public class ExitCommand extends Command
{

	public ExitCommand()
	{
		super("exit", new String[] {"end"}, "exit", "", "Shuts down the ConsoleInputThread.");
	}

	@Override
	public void onCommand(String command, String[] args, String message) throws Exception
	{
		Logger.info("Shutting down the ConsoleInputThread...");
		ConsoleInputThread.shutdown();
		Logger.info("Successfully shut down the ConsoleInputThread!");
	}

}
