package com.aedyl.domain;

import com.aedyl.domain.combat.CombatStatistics;
import com.aedyl.domain.combat.Round;
import com.aedyl.domain.fighter.Human;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Arena {

	private List<Human> survivors;
	private final List<Round> rounds = new ArrayList<>();

	public Arena(List<Human> fighters) {
		this.survivors = new ArrayList<>(fighters);
	}

	public void fight(int numberMaxOfRound) {
		int currentRoundIndex = 0;


		while (survivors.size() > 1 && currentRoundIndex <= numberMaxOfRound) {
			currentRoundIndex++;

			final List<CombatStatistics> statistics = survivors.stream()
					.sorted((x, y) -> Integer.compare(y.characteristics.initiative, x.characteristics.initiative))
					.filter(Human::isAlive)
					.map(human -> human.fight(survivors))
					.collect(Collectors.toList());

			survivors = survivors.stream().filter(Human::isAlive).collect(Collectors.toList());

			rounds.add(new Round(currentRoundIndex, statistics));
		}

	}

	public List<Round> getRounds() {
		return rounds;
	}

	public List<Human> getSurvivors() {
		return survivors;
	}
}
