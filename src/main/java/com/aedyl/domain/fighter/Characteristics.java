package com.aedyl.domain.fighter;

public record Characteristics(int initiative, int maxLife, int life, int strength) {

	public Characteristics decreaseLife(int lifeToRemove) {
		return new Characteristics(initiative, maxLife, life - lifeToRemove, strength);
	}
}
