package com.aedyl.arenagame.domain.statistics;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.Attacker;
import com.aedyl.arenagame.domain.combat.Defender;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.HumanId;
import com.aedyl.arenagame.domain.statistics.model.FighterStatistics;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsEvent;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsPublisher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {

	private final StatisticsPublisher publisher;
	private final Map<HumanId, FighterStatistics> statistics = new HashMap<>();

	public StatisticsService(StatisticsPublisher publisher) {
		this.publisher = publisher;
	}

	public void roundCompleted(Round round) {
		for (AttackResult attackResult : round.stats()) {
			final Attacker attacker = attackResult.assailant();
			statistics.get(attacker.id()).updateAttacker(attackResult);
			final Defender defender = attackResult.defender();
			if (defender != null) {
				statistics.get(defender.id()).updateDefender(attackResult);
			}
		}

	}

	public void humanJoinedArena(HumanId humanId, String name) {
		statistics.put(humanId, new FighterStatistics(humanId, name));
	}


	public void arenaCompleted(ArenaId arenaId, List<HumanId> survivorIds) {
		survivorIds.forEach(survivorId -> statistics.get(survivorId).survived());
		publisher.publish(StatisticsEvent.ArenaStatisticsEvent.from(arenaId, Collections.unmodifiableMap(statistics)));
	}

}
