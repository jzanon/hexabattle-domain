package com.aedyl.arenagame.domain.arena.port.input;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.fighter.Human;

/**
 * Exposed commands to deal with arena management.
 */
public interface ArenaCommand {

	record CreateArenaCommand(int arenaSize, int nbRoundMax) implements ArenaCommand {

		public static CreateArenaCommand create(int arenaSize, int nbRoundMax) {
			return new CreateArenaCommand(arenaSize, nbRoundMax);
		}
	}

	record AddFighterCommand(ArenaId arenaId, Human fighter) implements ArenaCommand {

		public static AddFighterCommand create(ArenaId arenaId, Human fighter) {
			return new AddFighterCommand(arenaId, fighter);
		}
	}

	record RunArenaCommand(ArenaId arenaId) implements ArenaCommand {
		public static RunArenaCommand create(ArenaId arenaId) {
			return new RunArenaCommand(arenaId);
		}
	}
}
