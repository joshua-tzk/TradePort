package com.tradeport.core.engine;

import com.tradeport.core.model.BuildingRepository.Building;
import com.tradeport.core.model.Player;

/**
 * Static dispatch and implementations of building visit effects.
 * Each xxxActionEvent receives the Controller so effects can reach
 * the board, validator and player-input channel.
 */
public final class BuildingEffects {

	private BuildingEffects() {
	}

	// Dispatches the unique action of the visited building.
	// Can be called on its own when an ability uses another building's effect.
	public static void triggerBuildingAction(Controller controller, Player currentPlayer, Building building) {
		switch (building) {
		case Training_Dummy: trainingDummyActionEvent(controller, currentPlayer); break;
		case Abbey: abbeyActionEvent(controller, currentPlayer); break;
		case Alchemist_Shop: alchemistShopActionEvent(controller, currentPlayer); break;
		case Architectural_Society: architecturalSocietyActionEvent(controller, currentPlayer); break;
		case Bait_Shop: baitShopActionEvent(controller, currentPlayer); break;
		case Bank: bankActionEvent(controller, currentPlayer); break;
		case Blacksmith: blacksmithActionEvent(controller, currentPlayer); break;
		case Butcher: butcherActionEvent(controller, currentPlayer); break;
		case Canal_Lock: canalLockActionEvent(controller, currentPlayer); break;
		case Carpenters_Guild: carpentersGuildActionEvent(controller, currentPlayer); break;
		case Cartographers: cartographersActionEvent(controller, currentPlayer); break;
		case Clocktower: clocktowerActionEvent(controller, currentPlayer); break;
		case Fish_Market: fishMarketActionEvent(controller, currentPlayer); break;
		case Floating_Dock: floatingDockActionEvent(controller, currentPlayer); break;
		case Foreign_Trade_Office: foreignTradeOfficeActionEvent(controller, currentPlayer); break;
		case Ghost: ghostActionEvent(controller, currentPlayer); break;
		case Golem_Crafters: golemCraftersActionEvent(controller, currentPlayer); break;
		case House_of_Weights_and_Standards: houseOfWeightsAndStandardsActionEvent(controller, currentPlayer); break;
		case Inn: innActionEvent(controller, currentPlayer); break;
		case Library: libraryActionEvent(controller, currentPlayer); break;
		case Lighthouse: lighthouseActionEvent(controller, currentPlayer); break;
		case Lumber_Yard: lumberYardActionEvent(controller, currentPlayer); break;
		case Masoners_Hall: masonersHallActionEvent(controller, currentPlayer); break;
		case Privateer_Ship: privateerShipActionEvent(controller, currentPlayer); break;
		case Pub: pubActionEvent(controller, currentPlayer); break;
		case Quarry: quarryActionEvent(controller, currentPlayer); break;
		case Ranch: ranchActionEvent(controller, currentPlayer); break;
		case Salvage_Yard: salvageYardActionEvent(controller, currentPlayer); break;
		case Seaside_Crane: seasideCraneActionEvent(controller, currentPlayer); break;
		case Shipbuilders_Guild: shipbuildersGuildActionEvent(controller, currentPlayer); break;
		case Stock_Market: stockMarketActionEvent(controller, currentPlayer); break;
		case Storehouse: storehouseActionEvent(controller, currentPlayer); break;
		case Sushi_Shop: sushiShopActionEvent(controller, currentPlayer); break;
		case Tax_Office: taxOfficeActionEvent(controller, currentPlayer); break;
		case Traders_Guild: tradersGuildActionEvent(controller, currentPlayer); break;
		case Wizards_Traveling_Imaginarium: wizardsTravelingImaginariumActionEvent(controller, currentPlayer); break;
		case Woodworkers_Hut: woodworkersHutActionEvent(controller, currentPlayer); break;
		case Alchemist: alchemistActionEvent(controller, currentPlayer); break;
		case Bookkeeper: bookkeeperActionEvent(controller, currentPlayer); break;
		case Clockwork_Tradesman: clockworkTradesmanActionEvent(controller, currentPlayer); break;
		case Diplomat: diplomatActionEvent(controller, currentPlayer); break;
		case Dockhand: dockhandActionEvent(controller, currentPlayer); break;
		case Drover: droverActionEvent(controller, currentPlayer); break;
		case Efficiency_Expert: efficiencyExpertActionEvent(controller, currentPlayer); break;
		case Entrepreneur: entrepreneurActionEvent(controller, currentPlayer); break;
		case Investor: investorActionEvent(controller, currentPlayer); break;
		case Lord_Mayor: lordMayorActionEvent(controller, currentPlayer); break;
		case Neighbour: neighbourActionEvent(controller, currentPlayer); break;
		case Penny_Pincher: pennyPincherActionEvent(controller, currentPlayer); break;
		case Structural_Engineer: structuralEngineerActionEvent(controller, currentPlayer); break;
		case The_Wharfs: theWharfsActionEvent(controller, currentPlayer); break;
		case Travel_Agent: travelAgentActionEvent(controller, currentPlayer); break;
		}
	}

