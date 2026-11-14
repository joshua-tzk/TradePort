package com.tradeport.core.engine;

import com.tradeport.core.model.Board;
import com.tradeport.core.model.BuildingRepository.Building;
import com.tradeport.core.model.ConstantsLibrary.GoodType;
import com.tradeport.core.model.Player;

import java.util.Arrays;

public class Validator {

//	************************************************************
//	Attributes
//	************************************************************

	private final Board objBoard;


//	************************************************************
//	Constructor
//	************************************************************

	public Validator(Board objBoard) {
		if (objBoard == null) {
			throw new IllegalArgumentException("Validator requires a board.");
		}
		this.objBoard = objBoard;
	}

//	************************************************************
//	Methods
//	************************************************************

	/**Validates player accessing a building<p>
	 * Note that if a player is playing the Neighbour character, her building can
	 * use the abilities of any other existing building regardless of whether or
	 * not it is occupied.
	 */
	void validateBuildingAccess() {

	}

	/**
	 * Validates that a building may be visited this turn: it must be one of the
	 * buildings currently on the board.
	 * @throws IllegalArgumentException if the building is null or not on the board
	 */
	public void validateBuildingVisit(Player player, Building building) {
		if (building == null || !building.onBoard(objBoard.getBuildingsOnBoard())) {
			throw new IllegalArgumentException("Building " + building + " is not on the board.");
		}
	}

	/**
	 * Validates a goods-to-sell selection against a player.
	 * Each good type may appear at most once, and the player must own at least
	 * one of every selected type (selling a type means selling the whole stack).
	 * @throws IllegalArgumentException if the selection is empty, contains nulls
	 * or duplicates, or names a good the player does not own
	 */
	public void validateGoodsSold(Player player, GoodType[] goodsSold) {
		if (goodsSold == null || goodsSold.length == 0) {
			throw new IllegalArgumentException("No goods selected to sell.");
		}
		boolean[] seen = new boolean[GoodType.values().length];
		for (GoodType goodType : goodsSold) {
			if (goodType == null) {
				throw new IllegalArgumentException("Goods selection contains a null good type.");
			}
			if (seen[goodType.ordinal()]) {
				throw new IllegalArgumentException("Each good type must appear exactly once.");
			}
			seen[goodType.ordinal()] = true;
			if (player.getGoodsQuantity(goodType) == 0) {
				throw new IllegalArgumentException("Player " + player.getPlayerId()
						+ " does not own any " + goodType + ".");
			}
		}
	}

	// Phase 1: Before buying a building, the validator checks for buyable buildings on the board based on what the user decides to sell
	public Building[] checkBuyableBuildings(int maximumValue) {
		Building[] onBoard = objBoard.getBuildingsOnBoard();
		Building[] buyableBuildings = new Building[onBoard.length];
		int count = 0;
		for(Building building : onBoard)
			if(building != null && building.getCost() <= maximumValue)
				buyableBuildings[count++] = building;
		return Arrays.copyOf(buyableBuildings, count);
	}

	// Phase 2: If the player inputs a value of the index in the list of buyable buildings, we check if it is out of bounds
	// If we manage to find the building, we double check if the goods sold is actually enough to buy the building
	public Building validateBuyableBuildingByIndex(Player buyingPlayer, int indexInBuyableBuildings, GoodType[] goodsSold) {
		validateGoodsSold(buyingPlayer, goodsSold);
		int maximumValue = objBoard.playerCouldSellGoods(buyingPlayer, goodsSold);
		Building[] buyableBuildings = checkBuyableBuildings(maximumValue);
		if(indexInBuyableBuildings < 0 || indexInBuyableBuildings >= buyableBuildings.length)
			throw new IndexOutOfBoundsException("Buyable building index out of range: " + indexInBuyableBuildings);
		Building buildingToBuy = buyableBuildings[indexInBuyableBuildings];
		if(buildingToBuy.getCost() > maximumValue)
			throw new IllegalStateException("Insufficient funds to buy " + buildingToBuy.getName());
		return buildingToBuy;
	}

}
