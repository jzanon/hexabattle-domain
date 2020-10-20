package com.aedyl.arenagame.domain.statistics;

import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.fighter.HumanId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class FighterStatistics {
	public final HumanId fighterId;
	public final String name;
	private final Map<FighterStatType, FighterStatistic> stats = new HashMap<>();


	public enum FighterStatType {
		KILL, ATTACK, DEFENSE, MISS, DEATH, HIT_SUM, SUFFERED_HIT_SUM, NO_ENEMY_FOUND, SURVIVOR
	}

	public FighterStatistics(HumanId fighterId, String name) {
		this.fighterId = fighterId;
		this.name = name;
		for (FighterStatType statType : FighterStatType.values()) {
			stats.put(statType, new FighterStatistic(statType, 0));
		}
	}

	public Collection<FighterStatistic> updateAttacker(AttackResult attackResult) {
		switch (attackResult.status()) {
			case SUCCESS -> {
				stats.compute(FighterStatType.ATTACK, sumStatUpdater(1));
				stats.compute(FighterStatType.HIT_SUM, sumStatUpdater(attackResult.hit()));
				if (!attackResult.defender().isAlive()) {
					stats.compute(FighterStatType.KILL, sumStatUpdater(1));
				}
			}
			case MISSED -> stats.compute(FighterStatType.MISS, sumStatUpdater(1));
			case NO_ENEMY_FOUND -> stats.compute(FighterStatType.NO_ENEMY_FOUND, sumStatUpdater(1));
			default -> throw new IllegalStateException("Unexpected value: " + attackResult.status());
		}
		return stats.values();
	}

	public Collection<FighterStatistic> updateDefender(AttackResult attackResult) {
		switch (attackResult.status()) {
			case SUCCESS -> {
				stats.compute(FighterStatType.DEFENSE, sumStatUpdater(1));
				stats.compute(FighterStatType.SUFFERED_HIT_SUM, sumStatUpdater(attackResult.hit()));
				if (!attackResult.defender().isAlive()) {
					stats.compute(FighterStatType.DEATH, sumStatUpdater(1));
				}
			}
			case MISSED, NO_ENEMY_FOUND -> {
			}
		}
		return stats.values();
	}

	private BiFunction<FighterStatType, FighterStatistic, FighterStatistic> sumStatUpdater(int valueToAdd) {
		return (fighterStatType, fighterStatistic) -> {
			if (fighterStatistic == null) {
				return new FighterStatistic(fighterStatType, valueToAdd);
			} else {

				return new FighterStatistic(fighterStatType, fighterStatistic.value() + valueToAdd);
			}
		};
	}


	public Collection<FighterStatistic> getStats() {
		return stats.values();
	}

	public FighterStatistic getStat(FighterStatType type) {
		return stats.getOrDefault(type, new FighterStatistic(type, 0));
	}

	public void survived() {
		stats.compute(FighterStatType.SURVIVOR, sumStatUpdater(1));
	}


}
