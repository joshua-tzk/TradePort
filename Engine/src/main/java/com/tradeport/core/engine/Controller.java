package com.tradeport.core.engine;

import com.tradeport.core.model.Board;
import com.tradeport.core.model.CharacterPool;
import com.tradeport.core.model.BuildingRepository.Building;
import com.tradeport.core.model.ConstantsLibrary.GoodType;
import com.tradeport.core.model.Player;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * This is the class that opens a game session, which "opens" a game board.
 * This will be the class that regulates all performable actions to all pieces on the Board.
 * This includes triggering actions when moved to a building and buying a building.
 * Validations will also be performed depending on the actions available.
 * Actions will be logged by invoking the Logger methods.
 */
public class Controller {

	int sessionId;
	GameConfig objGameConfig;
	Board objBoard;
	Validator objValidator;
	private PlayerInput playerInput;
	private final SecureRandom random = new SecureRandom();

	// Character draft state (null once the draft is finished)
	private CharacterPool characterPool;
	private Building[][] draftHands;
	private Player[] players;
	private int playersChosen;


//	************************************************************
//	Constructor & Game Setup
//	************************************************************

	public Controller(GameConfig gameConfig) {
		this.objGameConfig = gameConfig;
		this.objBoard = new Board(gameConfig.getNumOfPlayers());
		this.objValidator = new Validator(objBoard);
		setupGame();
	}

	/**
	 * Sets up the game session from the GameConfig:
	 * - The Wharfs mode: every player gets The Wharfs, no draft
	 * - Solo mode: the single player gets Training_Dummy, no draft
	 * - Otherwise: each player is dealt a draft hand of two characters.
	 *   The draft itself is driven by the user interface through
	 *   {@link #getDraftHand} and {@link #chooseCharacter}; check
	 *   {@link #isDraftPending} to know whether choices are still required.
	 */
	private void setupGame() {
		int numOfPlayers = objGameConfig.getNumOfPlayers();
		players = new Player[numOfPlayers];

		if (objGameConfig.isTheWharfsOnly()) {
			for (int i = 0; i < numOfPlayers; i++) {
				players[i] = createPlayer(i, Building.The_Wharfs);
			}
			finishSetup();
		} else if (objGameConfig.isSoloMode()) {
			players[0] = createPlayer(0, Building.Training_Dummy);
			finishSetup();
		} else {
			characterPool = new CharacterPool();
			draftHands = new Building[numOfPlayers][];
			for (int i = 0; i < numOfPlayers; i++) {
				draftHands[i] = characterPool.dealDraftHand();
			}
			playersChosen = 0;
		}
	}

	private Player createPlayer(int playerIndex, Building character) {
		GoodType[][] startingGoods = objGameConfig.getStartingGoods();
		GoodType bonusCardGoodType = objGameConfig.isBonusCardsIncluded() ? randomGoodType() : null;
		return Player.createPlayer(playerIndex + 1, character,
				startingGoods[playerIndex][0], startingGoods[playerIndex][1], bonusCardGoodType);
	}

	/**
	 * Player picks one character from her draft hand. The unchosen character
	 * returns to the pool. When every player has chosen, the game setup finishes.
	 * @param playerId player id (1-based, matches player order)
	 * @param handIndex 0 or 1: which card of the draft hand to take
	 */
	public void chooseCharacter(int playerId, int handIndex) {
		if (draftHands == null) {
			throw new IllegalStateException("There is no character draft in this game mode.");
		}
		int index = playerId - 1;
		if (index < 0 || index >= players.length || players[index] != null) {
			throw new IllegalArgumentException("Invalid or already-resolved player id: " + playerId);
		}
		if (handIndex < 0 || handIndex >= draftHands[index].length) {
			throw new IndexOutOfBoundsException("Draft hand index out of range: " + handIndex);
		}
		Building chosen = draftHands[index][handIndex];
		Building unchosen = draftHands[index][1 - handIndex];
		characterPool.returnCharacter(unchosen);
		players[index] = createPlayer(index, chosen);
		playersChosen++;
		System.out.println("Player " + playerId + " chose " + chosen.getName() + ".");
		if (playersChosen == players.length) {
			finishSetup();
		}
	}

	/** @return the two characters dealt to a player, or null outside draft mode */
	public Building[] getDraftHand(int playerId) {
		if (draftHands == null) {
			return null;
		}
		int index = playerId - 1;
		if (index < 0 || index >= draftHands.length || players[index] != null) {
			return null;
		}
		return draftHands[index];
	}

	private void finishSetup() {
		draftHands = null;
		characterPool = null;
		objBoard.setPlayers(players);
		Building[] buildingsOnBoard = objBoard.dealStartingCards();

		System.out.println("=== Game session started ===");
		for (Player player : players) {
			System.out.println("Player " + player.getPlayerId() + ": " + player.getCharacterName()
					+ " | Goods: " + Arrays.toString(player.getGoodsQuantity())
					+ (player.getBonusCardGoodType() != null ? " | Bonus card: " + player.getBonusCardGoodType() : ""));
		}
		System.out.println("Buildings on board: ");
		for (Building building : buildingsOnBoard) {
			System.out.println("- " + building.getName() + " (cost " + building.getCost() + ")");
		}
	}

	/** @return true while players still have to pick their draft characters */
	public boolean isDraftPending() {
		return draftHands != null;
	}

	/** @return the game board */
	public Board getBoard() {
		return objBoard;
	}

	/** @return the action validator */
	public Validator getValidator() {
		return objValidator;
	}

	/** @return the player with the given id */
	public Player findPlayer(int playerId) {
		return objBoard.findPlayerById(playerId);
	}

