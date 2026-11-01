package com.tradeport.core.model;

public interface BuildingRepository extends Enum{

	
	/***
	 * Enum: Building<p>
	 * Includes all buildings that can be visited during the game.
	 * This includes building cards in the deck and starting buildings of each characters.
	 * <p>Keep note that the Dockhand has two anchor perks. Implement this in the {@link Player#Player(com.tradeport.core.model.Enum.GoodType, com.tradeport.core.model.Enum.GoodType) Player} constructor
	 */
	public enum Building {
		
//		************************************************************
//		Building Enum List
//		************************************************************
		// Starting Building for Character: Training Dummy (cuz why not put him as card no. zero)
		Training_Dummy(true, false, false, false),
		// Building Card to be Put into the Deck
		Abbey(11, 11, false, false, true, false),
		Alchemist_Shop(6, 5, true, false, false, false),
		Architectural_Society(10, 10, false, true, false, false),
		Bait_Shop(8, 8, false, false, false, true),
		Bank(10, 10, false, true, false, false),
		Blacksmith(6, 5, true, false, false, false),
		Butcher(7, 7, false, false, false, true),
		Canal_Lock(9, 9, true, false, false, false),
		Carpenters_Guild(11, 11, true, false, false, false),
		Cartographers(10, 9, true, false, true, false),
		Clocktower(12, 12, true, true, false, false),
		Fish_Market(11, 13, false, false, false, true),
		Floating_Dock(10, 9, false, false, true, true),
		Foreign_Trade_Office(9, 8, false, true, false, false),
		Ghost(0, 0, false, false, false, false),
		Golem_Crafters(6, 6, false, false, false, true),
		House_of_Weights_and_Standards(6, 6, false, false, false, true),
		Inn(7, 8, false, false, false, true),
		Library(10, 10, false, false, true, false),
		Lighthouse(10, 10, false, false, true, false),
		Lumber_Yard(8, 6, false, false, true, false),
		Masoners_Hall(9, 8, false, true, false, true),
		Privateer_Ship(9, 9, true, false, true, false),
		Pub(7, 5, false, false, true, false),
		Quarry(8, 6, false, true, false, false),
		Ranch(7, 5, false, true, false, false),
		Salvage_Yard(9, 8, false, true, false, false),
		Seaside_Crane(11, 10, false, true, false, true),
		Shipbuilders_Guild(9, 9, false, false, false, true),
		Stock_Market(11, 11, false, true, false, false),
		Storehouse(10, 8, true, false, true, false),
		Sushi_Shop(8, 7, true, false, false, false),
		Tax_Office(11, 10, false, true, false, true),
		Traders_Guild(10, 11, true, false, false, false),
		Wizards_Traveling_Imaginarium(0, 0, false, false, false, false),
		Woodworkers_Hut(7, 7, false, false, false, true),
		//Remaining Character's Starting Buildings
		Alchemist(true, false, false, false),
		Bookkeeper(false, true, false, false),
		Clockwork_Tradesman(true, false, false, false),
		Diplomat(true, false, true, false),
		Dockhand(true, false, false, false),
		Drover(true, false, false, false), // Remark: This character has two starting Anchor perks
		Efficiency_Expert(true, false, false, true),
		Entrepreneur(true, false, false, true),
		Investor(false, true, false, false),
		Lord_Mayor(true, false, false, false),
		Neighbour(false, false, false, true),
		Penny_Pincher(false, false, false, true),
		Structural_Engineer(true, false, false, false),
		The_Wharfs(true, false, false, false),
		Travel_Agent(true, false, false, true);


//		************************************************************
//		Attributes
//		************************************************************

		private int cost;
		private int victoryPointsAwarded;
		// IMPORTANT: The sequence for perks awarded is Anchor, Coin, Top Hat and Warehouse respectively
		boolean perksAwarded[] = new boolean[4];


//		************************************************************
//		Constructors
//		************************************************************
		// For Building Cards
		private Building(int cost, int victoryPointsAwarded, boolean anchorPerk, boolean coinPerk, boolean topHatPerk, boolean warehousePerk) {
			this.cost = cost;
			this.victoryPointsAwarded = victoryPointsAwarded;
			this.perksAwarded[Perk.ANCHOR_PERK.ordinal()] = anchorPerk;
			this.perksAwarded[Perk.COIN_PERK.ordinal()] = coinPerk;
			this.perksAwarded[Perk.TOP_HAT_PERK.ordinal()] = topHatPerk;
			this.perksAwarded[Perk.WAREHOUSE_PERK.ordinal()] = warehousePerk;
		}
		// For Characters' Starting Buildings
		private Building(boolean anchorPerk, boolean coinPerk, boolean topHatPerk, boolean warehousePerk) {
			this.cost = 0;
			this.victoryPointsAwarded = 0;
			this.perksAwarded[Perk.ANCHOR_PERK.ordinal()] = anchorPerk;
			this.perksAwarded[Perk.COIN_PERK.ordinal()] = coinPerk;
			this.perksAwarded[Perk.TOP_HAT_PERK.ordinal()] = topHatPerk;
			this.perksAwarded[Perk.WAREHOUSE_PERK.ordinal()] = warehousePerk;
		}


//		************************************************************
//		Getters and Setters
//		************************************************************

		// Keep this commented for potential future use
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}

		public int getCost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public int getVictoryPointsAwarded() {
			return victoryPointsAwarded;
		}

		public void setVictoryPointsAwarded(int victoryPointsAwarded) {
			this.victoryPointsAwarded = victoryPointsAwarded;
		}

		public boolean[] getPerksAwarded() {
			return perksAwarded;
		}

		public void setPerksAwarded(boolean[] perksAwarded) {
			this.perksAwarded = perksAwarded;
		}
	};

}
