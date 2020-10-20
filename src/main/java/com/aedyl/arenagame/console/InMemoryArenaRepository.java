package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryArenaRepository implements ArenaRepository {
	private final Map<ArenaId, Arena> arenaMap = new HashMap<>();

	@Override
	public void save(Arena arena) {
		arenaMap.put(arena.id(), arena);
	}

	@Override
	public Optional<Arena> findById(ArenaId arenaId) {
		return Optional.ofNullable(arenaMap.get(arenaId));
	}
}
