package com.tradeport.core.engine;

import com.tradeport.core.model.Board;
import com.tradeport.core.model.Player;
import com.tradeport.core.model.BuildingRepository.Building;
import com.tradeport.core.model.Enum.GoodType;

import java.util.Scanner;

/**
 * This is the class that opens a game session, which "opens" a game board.
 * This will be the class that regulates all performable actions to all pieces on the Board.
 * This includes triggering actions when moved to a building and buying a building.
 * Validations will also be performed depending on the actions available.
 * Actions will be logged by invoking the Logger methods.
 */

public class Controller {
	
	int sessionId;
	GameConfig objGameConfig;
	Board objBoard;
	Scanner objScanner = new Scanner (System.in);
	Validator objValidator = new Validator(objBoard);


//	************************************************************
//	Constructor
//	************************************************************

	public Controller() {
		// TODO Auto-generated constructor stub
	}


//	************************************************************
//	Getters & Setters
//	************************************************************

//	void prepareValidation() {
//		// Now the validator can get a state of the board every time a validation is needed
//		objValidator.setBoard(objBoard);
//	}


//	************************************************************
//	Game Mechanic Control Methods
//	************************************************************

	public String inquireBuyableBuildings(int playerId, GoodType[] goodsSold) throws Exception {
		Player buyingPlayer = objBoard.findPlayerById(playerId); // Find player by Id
		int maximumValue = objBoard.playerCouldSellGoods(buyingPlayer, goodsSold); // Sell goods (temporarily, goods will not be lost if action is rejected)
		// Perform Validations
//		if(objValidator.getBoard() != objBoard) // We do not need to refresh board if they point to the same object
//			objValidator.refreshBoard(objBoard);
		Building buyableBuildings[] = objValidator.checkBuyableBuildings(maximumValue); // Validation: Check what buildings can be bought
		// Player chooses what building to buy
		String output = "Buyable Buildings:";
		for(int i = 1; i <= buyableBuildings.length; i++)
			output += i + ". " + buyableBuildings[i].getDetailsinString() + "\n\n";
		output += "Choose of the building you want to buy \nPress \"0\" to return to action menu\nOption: ";
		return output;
	}

	public void executeBuyABuildingMainProcess(int playerId, GoodType[] goodsSold) throws Exception{
		Player buyingPlayer = objBoard.findPlayerById(playerId); // Find player by Id
		String stringBuildingToBuy = objScanner.nextLine();
		int i = Integer.parseInt(stringBuildingToBuy);
		Building buildingToBuy = objValidator.validateBuyableBuildingByIndex(buyingPlayer, i - 1, goodsSold);
		objBoard.buyBuildingWithGoods(buyingPlayer, buildingToBuy, goodsSold);
//		objValidator.refreshBoard(objBoard); // We do not need to refresh board if they point to the same object
	}


	public void sellGoods(GoodType[] goodsSold) {
		
	}


}
