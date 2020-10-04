package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.HumanFactoryForTests;
import com.aedyl.arenagame.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.arenagame.domain.characteristics.Trait;
import com.aedyl.arenagame.domain.fighter.Human;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {
	private final HumanFactoryForTests factory = new HumanFactoryForTests();


	@Test
	void fightStopWhenRoundLimitReached() {
		// Use APATHETIC humans to ensure no damage will occurs
		final Human apatheticFighter1 = factory.createHuman((CharacteristicsSupplier.StrengthSupplier)()-> 0);
		final Human apatheticFighter2 = factory.createHuman(() -> Trait.APATHETIC, (Trait t) -> Trait.APATHETIC);

		final AtomicInteger roundCounter = new AtomicInteger(0);
		ArenaEventPublisher eventPublisher = arenaEvent -> {
			if (arenaEvent instanceof ArenaEvent.HumanJoinedArenaEvent event) {
				assertNotNull(event.fighter());
			} else if (arenaEvent instanceof ArenaEvent.RoundCompletedEvent event) {
				assertEquals(roundCounter.incrementAndGet(), event.round().number());
			} else {
				fail("Unexpected event received: " + arenaEvent);
			}
		};

		Arena arena = new Arena(eventPublisher);
		arena.addFighter(apatheticFighter1);
		arena.addFighter(apatheticFighter2);

		roundCounter.set(0); // reset counter
		IntStream.range(0, 6).forEach(value -> arena.fight());


		assertEquals(6, roundCounter.get());
		assertEquals(6, arena.getNbOfRoundExecuted());
		assertEquals(2, arena.getSurvivors().size());
	}


	@Test
	void noFightWhenOnlyOneSurvivor() {
		final Human fighter = factory.createRandomHuman();

		final AtomicInteger eventCounter = new AtomicInteger(0);
		ArenaEventPublisher eventPublisher = arenaEvent -> {
			eventCounter.incrementAndGet();
			if (arenaEvent instanceof ArenaEvent.HumanJoinedArenaEvent event) {
				assertSame(fighter, event.fighter());
			} else if (arenaEvent instanceof ArenaEvent.RoundCompletedEvent event) {
				fail("No fight should occurs but a round event has been received: " + event);
			} else {
				fail("Unexpected event received: " + arenaEvent);
			}
		};

		Arena arena = new Arena(eventPublisher);
		arena.addFighter(fighter);
		arena.fight();

		assertEquals(1, eventCounter.get());
		assertEquals(0, arena.getNbOfRoundExecuted());
		assertEquals(1, arena.getSurvivors().size());
		assertSame(fighter, arena.getSurvivors().get(0));
	}

	@Test
	void addFighterRaiseEvent() {
		final Human fighter = factory.createRandomHuman();
		final AtomicInteger eventCounter = new AtomicInteger(0);
		ArenaEventPublisher eventPublisher = arenaEvent -> {
			eventCounter.incrementAndGet();
			if (arenaEvent instanceof ArenaEvent.HumanJoinedArenaEvent event) {
				assertSame(fighter, event.fighter());

			} else {
				fail("Unexpected event received: " + arenaEvent);
			}
		};

		Arena arena = new Arena(eventPublisher);
		arena.addFighter(fighter);

		assertEquals(1, eventCounter.get());
		assertEquals(0, arena.getNbOfRoundExecuted());
		assertEquals(1, arena.getSurvivors().size());
		assertSame(fighter, arena.getSurvivors().get(0));
	}
}
