package com.aedyl.arenagame.domain;

import com.aedyl.arenagame.domain.arena.Arena;
import com.aedyl.arenagame.domain.arena.ArenaEventPublisher;
import com.aedyl.arenagame.domain.fighter.Human;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.aedyl.arenagame.domain.arena.ArenaEvent.*;

public class Game {

	private final ArenaEventPublisher arenaEventPublisher;
	private final Supplier<Human> humanSupplier;

	public Game(ArenaEventPublisher arenaEventPublisher, Supplier<Human> fighterSupplier) {
		this.arenaEventPublisher = arenaEventPublisher;
		humanSupplier = fighterSupplier;
	}

	public void launch(int arenaSize, int nbRoundMax) {
		Arena arena = new Arena(arenaEventPublisher);

		IntStream.range(0, arenaSize)
				.mapToObj(value -> humanSupplier.get())
				.forEach(arena::addFighter);

		arenaEventPublisher.publish(ArenaInitializedEvent.from(arena));

		while (arena.hasEnoughFighters() && arena.getNbOfRoundExecuted() <= nbRoundMax) {
			arena.fight();
		}

		arenaEventPublisher.publish(ArenaCompletedEvent.from(arena));
	}

}
