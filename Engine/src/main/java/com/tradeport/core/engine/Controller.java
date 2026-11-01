package com.tradeport.core.engine;

import com.tradeport.core.model.Board;

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
	static Validator objValidator;


//	************************************************************
//	Constructor
//	************************************************************

	public Controller() {
		// TODO Auto-generated constructor stub
	}


//	************************************************************
//	Getters & Setters
//	************************************************************

	void prepareValidation() {
		// Now the validator can get a state of the board every time a validation is needed
		objValidator.setBoard(objBoard);
	}

}
