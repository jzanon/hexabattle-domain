package com.aedyl.arenagame.domain.combat;

import com.aedyl.arenagame.domain.fighter.model.Human;

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
