package com.tradeport.core.userInterface;

import com.tradeport.core.engine.Controller;
import com.tradeport.core.engine.GameConfig;
import com.tradeport.core.engine.PlayerInput;
import com.tradeport.core.model.ConstantsLibrary.GoodType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Text-console user interface. This is the temporary UI: it drives the
 * Controller by calling the per-action subroutines in this package
 * (one class per action performable by the player).
 *
 * Every action reports its inputs and outputs as a Map<String, Object>
 * (strings, numbers and booleans only), which Console stores in
 * {@link #actionHistory}. The records are deliberately serialization-ready
 * so a future online/JSON transport can reuse them unchanged.
 */
public class Console implements PlayerInput {

	private final Controller controller;
	private final Scanner scanner;

	// Ordered log of every action's inputs/outputs, keyed by sequence number
	private final Map<Integer, Map<String, Object>> actionHistory = new LinkedHashMap<>();
	private int actionCounter = 0;

	public Console(Controller controller) {
		this(controller, new Scanner(System.in));
	}

	public Console(Controller controller, Scanner scanner) {
		this.controller = controller;
		this.scanner = scanner;
		controller.setPlayerInput(this);
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		GameConfig gameConfig = promptForGameConfig(scanner);
		Controller controller = new Controller(gameConfig);
		new Console(controller, scanner).run();
	}

	public void run() {
		// Draft phase first, if the game mode requires it
		if (controller.isDraftPending()) {
			ChooseCharacterAction.run(this);
		}

		// Main action menu
		while (true) {
			System.out.println("\n========== Trade Port ==========");
			System.out.println("1. Visit a Building");
			System.out.println("2. Buy a Building");
			System.out.println("0. Exit");
			System.out.print("Option: ");
			int choice = readInt();
			switch (choice) {
			case 1:
				VisitBuildingAction.run(this);
				break;
			case 2:
				BuyBuildingAction.run(this);
				break;
			case 0:
				System.out.println("Thanks for playing!");
				return;
			default:
				System.out.println("Invalid option.");
			}
		}
	}

//	************************************************************
//	Action history (I/O records for future transport layers)
//	************************************************************

	/** Stores one action's input/output record. Called by action subroutines. */
	public void recordAction(Map<String, Object> record) {
		actionHistory.put(++actionCounter, record);
	}

	/** @return the ordered history of all recorded actions */
	public Map<Integer, Map<String, Object>> getActionHistory() {
		return actionHistory;
	}

	/** Creates a fresh record pre-filled with the action name. */
	public static Map<String, Object> newActionRecord(String actionName) {
		Map<String, Object> record = new HashMap<>();
		record.put("action", actionName);
		return record;
	}

//	************************************************************
//	Game setup prompts
//	************************************************************

	private static GameConfig promptForGameConfig(Scanner scanner) {
		System.out.println("=== Trade Port - Game Setup ===");
		System.out.print("The Wharfs only mode? (0/1): ");
		boolean theWharfsOnly = readYesNo(scanner);
		System.out.print("Solo mode (you vs Training Dummy)? (0/1): ");
		boolean soloMode = readYesNo(scanner);
		System.out.print("Inland Traders expansion? (0/1): ");
		boolean inlandTradersExpansion = readYesNo(scanner);
		System.out.print("Bonus cards included? (0/1): ");
		boolean bonusCardsIncluded = readYesNo(scanner);

		int numOfPlayers = soloMode ? 1 : readInt(scanner, "Number of players: ");
		if (numOfPlayers < 1) {
			throw new IllegalArgumentException("A game needs at least one player.");
		}

		GoodType[][] startingGoods = new GoodType[numOfPlayers][];
		for (int i = 0; i < numOfPlayers; i++) {
			System.out.println("Player " + (i + 1) + ", choose your two starting goods:");
			GoodType first = promptGoodType(scanner, "First good (1-" + GoodType.values().length + "): ");
			GoodType second = promptGoodType(scanner, "Second good (1-" + GoodType.values().length + "): ");
			startingGoods[i] = new GoodType[]{first, second};
		}
		return new GameConfig(theWharfsOnly, soloMode, inlandTradersExpansion, bonusCardsIncluded, numOfPlayers, startingGoods);
	}

	private static boolean readYesNo(Scanner scanner) {
		while (true) {
			String input = scanner.nextLine().trim();
			if (input.equals("0") || input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
				return false;
			}
			if (input.equals("1") || input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
				return true;
			}
			System.out.print("Please enter 0 or 1: ");
		}
	}

	private static int readInt(Scanner scanner, String prompt) {
		System.out.print(prompt);
		return Integer.parseInt(scanner.nextLine().trim());
	}

	private static GoodType promptGoodType(Scanner scanner, String prompt) {
		GoodType[] goodTypes = GoodType.values();
		while (true) {
			System.out.print(prompt);
			try {
				int choice = Integer.parseInt(scanner.nextLine().trim());
				if (choice >= 1 && choice <= goodTypes.length) {
					return goodTypes[choice - 1];
				}
			} catch (NumberFormatException ignored) {
			}
			System.out.println("Invalid choice. Enter a number between 1 and " + goodTypes.length + ".");
		}
	}

//	************************************************************
//	Accessors for action subroutines
//	************************************************************

	public Controller getController() {
		return controller;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public int readInt() {
		return Integer.parseInt(scanner.nextLine().trim());
	}

//	************************************************************
//	PlayerInput implementation (engine -> UI decision channel)
//	************************************************************

	@Override
	public int choose(String prompt, String... options) {
		System.out.println(prompt);
		for (int i = 0; i < options.length; i++) {
			System.out.println("  " + (i + 1) + ". " + options[i]);
		}
		while (true) {
			System.out.print("Option: ");
			try {
				int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
				if (choice >= 0 && choice < options.length) {
					return choice;
				}
			} catch (NumberFormatException ignored) {
			}
			System.out.println("Invalid choice. Enter a number between 1 and " + options.length + ".");
		}
	}

	@Override
	public boolean confirm(String prompt) {
		System.out.print(prompt + " (0/1): ");
		while (true) {
			String input = scanner.nextLine().trim();
			if (input.equals("0") || input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
				return false;
			}
			if (input.equals("1") || input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
				return true;
			}
			System.out.print("Please enter 0 or 1: ");
		}
	}

	@Override
	public String askLine(String prompt) {
		System.out.print(prompt + ": ");
		return scanner.nextLine().trim();
	}
}
