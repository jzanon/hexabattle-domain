package com.aedyl.domain;

import com.aedyl.domain.combat.CombatStatistics;
import com.aedyl.domain.combat.CombatStatus;
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

			final List<Human> survivorsInsideRound = new ArrayList<>(survivors);

			final List<CombatStatistics> statistics = survivors.stream()
					.sorted((x, y) -> Integer.compare(y.characteristics().initiative(), x.characteristics().initiative()))
					.filter(survivorsInsideRound::contains)
					.map(human -> human.fight(survivorsInsideRound))
					.peek(combatStatistics -> updateSurvivorsInsideRound(survivorsInsideRound, combatStatistics))
					.collect(Collectors.toList());

			survivors = survivorsInsideRound;

			rounds.add(new Round(currentRoundIndex, statistics));
		}

	}

	private void updateSurvivorsInsideRound(List<Human> survivorsInsideRound, CombatStatistics combatStatistics) {
		if (combatStatistics.status().equals(CombatStatus.SUCCESS)) {
			survivorsInsideRound.remove(combatStatistics.defender());
			if (combatStatistics.defender().isAlive()) {
				survivorsInsideRound.add(combatStatistics.defender());
			}
		}
	}

	public List<Round> getRounds() {
		return rounds;
	}

	public List<Human> getSurvivors() {
		return survivors;
	}
}
