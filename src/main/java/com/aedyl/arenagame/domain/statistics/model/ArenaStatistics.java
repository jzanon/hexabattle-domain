package com.aedyl.arenagame.domain.statistics.model;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.fighter.model.HumanId;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ArenaStatistics {
	public final ArenaId arenaId;
	private final Map<HumanId, FighterStatistics> statistics;

	public ArenaStatistics(ArenaId arenaId) {
		this.arenaId = arenaId;
		this.statistics = new HashMap<>();
	}

	public ArenaStatistics(ArenaId arenaId, Collection<FighterStatistics> statistics) {
		this.arenaId = arenaId;
		this.statistics = statistics.stream().collect(Collectors.toMap(s -> s.fighterId, s -> s));
	}

	public FighterStatistics updateAttacker(HumanId id, AttackResult attackResult) {
		final FighterStatistics fighterStatistics = statistics.get(id);
		if (fighterStatistics == null) {
			String errorMessage = String.format("Attacker '%s' does not have statistics in Arena '%s'", id, arenaId);
			throw new IllegalArgumentException(errorMessage);
		}
		fighterStatistics.updateAttacker(attackResult);
		return fighterStatistics;
	}

	public FighterStatistics updateDefender(HumanId id, AttackResult attackResult) {
		final FighterStatistics fighterStatistics = statistics.get(id);
		if (fighterStatistics == null) {
			String errorMessage = String.format("Defender '%s' does not have statistics in Arena '%s'", id, arenaId);
			throw new IllegalArgumentException(errorMessage);
		}
		fighterStatistics.updateDefender(attackResult);
		return fighterStatistics;
	}

	public FighterStatistics get(HumanId humanId) {
		return statistics.get(humanId);
	}

	public Collection<FighterStatistics> getStatistics() {
		return Collections.unmodifiableCollection(statistics.values());
	}


	public FighterStatistics initializeStats(HumanId humanId, String name) {
		final FighterStatistics initialStats = new FighterStatistics(arenaId, humanId, name);
		statistics.put(humanId, initialStats);
		return initialStats;
	}


}
