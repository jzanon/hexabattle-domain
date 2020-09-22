package com.aedyl.domain.fighter;

import com.aedyl.domain.combat.CombatStatistics;
import com.aedyl.domain.combat.CombatStatus;

import java.util.Random;

public class AttackResolver {
	private final Random random;

	public AttackResolver() {
		random = new Random();
	}

	public CombatStatistics resolveAttack(Human attacker, Human defender) {
		int hit = getAttackPower(attacker);
		Human enemyAfterFight = defender.suffersStroke(hit);
		if (hit == 0) {
			return new CombatStatistics(CombatStatus.MISSED, attacker, enemyAfterFight, hit);
		}
		return new CombatStatistics(CombatStatus.SUCCESS, attacker, enemyAfterFight, hit);
	}

	int getAttackPower(Human attacker) {
		return random.nextInt(attacker.characteristics().strength());
	}
}
