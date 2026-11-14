package com.tradeport.core.engine;

import com.tradeport.core.model.ConstantsLibrary.GoodType;
import java.util.Arrays;

public class GameConfig {

	private final boolean theWharfsOnly;
	private final boolean soloMode;
	private final boolean inlandTradersExpansion;
	private final boolean bonusCardsIncluded;
	private final int numOfPlayers;

	// Starting goods per player: startingGoods[i] holds exactly two GoodTypes for player i
	private final GoodType[][] startingGoods;

	/**
	 * @param theWharfsOnly if true, every player uses The Wharfs as her character
	 * @param soloMode if true, a single player faces Training_Dummy alone
	 * @param inlandTradersExpansion whether the Inland Traders expansion is enabled
	 * @param bonusCardsIncluded whether bonus good cards are in play
	 * @param numOfPlayers number of players in the session
	 * @param startingGoods two starting goods per player, in player order
	 */
	public GameConfig(boolean theWharfsOnly, boolean soloMode, boolean inlandTradersExpansion, boolean bonusCardsIncluded, int numOfPlayers, GoodType[]... startingGoods) {
		if (numOfPlayers < 1) {
			throw new IllegalArgumentException("A game needs at least one player.");
		}
		if (soloMode && numOfPlayers != 1) {
			throw new IllegalArgumentException("Solo mode is for exactly one player.");
		}
		if (startingGoods == null || startingGoods.length != numOfPlayers) {
			throw new IllegalArgumentException("Starting goods must be provided for each of the " + numOfPlayers + " players.");
		}
		for (GoodType[] goods : startingGoods) {
			if (goods == null || goods.length != 2 || goods[0] == null || goods[1] == null) {
				throw new IllegalArgumentException("Each player must begin with exactly two goods.");
			}
		}
		this.theWharfsOnly = theWharfsOnly;
		this.soloMode = soloMode;
		this.inlandTradersExpansion = inlandTradersExpansion;
		this.bonusCardsIncluded = bonusCardsIncluded;
		this.numOfPlayers = numOfPlayers;
		this.startingGoods = new GoodType[numOfPlayers][];
		for (int i = 0; i < numOfPlayers; i++) {
			this.startingGoods[i] = Arrays.copyOf(startingGoods[i], 2);
		}
	}

	public boolean isTheWharfsOnly() {
		return theWharfsOnly;
	}

	public boolean isSoloMode() {
		return soloMode;
	}

	public boolean isInlandTradersExpansion() {
		return inlandTradersExpansion;
	}

	public boolean isBonusCardsIncluded() {
		return bonusCardsIncluded;
	}

	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	/** @return copy of the per-player starting goods (two entries per player) */
	public GoodType[][] getStartingGoods() {
		GoodType[][] copy = new GoodType[numOfPlayers][];
		for (int i = 0; i < numOfPlayers; i++) {
			copy[i] = Arrays.copyOf(startingGoods[i], 2);
		}
		return copy;
	}
}
