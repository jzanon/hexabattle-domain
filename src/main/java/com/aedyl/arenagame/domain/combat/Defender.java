package com.aedyl.arenagame.domain.combat;

import com.aedyl.arenagame.domain.characteristics.Characteristics;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanId;

public record Defender(HumanId id,
                       String name,
                       Characteristics characteristics) implements Fighter {

	public static Defender fromHuman(Human attacker) {
		return new Defender(attacker.uniqueId, attacker.name, attacker.getCharacteristics());
	}

	public boolean isAlive() {
		return characteristics.life() > 0;
	}
}
