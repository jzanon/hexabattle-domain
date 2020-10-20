package com.aedyl.arenagame.domain.combat;

import com.aedyl.arenagame.domain.characteristics.Characteristics;
import com.aedyl.arenagame.domain.characteristics.Trait;
import com.aedyl.arenagame.domain.fighter.EnemyChooser;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanId;
import com.aedyl.arenagame.domain.fighter.HumanSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttackResolverTest {
	private static HumanSupplier randomHumanSupplier;

	@BeforeAll
	static void initSuppliers() {
		randomHumanSupplier = new HumanSupplier();
	}

	/**
	 * FLAKY due to Random
	 */
	@ParameterizedTest
	@ValueSource(ints = {1, 3, 5, -3, 15, Integer.MAX_VALUE})
	void fightSuccess(int attackValue) {
		final AttackResolverWithoutRandom attackResolver = new AttackResolverWithoutRandom();
		attackResolver.setAttackPower(attackValue);

		final Human me = new Human(HumanId.randomId(), "Plop", new Characteristics(5, 7, 0, 9, Trait.MERCIFUL, Trait.PASSIONATE), new EnemyChooser(i -> 0), attackResolver);
		Human enemy = randomHumanSupplier.get();
		final int initialLifeOfEnemy = enemy.getCharacteristics().life();

		final AttackResult statistics = me.fight(enemy);

		assertEquals(Attacker.fromHuman(me), statistics.assailant());
		assertEquals(AttackStatus.SUCCESS, statistics.status());
		assertEquals(attackValue, statistics.hit());
		assertEquals(enemy.uniqueId, statistics.defender().id());
		assertEquals(initialLifeOfEnemy - attackValue, statistics.defender().characteristics().life());
	}

	@Test
	void fightMissed() {
		final AttackResolverWithoutRandom attackResolver = new AttackResolverWithoutRandom();
		attackResolver.setAttackPower(0);

		final Human me = new Human(HumanId.randomId(), "Plop", new Characteristics(5, 7, 0, 9, Trait.MERCIFUL, Trait.PASSIONATE), new EnemyChooser(i -> 0), attackResolver);
		Human enemy = randomHumanSupplier.get();
		final AttackResult statistics = me.fight(enemy);

		assertEquals(Attacker.fromHuman(me), statistics.assailant());
		assertEquals(AttackStatus.MISSED, statistics.status());
		assertEquals(0, statistics.hit());
		assertEquals(Defender.fromHuman(enemy), statistics.defender());
		assertEquals(enemy.uniqueId, statistics.defender().id());
		assertEquals(enemy.getCharacteristics().life(), statistics.defender().characteristics().life());
	}


}
