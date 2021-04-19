package com.aedyl.domain;

public record Characteristics(int initiative,
                              int maxLife,
                              int life,
                              int strength,
                              com.aedyl.domain.Trait primary,
                              com.aedyl.domain.Trait secondary) {

	public Characteristics decreaseLife(int lifeToRemove) {
		return new Characteristics(initiative, maxLife, life - lifeToRemove, strength, primary, secondary);
	}
}
