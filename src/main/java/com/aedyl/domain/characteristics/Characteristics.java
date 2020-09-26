package com.aedyl.domain.characteristics;

import java.util.Set;

public record Characteristics(int initiative,
                              int maxLife,
                              int life,
                              int strength,
                              Set<Trait> traits) {

	public Characteristics decreaseLife(int lifeToRemove) {
		return new Characteristics(initiative, maxLife, life - lifeToRemove, strength, traits);
	}
}
