package com.aedyl.arenagame.console;

import com.aedyl.arenagame.domain.arena.Arena;
import com.aedyl.arenagame.domain.arena.ArenaRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryArenaRepository implements ArenaRepository {
	private final Map<UUID, Arena> arenaMap = new HashMap<>();

	@Override
	public void save(Arena arena) {
		arenaMap.put(arena.id(), arena);
	}

	@Override
	public Optional<Arena> findById(UUID arenaId) {
		return Optional.ofNullable(arenaMap.get(arenaId));
	}
}
