package com.tradeport.core.model;

import java.security.SecureRandom;

/**
 * The pool of playable characters for a game session.
 * Contains every character building except Training_Dummy
 * (Alchemist up to Travel_Agent). Each character is unique and can never
 * appear twice in the same game.
 *
 * Like {@link Deck}, the pool is shuffled on creation and cards are dealt
 * from the top. Unchosen draft cards can be returned to the pool.
 */
public class CharacterPool implements BuildingRepository{

//	************************************************************
//	Attributes
//	************************************************************

	private final Building[] characters;
	private int size;

	private final SecureRandom random = new SecureRandom();


//	************************************************************
//	Constructor
//	************************************************************

	public CharacterPool() {
		int count = 0;
		for (Building building : Building.values()) {
			if (building.isCharacter() && building != Building.Training_Dummy) {
				count++;
			}
		}
		characters = new Building[count];
		size = 0;
		for (Building building : Building.values()) {
			if (building.isCharacter() && building != Building.Training_Dummy) {
				characters[size++] = building;
			}
		}
		shuffle();
	}

	/** Fisher-Yates shuffle of the characters currently in the pool. */
	private void shuffle() {
		for (int i = size - 1; i > 0; i--) {
			int j = random.nextInt(i + 1); // 0..i inclusive, unbiased
			Building tmp = characters[i];
			characters[i] = characters[j];
			characters[j] = tmp;
		}
	}


//	************************************************************
//	Dealing & Returning
//	************************************************************

	/**
	 * Deals characters from the top of the pool.
	 * @return the dealt characters, in deal order
	 * @throws IllegalStateException if the pool does not hold enough characters
	 */
	public Building[] deal(int quantity) {
		if (quantity < 0 || quantity > size) {
			throw new IllegalStateException("Cannot deal " + quantity + " characters; only " + size + " remain.");
		}
		Building[] dealt = new Building[quantity];
		for (int i = 0; i < quantity; i++) {
			size--;
			dealt[i] = characters[size];
		}
		return dealt;
	}

	/** Convenience: deals two characters for a player to choose between. */
	public Building[] dealDraftHand() {
		return deal(2);
	}

	/**
	 * Returns a character to the pool (e.g. the unchosen card of a draft hand).
	 * The character goes back on top, so it may be dealt again.
	 */
	public void returnCharacter(Building character) {
		if (character == null || !character.isCharacter() || character == Building.Training_Dummy) {
			throw new IllegalArgumentException("Only pool characters can be returned.");
		}
		if (contains(character)) {
			throw new IllegalArgumentException(character.getName() + " is already in the pool.");
		}
		characters[size] = character;
		size++;
	}

	public boolean contains(Building character) {
		for (int i = 0; i < size; i++) {
			if (characters[i] == character) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		return size;
	}

}
