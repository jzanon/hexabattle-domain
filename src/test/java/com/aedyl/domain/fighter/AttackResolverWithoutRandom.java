package com.aedyl.domain.fighter;

public class AttackResolverWithoutRandom extends AttackResolver {

	private int attackPower;

	public void setAttackPower(int attackPower) {
		this.attackPower = attackPower;
	}

	@Override
	int getAttackPower(Human attacker) {
		return attackPower;
	}
}
