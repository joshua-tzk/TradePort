package com.tradeport.core.model;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Market implements ConstantsLibrary {

//	************************************************************
//	Attributes
//	************************************************************

	// The position tracker for goods, with 0 being the leftmost position and 3 being the rightmost position
	// Gives the position of Fish, Meat, Stone and Wood in the goodsPositionTracker respectively (by sequence in ConstantsLibrary)
	private int[] goodsPositionTracker = IntStream.range(0, GoodType.values().length).toArray();
	public static final int LOWEST_POSITION = 0, HIGHEST_POSITION = GoodType.values().length - 1; // Constants used by Building cards
	private final SecureRandom random = new SecureRandom();

//	************************************************************
//	Constructor
//	************************************************************

	Market() {
		// Shuffle the good tokens when it is initiated
		randomiseStartingPositions();
		assertValidPositions();
	}

	private void randomiseStartingPositions() { // Method mechanism refers to Deck.shuffle()
		for (int i = goodsPositionTracker.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			int temp = goodsPositionTracker[i];
			goodsPositionTracker[i] = goodsPositionTracker[j];
			goodsPositionTracker[j] = temp;
		}
	}

//	************************************************************
//	Lookup and Swap Methods
//	************************************************************

	// Lookup by GoodType
	int getPosition(GoodType goodType) {
		return goodsPositionTracker[goodType.ordinal()];
	}

	// Reverse lookup by position: returns the good occupying the given position
	// TODO: can be used by future market UI/effects
	GoodType getPosition(int goodsPosition) {
		for (GoodType goodType : GoodType.values()) {
			if (goodsPositionTracker[goodType.ordinal()] == goodsPosition) {
				return goodType;
			}
		}
		throw new IllegalArgumentException("No good occupies position " + goodsPosition);
	}

	// Checks if the former good marker has a lower position than the latter
	boolean oneLowerThan2(GoodType goodType1, GoodType goodType2) {
		return goodsPositionTracker[goodType1.ordinal()] < goodsPositionTracker[goodType2.ordinal()];
	}


	/**
	 * Swaps the positions of two goods.
	 */
	void swap(GoodType goodType1, GoodType goodType2) {
		int tmp = goodsPositionTracker[goodType1.ordinal()];
		goodsPositionTracker[goodType1.ordinal()] = goodsPositionTracker[goodType2.ordinal()];
		goodsPositionTracker[goodType2.ordinal()] = tmp;
	}


//	************************************************************
//	Shifting Methods
//	************************************************************

	/*
	 * Private Core Mechanism: Moves a good's marker to the target position.
	 * Can only be called by shiftGoodMarker().
	 * Every good whose marker sits between the old and new position is shifted
	 * one slot towards the vacated position, preserving the relative order of all markers.
	 * Does nothing if the good is already at the target position.
	 */
	private void shift(GoodType goodType, int targetPosition) {
		// Checks for illegal target positioning
		if (targetPosition < LOWEST_POSITION || targetPosition > HIGHEST_POSITION)
			throw new IllegalArgumentException("Position out of range: " + targetPosition);
		int currentPosition = getPosition(goodType);
		if (targetPosition == currentPosition)
			// throw new IllegalArgumentException("Good token is at the same position " + targetPosition);
			return;
		// Since target != current, if(target > current) else(current < target)
		if(targetPosition > currentPosition) {
			for(int i = 0; i < goodsPositionTracker.length; i++)
				if(goodsPositionTracker[i] == currentPosition)
					goodsPositionTracker[i] = targetPosition;
				else if(goodsPositionTracker[i] <= targetPosition && goodsPositionTracker[i] > currentPosition)
					goodsPositionTracker[i]--;
		}
		else { // then targetPosition < currentPosition
			for(int i = 0; i < goodsPositionTracker.length; i++)
				if(goodsPositionTracker[i] == currentPosition)
					goodsPositionTracker[i] = targetPosition;
				else if(goodsPositionTracker[i] >= targetPosition && goodsPositionTracker[i] < currentPosition)
					goodsPositionTracker[i]++;
		}
		assertValidPositions(); // Checks for array validity in case it's corrupted
	}

	// Sanity check: every position in range and no two goods share a position
	private void assertValidPositions() {
		boolean[] occupied = new boolean[goodsPositionTracker.length];
		for (int pos : goodsPositionTracker) {
			if (pos < LOWEST_POSITION || pos > HIGHEST_POSITION || occupied[pos]) {
				throw new IllegalStateException("Market positions are corrupted: " + Arrays.toString(goodsPositionTracker));
			}
			occupied[pos] = true;
		}
	}


//	************************************************************
//	Market Good Marker Swappers & Shifters
//	************************************************************

	/**
	 * Action: Shift a Good Marker to a certain position<p>
	 * Moves a good's marker to the given market position.
	 * Market decides how to validate/clamp the position and how it treats
	 * other markers (it owns that private state).
	 */
	protected void shiftGoodMarker(GoodType goodType, int goodPosition) {
		shift(goodType, goodPosition);
	}


	/**
	 * Action: Shift a Good Marker to the lowest position in the market
	 */
	protected void shiftToLowest(GoodType goodType) {
		shiftGoodMarker(goodType, Market.LOWEST_POSITION);
	}

	/**
	 * Action: Shift a Good Marker to the highest position in the market
	 */
	protected void shiftToHighest(GoodType goodType) {
		shiftGoodMarker(goodType, Market.HIGHEST_POSITION);
	}


	/* Action: Performs Market Shift Based on Goods Sold
	 * Decides which function to call based on switch statement on the number of types of goods sold
	 */
	protected void shiftMarket(GoodType[] goodsSold) {
		// Cheap sanity checks
		// Each good must appear exactly once
		for(int i = 0; i < goodsSold.length; i++)
			for(int j = i + 1; j < goodsSold.length; j++)
				if(goodsSold[i] == goodsSold[j])
					throw new IllegalArgumentException("Each good type must appear exactly once.");
		switch(goodsSold.length) {
		case 1: shiftMarket(goodsSold[0]); break;
		case 2: shiftMarket(goodsSold[0], goodsSold[1]); break;
		case 3: shiftMarket(goodsSold[0], goodsSold[1], goodsSold[2]); break;
		case 4: shiftMarket(goodsSold[0], goodsSold[1], goodsSold[2], goodsSold[3]); break;
		default: throw new IllegalArgumentException("Cannot shift market for " + goodsSold.length + " good types.");
		}
	}

	/**
	 * Action: Shifts the market after someone buys a building<p>
	 * The number of good markers may vary, and overloaded methods with lower arguments
	 * will be called if there's less arguments.<p>
	 * shiftMarket() that involved all four good markers will swap positions with each other
	 * directly whereas shiftMarket() with less arguments will have their positions compard.
	 * The principle is to move good markers with lower positions first, then higher positions,
	 * assuming that no good markers will have their positions equal at any given point in time.
	 */
	protected void shiftMarket(GoodType g1, GoodType g2, GoodType g3, GoodType g4) {
		// All four goods were sold: rank them by current position, then swap
		// the ends pairwise (lowest<->highest, middle two) — equivalent to a
		// full reversal of the market order.
		GoodType[] goods = {g1, g2, g3, g4};
		for (int i = 1; i < goods.length; i++) {
			GoodType key = goods[i];
			int keyPos = getPosition(key);
			int j = i - 1;
			while (j >= 0 && getPosition(goods[j]) > keyPos) {
				goods[j + 1] = goods[j];
				j--;
			}
			goods[j + 1] = key;
		}
		swap(goods[0], goods[3]);
		swap(goods[1], goods[2]);
	}

	// Three good markers
	protected void shiftMarket(GoodType g1, GoodType g2, GoodType g3) {
		if(oneLowerThan2(g1, g2) && oneLowerThan2(g1, g3)) {
			shiftMarket(g1);
			shiftMarket(g2, g3);
		}
		else if(oneLowerThan2(g2, g1) && oneLowerThan2(g2, g3)) {
			shiftMarket(g2);
			shiftMarket(g1, g3);
		}
		else if(oneLowerThan2(g3, g1) && oneLowerThan2(g3, g2)) {
			shiftMarket(g3);
			shiftMarket(g1, g2);
		}

	}

	// Two good markers
	protected void shiftMarket(GoodType goodType1, GoodType goodType2) {
		if(oneLowerThan2(goodType1, goodType2)) {
			shiftMarket(goodType1);
			shiftMarket(goodType2);
		}
		else {
			shiftMarket(goodType2);
			shiftMarket(goodType1);
		}

	}

	// One good marker: No comparison required
	protected void shiftMarket(GoodType goodType1) {
		shiftToLowest(goodType1);
	}


//	************************************************************
//	Getters & Setters
//	************************************************************

	public void setGoodsPosition(GoodType goodType, int position) {
		if (position < LOWEST_POSITION || position > HIGHEST_POSITION) {
			throw new IllegalArgumentException("Position out of range: " + position);
		}
		this.goodsPositionTracker[goodType.ordinal()] = position;
	}

	//Special Getter: Get Market Price of a Good (makes other code cleaner)
	public int getPrice(GoodType goodType) {
		return getPosition(goodType) + 2;
	}

	// General Getter & Setter
	public int[] getGoodsPositionTracker() {
		return goodsPositionTracker;
	}

	public void setGoodsPositionTracker(int[] goodsTracker) {
		if (goodsTracker == null || goodsTracker.length != GoodType.values().length) {
			throw new IllegalArgumentException("Goods tracker must have exactly " + GoodType.values().length + " entries.");
		}
		this.goodsPositionTracker = goodsTracker;
	}


}
