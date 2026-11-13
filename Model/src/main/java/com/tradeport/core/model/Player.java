package com.tradeport.core.model;

import java.util.NoSuchElementException;

public class Player implements BuildingRepository {

//	************************************************************
//	Attributes
//	************************************************************

	// Player ID: Unique, used for identification and turn control.
	private int playerId;
	// List of goods, ordered by fish, meat, stone and wood (order is fixed by ConstantsLibrary)
	protected int goodsQuantity[]= {0, 0, 0, 0};
	// Will assign Character to player as first owned building and its first location
	protected Building ownedBuildings[] = {null, null, null, null, null};
	protected int buildingCount;
	protected Building location;
	// Perks obtained throughout the game
	protected int anchorPerk = 0, coinPerk = 0, warehousePerk = 0;
	protected boolean topHatPerk = false;
	// Victory Points
	protected int victoryPoints;
	// Used if bonus cards are used
	protected GoodType bonusCardGoodType = null;

	// By game rules a player can never hold more than this many of the same good
	public static final int MAX_GOODS_PER_TYPE = 6;


//	************************************************************
//	Constructor & Factory
//	************************************************************

	/** Constructing a player based on her Player ID */
	Player(int playerId) {
		this.playerId = playerId;
	}

	// Constructs a blank player
	Player() {
		this.playerId = 0;
	}

	/**
	 * Creates a fully initialised player.
	 * @param playerId unique player id
	 * @param character the player's starting building (her character)
	 * @param goodType1 first starting good token
	 * @param goodType2 second starting good token
	 * @param bonusCardGoodType good type of the bonus card, or null if none
	 */
	public static Player createPlayer(int playerId, Building character, GoodType goodType1, GoodType goodType2, GoodType bonusCardGoodType) {
		if (character == null) {
			throw new IllegalArgumentException("A player must have a character building.");
		}
		if (goodType1 == null || goodType2 == null) {
			throw new IllegalArgumentException("A player must begin with two goods.");
		}
		Player player = new Player(playerId);
		player.initialisePlayer(playerId, character, goodType1, goodType2, bonusCardGoodType);
		return player;
	}

	private void initialisePlayer (int playerId, Building character, GoodType goodType1, GoodType goodType2, GoodType bonusCardGoodType) {
		this.playerId = playerId;
		this.ownedBuildings[0] = character;
		this.location = ownedBuildings[0];
		this.buildingCount = 1;
		this.gainPerks(ownedBuildings[0]);
		this.victoryPoints = 0;
		this.goodsQuantity[goodType1.ordinal()]++;
		this.goodsQuantity[goodType2.ordinal()]++;
		if(bonusCardGoodType != null)
			this.bonusCardGoodType = bonusCardGoodType;
	}


	public int calculateBonusPoints() {
		if (bonusCardGoodType == null) {
			return 0;
		}
		int quantity = goodsQuantity[bonusCardGoodType.ordinal()];
		return BONUS_VICTORY_POINTS[Math.min(quantity, BONUS_VICTORY_POINTS.length - 1)];
	}

	/**
	 * Total victory points: sum of victory points awarded by all owned buildings
	 * plus the bonus-card points. Also stores the result in {@link #victoryPoints}.
	 */
	public int calculateVictoryPoints() {
		int points = calculateBonusPoints();
		for (Building building : ownedBuildings) {
			if (building != null) {
				points += building.getVictoryPointsAwarded();
			}
		}
		this.victoryPoints = points;
		return points;
	}


//	************************************************************
//	Special: Lookup
//	************************************************************

	static Player findPlayerById(Player[] players, int playerId) {
		for (Player player : players) {
			if (player != null && player.getPlayerId() == playerId) {
				return player;
			}
		}
		throw new NoSuchElementException("No player found with id " + playerId);
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
	 */
	protected void obtainBuilding(Building obtainedBuilding) {
		if (buildingCount >= ownedBuildings.length) {
			throw new IllegalStateException("Player " + playerId + " already owns the maximum number of buildings.");
		}
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
		if(perksAwarded[0] == true) anchorPerk++; if(obtainedBuilding == Building.Drover) anchorPerk++; // Drover has two starting Anchor perks
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
		if (quantity > MAX_GOODS_PER_TYPE)
			this.goodsQuantity[goodType.ordinal()] = MAX_GOODS_PER_TYPE;
		else if (quantity < 0)
			this.goodsQuantity[goodType.ordinal()] = 0;
		else
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
		if (goodsQuantity == null || goodsQuantity.length != GoodType.values().length) {
			throw new IllegalArgumentException("Goods quantity must have exactly " + GoodType.values().length + " entries.");
		}
		for (int quantity : goodsQuantity) {
			if (quantity < 0 || quantity > MAX_GOODS_PER_TYPE) {
				throw new IllegalArgumentException("Goods quantity must be between 0 and " + MAX_GOODS_PER_TYPE + ".");
			}
		}
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

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

}
