package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.Game;
import com.aedyl.arenagame.domain.arena.ArenaEventPublisher;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanSupplier;
import com.aedyl.arenagame.statistics.StatisticsAdapter;

import java.util.function.Supplier;

public class Main {


	public static void main(String[] args) {

		final int numberOfFighter = 1000;
		final int nbRoundMax = 25;

		ConsoleAdapter consoleAdapter = new ConsoleAdapter();
		StatisticsAdapter statisticsAdapter = new StatisticsAdapter(consoleAdapter);

		Supplier<Human> fighterSupplier = new HumanSupplier();
		ArenaEventPublisher arenaEventPublisher = arenaEvent -> {
			consoleAdapter.publish(arenaEvent);
			statisticsAdapter.publish(arenaEvent);
		};
		Game game = new Game(arenaEventPublisher, fighterSupplier);
		game.launch(numberOfFighter, nbRoundMax);
	}


}
