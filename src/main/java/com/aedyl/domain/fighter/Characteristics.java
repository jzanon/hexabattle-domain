package com.aedyl.domain.fighter;

public class Characteristics {
	public final int initiative;
	public final int strength;
	public final long maxLife;
	public long life;

	public Characteristics(int initiative, int maxLife, int strength) {
		this.initiative = initiative;
		this.maxLife = maxLife;
		this.strength = strength;
		this.life = maxLife;
	}

	public void decreaseLife(long lifeToRemove) {
		this.life = this.life - lifeToRemove;
	}
}
