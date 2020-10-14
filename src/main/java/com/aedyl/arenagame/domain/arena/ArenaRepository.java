package com.aedyl.arenagame.domain.arena;

import java.util.Optional;
import java.util.UUID;

public interface ArenaRepository {
	void save(Arena arena);

	Optional<Arena> findById(UUID arenaId);
}
