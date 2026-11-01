package com.tradeport.core.engine;

public class GameConfig {
	
	boolean theWharfsOnly;
	boolean inlandTradersExpansion;
	boolean bonusCardsIncluded;
	int numOfPlayers;

	public GameConfig(boolean inlandTradersExpansion, boolean bonusCardsIncluded, int numOfPlayers) {
		this.inlandTradersExpansion = inlandTradersExpansion;
		this.bonusCardsIncluded = bonusCardsIncluded;
		this.numOfPlayers = numOfPlayers;
	}
}
