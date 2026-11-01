package com.tradeport.core.model;

public class Market implements Enum {
	
//	************************************************************
//	Attributes
//	************************************************************

	// The position tracker for goods, with 0 being the leftmost position and 3 being the rightmost position
	// Gives the position of Fish, Meat, Stone and Wood in the goodsTracker respectively (by sequence in Enum)
	int[] goodsPositionTracker = new int[4];
	public static final int LOWEST_POSITION = 0, HIGHEST_POSITION = 3; // Constants used by Building cards

//	************************************************************
//	Constructor
//	************************************************************

	Market() {
	}

//	************************************************************
//	Lookup and Swap Methods
//	************************************************************

	// Lookup by GoodType
	int getPosition(GoodType goodType) {
		return goodsPositionTracker[goodType.ordinal()];
	}
	
	// Lookup by GoodType's position
	GoodType getPosition(int goodsPosition) {
//		for(int i = 0; i < goodsPositionTracker.length; i++)
//			if(goodsPositionTracker[i] == goodsPosition)
				return GoodType.FISH;
	}

	// Checks if the former good marker has a lower position than the latter
	boolean oneLowerThan2(GoodType goodType1, GoodType goodType2) {
		return (goodsPositionTracker[goodType1.ordinal()] < goodsPositionTracker[goodType2.ordinal()]) ? true : false;
	}


	/**
	 * Swap by XOR Addition (for fun)
	 * int a = 1, b = 2;
	 * a = a + b (a = 3)
	 * b = a - b (b = 3 - 2 = 1)
	 * a = a - b (a = 3 - 1 = 2)
	 */
	void swap(GoodType goodType1, GoodType goodType2) {
		goodsPositionTracker[goodType1.ordinal()] += goodType2.ordinal();
		goodsPositionTracker[goodType2.ordinal()] -= goodType1.ordinal();
		goodsPositionTracker[goodType1.ordinal()] -= goodType2.ordinal();
	}

	/*
	 * This is a recursive function that moves a goodType to a position, and shifts the others to a certain direction.
	 * Repeats swaps to achieve the shift.
	 */
	protected void shift(GoodType goodType, int targetPosition) {
		int originalPosition = goodsPositionTracker[goodType.ordinal()];
		if(originalPosition == targetPosition)
			throw new IllegalArgumentException("Why are you shifting it to its original position?");
		else if (originalPosition > targetPosition) {
			for(GoodType objEnum : GoodType.values())
				if(goodsPositionTracker[objEnum.ordinal()] == 1);
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
		// Call method with lower arguments if conditions satisfy
		if (g4 == null) { shiftMarket(g1, g2, g3); return; }
		// Cheap sanity checks
		// Each good must appear exactly once
		if (g1 == g2 || g1 == g3 || g1 == g4 || g2 == g3 || g2 == g4 || g3 == g4) {
			throw new IllegalArgumentException("Each good type must appear exactly once.");
		}
		for(int goodMarkerPosition : goodsPositionTracker) {
			if(goodMarkerPosition == 0) goodMarkerPosition = 3;
			if(goodMarkerPosition == 1) goodMarkerPosition = 2;
			if(goodMarkerPosition == 2) goodMarkerPosition = 1;
			if(goodMarkerPosition == 3) goodMarkerPosition = 1;
		}
	}

	// Three good markers
	protected void shiftMarket(GoodType g1, GoodType g2, GoodType g3) {
		if (g3 == null) { shiftMarket(g1, g2); return; }
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

	// Index-specific Getter & Setter
	public int getGoodsTracker(GoodType goodType) {
		return getPosition(goodType);
	}

	public void setGoodsTracker(GoodType goodType, int position) {
		this.goodsPositionTracker[goodType.ordinal()] = position;
	}
	
	// General Getter & Setter
	public int[] getGoodsTracker() {
		return goodsPositionTracker;
	}

	public void setGoodsTracker(int[] goodsTracker) {
		this.goodsPositionTracker = goodsTracker;
	}


}
