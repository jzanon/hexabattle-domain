package com.aedyl.arenagame.statistics;

import com.aedyl.arenagame.console.ConsoleAdapter;
import com.aedyl.arenagame.domain.arena.ArenaEvent;
import com.aedyl.arenagame.domain.arena.ArenaEventPublisher;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.Attacker;
import com.aedyl.arenagame.domain.combat.Defender;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.statistics.FighterStatistics;

import java.util.*;

public class StatisticsAdapter implements ArenaEventPublisher {

	private final StatisticsPublisher publisher;
	private Map<UUID, FighterStatistics> statistics = new HashMap<>();

	public StatisticsAdapter(StatisticsPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public void publish(ArenaEvent arenaEvent) {
		if (arenaEvent instanceof ArenaEvent.HumanJoinedArenaEvent humanJoinedArenaEvent) {
			humanJoinedArena(humanJoinedArenaEvent.fighter());
		} else if (arenaEvent instanceof ArenaEvent.RoundCompletedEvent roundCompleted) {
			roundCompleted(roundCompleted.round());
		}  else if (arenaEvent instanceof ArenaEvent.ArenaCompletedEvent arenaCompletedEvent){
			arenaCompletedEvent.arena().getSurvivors().forEach(survivor -> statistics.get(survivor.uniqueId).survived());
			publisher.publish(StatisticsEvent.ArenaStatisticsEvent.from(arenaCompletedEvent.arena(), Collections.unmodifiableMap(statistics)));
		}
	}

	private void roundCompleted(Round round) {
		for (AttackResult attackResult : round.stats()) {
			final Attacker attacker = attackResult.assailant();
			statistics.get(attacker.id()).updateAttacker(attackResult);
			final Defender defender = attackResult.defender();
			if (defender != null) {
				statistics.get(defender.id()).updateDefender(attackResult);
			}
		}

	}

	private void humanJoinedArena(Human fighter) {
		statistics.put(fighter.uniqueId, new FighterStatistics(fighter));
	}


}
