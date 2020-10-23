package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEvent;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.Defender;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.statistics.model.FighterStatistics;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsEvent;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleAdapter implements ArenaEventPublisher, StatisticsPublisher {
	private static final Logger logger = LoggerFactory.getLogger(ConsoleAdapter.class);
	static final int STATS_NB_COLUMN_MAX = 3;

	@Override
	public void publish(ArenaEvent arenaEvent) {
		// still waiting for Pattern matching on sealed interface !
		if (arenaEvent instanceof ArenaEvent.ArenaCompletedEvent arenaCompleted) {
			final List<String> winners = arenaCompleted.survivors()
					.stream()
					.map(ArenaEvent.Human::name)
					.collect(Collectors.toList());
			arenaCompleted(arenaCompleted.nbOfRoundExecuted(), winners);
		} else if (arenaEvent instanceof ArenaEvent.ArenaCreatedEvent arenaInitialized) {
			arenaInitialized(arenaInitialized.arenaId(), arenaInitialized.maxSize());
		} else if (arenaEvent instanceof ArenaEvent.RoundCompletedEvent roundCompleted) {
			roundCompleted(roundCompleted.round());
		} else if (arenaEvent instanceof ArenaEvent.HumanJoinedArenaEvent humanJoinedArenaEvent) {
			humanJoinedArena(humanJoinedArenaEvent.arenaId(), humanJoinedArenaEvent.fighter().name());
		} else {
			throw new IllegalStateException("Unexpected event type: " + arenaEvent);
		}
	}


	public void arenaInitialized(ArenaId arenaId, int maxSize) {
		logger.info("A new Arena has been created: id='{}', maxSize={}", arenaId, maxSize);
	}

	public void arenaCompleted(int nbOfRoundExecuted, List<String> winners) {
		if (winners.size() == 1) {
			logger.info("At then end of round {}, the winner is: {}", nbOfRoundExecuted, winners);
		} else {
			logger.info("At then end of round {}, there are {} survivors: {}", nbOfRoundExecuted, winners.size(), winners);
		}
	}

	public void roundCompleted(Round round) {
		logger.info(this.buildSummary(round));
	}

	public String buildSummary(Round round) {
		String prefix = "[Round: " + round.number() + "] ";
		return prefix + "starting:\n" + round.stats().stream()
				.map(this::buildSummary)
				.map(summary -> prefix + summary)
				.collect(Collectors.joining("\n"));
	}

	private String buildSummary(AttackResult attackResult) {
		final String name = attackResult.assailant().name();
		switch (attackResult.status()) {
			case SUCCESS -> {
				String consequenceOnDefender = getConsequenceOnDefender(attackResult);
				return String.format("%s hit %s : %s damages. %s", name, attackResult.defender().name(), attackResult.hit(), consequenceOnDefender);
			}
			case NO_ENEMY_FOUND -> {
				return String.format("%s did not found enemy", name);
			}
			case MISSED -> {
				return String.format("%s missed %s", name, attackResult.defender().name());
			}
			default -> throw new IllegalStateException("Combat status not managed: " + attackResult.status());
		}
	}

	public String getConsequenceOnDefender(AttackResult attackResult) {
		final Defender defender = attackResult.defender();
		if (defender == null) {
			return "";
		}
		return String.format("%s's life: %s/%s%s", defender.name(),
				defender.characteristics().life(),
				defender.characteristics().maxLife(),
				defender.isAlive() ? "" : " (dead)");
	}

	public void humanJoinedArena(ArenaId arenaId, String fighterName) {
		logger.info("[Arena:'{}'] New fighter joined Arena: {}", arenaId, fighterName);
	}


	@Override
	public void publish(StatisticsEvent event) {
		if (event instanceof StatisticsEvent.ArenaStatisticsEvent arenaStatisticsEvent) {
			final Collection<FighterStatistics> statistics = arenaStatisticsEvent.fighterStatistics();
			statistics.stream()
					.filter(o -> o.getStat(FighterStatistics.FighterStatType.SURVIVOR).value() > 0)
					.forEach(fighterStatistics -> logger.info("[Arena:'{}'] Stats of survivor {}: {}", arenaStatisticsEvent.arenaId(), fighterStatistics.name, buildSummary(fighterStatistics)));

			statistics.stream()
					.max(Comparator.comparingInt(o -> o.getStat(FighterStatistics.FighterStatType.KILL).value()))
					.ifPresent(fighterStatistics -> logger.info("[Arena:'{}'] Stats of Best Killer {}: {}", arenaStatisticsEvent.arenaId(), fighterStatistics.name, buildSummary(fighterStatistics)));
		}
	}

	private String buildSummary(FighterStatistics statistics) {

		final var stats = statistics.getStats();
		final int nbOfLine = (stats.size() / STATS_NB_COLUMN_MAX) + (stats.size() % STATS_NB_COLUMN_MAX == 0 ? 0 : 1);

		StringBuilder builder = new StringBuilder("[\n");
		for (int currentLine = 0; currentLine < nbOfLine; currentLine++) {
			builder.append("| ");
			final long nbOfColumn = stats.stream()
					.skip(currentLine * STATS_NB_COLUMN_MAX) // skip stats from previous lines
					.limit(STATS_NB_COLUMN_MAX)
					.map(stat -> String.format("%s: %s", stat.type(), stat.value()))
					.peek(stat -> builder.append(String.format("%-25s| ", stat)))
					.count();
			builder.append(getEmptyColumnsToFillLine(nbOfColumn));
			builder.append("\n");
		}
		builder.append("]");
		return builder.toString();

	}

	private String getEmptyColumnsToFillLine(long nbOfColumnAlreadyFilled) {
		StringBuilder fillColumns = new StringBuilder();
		for (long i = nbOfColumnAlreadyFilled; i < STATS_NB_COLUMN_MAX && i > 0; i++) {
			fillColumns.append(" ".repeat(25));
			fillColumns.append("| ");
		}
		return fillColumns.toString();
	}
}
