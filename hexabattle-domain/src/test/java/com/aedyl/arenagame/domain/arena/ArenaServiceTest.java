package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.HumanFactoryForTests;
import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEvent;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository;
import com.aedyl.arenagame.domain.fighter.model.Human;
import com.aedyl.arenagame.domain.fighter.model.HumanId;
import com.aedyl.arenagame.domain.fighter.port.output.HumanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ArenaServiceTest {

	private ArenaRepository arenaRepositoryStub;
	private HumanRepository humanRepositoryStub;

	@BeforeEach
	public void beforeEach() {
		arenaRepositoryStub = new ArenaRepository() {
			@Override
			public void save(Arena arena) {
			}

			@Override
			public Optional<Arena> findById(ArenaId arenaId) {
				return Optional.empty();
			}
		};
		humanRepositoryStub = new HumanRepository() {

			@Override
			public void save(Human human) {

			}

			@Override
			public Optional<Human> findById(HumanId humanId) {
				return Optional.empty();
			}
		};
	}

	@Test
	void createArena() {
		// TODO : use mockito here
		ArenaService arenaService = new ArenaService(e -> {
		}, arenaRepositoryStub, humanRepositoryStub);

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
		}, arenaRepository, humanRepositoryStub);

		final Arena arena = arenaService.createArena(10, 15);

		assertSame(arena, arenaRepository.findById(arena.id()).orElseThrow());
	}

	@Test
	void createArenaRaiseEvent() {
		final AtomicReference<ArenaEvent.ArenaCreatedEvent> eventHolder = new AtomicReference<>();
		ArenaEventPublisher eventPublisher = arenaEvent -> {
			if (arenaEvent instanceof ArenaEvent.ArenaCreatedEvent createdEvent) {
				eventHolder.set(createdEvent);
			} else {
				fail("No other event is expected: " + arenaEvent);
			}
		};
		ArenaService arenaService = new ArenaService(eventPublisher, arenaRepositoryStub, humanRepositoryStub);

		final Arena arena = arenaService.createArena(10, 15);

		assertEquals(10, arena.maxSize());
		assertEquals(15, arena.nbRoundMax());
		assertSame(arena.id(), eventHolder.get().arenaId());
		assertEquals(arena.maxSize(), eventHolder.get().maxSize());
	}

	@Test
	void fillUnknowArenaRaiseError() {
		ArenaService arenaService = new ArenaService(e -> {
		}, arenaRepositoryStub, humanRepositoryStub);

		assertThrows(IllegalArgumentException.class, () -> arenaService.addFighter(null, null));
	}

	@Test
	void addFighterToExpectedArena() {
		Arena arena = new Arena();

		ArenaService arenaService = new ArenaService(e -> {
		}, arenaRepositoryStub, humanRepositoryStub);
		assertEquals(0, arena.getSurvivors().size());
		arenaService.addFighter(arena, new HumanFactoryForTests().createRandomHuman());
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
		}, humanRepositoryStub);

		final Arena arena = arenaService.createArena(10, 10);
		assertEquals(0, savedArena.get().getSurvivors().size());
		arenaService.addFighter(arena, new HumanFactoryForTests().createRandomHuman());
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
		}, humanRepositoryStub);

		final HumanFactoryForTests factory = new HumanFactoryForTests();
		arenaService.addFighter(arena, factory.createRandomHuman());
		arenaService.addFighter(arena, factory.createRandomHuman());
		assertEquals(2, events.size());
	}

	@Test
	void run() {
		// TODO
	}
}
