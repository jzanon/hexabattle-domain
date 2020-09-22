package com.aedyl;

import com.aedyl.domain.Arena;
import com.aedyl.domain.combat.Round;
import com.aedyl.domain.fighter.Characteristics;
import com.aedyl.domain.fighter.CharacteristicsSupplier;
import com.aedyl.domain.fighter.Human;
import com.aedyl.domain.fighter.HumanSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
	private static final Logger logger = LoggerFactory.getLogger(Game.class);

	public void launch() {
		int numberOfFighter = 100;

		Arena arena = setupArena(numberOfFighter);

		arena.getSurvivors().forEach(fighter -> logger.info("Fighter: {}", fighter));

		arena.fight(15);

		logResults(arena);
	}

	private void logResults(Arena arena) {
		final List<Round> rounds = arena.getRounds();

		rounds.stream()
				.map(Round::buildSummary)
				.forEach(logger::info);

		final List<String> winners = arena.getSurvivors().stream()
				.map(human -> human.name())
				.collect(Collectors.toList());
		if (winners.size() == 1) {
			logger.info("At then end of round {}, the winner is: {}", rounds.size(), winners);
		} else {
			logger.info("At then end of round {}, there are {} survivors: {}", rounds.size(), winners.size(), winners);
		}
	}


	private Arena setupArena(int numberOfFighter) {
		Supplier<Characteristics> charactSupplier = new CharacteristicsSupplier();
		Supplier<Human> humanSupplier = new HumanSupplier(charactSupplier);

		List<Human> fighters = IntStream.range(0, numberOfFighter)
				.mapToObj(value -> humanSupplier.get())
				.collect(Collectors.toUnmodifiableList());

		return new Arena(fighters);
	}
}
