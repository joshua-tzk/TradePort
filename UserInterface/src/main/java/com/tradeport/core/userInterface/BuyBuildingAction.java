package com.tradeport.core.userInterface;

import com.tradeport.core.engine.Controller;
import com.tradeport.core.model.BuildingRepository.Building;
import com.tradeport.core.model.ConstantsLibrary.GoodType;
import com.tradeport.core.model.Player;

import java.util.Arrays;
import java.util.Map;

/**
 * Action subroutine: buy a building.
 * Prompts for the player, which stacks of goods to sell (selling a good
 * type means selling the player's entire stack of it), shows the buildings
 * she can afford, and executes the purchase.
 * The outcome is recorded via {@link Console#recordAction}.
 */
public final class BuyBuildingAction {

	private BuyBuildingAction() {
	}

	public static void run(Console console) {
		Controller controller = console.getController();
		Map<String, Object> record = Console.newActionRecord("buyBuilding");
		record.put("success", false);

		try {
			int playerId = promptPlayerId(console);
			Player player = controller.findPlayer(playerId);
			GoodType[] goodsSold = promptGoodsToSell(console, player);
			record.put("playerId", playerId);
			record.put("goodsSold", goodNames(goodsSold));
			if (goodsSold.length == 0) {
				record.put("message", "No goods selected.");
				System.out.println("No goods selected. Purchase cancelled.");
				return;
			}

			Building[] buyableBuildings = controller.getBuyableBuildings(playerId, goodsSold);
			record.put("buyableCount", buyableBuildings.length);
			if (buyableBuildings.length == 0) {
				record.put("message", "Nothing affordable.");
				System.out.println("No buildings are affordable with the selected goods.");
				return;
			}

			System.out.println("\nBuyable Buildings:");
			for (int i = 0; i < buyableBuildings.length; i++) {
				Building building = buyableBuildings[i];
				System.out.println("  " + (i + 1) + ". " + building.getName() + " (cost " + building.getCost() + ")");
			}
			System.out.print("Choose a building (0 to cancel): ");
			int choice = console.readInt();
			if (choice == 0) {
				record.put("message", "Cancelled by player.");
				System.out.println("Purchase cancelled.");
				return;
			}

			Building bought = controller.buyBuilding(playerId, goodsSold, choice - 1);
			record.put("building", bought.getName());
			record.put("success", true);
			System.out.println(player.getCharacterName() + " bought " + bought.getName() + ".");
		} catch (RuntimeException e) {
			record.put("error", e.getMessage());
			System.out.println("Action rejected: " + e.getMessage());
		} finally {
			console.recordAction(record);
		}
	}

	// GoodType[] -> String[] so records stay serialization-friendly
	private static String[] goodNames(GoodType[] goods) {
		String[] names = new String[goods.length];
		for (int i = 0; i < goods.length; i++) {
			names[i] = goods[i].name();
		}
		return names;
	}

	private static int promptPlayerId(Console console) {
		int numOfPlayers = console.getController().getBoard().getPlayers().length;
		while (true) {
			System.out.print("Player ID (1-" + numOfPlayers + "): ");
			int playerId = console.readInt();
			if (playerId >= 1 && playerId <= numOfPlayers) {
				return playerId;
			}
			System.out.println("Invalid player ID.");
		}
	}

	// For each good type the player owns, ask whether to sell the entire stack.
	// Returns each selected good type once (selling a type sells the whole stack).
	private static GoodType[] promptGoodsToSell(Console console, Player player) {
		GoodType[] selection = new GoodType[GoodType.values().length];
		int count = 0;
		for (GoodType goodType : GoodType.values()) {
			int owned = player.getGoodsQuantity(goodType);
			if (owned == 0) {
				continue;
			}
			System.out.print("Sell all " + owned + " " + goodType + "? (0/1): ");
			if (readYesNo(console)) {
				selection[count++] = goodType;
			}
		}
		return Arrays.copyOf(selection, count);
	}

	private static boolean readYesNo(Console console) {
		while (true) {
			String input = console.getScanner().nextLine().trim();
			if (input.equals("0") || input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
				return false;
			}
			if (input.equals("1") || input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
				return true;
			}
			System.out.print("Please enter 0 or 1: ");
		}
	}
}
