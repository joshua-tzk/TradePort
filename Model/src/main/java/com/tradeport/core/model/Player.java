package com.tradeport.core.model;

public class Player implements Enum, BuildingRepository {
	
//	************************************************************
//	Attributes
//	************************************************************
	
	// Player ID: Unique, used for identification and turn control.
	private int playerId;
	// List of goods, ordered by fish, meat, stone and wood (order is fixed by Enum)
	protected int goodsQuantity[]= {0, 0, 0, 0};
	// Assign player to startingBuilding as its first owned building and its first location
	protected Building startingBuilding;
	protected Building buildings[] = {null, null, null, null, null};
	protected Building location;
	protected int buildingCount = 1;
	// Perks obtained throughout the game
	protected int anchorPerk = 0, coinPerk = 0, warehousePerk = 0;
	protected boolean topHatPerk = false;

	// Miscellaneous
	protected int victoryPoints = 0;
	protected String characterName = "DUMMY";

	// Used if bonus cards are used
	protected GoodType bonusCardGoodType = null;
	public static final int[] BONUS_VICTORY_POINTS = {0, 1, 1, 2, 3, 4, 5};
	
	
	// If bonus cards are NOT in play, we use this constructor
	protected void initialisePlayer (int playerId, Building character, GoodType goodType1, GoodType goodType2) {
		if(this.playerId != playerId) throw new IllegalArgumentException("Invalid player ID.");
		this.startingBuilding = character;
		buildings[0] = character;
		setStartingGoods(goodType1, goodType2);
	}

	// If bonus cards are in play, we use this constructor
	protected void initialisePlayer(int playerId, Building character, GoodType goodType1, GoodType goodType2, GoodType bonusCardGoodType) {
		if(this.playerId != playerId) throw new IllegalArgumentException("Invalid player ID.");
		this.startingBuilding = character;
		buildings[0] = character;
		setStartingGoods(goodType1, goodType2);
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
		buildings[buildingCount] = obtainedBuilding; // Remark: buildingCount starts from 1, the second index of the int array
		buildingCount++;
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

	/** Method for setting starting goods */
	public void setStartingGoods(GoodType goodType1, GoodType goodType2) {
		this.goodsQuantity[goodType1.ordinal()] = 1;
		this.goodsQuantity[goodType2.ordinal()] = 1;
	}


	// General Getters & Setters
	public int[] getPlayerId() {
		return goodsQuantity;
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


	public Building[] getBuildings() {
		return buildings;
	}

	public void setBuildings(Building[] buildings) {
		this.buildings = buildings;
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


	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}


	public GoodType getBonusCardGoodType() {
		return bonusCardGoodType;
	}

	public void setBonusCardGoodType(GoodType bonusCardGoodType) {
		this.bonusCardGoodType = bonusCardGoodType;
	}

}
