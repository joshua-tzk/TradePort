package com.tradeport.core.userInterface;

import com.tradeport.core.engine.Controller;

import java.util.Map;

/**
 * Action subroutine: visit a building.
 * The controller asks the player which building to visit, validates the
 * choice, moves her there and triggers the building's effect (which may ask
 * for further input through PlayerInput).
 * The outcome is recorded via {@link Console#recordAction}.
 */
public final class VisitBuildingAction {

	private VisitBuildingAction() {
	}

	public static void run(Console console) {
		Controller controller = console.getController();
		Map<String, Object> record = Console.newActionRecord("visitBuilding");
		record.put("success", false);

		try {
			int playerId = promptPlayerId(console);
			record.put("playerId", playerId);
			controller.performBuildingVisit(playerId);
			record.put("building", String.valueOf(controller.findPlayer(playerId).getLocation()));
			record.put("success", true);
		} catch (RuntimeException e) {
			record.put("error", e.getMessage());
			System.out.println("Action rejected: " + e.getMessage());
		} finally {
			console.recordAction(record);
		}
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
}
