package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.ArenaService;
import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.AddFighterCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.CreateArenaCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.RunArenaCommand;
import com.aedyl.arenagame.domain.fighter.FighterService;
import com.aedyl.arenagame.domain.fighter.port.input.FghterCommand;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws Exception {

		final int numberOfFighter = 1000;
		final int nbRoundMax = 25;

		final MainServiceConfig config = new MainServiceConfig();

		final ArenaService arenaService = config.getArenaService();
		final FighterService fighterService = config.getFighterService();

		final Arena arena = arenaService.handle(CreateArenaCommand.create(numberOfFighter, nbRoundMax));

		fighterService.handle(FghterCommand.CreateRandomHumansCommand.create(arena.maxSize()))
				.stream()
				.map(fighterId -> AddFighterCommand.create(arena.id(), fighterId))
				.forEach(arenaService::handle);

		final Future<Void> run = arenaService.handle(RunArenaCommand.create(arena.id()));
		run.get(2, TimeUnit.SECONDS);
	}


}