	// Effect triggered when a player visits Training_Dummy
	public static void trainingDummyActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Training_Dummy's visit effect
	}

	// Effect triggered when a player visits Abbey
	public static void abbeyActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Abbey's visit effect
	}

	// Effect triggered when a player visits Alchemist_Shop
	public static void alchemistShopActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Alchemist_Shop's visit effect
	}

	// Effect triggered when a player visits Architectural_Society
	public static void architecturalSocietyActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Architectural_Society's visit effect
	}

	// Effect triggered when a player visits Bait_Shop
	public static void baitShopActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Bait_Shop's visit effect
	}

	// Effect triggered when a player visits Bank
	public static void bankActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Bank's visit effect
	}

	// Effect triggered when a player visits Blacksmith
	public static void blacksmithActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Blacksmith's visit effect
	}

	// Effect triggered when a player visits Butcher
	public static void butcherActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Butcher's visit effect
	}

	// Effect triggered when a player visits Canal_Lock
	public static void canalLockActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Canal_Lock's visit effect
	}

	// Effect triggered when a player visits Carpenters_Guild
	public static void carpentersGuildActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Carpenters_Guild's visit effect
	}

	// Effect triggered when a player visits Cartographers
	public static void cartographersActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Cartographers's visit effect
	}

	// Effect triggered when a player visits Clocktower
	public static void clocktowerActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Clocktower's visit effect
	}

	// Effect triggered when a player visits Fish_Market
	public static void fishMarketActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Fish_Market's visit effect
	}

	// Effect triggered when a player visits Floating_Dock
	public static void floatingDockActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Floating_Dock's visit effect
	}

	// Effect triggered when a player visits Foreign_Trade_Office
	public static void foreignTradeOfficeActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Foreign_Trade_Office's visit effect
	}

	// Effect triggered when a player visits Ghost
	public static void ghostActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Ghost's visit effect
	}

	// Effect triggered when a player visits Golem_Crafters
	public static void golemCraftersActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Golem_Crafters's visit effect
	}

	// Effect triggered when a player visits House_of_Weights_and_Standards
	public static void houseOfWeightsAndStandardsActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement House_of_Weights_and_Standards's visit effect
	}

	// Effect triggered when a player visits Inn
	public static void innActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Inn's visit effect
	}

	// Effect triggered when a player visits Library
	public static void libraryActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Library's visit effect
	}

	// Effect triggered when a player visits Lighthouse
	public static void lighthouseActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Lighthouse's visit effect
	}

	// Effect triggered when a player visits Lumber_Yard
	public static void lumberYardActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Lumber_Yard's visit effect
	}

	// Effect triggered when a player visits Masoners_Hall
	public static void masonersHallActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Masoners_Hall's visit effect
	}

	// Effect triggered when a player visits Privateer_Ship
	public static void privateerShipActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Privateer_Ship's visit effect
	}

	// Effect triggered when a player visits Pub
	public static void pubActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Pub's visit effect
	}

	// Effect triggered when a player visits Quarry
	public static void quarryActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Quarry's visit effect
	}

	// Effect triggered when a player visits Ranch
	public static void ranchActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Ranch's visit effect
	}

	// Effect triggered when a player visits Salvage_Yard
	public static void salvageYardActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Salvage_Yard's visit effect
	}

	// Effect triggered when a player visits Seaside_Crane
	public static void seasideCraneActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Seaside_Crane's visit effect
	}

	// Effect triggered when a player visits Shipbuilders_Guild
	public static void shipbuildersGuildActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Shipbuilders_Guild's visit effect
	}

	// Effect triggered when a player visits Stock_Market
	public static void stockMarketActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Stock_Market's visit effect
	}

	// Effect triggered when a player visits Storehouse
	public static void storehouseActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Storehouse's visit effect
	}

	// Effect triggered when a player visits Sushi_Shop
	public static void sushiShopActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Sushi_Shop's visit effect
	}

	// Effect triggered when a player visits Tax_Office
	public static void taxOfficeActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Tax_Office's visit effect
	}

	// Effect triggered when a player visits Traders_Guild
	public static void tradersGuildActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Traders_Guild's visit effect
	}

	// Effect triggered when a player visits Wizards_Traveling_Imaginarium
	public static void wizardsTravelingImaginariumActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Wizards_Traveling_Imaginarium's visit effect
	}

	// Effect triggered when a player visits Woodworkers_Hut
	public static void woodworkersHutActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Woodworkers_Hut's visit effect
	}

	// Effect triggered when a player visits Alchemist
	public static void alchemistActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Alchemist's visit effect
	}

	// Effect triggered when a player visits Bookkeeper
	public static void bookkeeperActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Bookkeeper's visit effect
	}

	// Effect triggered when a player visits Clockwork_Tradesman
	public static void clockworkTradesmanActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Clockwork_Tradesman's visit effect
	}

	// Effect triggered when a player visits Diplomat
	public static void diplomatActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Diplomat's visit effect
	}

	// Effect triggered when a player visits Dockhand
	public static void dockhandActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Dockhand's visit effect
	}

	// Effect triggered when a player visits Drover
	public static void droverActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Drover's visit effect
	}

	// Effect triggered when a player visits Efficiency_Expert
	public static void efficiencyExpertActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Efficiency_Expert's visit effect
	}

	// Effect triggered when a player visits Entrepreneur
	public static void entrepreneurActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Entrepreneur's visit effect
	}

	// Effect triggered when a player visits Investor
	public static void investorActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Investor's visit effect
	}

	// Effect triggered when a player visits Lord_Mayor
	public static void lordMayorActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Lord_Mayor's visit effect
	}

	// Effect triggered when a player visits Neighbour
	public static void neighbourActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Neighbour's visit effect
	}

	// Effect triggered when a player visits Penny_Pincher
	public static void pennyPincherActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Penny_Pincher's visit effect
	}

	// Effect triggered when a player visits Structural_Engineer
	public static void structuralEngineerActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Structural_Engineer's visit effect
	}

	// Effect triggered when a player visits The_Wharfs
	public static void theWharfsActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement The_Wharfs's visit effect
	}

	// Effect triggered when a player visits Travel_Agent
	public static void travelAgentActionEvent(Controller controller, Player currentPlayer) {
		// TODO: implement Travel_Agent's visit effect
	}

}
