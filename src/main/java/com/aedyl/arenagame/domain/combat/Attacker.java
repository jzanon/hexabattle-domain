package com.aedyl.arenagame.domain.combat;

import com.aedyl.arenagame.domain.characteristics.Characteristics;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanId;

public record Attacker(HumanId id,
                       String name,
                       Characteristics characteristics) implements Fighter {
	public static Attacker fromHuman(Human attacker) {
		return new Attacker(attacker.uniqueId, attacker.name, attacker.getCharacteristics());
	}
}
