package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.ArenaService;
import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommandHandler;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.fighter.FighterService;
import com.aedyl.arenagame.domain.statistics.StatisticsService;
import com.aedyl.arenagame.domain.statistics.port.input.StatisticsAdapter;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws Exception {

		final int numberOfFighter = 1000;
		final int nbRoundMax = 25;

		ConsoleAdapter consoleAdapter = new ConsoleAdapter();

		StatisticsService statisticsService = new StatisticsService(consoleAdapter);

		StatisticsAdapter statisticsAdapter = new StatisticsAdapter(statisticsService);

		ArenaEventPublisher arenaEventPublisher = arenaEvent -> {
			consoleAdapter.publish(arenaEvent);
			statisticsAdapter.publish(arenaEvent);
		};

		ArenaCommandHandler arenaService = new ArenaService(arenaEventPublisher, new InMemoryArenaRepository());

		final Arena arena = arenaService.handle(ArenaCommandHandler.CreateArenaCommand.create(numberOfFighter, nbRoundMax));

		FighterService fighterService = new FighterService();
		fighterService.createRandomFighters(arena.maxSize())
				.stream()
				.map(fighter -> ArenaCommandHandler.AddFighterCommand.create(arena.id(), fighter))
				.forEach(arenaService::handle);

		final Future<Void> run = arenaService.handle(ArenaCommandHandler.RunArenaCommand.create(arena.id()));
		run.get(2, TimeUnit.SECONDS);
	}


}