	/**
	 * Supplies the input channel used by building effects and other actions
	 * that need decisions from the player. Called by the UI once it exists.
	 */
	public void setPlayerInput(PlayerInput playerInput) {
		this.playerInput = playerInput;
	}

	/**
	 * The input channel for player decisions. Throws if the UI has not
	 * supplied one yet.
	 */
	public PlayerInput getPlayerInput() {
		if (playerInput == null) {
			throw new IllegalStateException("No PlayerInput has been set; the UI must call setPlayerInput first.");
		}
		return playerInput;
	}

	private GoodType randomGoodType() {
		GoodType[] goodTypes = GoodType.values();
		return goodTypes[random.nextInt(goodTypes.length)];
	}


//	************************************************************
//	Game Mechanic Control Methods
//	************************************************************

	/**
	 * Validates the player's goods selection and returns the buildings she can
	 * currently afford. The controller (or UI) uses this to present options.
	 */
	public Building[] getBuyableBuildings(int playerId, GoodType[] goodsSold) {
		Player buyingPlayer = objBoard.findPlayerById(playerId);
		objValidator.validateGoodsSold(buyingPlayer, goodsSold);
		int maximumValue = objBoard.playerCouldSellGoods(buyingPlayer, goodsSold);
		return objValidator.checkBuyableBuildings(maximumValue);
	}

	/**
	 * Buys a building: re-validates everything (goods selection, index, funds),
	 * then lets the board perform the finalising action.
	 * @param choiceIndex index into the list returned by {@link #getBuyableBuildings}
	 * @return the building that was bought
	 */
	public Building buyBuilding(int playerId, GoodType[] goodsSold, int choiceIndex) {
		Player buyingPlayer = objBoard.findPlayerById(playerId);
		Building buildingToBuy = objValidator.validateBuyableBuildingByIndex(buyingPlayer, choiceIndex, goodsSold);
		objBoard.buyBuildingWithGoods(buyingPlayer, buildingToBuy, goodsSold);
		return buildingToBuy;
	}


	// Visiting a building and triggering its effect are paired here, but the
	// two steps are also exposed separately (see triggerBuildingAction) because
	// some character abilities trigger a building's effect without moving there.
	public void visitBuilding(Player currentPlayer, Building visitedBuilding) {
		objValidator.validateBuildingVisit(currentPlayer, visitedBuilding);
		currentPlayer.setLocation(visitedBuilding);
		triggerBuildingAction(currentPlayer, visitedBuilding);
	}

	/**
	 * Full visit turn for a player:
	 * 1. Requests which building on the board she wants to visit
	 * 2. Validates the choice and moves her there
	 * 3. Triggers the building's effect, which may request further input
	 *    from the player (see PlayerInput) before validating and executing
	 */
	public void performBuildingVisit(int playerId) {
		Player currentPlayer = objBoard.findPlayerById(playerId);
		Building destination = promptVisitDestination(currentPlayer);
		visitBuilding(currentPlayer, destination);
	}

	// Offers the buildings currently on the board as visit destinations
	private Building promptVisitDestination(Player currentPlayer) {
		Building[] onBoard = objBoard.getBuildingsOnBoard();
		int count = 0;
		for (Building building : onBoard) {
			if (building != null) count++;
		}
		Building[] options = new Building[count];
		String[] labels = new String[count];
		count = 0;
		for (Building building : onBoard) {
			if (building != null) {
				options[count] = building;
				labels[count] = building.getName() + " (cost " + building.getCost() + ")";
				count++;
			}
		}
		int choice = getPlayerInput().choose(
				currentPlayer.getCharacterName() + ", choose a building to visit:", labels);
		return options[choice];
	}

	// Convenience for building effects that need the player to pick an option
	protected int askEffectChoice(String prompt, String... options) {
		return getPlayerInput().choose(prompt, options);
	}

	// Dispatches the unique action of the visited building.
	// Can be called on its own when an ability uses another building's effect.
	public void triggerBuildingAction(Player currentPlayer, Building building) {
		BuildingEffects.triggerBuildingAction(this, currentPlayer, building);
	}
	
	
	// Methods to increase or decrease goods
	void increaseGood(Player currentPlayer, GoodType goodType, int quantity) {
		objBoard.increaseGood(currentPlayer, goodType, quantity);
	}

	void decreaseGood(Player currentPlayer, GoodType goodType, int quantity) {
		objBoard.decreaseGood(currentPlayer, goodType, quantity);
	}

	/**
	 * Composite method: Swapping involves decreasing a type of good and increasing another
	 */
	void swapTwoGoods(Player currentPlayer, GoodType goodType1, int quantity1, GoodType goodType2, int quantity2) {
		objBoard.swapGoods(currentPlayer, goodType1, quantity1, goodType2, quantity2);
	}

	/**
	 * Composite action: trades 1 of goodType for 1 of every other good type.
	 * Does nothing if the player owns none of goodType, or if any of the
	 * receiving types is already at the maximum stack size.
	 */
	void swapGoodTypeForEveryOther(Player currentPlayer, GoodType goodType) {
		Player player = getBoard().findPlayerById(currentPlayer.getPlayerId());
		if (player.getGoodsQuantity(goodType) < 1) {
			return;
		}
		for (GoodType other : GoodType.values()) {
			if (other != goodType && player.getGoodsQuantity(other) >= Player.MAX_GOODS_PER_TYPE) {
				return; // a receiving stack is full; the swap cannot complete
			}
		}
		decreaseGood(player, goodType, 1);
		for (GoodType other : GoodType.values()) {
			if (other != goodType) {
				increaseGood(player, other, 1);
			}
		}
	}
}
