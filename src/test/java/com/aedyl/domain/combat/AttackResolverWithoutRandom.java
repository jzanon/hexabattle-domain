package com.aedyl.domain.combat;

import com.aedyl.domain.combat.AttackResolver;
import com.aedyl.domain.fighter.Human;

public class AttackResolverWithoutRandom extends AttackResolver {

	private int attackPower;

	public void setAttackPower(int attackPower) {
		this.attackPower = attackPower;
	}

	@Override
	public int getAttackPower(Human attacker) {
		return attackPower;
	}
}
