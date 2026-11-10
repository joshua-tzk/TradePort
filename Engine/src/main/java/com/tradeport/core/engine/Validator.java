package com.tradeport.core.engine;

import com.tradeport.core.model.Board;
import com.tradeport.core.model.BuildingRepository.Building;
import com.tradeport.core.model.Enum.GoodType;
import com.tradeport.core.model.Player;

public class Validator {

//	************************************************************
//	Attributes
//	************************************************************

	Board objBoard;
	private static final Exception OutofIndexBoundsException = null;
	private static final Exception InsufficientFundsException = null;
	Building buyableBuildings[] = null;




//	************************************************************
//	Constructor
//	************************************************************

	public Validator() {
		refreshBoard(objBoard);
	}

	public Validator(Board objBoard) {
		this.objBoard = objBoard;
	}
	/**Validates player accessing a building<p>
	 * Note that if a player is playing the Neighbour character, her building can
	 * use the abilities of any other existing building regardless of whether or
	 * not it is occupied.
	 */
	void validateBuildingAccess() {
		
	}
	


//	************************************************************
//	Methods
//	************************************************************

	// Phase 1: Before buying a building, the validator checks for buyable buildings on the board based on what the user decides to sell
	public Building[] checkBuyableBuildings(int maximumValue) {
		buyableBuildings = new Building[objBoard.getPlayers().length + 3];
		int i = 0;
		for(Building building : objBoard.getBuildingsOnBoard())
			if(building.getCost() <= maximumValue) {
				buyableBuildings[i] = building;
				i++;
			}
		return buyableBuildings;
	}

	// Phase 2: If the player inputs a value of the index in the list of buyable buildings, we check if it is out of bounds
	// If we manage to find the building, we double check if the goods sold is actually enough to buy the building
	public Building validateBuyableBuildingByIndex(Player buyingPlayer, int indexInBuyableBuildings, GoodType[] goodsSold) throws Exception {
		if(indexInBuyableBuildings >= buyableBuildings.length)
			throw OutofIndexBoundsException;
		Building buildingToBuy = buyableBuildings[indexInBuyableBuildings];
		int maximumValue = 0;
		for(GoodType goodSold : goodsSold)
			maximumValue += objBoard.getMarket().getPrice(goodSold);
		if(maximumValue < buildingToBuy.getCost())
			throw InsufficientFundsException;
		return buildingToBuy;
	}

//	************************************************************
//	Getters & Setters
//	************************************************************

	void refreshBoard(Board objBoard) {
		this.objBoard = objBoard;
	}

	Board getBoard() {
		return this.objBoard;
	}


}
