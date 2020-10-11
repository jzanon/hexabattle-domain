package com.aedyl.arenagame.statistics;

import com.aedyl.arenagame.domain.arena.Arena;
import com.aedyl.arenagame.domain.statistics.FighterStatistics;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public sealed interface StatisticsEvent {

	record ArenaStatisticsEvent(Instant createdAt, Arena arena,
	                            Map<UUID, FighterStatistics> fighterStatistics) implements StatisticsEvent {
		public static ArenaStatisticsEvent from(Arena arena, Map<UUID, FighterStatistics> fighterStatistics) {
			return new ArenaStatisticsEvent(Instant.now(), arena, fighterStatistics);
		}
	}

}
