package com.tradeport.core.model;

/***
 * Here we declare and define all actions that can be done to the board,
 * including the market, building cards, deck, etc.
 * <p>
 */
public class Board implements Enum, BuildingRepository{
	
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


	/** Deals the starting cards */
	void dealStartingCards() {
		objDeck.drawBuildings(buildingsOnBoard.length);
		if(true);
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
	protected boolean adjustGood(Player player, GoodType goodType, int quantity) {
		int updated = player.getGoodsQuantity(goodType) + quantity;
		if (updated < 0) {
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
	protected boolean swapGoods(Player player, GoodType g1, int q1, GoodType g2, int q2) {
		if (q1 <= 0 || q2 <= 0) {
			throw new IllegalArgumentException("Quantities must be positive.");
		}
		if (!adjustGood(player, g1, -q1)) {
			return false;
		}
		adjustGood(player, g2, q2); // a gain can never fail
		return true;
	}


	/**
	 * Action: Shift a Good Marker to a certain position<p>
	 * Moves a good's marker to the given market position.
	 * Market decides how to validate/clamp the position and how it treats
	 * other markers (it owns that private state).
	 */
	protected void shiftGoodMarker(GoodType goodType, int goodPosition) {
		objMarket.shift(goodType, goodPosition);
	}


	/**
	 * Action: Shift a Good Marker to the lowest position in the market
	 */
	protected void shiftToLowest(GoodType goodType) {
		shiftGoodMarker(goodType, Market.LOWEST_POSITION);
	}

	/**
	 * Action: Shift a Good Marker to the highest position in the market
	 */
	protected void shiftToHighest(GoodType goodType) {
		shiftGoodMarker(goodType, Market.HIGHEST_POSITION);
	}


	protected void swapGoodMarkers(GoodType g1, GoodType g2) {
	    if (g1 == g2) {
	        throw new IllegalArgumentException("Cannot swap a marker with itself.");
	    }
	    objMarket.swap(g1, g2);
	}

	// ===== 6. Market: redefine every position at once ==========================

	protected void rearrangeMarket(GoodType g1, int p1, GoodType g2, int p2, GoodType g3, int p3, GoodType g4, int p4) {
		// Cheap sanity checks
		// Each good must appear exactly once.
		if (g1 == g2 || g1 == g3 || g1 == g4 || g2 == g3 || g2 == g4 || g3 == g4) {
			throw new IllegalArgumentException("Each good type must appear exactly once.");
		}
		// No two goods can be at the same position.
		if (p1 == p2 || p1 == p3 || p1 == p4 || p2 == p3 || p2 == p4 || p3 == p4) {
			throw new IllegalArgumentException("No two goods can be at the same position.");
		}
		objMarket.setGoodsTracker(g1, p1);
		objMarket.setGoodsTracker(g2, p2);
		objMarket.setGoodsTracker(g3, p3);
		objMarket.setGoodsTracker(g4, p4);
	}


	/**
	 * Remark: Cannot perform the entirety of "Buy a Building" in Board as it involves value checking.
	 * Can perform purely "Buy a building" action
	 * // TODO: define the "Buy a building" action. In short, in ONE action:
	 * //   1. calculate the profit from selling goods at the market
	 * //   2. use that profit to buy a building
	 * //   3. shift the market's good markers
	 */
	protected void processBuyABuilding(Player player, Building boughtBuilding, GoodType[] goodsSold) {
		player.obtainBuilding(boughtBuilding);
		objMarket.shiftMarket(goodsSold[0], goodsSold[1], goodsSold[2], goodsSold[3]);
	}


//	************************************************************
//	Getters and Setters
//	************************************************************

	public Deck getObjDeck() {
		return objDeck;
	}

	public void setObjDeck(Deck objDeck) {
		this.objDeck = objDeck;
	}

	public Market getObjMarket() {
		return objMarket;
	}

	public void setObjMarket(Market objMarket) {
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