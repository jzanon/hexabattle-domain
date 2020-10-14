package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Arena {

	private static final int DEFAULT_MAX_SIZE = 100;
	private static final int DEFAULT_NB_ROUND_MAX = 20;
	private final UUID id;
	private List<Human> survivors = new ArrayList<>();
	private int nbOfRoundExecuted = 0;
	private final int maxSize;
	private final int nbRoundMax;
	private boolean finished;

	public Arena() {
		this.id = UUID.randomUUID();
		this.maxSize = DEFAULT_MAX_SIZE;
		this.nbRoundMax = DEFAULT_NB_ROUND_MAX;
	}

	public Arena(int maxSize, int nbRoundMax) {
		this.id = UUID.randomUUID();
		this.maxSize = maxSize;
		this.nbRoundMax = nbRoundMax;
	}

	public Round roundTick() {
		nbOfRoundExecuted++;
		if (notEnoughFighters()) {
			return new Round(nbOfRoundExecuted, Collections.emptyList());
		}
		final List<AttackResult> attackResults = survivors.stream()
				.sorted(HumanComparator::compareByInitiative)
				.filter(Human::isAlive)
				.map(human -> human.fight(survivors))
				.collect(Collectors.toList());

		survivors = survivors.stream().filter(Human::isAlive).collect(Collectors.toList());

		return new Round(nbOfRoundExecuted, attackResults);
	}

	public List<Human> getSurvivors() {
		return survivors;
	}

	public int getNbOfRoundExecuted() {
		return nbOfRoundExecuted;
	}

	public boolean notEnoughFighters() {
		return survivors.size() <= 1;
	}

	public void addFighter(Human fighter) {
		survivors.add(fighter);
	}

	public int maxSize() {
		return maxSize;
	}

	public UUID id() {
		return id;
	}

	public int nbRoundMax() {
		return nbRoundMax;
	}

	public void markFinished() {
		this.finished = true;
	}

	public boolean isFinished() {
		return finished;
	}
}
