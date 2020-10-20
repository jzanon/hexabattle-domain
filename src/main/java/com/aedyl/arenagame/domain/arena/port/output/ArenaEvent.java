package com.aedyl.arenagame.domain.arena.port.output;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.HumanId;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public sealed interface ArenaEvent {

	record ArenaCreatedEvent(Instant createdAt, ArenaId arenaId, int maxSize) implements ArenaEvent {
		public static ArenaCreatedEvent from(Arena arena) {
			return new ArenaCreatedEvent(Instant.now(), arena.id(), arena.maxSize());
		}
	}

	record Human(HumanId humanId, String name) {
		public static Human from(com.aedyl.arenagame.domain.fighter.Human human) {
			return new Human(human.uniqueId, human.name);
		}
	}

	record ArenaCompletedEvent(Instant createdAt,
	                           ArenaId arenaId,
	                           int nbOfRoundExecuted,
	                           List<Human> survivors
	) implements ArenaEvent {
		public static ArenaCompletedEvent from(Arena arena) {
			final List<Human> survivors = arena.getSurvivors().stream()
					.map(Human::from)
					.collect(Collectors.toList());
			return new ArenaCompletedEvent(Instant.now(), arena.id(), arena.getNbOfRoundExecuted(), survivors);
		}
	}

	record HumanJoinedArenaEvent(Instant createdAt, ArenaId arenaId, Human fighter) implements ArenaEvent {
		public static HumanJoinedArenaEvent from(ArenaId arenaId, com.aedyl.arenagame.domain.fighter.Human fighter) {
			return new HumanJoinedArenaEvent(Instant.now(), arenaId, Human.from(fighter));
		}
	}

	record RoundCompletedEvent(Instant createdAt, Round round) implements ArenaEvent {
		public static RoundCompletedEvent from(Round round) {
			return new RoundCompletedEvent(Instant.now(), round);
		}
	}

}
