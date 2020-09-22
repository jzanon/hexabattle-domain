package com.aedyl.domain.fighter;

import com.aedyl.domain.combat.AttackResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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

		final EnemyChooser enemyChooser = new EnemyChooser();
		Human humanCloned = new Human(human.uniqueId(), human.name(), human.characteristics(), enemyChooser, new AttackResolver());
		assertEquals(human, humanCloned);

		Human humanWithSameId = new Human(human.uniqueId(), "Another name", randomCharacteristicsSupplier.get(), enemyChooser, new AttackResolver());
		assertEquals(human, humanWithSameId);

		Human humanWithDifferentId = new Human(UUID.randomUUID(), human.name(), human.characteristics(), enemyChooser, new AttackResolver());
		assertNotEquals(human, humanWithDifferentId);
	}

	@Test
	void isAlive() {
		final EnemyChooser enemyChooser = new EnemyChooser();
		Human human = new Human(UUID.randomUUID(), "Plop", new Characteristics(5, 7, 7, 9), enemyChooser, new AttackResolver());
		assertTrue(human.isAlive());

		human = new Human(UUID.randomUUID(), "Plop", new Characteristics(5, 7, 0, 9), enemyChooser, new AttackResolver());
		assertFalse(human.isAlive());
	}


}
