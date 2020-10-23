package com.aedyl.arenagame.domain.characteristics;

import com.aedyl.arenagame.domain.fighter.Human;

import java.util.Optional;

public class HumanMemory {
	private Human lastHumanWhoAttackedMe;
	private Human lastHumanIAttacked;


	public Optional<Human> getLastHumanWhoAttackedMe() {
		return Optional.ofNullable(lastHumanWhoAttackedMe);
	}

	public HumanMemory setLastHumanWhoAttackedMe(Human lastHumanWhoAttackedMe) {
		this.lastHumanWhoAttackedMe = lastHumanWhoAttackedMe;
		return this;
	}

	public Optional<Human> getLastHumanIAttacked() {
		return Optional.ofNullable(lastHumanIAttacked);
	}

	public HumanMemory setLastHumanIAttacked(Human lastHumanIAttacked) {
		this.lastHumanIAttacked = lastHumanIAttacked;
		return this;
	}
}
