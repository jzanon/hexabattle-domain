package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.Human;

import java.time.Instant;
import java.util.UUID;

public sealed interface ArenaEvent {

	record ArenaCreatedEvent(Instant createdAt, Arena arena) implements ArenaEvent {
		public static ArenaCreatedEvent from(Arena arena) {
			return new ArenaCreatedEvent(Instant.now(), arena);
		}

	}

	record ArenaCompletedEvent(Instant createdAt, Arena arena) implements ArenaEvent {
		public static ArenaCompletedEvent from(Arena arena) {
			return new ArenaCompletedEvent(Instant.now(), arena);
		}
	}

	record HumanJoinedArenaEvent(Instant createdAt, UUID arenaId, Human fighter) implements ArenaEvent {
		public static HumanJoinedArenaEvent from(UUID arenaId, Human fighter) {
			return new HumanJoinedArenaEvent(Instant.now(), arenaId, fighter);
		}
	}

	record RoundCompletedEvent(Instant createdAt, Round round) implements ArenaEvent {
		public static RoundCompletedEvent from(Round round) {
			return new RoundCompletedEvent(Instant.now(), round);
		}
	}

}
