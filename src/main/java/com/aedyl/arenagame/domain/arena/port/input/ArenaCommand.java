package com.aedyl.arenagame.domain.arena.port.input;

import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.fighter.model.HumanId;

/**
 * Exposed commands to deal with arena management.
 */
public interface ArenaCommand {

	record CreateArenaCommand(int arenaSize, int nbRoundMax) implements ArenaCommand {

		public static CreateArenaCommand create(int arenaSize, int nbRoundMax) {
			return new CreateArenaCommand(arenaSize, nbRoundMax);
		}
	}

	record AddFighterCommand(ArenaId arenaId, HumanId fighterId) implements ArenaCommand {

		public static AddFighterCommand create(ArenaId arenaId, HumanId fighterId) {
			return new AddFighterCommand(arenaId, fighterId);
		}
	}

	record RunArenaCommand(ArenaId arenaId) implements ArenaCommand {
		public static RunArenaCommand create(ArenaId arenaId) {
			return new RunArenaCommand(arenaId);
		}
	}
}
