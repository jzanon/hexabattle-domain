package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.ArenaEventPublisher;
import com.aedyl.arenagame.domain.Game;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanSupplier;

import java.util.function.Supplier;

public class Main {


	public static void main(String[] args) {

		final int numberOfFighter = 1000;
		final int nbRoundMax = 25;

		ArenaEventPublisher arenaEventPublisher = new ConsoleAdapter();

		Supplier<Human> fighterSupplier = new HumanSupplier();
		Game game = new Game(arenaEventPublisher, fighterSupplier);
		game.launch(numberOfFighter, nbRoundMax);
	}


}
