package com.aedyl.arenagame.domain.arena.port.input;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.fighter.Human;

import java.util.concurrent.Future;

public interface ArenaCommandHandler {

	record CreateArenaCommand(int arenaSize, int nbRoundMax) {
		public static CreateArenaCommand create(int arenaSize, int nbRoundMax) {
			return new CreateArenaCommand(arenaSize, nbRoundMax);
		}
	}

	Arena handle(CreateArenaCommand createArenaCommand);

	record AddFighterCommand(ArenaId arenaId, Human fighter) {
		public static AddFighterCommand create(ArenaId arenaId, Human fighter) {
			return new AddFighterCommand(arenaId, fighter);
		}
	}

	void handle(AddFighterCommand addFighterCommand);


	record RunArenaCommand(ArenaId arenaId) {
		public static RunArenaCommand create(ArenaId arenaId) {
			return new RunArenaCommand(arenaId);
		}
	}

	Future<Void> handle(RunArenaCommand addFighterCommand);

}
