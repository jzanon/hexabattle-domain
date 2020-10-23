package com.aedyl.arenagame.domain.statistics.port.output;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.statistics.model.ArenaStatistics;
import com.aedyl.arenagame.domain.statistics.model.FighterStatistics;

import java.util.Optional;

public interface StatisticsRepository {

	Optional<ArenaStatistics> find(ArenaId arenaId);

	void save(ArenaStatistics arenaStatistics);

	void save(FighterStatistics fighterStatistics);
}
