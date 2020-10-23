package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository;
import com.aedyl.arenagame.domain.statistics.model.ArenaStatistics;
import com.aedyl.arenagame.domain.statistics.model.FighterStatistics;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository implements ArenaRepository, StatisticsRepository {
	private final Map<ArenaId, Arena> arenaMap = new HashMap<>();
	private final Map<ArenaId, ArenaStatistics> arenaStatsMap = new HashMap<>();

	@Override
	public void save(Arena arena) {
		arenaMap.put(arena.id(), arena);
	}

	@Override
	public Optional<Arena> findById(ArenaId arenaId) {
		return Optional.ofNullable(arenaMap.get(arenaId));
	}


	@Override
	public Optional<ArenaStatistics> find(ArenaId arenaId) {
		return Optional.ofNullable(arenaStatsMap.get(arenaId));
	}

	@Override
	public void save(ArenaStatistics arenaStatistics) {
		arenaStatsMap.put(arenaStatistics.arenaId,arenaStatistics);
	}

	@Override
	public void save(FighterStatistics fighterStatistics) {
		find(fighterStatistics.arenaId).ifPresent(arenaStatistics -> {
			final ArenaId arenaId = arenaStatistics.arenaId;
			final Collection<FighterStatistics> statistics = arenaStatistics.getStatistics();
			arenaStatsMap.put(arenaId, new ArenaStatistics(arenaId, statistics));
		});
	}
}
