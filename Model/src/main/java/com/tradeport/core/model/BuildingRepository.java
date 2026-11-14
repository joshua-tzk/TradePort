package com.tradeport.core.model;

public interface BuildingRepository extends ConstantsLibrary{


	/***
	 * Enum: Building<p>
	 * Includes all buildings that can be visited during the game.
	 * This includes building cards in the deck and starting buildings of each characters.
	 * <p>Keep note that the Dockhand has two anchor perks. Implement this in the player initialiser
	 */
	public enum Building {
		
//		************************************************************
//		Building Enum List
//		************************************************************
		// Starting Building for Character: Training Dummy (cuz why not put him as card no. zero)
		Training_Dummy("Training Dummy", true, false, false, false),
		// Building Card to be Put into the Deck
		Abbey("Abbey", 11, 11, false, false, true, false),
		Alchemist_Shop("Alchemist Shop", 6, 5, true, false, false, false),
		Architectural_Society("Architectural Society", 10, 10, false, true, false, false),
		Bait_Shop("Bait Shop", 8, 8, false, false, false, true),
		Bank("Bank", 10, 10, false, true, false, false),
		Blacksmith("Blacksmith", 6, 5, true, false, false, false),
		Butcher("Butcher", 7, 7, false, false, false, true),
		Canal_Lock("Canal Lock", 9, 9, true, false, false, false),
		Carpenters_Guild("Carpenter's Guild", 11, 11, true, false, false, false),
		Cartographers("Cartographers", 10, 9, true, false, true, false),
		Clocktower("Clocktower", 12, 12, true, true, false, false),
		Fish_Market("Fish Market", 11, 13, false, false, false, true),
		Floating_Dock("Floating Dock", 10, 9, false, false, true, true),
		Foreign_Trade_Office("Foreign Trade Office", 9, 8, false, true, false, false),
		Ghost("Ghost", 0, 0, false, false, false, false),
		Golem_Crafters("Golem Crafters", 6, 6, false, false, false, true),
		House_of_Weights_and_Standards("House of Weights and Standards", 6, 6, false, false, false, true),
		Inn("Inn", 7, 8, false, false, false, true),
		Library("Library", 10, 10, false, false, true, false),
		Lighthouse("Lighthouse", 10, 10, false, false, true, false),
		Lumber_Yard("Lumber Yard", 8, 6, false, false, true, false),
		Masoners_Hall("Masoner's Hall", 9, 8, false, true, false, true),
		Privateer_Ship("Privateer Ship", 9, 9, true, false, true, false),
		Pub("Pub", 7, 5, false, false, true, false),
		Quarry("Quarry", 8, 6, false, true, false, false),
		Ranch("Ranch", 7, 5, false, true, false, false),
		Salvage_Yard("Salvage Yard", 9, 8, false, true, false, false),
		Seaside_Crane("Seaside Crane", 11, 10, false, true, false, true),
		Shipbuilders_Guild("Shipbuilder's Guild", 9, 9, false, false, false, true),
		Stock_Market("Stock Market", 11, 11, false, true, false, false),
		Storehouse("Storehouse", 10, 8, true, false, true, false),
		Sushi_Shop("Sushi Shop", 8, 7, true, false, false, false),
		Tax_Office("Tax Office", 11, 10, false, true, false, true),
		Traders_Guild("Traders Guild", 10, 11, true, false, false, false),
		Wizards_Traveling_Imaginarium("Wizard's Traveling Imaginarium", 0, 0, false, false, false, false),
		Woodworkers_Hut("Woodworker's Hut", 7, 7, false, false, false, true),
		//Remaining Character's Starting Buildings
		Alchemist("Alchemist", true, false, false, false),
		Bookkeeper("Bookkeeper", false, true, false, false),
		Clockwork_Tradesman("Clockwork Tradesman", true, false, false, false),
		Diplomat("Diplomat", true, false, true, false),
		Dockhand("Dockhand", true, false, false, false),
		Drover("Drover", true, false, false, false), // Remark: This character has two starting Anchor perks
		Efficiency_Expert("Efficiency Expert", true, false, false, true),
		Entrepreneur("Entrepreneur", true, false, false, true),
		Investor("Investor", false, true, false, false),
		Lord_Mayor("Lord Mayor", true, false, false, false),
		Neighbour("Neighbour", false, false, false, true),
		Penny_Pincher("Penny Pincher", false, false, false, true),
		Structural_Engineer("Structural Engineer", true, false, false, false),
		The_Wharfs("The Wharfs", true, false, false, false),
		Travel_Agent("Travel Agent", true, false, false, true);


//		************************************************************
//		Attributes
//		************************************************************

		private String name = null;
		private int cost;
		private int victoryPointsAwarded;
		// IMPORTANT: The sequence for perks awarded is Anchor, Coin, Top Hat and Warehouse respectively
		boolean perksAwarded[] = new boolean[4];
		// True for characters' starting buildings (incl. Training_Dummy), false for deck cards
		private final boolean isCharacter;


//		************************************************************
//		Constructors
//		************************************************************
		// For Building Cards
		private Building(String name, int cost, int victoryPointsAwarded, boolean anchorPerk, boolean coinPerk, boolean topHatPerk, boolean warehousePerk) {
			this.name = name;
			this.cost = cost;
			this.victoryPointsAwarded = victoryPointsAwarded;
			this.perksAwarded[Perk.ANCHOR_PERK.ordinal()] = anchorPerk;
			this.perksAwarded[Perk.COIN_PERK.ordinal()] = coinPerk;
			this.perksAwarded[Perk.TOP_HAT_PERK.ordinal()] = topHatPerk;
			this.perksAwarded[Perk.WAREHOUSE_PERK.ordinal()] = warehousePerk;
			this.isCharacter = false;
		}
		// For Characters' Starting Buildings
		private Building(String name, boolean anchorPerk, boolean coinPerk, boolean topHatPerk, boolean warehousePerk) {
			this.name = name;
			this.cost = 0;
			this.victoryPointsAwarded = 0;
			this.perksAwarded[Perk.ANCHOR_PERK.ordinal()] = anchorPerk;
			this.perksAwarded[Perk.COIN_PERK.ordinal()] = coinPerk;
			this.perksAwarded[Perk.TOP_HAT_PERK.ordinal()] = topHatPerk;
			this.perksAwarded[Perk.WAREHOUSE_PERK.ordinal()] = warehousePerk;
			this.isCharacter = true;
			// Setting up name
			this.name = getName();
		}


//		************************************************************
//		Methods
//		************************************************************

		public boolean onBoard(Building[] buildingsOnBoard) {
			for(Building building : buildingsOnBoard)
				if(this == building) return true;
			return false;
		}


//		************************************************************
//		Getters & Setters
//		************************************************************

		// Special Getter: getDetailsinString(): Returns all details of a building
		public String getDetailsinString() {
			String name = getName();
			String cost = "Cost: " + String.valueOf(getCost()) + "\n";
			String victoryPointsAwarded = "Victory Points Awarded: " + String.valueOf(getVictoryPointsAwarded()) + "\n";
			String perks = "Anchor Perk: " + perksAwarded[0] + "\n";
			perks += "Coin Perk: " + perksAwarded[1] + "\n";
			perks += "Top Hat Perk: " + perksAwarded[2] + "\n";
			perks += "Warehouse Perk: " + perksAwarded[3] + "\n";
			
			return name + cost + victoryPointsAwarded + perks;
		}
		
		// General Getters & Setters
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

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

		/** True if this building is a character's starting building (not a deck card). */
		public boolean isCharacter() {
			return isCharacter;
		}
	};

}
