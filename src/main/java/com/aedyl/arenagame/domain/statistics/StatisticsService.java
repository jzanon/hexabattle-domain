package com.aedyl.arenagame.domain.statistics;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.Attacker;
import com.aedyl.arenagame.domain.combat.Defender;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.HumanId;
import com.aedyl.arenagame.domain.statistics.model.ArenaStatistics;
import com.aedyl.arenagame.domain.statistics.model.FighterStatistics;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsEvent;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsEvent.FighterStatsUpdated;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsPublisher;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsRepository;

import java.util.List;

public class StatisticsService {

	private final StatisticsPublisher publisher;
	private final StatisticsRepository repository;

	public StatisticsService(StatisticsPublisher publisher, StatisticsRepository repository) {
		this.publisher = publisher;
		this.repository = repository;
	}

	public void roundCompleted(ArenaId arenaId, Round round) {
		ArenaStatistics arenaStatistics = repository.find(arenaId).orElseThrow();
		for (AttackResult attackResult : round.stats()) {
			final Attacker attacker = attackResult.assailant();
			final FighterStatistics attackerStats = arenaStatistics.updateAttacker(attacker.id(), attackResult);
			publisher.publish(FighterStatsUpdated.from(attackerStats));
			final Defender defender = attackResult.defender();
			if (defender != null) {
				final FighterStatistics defenderStats = arenaStatistics.updateDefender(defender.id(), attackResult);
				publisher.publish(FighterStatsUpdated.from(defenderStats));
			}
		}

		repository.save(arenaStatistics);
	}

	public void humanJoinedArena(ArenaId arenaId, HumanId humanId, String name) {
		ArenaStatistics arenaStatistics = repository.find(arenaId).orElseThrow();
		final FighterStatistics fighterStatistics = arenaStatistics.initializeStats(humanId, name);
		publisher.publish(FighterStatsUpdated.from(fighterStatistics));
		repository.save(fighterStatistics);
	}


	public void arenaCompleted(ArenaId arenaId, List<HumanId> survivorIds) {
		ArenaStatistics arenaStatistics = repository.find(arenaId).orElseThrow();
		survivorIds.forEach(survivorId -> {
			final FighterStatistics fighterStatistics = arenaStatistics.get(survivorId);
			fighterStatistics.survived();
		});
		repository.save(arenaStatistics);
		publisher.publish(StatisticsEvent.ArenaStatisticsEvent.from(arenaStatistics));
	}

	public void initializeStatsForArena(ArenaId arenaId) {
		ArenaStatistics arenaStatistics = new ArenaStatistics(arenaId);
		repository.save(arenaStatistics);
		publisher.publish(StatisticsEvent.ArenaStatisticsEvent.from(arenaStatistics));
	}
}
