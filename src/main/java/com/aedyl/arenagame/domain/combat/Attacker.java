package com.aedyl.arenagame.domain.combat;

import com.aedyl.arenagame.domain.characteristics.Characteristics;
import com.aedyl.arenagame.domain.fighter.model.Human;
import com.aedyl.arenagame.domain.fighter.model.HumanId;

public record Attacker(HumanId id,
                       String name,
                       Characteristics characteristics) implements Fighter {
	public static Attacker fromHuman(Human attacker) {
		return new Attacker(attacker.uniqueId, attacker.name, attacker.getCharacteristics());
	}
}
