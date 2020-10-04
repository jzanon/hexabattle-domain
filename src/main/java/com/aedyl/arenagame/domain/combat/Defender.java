package com.aedyl.arenagame.domain.combat;

import com.aedyl.arenagame.domain.characteristics.Characteristics;
import com.aedyl.arenagame.domain.fighter.Human;

import java.util.UUID;

public record Defender(UUID id,
                       String name,
                       Characteristics characteristics) {

	public static Defender fromHuman(Human attacker) {
		return new Defender(attacker.uniqueId, attacker.name, attacker.getCharacteristics());
	}

	public boolean isAlive() {
		return characteristics.life() > 0;
	}
}
