package com.tradeport.core.model;

public class Player implements Enum, BuildingRepository {
	
	// Exceptions
	private static final Exception NoPlayerFoundException = null;

//	************************************************************
//	Attributes
//	************************************************************
	
	// Player ID: Unique, used for identification and turn control.
	private int playerId;
	// List of goods, ordered by fish, meat, stone and wood (order is fixed by Enum)
	protected int goodsQuantity[]= {0, 0, 0, 0};
	// Building and quantity tracker
	// Assign Character to player as first owned building and its first location
	protected Building ownedBuildings[] = {null, null, null, null, null};
	protected int buildingCount;
	// Location: Starts at character-owned building
	protected Building location;
	// Perks obtained throughout the game
	protected int anchorPerk = 0, coinPerk = 0, warehousePerk = 0;
	protected boolean topHatPerk = false;
	// Victory Points
	protected int victoryPoints;
	// Used if bonus cards are used
	protected GoodType bonusCardGoodType = null;
	
	
	/**
	 * Constructor
	 * @param playerId Assigns Player Id
	 * @param character Assigns character to player as first building owned
	 * (each character has a starting building).
	 * Also assigns the character's starting building as her starting location
	 * @param goodType1 Player begins the game with two good tokens
	 * @param goodType2
	 * @param bonusCardGoodType If there's a bonus good card, then we assign an enum accordingly
	 */
	protected void initialisePlayer (int playerId, Building character, GoodType goodType1, GoodType goodType2, GoodType bonusCardGoodType) {
		this.playerId = playerId;
		this.ownedBuildings[0] = character;
		this.location = ownedBuildings[0];
		this.buildingCount = 1;
		this.location = ownedBuildings[0];
		this.gainPerks(ownedBuildings[0]);
		this.victoryPoints = 0;
		this.goodsQuantity[goodType1.ordinal()] = 1;
		this.goodsQuantity[goodType2.ordinal()] = 1;
		if(bonusCardGoodType != null)
			this.bonusCardGoodType = bonusCardGoodType;
	}


	public int calculateBonusPoints() {
		return (bonusCardGoodType != null) ? (BONUS_VICTORY_POINTS[goodsQuantity[bonusCardGoodType.ordinal()]]) : 00;
	}


//	************************************************************
//	Constructor
//	************************************************************
	
	/** Constructing a player based on her Player ID */
	Player(int playerId) {
		this.playerId = playerId;
	}
	


//	************************************************************
//	Special: Initialiser
//	************************************************************
	
	// Constructs a blank player
	Player() {
		this.playerId = 0;
	}
	
	static Player findPlayerById(Player[] players, int playerId) throws Exception {
		Player buyingPlayer = new Player();
		for(Player player : players) if(player.getPlayerId() == playerId) buyingPlayer = player;
		if(buyingPlayer.getPlayerId() == 0) throw NoPlayerFoundException;
		return buyingPlayer;
	}
	

//	************************************************************
//	Methods on Gaining Perks
//	************************************************************

	public void gainAnchorPerk() {
		this.anchorPerk ++;
	}

	public void gainCoinPerk() {
		this.coinPerk ++;
	}

	public void gainTopHatPerk() {
		this.topHatPerk = true;
	}

	public void gainWarehousePerk() {
		this.warehousePerk ++;
	}



//	************************************************************
//	Actions that Affect the Stats of a Player
//	************************************************************

	/**
	 * Action: Player obtain a building
	 * @param obtainedBuilding
	 * @return
	 */
	protected void obtainBuilding(Building obtainedBuilding) {
		// Remark: buildingCount starts from 1, the second index of the int array
		ownedBuildings[buildingCount] = obtainedBuilding;
		gainPerks(obtainedBuilding);
		buildingCount++;
	}

	/*
	 * Action: Player gains perks when obtaining a building.
	 * Used every time a player obtains a building.
	 */
	protected void gainPerks(Building obtainedBuilding) {
		boolean[] perksAwarded = obtainedBuilding.getPerksAwarded();
		if(perksAwarded[0] == true) anchorPerk++;
		if(perksAwarded[1] == true) coinPerk++;
		if(perksAwarded[2] == true) topHatPerk = true;
		if(perksAwarded[3] == true) warehousePerk++;
	}


//	************************************************************
//	Getters & Setters
//	************************************************************

	// Specific Getters and Setters
	public int getGoodsQuantity(GoodType goodType) {
		return goodsQuantity[goodType.ordinal()];
	}

	public void setGoodsQuantity(GoodType goodType, int quantity) {
		this.goodsQuantity[goodType.ordinal()] = quantity;
	}

	public String getCharacterName() { // Special: Get character name
		return ownedBuildings[0].getName();
	}


	// General Getters & Setters
	public int getPlayerId() {
		return playerId;
	}

	// This prevents other classes from calling it
	private void setPlayerId(int playerId) {
		this.playerId = playerId;
	}


	public int[] getGoodsQuantity() {
		return goodsQuantity;
	}

	public void setGoodsQuantity(int[] goodsQuantity) {
		this.goodsQuantity = goodsQuantity;
	}


	public Building[] getOwnedBuildings() {
		return ownedBuildings;
	}

	public void setOwnedBuildings(Building[] ownedBuildings) {
		this.ownedBuildings = ownedBuildings;
	}


	public Building getLocation() {
		return location;
	}

	public void setLocation(Building location) {
		this.location = location;
	}


	public int getBuildingCount() {
		return buildingCount;
	}

	public void setBuildingCount(int buildingCount) {
		this.buildingCount = buildingCount;
	}


	public int getCoinPerk() {
		return coinPerk;
	}

	public void setCoinPerk(int coinPerk) {
		this.coinPerk = coinPerk;
	}


	public int getAnchorPerk() {
		return anchorPerk;
	}

	public void setAnchorPerk(int anchorPerk) {
		this.anchorPerk = anchorPerk;
	}


	public int getWarehousePerk() {
		return warehousePerk;
	}

	public void setWarehousePerk(int warehousePerk) {
		this.warehousePerk = warehousePerk;
	}


	public boolean isTopHatPerk() {
		return topHatPerk;
	}

	public void setTopHatPerk(boolean topHatPerk) {
		this.topHatPerk = topHatPerk;
	}


	public GoodType getBonusCardGoodType() {
		return bonusCardGoodType;
	}

	public void setBonusCardGoodType(GoodType bonusCardGoodType) {
		this.bonusCardGoodType = bonusCardGoodType;
	}

}
