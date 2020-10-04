package com.aedyl.domain;

import com.aedyl.console.ConsoleAdapter;
import com.aedyl.domain.combat.Round;
import com.aedyl.domain.fighter.Human;
import com.aedyl.domain.fighter.HumanSupplier;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {

	private final ConsoleAdapter adapter;

	public Game() {
		adapter = new ConsoleAdapter();
	}

	public void launch(int numberOfFighter, int nbRoundByStep, int numberStep) {

		Arena arena = setupArena(numberOfFighter);

		adapter.arenaInitialized(arena);

		int currentStep = 1;
		while (arena.hasEnoughFighters() && numberStep >= currentStep) {
			final List<Round> rounds = arena.fight(nbRoundByStep * currentStep);
			adapter.roundsCompleted(rounds);
			currentStep++;
		}

		adapter.arenaCompleted(arena);
	}


	private Arena setupArena(int numberOfFighter) {
		Supplier<Human> humanSupplier = new HumanSupplier();

		List<Human> fighters = IntStream.range(0, numberOfFighter)
				.mapToObj(value -> humanSupplier.get())
				.collect(Collectors.toUnmodifiableList());

		return new Arena(fighters);
	}
}
