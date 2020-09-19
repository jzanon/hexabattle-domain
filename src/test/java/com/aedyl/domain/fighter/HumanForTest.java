package com.aedyl.domain.fighter;

public class HumanForTest extends Human {

	private int attackValue;

	public HumanForTest(Human human) {
		super(human.uniqueId, human.name, human.characteristics);
	}

	public void setAttackValue(int attackValue) {
		this.attackValue = attackValue;
	}

	@Override
	int getAttackPower() {
		return attackValue;
	}
}
