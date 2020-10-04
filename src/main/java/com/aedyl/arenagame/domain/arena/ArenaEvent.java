package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.Human;

import java.time.Instant;

public sealed interface ArenaEvent {

	record ArenaInitializedEvent(Instant createdAt, Arena arena) implements ArenaEvent {
		public static ArenaInitializedEvent from(Arena arena) {
			return new ArenaInitializedEvent(Instant.now(), arena);
		}

	}

	record ArenaCompletedEvent(Instant createdAt, Arena arena) implements ArenaEvent {
		public static ArenaCompletedEvent from(Arena arena) {
			return new ArenaCompletedEvent(Instant.now(), arena);
		}
	}

	record HumanJoinedArenaEvent(Instant createdAt, Human fighter) implements ArenaEvent {
		public static HumanJoinedArenaEvent from(Human fighter) {
			return new HumanJoinedArenaEvent(Instant.now(), fighter);
		}
	}

	record RoundCompletedEvent(Instant createdAt, Round round) implements ArenaEvent {
		public static RoundCompletedEvent from(Round round) {
			return new RoundCompletedEvent(Instant.now(), round);
		}
	}

}
