package com.tradeport.core.engine;

import com.tradeport.core.model.Board;

public class Validator {

//	************************************************************
//	Attributes
//	************************************************************

	Board objBoard;





	//	************************************************************
//	Constructor
//	************************************************************

	public Validator() {
		// TODO Auto-generated constructor stub
	}

	/**Validates player accessing a building<p>
	 * Note that if a player is playing the Neighbour character, her building can
	 * use the abilities of any other existing building regardless of whether or
	 * not it is occupied.
	 */
	void validateBuildingAccess() {
		
	}
	


//	************************************************************
//	Getters & Setters
//	************************************************************

	void setBoard(Board objBoard) {
		this.objBoard = objBoard;
	}


}
