package com.aedyl.arenagame.domain;

import com.aedyl.arenagame.domain.arena.*;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanSupplier;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

	@Test
	void createArena() {
		// TODO : use mockito here
		GameService gameService = new GameService(e -> {
		}, () -> null, new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(UUID arenaId) {
				return Optional.empty();
			}
		});

		final Arena arena = gameService.createArena(10, 15);

		assertEquals(10, arena.maxSize());
		assertEquals(15, arena.nbRoundMax());
		assertEquals(0, arena.getNbOfRoundExecuted());
		assertEquals(0, arena.getSurvivors().size());
		assertTrue(arena.notEnoughFighters());
		assertEquals(ArenaStatus.CREATED, arena.getStatus());
	}

	@Test
	void createArenaSaveItInRepository() {
		ArenaRepository arenaRepository = new ArenaRepository() {
			private final Map<UUID, Arena> arenaMap = new HashMap<>();

			@Override
			public void save(Arena arena) {
				arenaMap.put(arena.id(), arena);
			}

			@Override
			public Optional<Arena> findById(UUID arenaId) {
				return Optional.ofNullable(arenaMap.get(arenaId));
			}
		};

		GameService gameService = new GameService(e -> {
		}, () -> null, arenaRepository);

		final Arena arena = gameService.createArena(10, 15);

		assertSame(arena, arenaRepository.findById(arena.id()).orElseThrow());
	}

	@Test
	void createArenaRaiseEvent() {
		// TODO : use mockito here
		ArenaRepository arenaRepository = new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(UUID arenaId) {
				return Optional.empty();
			}
		};

		final AtomicReference<ArenaEvent.ArenaCreatedEvent> eventHolder = new AtomicReference<>();
		ArenaEventPublisher eventPublisher = arenaEvent -> {
			if (arenaEvent instanceof ArenaEvent.ArenaCreatedEvent createdEvent) {
				eventHolder.set(createdEvent);
			} else {
				fail("No other event is expected: " + arenaEvent);
			}
		};
		GameService gameService = new GameService(eventPublisher, null, arenaRepository);

		final Arena arena = gameService.createArena(10, 15);

		assertEquals(10, arena.maxSize());
		assertEquals(15, arena.nbRoundMax());
		assertSame(arena, eventHolder.get().arena());
	}

	@Test
	void fillUnknowArenaRaiseError() {
		// TODO : use mockito here
		GameService gameService = new GameService(e -> {
		}, () -> null, new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(UUID arenaId) {
				return Optional.empty();
			}
		});


		assertThrows(NoSuchElementException.class, () -> gameService.fillArenaWithRandomFighters(UUID.fromString("59a33f0e-752a-4d72-bde2-781ceedd7d7c")));
	}

	@Test
	void fillArenaAddExpecteedNumberOfFighters() {
		Arena arena = new Arena(10, 10);

		GameService gameService = new GameService(e -> {
		}, new HumanSupplier(), new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(UUID arenaId) {
				return Optional.of(arena);
			}
		});

		final List<Human> humans = gameService.fillArenaWithRandomFighters(arena.id());

		assertEquals(10, humans.size());
		assertEquals(10, arena.getSurvivors().size());
	}

	@Test
	void fillArenaUpdateAndSaveIt() {
		Arena arena = new Arena(10, 10);
		final AtomicReference<Arena> savedArena = new AtomicReference<>();
		GameService gameService = new GameService(e -> {
		}, new HumanSupplier(), new ArenaRepository() {
			@Override
			public void save(Arena arena) {
				savedArena.set(arena);
			}

			@Override
			public Optional<Arena> findById(UUID arenaId) {
				return Optional.of(arena);
			}
		});

		gameService.fillArenaWithRandomFighters(arena.id());

		assertEquals(10, savedArena.get().getSurvivors().size());
	}

	@Test
	void fillArenaGenerateHumanAddedEvents() {
		Arena arena = new Arena(10, 10);
		final List<ArenaEvent.HumanJoinedArenaEvent> events = new ArrayList<>();
		GameService gameService = new GameService(e -> {
			if (e instanceof ArenaEvent.HumanJoinedArenaEvent humanJoinedArenaEvent) {
				events.add(humanJoinedArenaEvent);
			} else {
				fail("No other event is expected: " + e);
			}
		}, new HumanSupplier(), new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(UUID arenaId) {
				return Optional.of(arena);
			}
		});

		gameService.fillArenaWithRandomFighters(arena.id());

		assertEquals(10, events.size());
	}

	@Test
	void run() {
		// TODO
	}
}
