package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.ArenaService;
import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.AddFighterCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.CreateArenaCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.RunArenaCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommandHandler;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.fighter.FighterService;
import com.aedyl.arenagame.domain.fighter.port.input.FghterCommand;
import com.aedyl.arenagame.domain.statistics.StatisticsService;
import com.aedyl.arenagame.domain.statistics.port.input.StatisticsAdapter;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws Exception {

		final int numberOfFighter = 1000;
		final int nbRoundMax = 25;

		final InMemoryRepository repository = new InMemoryRepository();

		ConsoleAdapter consoleAdapter = new ConsoleAdapter();

		StatisticsService statisticsService = new StatisticsService(consoleAdapter, repository);

		StatisticsAdapter statisticsAdapter = new StatisticsAdapter(statisticsService);

		ArenaEventPublisher arenaEventPublisher = arenaEvent -> {
			consoleAdapter.publish(arenaEvent);
			statisticsAdapter.publish(arenaEvent);
		};

		ArenaCommandHandler arenaService = new ArenaService(arenaEventPublisher, repository, repository);

		final Arena arena = arenaService.handle(CreateArenaCommand.create(numberOfFighter, nbRoundMax));

		FighterService fighterService = new FighterService(repository);
		fighterService.handle(FghterCommand.CreateRandomHumansCommand.create(arena.maxSize()))
				.stream()
				.map(fighterId -> AddFighterCommand.create(arena.id(), fighterId))
				.forEach(arenaService::handle);

		final Future<Void> run = arenaService.handle(RunArenaCommand.create(arena.id()));
		run.get(2, TimeUnit.SECONDS);
	}


}
