package com.tradeport.core.model;

import java.util.Arrays;

/***
 * Here we declare and define all actions that can be done to the board,
 * including to the market, building cards, deck, etc.
 */
public class Board implements BuildingRepository{

//	************************************************************
//	Attributes
//	************************************************************

	Building[] buildingsOnBoard;
	Deck objDeck;
	Market objMarket;
	Player players[];
	boolean finalRoundFlag = false; // Place this to the controller in the future


//	************************************************************
//	Constructor
//	************************************************************

	public Board(int numOfPlayers) {
		objDeck = new Deck();
		objMarket = new Market();
		buildingsOnBoard = new Building[numOfPlayers + 3];
		players = new Player[numOfPlayers];
	}


	/**
	 * Deals the starting cards onto the board.
	 * Shuffles the deck first, then fills every slot of {@link #buildingsOnBoard}.
	 * @return the buildings now on the board
	 */
	public Building[] dealStartingCards() {
		objDeck.shuffle();
		buildingsOnBoard = objDeck.drawBuildings(buildingsOnBoard.length);
		return buildingsOnBoard;
	}

	void executeBuyABuilding() {
	}
	void executeExchange() {
	}

	boolean checkBuyABuilding() {
		return false;
	}

	boolean checkExecutibility() {
		return false;
	}


//	************************************************************
//	Methods: Actions performed during the game
//	************************************************************

	// Utility Methods
	/**
	 * Serves as a function to "read" the building card by index.
	 * Returns the Building enum object based on the index of the card in the deck.
	 * This allows the deck to be constructed using only the buildings' indices instead of the building themselves.
	 * @param ordinal
	 * @return
	 */
	public static Building getByOrdinal(int ordinal) {
		Building[] cardList = Building.values();
		if (ordinal >= 0 && ordinal < cardList.length) {
			return cardList[ordinal];
		}
		throw new IllegalArgumentException("Invalid ordinal: " + ordinal);
	}


	/**
	 * Gain (quantity > 0) or lose (quantity < 0) a good.
	 * @return true if applied; false (and nothing changed) if the player would
	 * end up with fewer than 0 of that good.
	 */
	public boolean adjustGood(Player player, GoodType goodType, int quantity) {
		int updated = player.getGoodsQuantity(goodType) + quantity;
		if (updated < 0 || updated > Player.MAX_GOODS_PER_TYPE) {
			return false;
		}
		player.setGoodsQuantity(goodType, updated);
		return true;
	}


	// Composite Action Methods
	/**
	 * Action: Swapping Goods
	 * <p>Gives up q1 of g1 in exchange for q2 of g2. All-or-nothing:
	 * if the player cannot afford q1 of g1, nothing changes.
	 * q1 and q2 are positive amounts.
	 */
	public boolean swapGoods(Player player, GoodType g1, int q1, GoodType g2, int q2) {
		if (q1 <= 0 || q2 <= 0) {
			throw new IllegalArgumentException("Quantities must be positive.");
		}
		if (g1 == g2) {
			throw new IllegalArgumentException("Cannot swap a good with itself.");
		}
		// All-or-nothing: check both sides before mutating anything
		if (player.getGoodsQuantity(g2) + q2 > Player.MAX_GOODS_PER_TYPE) {
			return false;
		}
		if (!adjustGood(player, g1, -q1)) {
			return false;
		}
		adjustGood(player, g2, q2); // cannot fail: affordability was checked above
		return true;
	}


	/**
	 * Action: Shift a Good Marker to a certain position<p>
	 * Moves a good's marker to the given market position.
	 * Market decides how to validate/clamp the position and how it treats
	 * other markers (it owns that private state).
	 */
	public void shiftGoodMarker(GoodType goodType, int goodPosition) {
		objMarket.shiftGoodMarker(goodType, goodPosition);
	}


	/**
	 * Action: Shift a Good Marker to the lowest position in the market
	 */
	public void shiftToLowest(GoodType goodType) {
		shiftGoodMarker(goodType, Market.LOWEST_POSITION);
	}

	/**
	 * Action: Shift a Good Marker to the highest position in the market
	 */
	public void shiftToHighest(GoodType goodType) {
		shiftGoodMarker(goodType, Market.HIGHEST_POSITION);
	}


	public void swapGoodMarkers(GoodType g1, GoodType g2) {
		if (g1 == g2) {
			throw new IllegalArgumentException("Cannot swap a marker with itself.");
		}
		objMarket.swap(g1, g2);
	}


