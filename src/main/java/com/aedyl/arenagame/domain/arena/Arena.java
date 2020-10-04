package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.aedyl.arenagame.domain.arena.ArenaEvent.HumanJoinedArenaEvent;

public class Arena {

	private final ArenaEventPublisher eventPublisher;
	private List<Human> survivors = new ArrayList<>();
	private int nbOfRoundExecuted = 0;

	public Arena(ArenaEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void fight() {
		if (!hasEnoughFighters()) {
			return;
		}
		final List<AttackResult> attackResults = survivors.stream()
				.filter(Human::isAlive)
				.sorted(HumanComparator::compareByInitiative)
				.map(human -> human.fight(survivors))
				.collect(Collectors.toList());

		survivors = survivors.stream().filter(Human::isAlive).collect(Collectors.toList());

		final Round round = new Round(++nbOfRoundExecuted, attackResults);
		eventPublisher.publish(ArenaEvent.RoundCompletedEvent.from(round));
	}

	public List<Human> getSurvivors() {
		return survivors;
	}

	public int getNbOfRoundExecuted() {
		return nbOfRoundExecuted;
	}

	public boolean hasEnoughFighters() {
		return survivors.size() > 1;
	}

	public void addFighter(Human fighter) {
		survivors.add(fighter);
		eventPublisher.publish(HumanJoinedArenaEvent.from(fighter));
	}

}
