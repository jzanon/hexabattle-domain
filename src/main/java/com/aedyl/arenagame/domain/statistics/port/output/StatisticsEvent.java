package com.aedyl.arenagame.domain.statistics.port.output;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.fighter.HumanId;
import com.aedyl.arenagame.domain.statistics.FighterStatistics;

import java.time.Instant;
import java.util.Map;

public sealed interface StatisticsEvent {

	record ArenaStatisticsEvent(Instant createdAt, ArenaId arenaId,
	                            Map<HumanId, FighterStatistics> fighterStatistics) implements StatisticsEvent {
		public static ArenaStatisticsEvent from(ArenaId arenaId, Map<HumanId, FighterStatistics> fighterStatistics) {
			return new ArenaStatisticsEvent(Instant.now(), arenaId, fighterStatistics);
		}
	}

}
