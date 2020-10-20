package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.HumanBuilder;
import com.aedyl.arenagame.domain.HumanFactoryForTests;
import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.arenagame.domain.combat.AttackResolver;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.EnemyChooser;
import com.aedyl.arenagame.domain.fighter.Human;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {
	private final HumanFactoryForTests factory = new HumanFactoryForTests();


	@Test
	void arenaMaxSizeReached() {
		final Human fighter1 = factory.createRandomHuman();
		final Human fighter2 = factory.createRandomHuman();
		final Human fighter3 = factory.createRandomHuman();

		Arena arena = new Arena(2, 1);
		assertEquals(ArenaStatus.CREATED, arena.getStatus());

		assertTrue(arena.addFighter(fighter1));
		assertEquals(ArenaStatus.CREATED, arena.getStatus());

		assertTrue(arena.addFighter(fighter2));
		assertEquals(ArenaStatus.FILLED, arena.getStatus());

		assertFalse(arena.addFighter(fighter3));
		assertEquals(ArenaStatus.FILLED, arena.getStatus());
	}


	@Test
	void fightStopWhenRoundLimitReached() {
		// Use humans without strength  to ensure no damage will occurs
		final Human nullStrengthFighter1 = factory.createHuman((CharacteristicsSupplier.StrengthSupplier) () -> 0);
		final Human nullStrengthFighter2 = factory.createHuman((CharacteristicsSupplier.StrengthSupplier) () -> 0);

		Arena arena = new Arena(2, 6);
		arena.addFighter(nullStrengthFighter1);
		arena.addFighter(nullStrengthFighter2);

		IntStream.range(0, 6).forEach(value -> {
			final Round round = arena.roundTick();
			assertEquals(value + 1, round.number());
		});

		assertEquals(6, arena.getNbOfRoundExecuted());
		assertEquals(ArenaStatus.FINISHED, arena.getStatus());
		assertEquals(2, arena.getSurvivors().size());
	}

	@Test
	void deadFighterCannotAttackIfDead() {
		final EnemyChooser enemyChooser = new EnemyChooser() {
			@Override
			public Optional<Human> pickEnemy(Human me, List<Human> fighters) {
				return fighters.stream().filter(enemy -> !enemy.uniqueId.equals(me.uniqueId)).findAny();
			}
		};

		final AttackResolver attackResolver = new AttackResolver() {
			@Override
			public AttackResult resolveAttack(Human attacker, Human defender) {
				int hit = defender.getCharacteristics().life(); // ensure one-shot kill
				Human enemyAfterFight = defender.suffersStroke(attacker, hit);
				return AttackResult.attack(attacker, enemyAfterFight, hit);
			}
		};
		final Human fighterWithHighInit = new HumanBuilder()
				.set(enemyChooser)
				.set(attackResolver)
				.set((CharacteristicsSupplier.InitiativeSupplier) () -> 10)
				.build();
		final Human fighterWithLowInit = new HumanBuilder()
				.set(enemyChooser)
				.set(attackResolver)
				.set((CharacteristicsSupplier.InitiativeSupplier) () -> 2)
				.build();

		Arena arena = new Arena();
		arena.addFighter(fighterWithLowInit);
		arena.addFighter(fighterWithHighInit);
		arena.roundTick();

		assertEquals(1, arena.getNbOfRoundExecuted());
		assertEquals(ArenaStatus.FINISHED, arena.getStatus());
		assertEquals(1, arena.getSurvivors().size());
		assertSame(fighterWithHighInit, arena.getSurvivors().get(0));
	}


	@Test
	void noFightWhenOnlyOneSurvivor() {
		final Human fighter = factory.createRandomHuman();

		Arena arena = new Arena();
		arena.addFighter(fighter);
		final Round round = arena.roundTick();

		assertEquals(ArenaStatus.FINISHED, arena.getStatus());
		assertEquals(1, arena.getSurvivors().size());
		assertSame(fighter, arena.getSurvivors().get(0));
		assertEquals(1, round.number());
		assertEquals(0, round.stats().size());
	}

}
