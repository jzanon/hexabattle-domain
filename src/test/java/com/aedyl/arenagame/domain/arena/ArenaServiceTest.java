package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.HumanFactoryForTests;
import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEvent;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ArenaServiceTest {

	@Test
	void createArena() {
		// TODO : use mockito here
		ArenaService arenaService = new ArenaService(e -> {
		}, new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(ArenaId arenaId) {
				return Optional.empty();
			}
		});

		final Arena arena = arenaService.createArena(10, 15);

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
			private final Map<ArenaId, Arena> arenaMap = new HashMap<>();

			@Override
			public void save(Arena arena) {
				arenaMap.put(arena.id(), arena);
			}

			@Override
			public Optional<Arena> findById(ArenaId arenaId) {
				return Optional.ofNullable(arenaMap.get(arenaId));
			}
		};

		ArenaService arenaService = new ArenaService(e -> {
		}, arenaRepository);

		final Arena arena = arenaService.createArena(10, 15);

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
			public Optional<Arena> findById(ArenaId arenaId) {
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
		ArenaService arenaService = new ArenaService(eventPublisher, arenaRepository);

		final Arena arena = arenaService.createArena(10, 15);

		assertEquals(10, arena.maxSize());
		assertEquals(15, arena.nbRoundMax());
		assertSame(arena.id(), eventHolder.get().arenaId());
		assertEquals(arena.maxSize(), eventHolder.get().maxSize());
	}

	@Test
	void fillUnknowArenaRaiseError() {
		// TODO : use mockito here
		ArenaService arenaService = new ArenaService(e -> {
		}, new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(ArenaId arenaId) {
				return Optional.empty();
			}
		});


		assertThrows(NoSuchElementException.class, () -> {
			final ArenaId nonexistentArenaId = ArenaId.from("59a33f0e-752a-4d72-bde2-781ceedd7d7c");
			arenaService.addFighter(nonexistentArenaId, null);
		});
	}

	@Test
	void addFighterToExpectedArena() {
		Arena arena = new Arena();

		ArenaService arenaService = new ArenaService(e -> {
		}, new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(ArenaId arenaId) {
				return Optional.of(arena);
			}
		});
		assertEquals(0, arena.getSurvivors().size());
		arenaService.addFighter(arena.id(), new HumanFactoryForTests().createRandomHuman());
		assertEquals(1, arena.getSurvivors().size());
	}

	@Test
	void addFighterUpdateAndSaveArena() {
		final AtomicReference<Arena> savedArena = new AtomicReference<>();
		ArenaService arenaService = new ArenaService(e -> {
		}, new ArenaRepository() {
			@Override
			public void save(Arena arena) {
				savedArena.set(arena);
			}

			@Override
			public Optional<Arena> findById(ArenaId arenaId) {
				return Optional.of(savedArena.get());
			}
		});

		final Arena arena = arenaService.createArena(10, 10);
		assertEquals(0, savedArena.get().getSurvivors().size());
		arenaService.addFighter(arena.id(), new HumanFactoryForTests().createRandomHuman());
		assertEquals(1, savedArena.get().getSurvivors().size());
	}

	@Test
	void fillArenaGenerateHumanAddedEvents() {
		Arena arena = new Arena(10, 10);
		final List<ArenaEvent.HumanJoinedArenaEvent> events = new ArrayList<>();
		ArenaService arenaService = new ArenaService(e -> {
			if (e instanceof ArenaEvent.HumanJoinedArenaEvent humanJoinedArenaEvent) {
				events.add(humanJoinedArenaEvent);
			} else {
				fail("No other event is expected: " + e);
			}
		}, new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(ArenaId arenaId) {
				return Optional.of(arena);
			}
		});

		final HumanFactoryForTests factory = new HumanFactoryForTests();
		arenaService.addFighter(arena.id(), factory.createRandomHuman());
		arenaService.addFighter(arena.id(), factory.createRandomHuman());
		assertEquals(2, events.size());
	}

	@Test
	void run() {
		// TODO
	}
}
