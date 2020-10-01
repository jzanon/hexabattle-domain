package com.aedyl.domain.characteristics;

public record Characteristics(int initiative,
                              int maxLife,
                              int life,
                              int strength,
                              Trait primary,
                              Trait secondary) {

	public Characteristics decreaseLife(int lifeToRemove) {
		return new Characteristics(initiative, maxLife, life - lifeToRemove, strength, primary, secondary);
	}
}
