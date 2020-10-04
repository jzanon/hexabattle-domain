package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.Arena;
import com.aedyl.arenagame.domain.arena.ArenaEvent;
import com.aedyl.arenagame.domain.arena.ArenaEventPublisher;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.Defender;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.Human;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ConsoleAdapter implements ArenaEventPublisher {
	private static final Logger logger = LoggerFactory.getLogger(ConsoleAdapter.class);


	@Override
	public void publish(ArenaEvent arenaEvent) {
		// still waiting for Pattern matching on sealed interface !
		if (arenaEvent instanceof ArenaEvent.ArenaCompletedEvent arenaCompleted) {
			arenaCompleted(arenaCompleted.arena());
		} else if (arenaEvent instanceof ArenaEvent.ArenaInitializedEvent arenaInitialized) {
			arenaInitialized(arenaInitialized.arena());
		} else if (arenaEvent instanceof ArenaEvent.RoundCompletedEvent roundCompleted) {
			roundCompleted(roundCompleted.round());
		} else if (arenaEvent instanceof ArenaEvent.HumanJoinedArenaEvent humanJoinedArenaEvent) {
			humanJoinedArena(humanJoinedArenaEvent.fighter());
		} else {
			throw new IllegalStateException("Unexpected event type: " + arenaEvent);
		}
	}

	public void arenaInitialized(Arena arena) {
		arena.getSurvivors().forEach(fighter -> logger.info("Fighter: {}", fighter));
	}

	public void arenaCompleted(Arena arena) {
		final List<String> winners = arena.getSurvivors().stream()
				.map(human -> human.name)
				.collect(Collectors.toList());
		if (winners.size() == 1) {
			logger.info("At then end of round {}, the winner is: {}", arena.getNbOfRoundExecuted(), winners);
		} else {
			logger.info("At then end of round {}, there are {} survivors: {}", arena.getNbOfRoundExecuted(), winners.size(), winners);
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

	public void humanJoinedArena(Human fighter) {
		logger.info("New fighter joined Arena: {}", fighter);
	}


}
