package com.aedyl.arenagame.domain.statistics.port.input;

import com.aedyl.arenagame.domain.arena.port.output.ArenaEvent;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.fighter.HumanId;
import com.aedyl.arenagame.domain.statistics.StatisticsService;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsAdapter implements ArenaEventPublisher {

	private final StatisticsService statisticsService;

	public StatisticsAdapter(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@Override
	public void publish(ArenaEvent arenaEvent) {
		if (arenaEvent instanceof ArenaEvent.ArenaCreatedEvent arenaCreatedEvent) {
			statisticsService.initializeStatsForArena(arenaCreatedEvent.arenaId());
		} else if (arenaEvent instanceof ArenaEvent.HumanJoinedArenaEvent humanJoinedArenaEvent) {
			statisticsService.humanJoinedArena(humanJoinedArenaEvent.arenaId(), humanJoinedArenaEvent.fighter().humanId(), humanJoinedArenaEvent.fighter().name());
		} else if (arenaEvent instanceof ArenaEvent.RoundCompletedEvent roundCompleted) {
			statisticsService.roundCompleted(roundCompleted.arenaId(),roundCompleted.round());
		} else if (arenaEvent instanceof ArenaEvent.ArenaCompletedEvent arenaCompletedEvent) {
			final List<HumanId> survivorIds = arenaCompletedEvent.survivors().stream().map(ArenaEvent.Human::humanId).collect(Collectors.toList());
			statisticsService.arenaCompleted(arenaCompletedEvent.arenaId(), survivorIds);
		}
	}

}
