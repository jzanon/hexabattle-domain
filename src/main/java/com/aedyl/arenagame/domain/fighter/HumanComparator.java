package com.aedyl.arenagame.domain.fighter;

public class HumanComparator {
	public static int compareByInitiative(Human x, Human y) {
		return Integer.compare(y.getCharacteristics().initiative(), x.getCharacteristics().initiative());
	}
}
