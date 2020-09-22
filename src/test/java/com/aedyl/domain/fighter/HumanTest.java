package com.aedyl.domain.fighter;

import com.aedyl.domain.combat.CombatStatistics;
import com.aedyl.domain.combat.CombatStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;

class HumanTest {


	private static CharacteristicsSupplier randomCharacteristicsSupplier;
	private static HumanSupplier randomHumanSupplier;

	@BeforeAll
	static void initSuppliers() {
		randomCharacteristicsSupplier = new CharacteristicsSupplier();
		randomHumanSupplier = new HumanSupplier(randomCharacteristicsSupplier);
	}

	@Test
	void testEqualsDependsOnID() {
		Human human = randomHumanSupplier.get();

		Human humanCloned = new Human(human.uniqueId(), human.name(), human.characteristics());
		assertEquals(human, humanCloned);

		Human humanWithSameId = new Human(human.uniqueId(), "Another name", randomCharacteristicsSupplier.get());
		assertEquals(human, humanWithSameId);

		Human humanWithDifferentId = new Human(UUID.randomUUID(), human.name(), human.characteristics());
		assertNotEquals(human, humanWithDifferentId);
	}

	@Test
	void isAlive() {
		Human human = new Human(UUID.randomUUID(), "Plop", new Characteristics(5, 7, 7, 9));
		assertTrue(human.isAlive());

		human = new Human(UUID.randomUUID(), "Plop", new Characteristics(5, 7, 0, 9));
		assertFalse(human.isAlive());
	}

	@Test
	void pickEnemyDoNotPickMySelf() {
		Human me = randomHumanSupplier.get();
		Human anotherHuman1 = randomHumanSupplier.get();
		Human anotherHuman2 = randomHumanSupplier.get();

		List<Human> fighters = List.of(me, anotherHuman1, anotherHuman2);
		Optional<Human> potentialEnemy = me.pickEnemy(fighters);

		assertTrue(potentialEnemy.isPresent());

		Human enemy = potentialEnemy.get();
		assertNotEquals(me, enemy);
		assertThat(List.of(anotherHuman1, anotherHuman2), hasItems(enemy));
	}

	@Test
	void pickNobody() {
		Human me = randomHumanSupplier.get();

		Optional<Human> potentialEnemy = me.pickEnemy(List.of(me));
		assertFalse(potentialEnemy.isPresent());

		potentialEnemy = me.pickEnemy(Collections.emptyList());
		assertFalse(potentialEnemy.isPresent());
	}

	/**
	 * FLAKY due to Random
	 */
	@ParameterizedTest
	@ValueSource(ints = {1, 3, 5, -3, 15, Integer.MAX_VALUE})
	void fightSuccess(int attackValue) {
		final Human me = randomHumanSupplier.get();
		Human enemy = randomHumanSupplier.get();
		final CombatStatistics statistics = me.fight(enemy);

		assertEquals(me, statistics.assailant());
		assertEquals(CombatStatus.SUCCESS, statistics.status());
		assertEquals(attackValue, statistics.hit());
		assertEquals(enemy.uniqueId(), statistics.defender().uniqueId());
		assertEquals(enemy.uniqueId(), statistics.defender().uniqueId());
		assertEquals(enemy.characteristics().life(), statistics.defender().characteristics().life());
		assertEquals(enemy.characteristics().life() - attackValue, statistics.defender().characteristics().life());
	}

	/**
	 * FLAKY due to Random
	 */
	@Test
	void fightMissed() {
		final Human me = randomHumanSupplier.get();
		Human enemy = randomHumanSupplier.get();
		final CombatStatistics statistics = me.fight(enemy);

		assertEquals(me, statistics.assailant());
		assertEquals(CombatStatus.MISSED, statistics.status());
		assertEquals(0, statistics.hit());
		assertEquals(enemy, statistics.defender());
		assertEquals(enemy.uniqueId(), statistics.defender().uniqueId());
		assertEquals(enemy.characteristics().life(), statistics.defender().characteristics().life());
	}
}
