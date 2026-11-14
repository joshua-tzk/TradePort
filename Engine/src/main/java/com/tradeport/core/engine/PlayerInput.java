package com.tradeport.core.engine;

/**
 * Abstraction over user input. Engine actions (building effects, drafts,
 * purchases) ask the player for decisions through this interface instead of
 * touching UI code directly.
 *
 * The console implements it with a Scanner today; a future network UI can
 * implement it by sending prompt requests to the client and awaiting replies.
 */
public interface PlayerInput {

	/**
	 * Asks the player to pick one of the given options.
	 * @param prompt text shown above the options
	 * @param options display labels for each choice
	 * @return index of the chosen option (0-based)
	 */
	int choose(String prompt, String... options);

	/**
	 * Asks a yes/no question.
	 * @return true for yes, false for no
	 */
	boolean confirm(String prompt);

	/**
	 * Asks for a free-text line of input.
	 */
	String askLine(String prompt);
}
