package com.tradeport.core.userInterface;

import com.tradeport.core.engine.Controller;
import com.tradeport.core.model.BuildingRepository.Building;

import java.util.Map;

/**
 * Action subroutine: character draft.
 * Each player is shown her two dealt characters and picks one.
 * Runs until every player has chosen (or the game mode has no draft).
 * Every choice is recorded via {@link Console#recordAction}.
 */
public final class ChooseCharacterAction {

	private ChooseCharacterAction() {
	}

	public static void run(Console console) {
		Controller controller = console.getController();
		int numOfPlayers = controller.getBoard().getPlayers().length;

		for (int playerId = 1; playerId <= numOfPlayers && controller.isDraftPending(); playerId++) {
			Building[] hand = controller.getDraftHand(playerId);
			if (hand == null) {
				continue; // already chosen
			}
			System.out.println("\nPlayer " + playerId + ", choose your character:");
			for (int i = 0; i < hand.length; i++) {
				System.out.println("  " + (i + 1) + ". " + hand[i].getName());
			}
			System.out.print("Option: ");
			Map<String, Object> record = Console.newActionRecord("chooseCharacter");
			record.put("playerId", playerId);
			try {
				int choice = console.readInt() - 1;
				record.put("choice", choice);
				controller.chooseCharacter(playerId, choice);
				record.put("character", hand[choice].getName());
				record.put("success", true);
			} catch (RuntimeException e) {
				System.out.println("Invalid choice: " + e.getMessage());
				record.put("success", false);
				record.put("error", e.getMessage());
				playerId--; // let this player try again
			}
			console.recordAction(record);
		}
	}
}
