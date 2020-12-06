package com.aedyl.arenagame.domain.statistics.port.output;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.statistics.model.ArenaStatistics;
import com.aedyl.arenagame.domain.statistics.model.FighterStatistics;

import java.time.Instant;
import java.util.Collection;

public sealed interface StatisticsEvent {

	record FighterStatsUpdated(Instant createdAt, FighterStatistics fighterStatistics) implements StatisticsEvent {
		public static FighterStatsUpdated from(FighterStatistics stats) {
			return new FighterStatsUpdated(Instant.now(), stats);
		}
	}

	record ArenaStatisticsEvent(Instant createdAt, ArenaId arenaId,
	                            Collection<FighterStatistics> fighterStatistics) implements StatisticsEvent {
		public static ArenaStatisticsEvent from(ArenaStatistics arenaStatistics) {
			return new ArenaStatisticsEvent(Instant.now(), arenaStatistics.arenaId, arenaStatistics.getStatistics());
		}
	}

}
