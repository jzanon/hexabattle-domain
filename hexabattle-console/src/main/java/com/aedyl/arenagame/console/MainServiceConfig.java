package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.ArenaService;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository;
import com.aedyl.arenagame.domain.fighter.FighterService;
import com.aedyl.arenagame.domain.fighter.port.output.HumanRepository;
import com.aedyl.arenagame.domain.statistics.StatisticsService;
import com.aedyl.arenagame.domain.statistics.port.input.StatisticsAdapter;
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsPublisher;

public class MainServiceConfig {
	private final InMemoryRepository repository = new InMemoryRepository();
	private final ConsoleAdapter consoleAdapter = new ConsoleAdapter();

	private InMemoryRepository getStatisticsRepository() {
		return repository;
	}

	private ConsoleAdapter getConsoleAdapter() {
		return consoleAdapter;
	}

	private HumanRepository getFighterRepository() {
		return repository;
	}

	private ArenaRepository getArenaRepository() {
		return repository;
	}


	private StatisticsPublisher getStatisticsPublisher() {
		return consoleAdapter;
	}


	public FighterService getFighterService() {
		return new FighterService(getFighterRepository());
	}

	public ArenaService getArenaService() {
		return new ArenaService(getArenaEventPublisher(), getArenaRepository(), getFighterRepository());
	}

	private ArenaEventPublisher getArenaEventPublisher() {
		return arenaEvent -> {
			getConsoleAdapter().publish(arenaEvent);
			getStatisticsAdapter().publish(arenaEvent);
		};
	}

	private StatisticsAdapter getStatisticsAdapter() {
		return new StatisticsAdapter(getStatisticsService());
	}

	public StatisticsService getStatisticsService() {
		return new StatisticsService(getStatisticsPublisher(), getStatisticsRepository());
	}


}
