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
	private ArenaStatus status;

	public Arena() {
		this.id = UUID.randomUUID();
		this.maxSize = DEFAULT_MAX_SIZE;
		this.nbRoundMax = DEFAULT_NB_ROUND_MAX;
		status = ArenaStatus.CREATED;
	}

	public Arena(int maxSize, int nbRoundMax) {
		this.id = UUID.randomUUID();
		this.maxSize = maxSize;
		this.nbRoundMax = nbRoundMax;
		status = ArenaStatus.CREATED;
	}

	public Round roundTick() {
		status = ArenaStatus.RUNNING_ROUND;
		nbOfRoundExecuted++;
		if (notEnoughFighters()) {
			if (isFinished()) {
				status = ArenaStatus.FINISHED;
			}
			return new Round(nbOfRoundExecuted, Collections.emptyList());
		}
		final List<AttackResult> attackResults = survivors.stream()
				.sorted(HumanComparator::compareByInitiative)
				.filter(Human::isAlive)
				.map(human -> human.fight(survivors))
				.collect(Collectors.toList());

		survivors = survivors.stream().filter(Human::isAlive).collect(Collectors.toList());

		if (isFinished()) {
			status = ArenaStatus.FINISHED;
		}

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

	public boolean addFighter(Human fighter) {
		if (survivors.size() >= maxSize) {
			return false;
		}
		survivors.add(fighter);
		if (survivors.size() >= maxSize) {
			status = ArenaStatus.FILLED;
		}
		return true;
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

	private boolean isFinished() {
		return notEnoughFighters()
				|| getNbOfRoundExecuted() >= nbRoundMax();
	}

	public ArenaStatus getStatus() {
		return status;
	}
}