	public void rearrangeMarket(GoodType g1, int p1, GoodType g2, int p2, GoodType g3, int p3, GoodType g4, int p4) {
		// Cheap sanity checks
		// Each good must appear exactly once.
		if (g1 == g2 || g1 == g3 || g1 == g4 || g2 == g3 || g2 == g4 || g3 == g4) {
			throw new IllegalArgumentException("Each good type must appear exactly once.");
		}
		// No two goods can be at the same position, and all positions must be in range.
		int[] positions = {p1, p2, p3, p4};
		for (int i = 0; i < positions.length; i++) {
			if (positions[i] < Market.LOWEST_POSITION || positions[i] > Market.HIGHEST_POSITION) {
				throw new IllegalArgumentException("Position out of range: " + positions[i]);
			}
			for (int j = i + 1; j < positions.length; j++) {
				if (positions[i] == positions[j]) {
					throw new IllegalArgumentException("No two goods can be at the same position.");
				}
			}
		}
		objMarket.setGoodsPosition(g1, p1);
		objMarket.setGoodsPosition(g2, p2);
		objMarket.setGoodsPosition(g3, p3);
		objMarket.setGoodsPosition(g4, p4);
	}


	/**
	 * Board performs the finalising action of "Buying a Building" after validation is passed:
	 * 1. Player sells her entire stack of each good type in goodsSold
	 * 2. Player obtains building
	 * 3. Market is shifted (each sold good type shifts once)
	 * 4. The bought building's slot is refilled from the deck
	 * 5. If the player just obtained her fifth building, the final round begins
	 */
	public void buyBuildingWithGoods(Player buyingPlayer, Building boughtBuilding, GoodType[] goodsSold){
		for(GoodType goodType : goodsSold) {
			if (goodType == null) {
				throw new IllegalArgumentException("Goods list contains a null good type.");
			}
			// Game rule: selling a good type means selling the player's entire stack of it
			buyingPlayer.setGoodsQuantity(goodType, 0);
		}
		buyingPlayer.obtainBuilding(boughtBuilding);
		objMarket.shiftMarket(goodsSold);
		replenishBuildingSlot(boughtBuilding);
		if (buyingPlayer.getBuildingCount() >= buyingPlayer.getOwnedBuildings().length) {
			setFinalRoundFlag(true);
		}
		System.out.println("Player " + buyingPlayer.getPlayerId() + " bought " + boughtBuilding.getName()
				+ " and sold goods " + Arrays.toString(goodsSold) + ".");
	}

	// Replaces a bought building on the board with the next card from the deck
	private void replenishBuildingSlot(Building boughtBuilding) {
		for (int i = 0; i < buildingsOnBoard.length; i++) {
			if (buildingsOnBoard[i] == boughtBuilding) {
				buildingsOnBoard[i] = objDeck.isEmpty() ? null : objDeck.drawBuilding();
				return;
			}
		}
	}

//	public Building[] checkBuyableBuildings(int maximumValue) {
//		Building[] buyableBuildings = new Building[players.length + 3];
//		int i = 0;
//		for(Building building : buildingsOnBoard)
//			if(building.getCost() <= maximumValue) {
//				buyableBuildings[i] = building;
//				i++;
//			}
//		return buyableBuildings;
//	}

	public Player findPlayerById(int playerId) {
		return Player.findPlayerById(players, playerId);
	}

	/**
	 * Computes the buying power a player gets from selling the given goods.
	 * goodsSold lists each good type to sell at most once; selling a type means
	 * selling the player's entire stack of it, so each type contributes
	 * quantity * price.
	 * @return total buying power
	 * @throws IllegalStateException if the player does not own any of a sold good
	 */
	public int playerCouldSellGoods(Player buyingPlayer, GoodType[] goodsSold){
		int potentialBuyingPower = 0;
		for(GoodType goodType : goodsSold) {
			if (goodType == null) {
				throw new IllegalArgumentException("Goods list contains a null good type.");
			}
			int owned = buyingPlayer.getGoodsQuantity(goodType);
			if (owned == 0) {
				throw new IllegalStateException("Player " + buyingPlayer.getPlayerId()
						+ " does not own any " + goodType + ".");
			}
			potentialBuyingPower += owned * objMarket.getPrice(goodType);
		}
		return potentialBuyingPower;
	}

	/**
	 * Computes final victory points for every player and stores them on the players.
	 * Intended to be called by the controller once the final round ends
	 * (i.e. when the turn of the player before the one who bought the fifth
	 * building finishes).
	 * @return victory points indexed by player slot
	 */
	public int[] calculateFinalVictoryPoints() {
		int[] scores = new int[players.length];
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				scores[i] = players[i].calculateVictoryPoints();
			}
		}
		return scores;
	}


//	************************************************************
//	Getters and Setters
//	************************************************************

	public Deck getDeck() {
		return objDeck;
	}

	public void setDeck(Deck objDeck) {
		this.objDeck = objDeck;
	}

	public Market getMarket() {
		return objMarket;
	}

	public void setMarket(Market objMarket) {
		this.objMarket = objMarket;
	}

	public Building[] getBuildingsOnBoard() {
		return this.buildingsOnBoard;
	}

	public void setBuildingsOnBoard(Building[] buildingsOnBoard) {
		this.buildingsOnBoard = buildingsOnBoard;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public boolean isFinalRoundFlag() {
		return finalRoundFlag;
	}

	public void setFinalRoundFlag(boolean finalRoundFlag) {
		this.finalRoundFlag = finalRoundFlag;
	}

}
