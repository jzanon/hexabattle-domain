package com.aedyl.arenagame.domain.arena.port.output;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;

import java.util.Optional;

public interface ArenaRepository {
	void save(Arena arena);

	Optional<Arena> findById(ArenaId arenaId);
}
