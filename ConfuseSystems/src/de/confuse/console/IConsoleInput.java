package de.confuse.console;

import de.confuse.command.Command;

public interface IConsoleInput
{
	/**
	 * Called every time the console receives a new Input.
	 * 
	 * @param input The full Line that was input
	 */
	public void consoleInput(String input);

	/**
	 * Only called when either no {@link Command} was found for the given input or
	 * the execution of a Command failed.
	 * 
	 * @param input The full Line that was input
	 */
	public void unhandledInput(String input);
}
