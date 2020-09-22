package com.aedyl.domain.combat;

import com.aedyl.domain.fighter.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttackResolverTest {
	private static HumanSupplier randomHumanSupplier;

	@BeforeAll
	static void initSuppliers() {
		CharacteristicsSupplier randomCharacteristicsSupplier = new CharacteristicsSupplier();
		randomHumanSupplier = new HumanSupplier(randomCharacteristicsSupplier);
	}

	/**
	 * FLAKY due to Random
	 */
	@ParameterizedTest
	@ValueSource(ints = {1, 3, 5, -3, 15, Integer.MAX_VALUE})
	void fightSuccess(int attackValue) {
		final AttackResolverWithoutRandom attackResolver = new AttackResolverWithoutRandom();
		attackResolver.setAttackPower(attackValue);

		final Human me = new Human(UUID.randomUUID(), "Plop", new Characteristics(5, 7, 0, 9), new EnemyChooser(), attackResolver);
		Human enemy = randomHumanSupplier.get();

		final CombatStatistics statistics = me.fight(enemy);

		assertEquals(me, statistics.assailant());
		assertEquals(CombatStatus.SUCCESS, statistics.status());
		assertEquals(attackValue, statistics.hit());
		assertEquals(enemy.uniqueId(), statistics.defender().uniqueId());
		assertEquals(enemy.characteristics().life() - attackValue, statistics.defender().characteristics().life());
	}

	/**
	 * FLAKY due to Random
	 */
	@Test
	void fightMissed() {
		final AttackResolverWithoutRandom attackResolver = new AttackResolverWithoutRandom();
		attackResolver.setAttackPower(0);

		final Human me = new Human(UUID.randomUUID(), "Plop", new Characteristics(5, 7, 0, 9), new EnemyChooser(), attackResolver);
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