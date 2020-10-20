package com.aedyl.arenagame.domain.statistics.port.input;

import com.aedyl.arenagame.domain.arena.port.output.ArenaEvent;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.fighter.HumanId;
import com.aedyl.arenagame.domain.statistics.StatisticsService;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsAdapter implements ArenaEventPublisher {

	private final StatisticsService facade;

	public StatisticsAdapter(StatisticsService facade) {
		this.facade = facade;
	}

	@Override
	public void publish(ArenaEvent arenaEvent) {
		if (arenaEvent instanceof ArenaEvent.HumanJoinedArenaEvent humanJoinedArenaEvent) {
			facade.humanJoinedArena(humanJoinedArenaEvent.fighter().humanId(), humanJoinedArenaEvent.fighter().name());
		} else if (arenaEvent instanceof ArenaEvent.RoundCompletedEvent roundCompleted) {
			facade.roundCompleted(roundCompleted.round());
		} else if (arenaEvent instanceof ArenaEvent.ArenaCompletedEvent arenaCompletedEvent) {
			final List<HumanId> survivorIds = arenaCompletedEvent.survivors().stream().map(ArenaEvent.Human::humanId).collect(Collectors.toList());
			facade.arenaCompleted(arenaCompletedEvent.arenaId(), survivorIds);
		}
	}

}
