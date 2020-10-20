package com.aedyl.arenagame.domain.arena.model;

import java.util.UUID;

public record ArenaId(UUID id) {
	public static ArenaId randomId() {
		return new ArenaId(UUID.randomUUID());
	}

	public static ArenaId from(String uuid) {
		return new ArenaId(UUID.fromString(uuid));
	}
}
