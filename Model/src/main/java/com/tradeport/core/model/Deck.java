package com.tradeport.core.model;

import java.security.SecureRandom;

/**
 * A deck of 36 cards. Each card is the ordinal (1..36) of a
 * BuildingRepository.Building constant: card N maps to Building.values()[N - 1].
 *
 * Java arrays have a fixed length, so the "size" of the deck is tracked by the
 * `size` counter. Only cards[0 .. size-1] are in the deck; the top card is
 * cards[size - 1]. Drawing shrinks the access range, putting back expands it.
 */
public class Deck implements BuildingRepository{

//	************************************************************
//	Attributes
//	************************************************************
	
	public static final int DECK_SIZE = 36; // For dynamic sizing in case more cards are available in expansions
	private final int[] cards = new int[DECK_SIZE];
	private int size; // Current deck size in the game 

	// Claude: Unpredictable, OS-entropy-seeded generator (see notes on randomness).
	private final SecureRandom random = new SecureRandom();


//	************************************************************
//	Constructor
//	************************************************************
	
	Deck() {
		reset();
	}

	/** Refills the deck with all 36 cards in order (1..36). Does not shuffle. */
	private void reset() {
		for (int i = 0; i < DECK_SIZE; i++) {
			cards[i] = i + 1;
		}
		size = DECK_SIZE;
	}

	/**
	 * Shuffles the cards currently in the deck (Fisher-Yates).
	 * Cards already drawn are not affected.
	 * Uses XOR addition to perform swapping (refer to {@link Market#swap(com.tradeport.core.model.Enum.GoodType, com.tradeport.core.model.Enum.GoodType) swap(GoodType GoodType1, GoodType GoodType2)})
	 * Depends on java.security.SecureRandom
	 */
	protected void shuffle() {
		for (int i = size - 1; i > 0; i--) {
			int j = random.nextInt(i + 1); // 0..i inclusive, unbiased
			cards[i] += cards[j];
			cards[j] -= cards[i];
			cards[i] -= cards[j];
		}
	}

	/**
	 * Gathers ALL 36 cards back (including those dealt) and shuffles them together.
	 * Used in case a second setup shuffle is needed, such as when there are three or more building cards
	 * with "Buy a Building" action available dealt on the board 
	 */
	protected void reshuffleAll() {
		reset();
		shuffle();
	}

	/**
	 * Draws the top card.
	 * @return the card (1..36)
	 * @throws IllegalStateException if the deck is empty
	 */
	private int draw() {
		if (size == 0) {
			throw new IllegalStateException("The deck is empty.");
		}
		size--;
		return cards[size];
	}

	/** Convenience: draws a card and returns its Building enum. */
	protected Building drawBuilding() {
		return toBuilding(draw());
	}

	/** Composite: draws a number of cards and returns its Building constant to be dealt onto the board. */
	protected Building[] drawBuildings(int quantity) {
		Building[] buildingsOnBoard = new Building[5];
		for(int i = 0; i < quantity; i++)
			buildingsOnBoard[i] = drawBuilding();
		return buildingsOnBoard;
	}

	/**
	 * Shuffles a card back into the deck at a random position.
	 * @throws IllegalArgumentException if the card is invalid or already in the deck
	 */
	private void putBack(int card) {
		if (card < 1 || card > DECK_SIZE) {
			throw new IllegalArgumentException("Card must be between 1 and " + DECK_SIZE);
		}
		if (contains(card)) {
			throw new IllegalArgumentException("Card " + card + " is already in the deck.");
		}
		// Insert at a random slot: move the card currently there to the top.
		int slot = random.nextInt(size + 1); // 0..size inclusive
		cards[size] = cards[slot];
		cards[slot] = card;
		size++;
	}

	protected void putBack(Building building) {
		putBack(building.ordinal() + 1);
	}

	protected boolean contains(int card) {
		for (int i = 0; i < size; i++) {
			if (cards[i] == card) {
				return true;
			}
		}
		return false;
	}

	protected int size() {
		return size;
	}

	protected boolean isEmpty() {
		return size == 0;
	}

	/** Card (1..36) -> Building constant. */
	private static Building toBuilding(int card) {
		return Building.values()[card - 1];
	}

}
