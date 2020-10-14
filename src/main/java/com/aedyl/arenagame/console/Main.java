package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.GameService;
import com.aedyl.arenagame.domain.arena.Arena;
import com.aedyl.arenagame.domain.arena.ArenaEventPublisher;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanSupplier;
import com.aedyl.arenagame.statistics.StatisticsAdapter;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Main {

	public static void main(String[] args) throws Exception {

		final int numberOfFighter = 1000;
		final int nbRoundMax = 25;

		ConsoleAdapter consoleAdapter = new ConsoleAdapter();
		StatisticsAdapter statisticsAdapter = new StatisticsAdapter(consoleAdapter);

		Supplier<Human> fighterSupplier = new HumanSupplier();
		ArenaEventPublisher arenaEventPublisher = arenaEvent -> {
			consoleAdapter.publish(arenaEvent);
			statisticsAdapter.publish(arenaEvent);
		};

		GameService gameService = new GameService(arenaEventPublisher, fighterSupplier, new InMemoryArenaRepository());
		final Arena arena = gameService.createArena(numberOfFighter, nbRoundMax);
		gameService.fillArenaWithRandomFighters(arena.id());
		final Future<Void> run = gameService.run(arena.id());
		run.get(2, TimeUnit.SECONDS);
	}


}
