package com.aedyl.domain.fighter;

import com.aedyl.domain.fighter.CharacteristicsSupplier;
import com.aedyl.domain.fighter.EnemyChooser;
import com.aedyl.domain.fighter.Human;
import com.aedyl.domain.fighter.HumanSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;

class EnemyChooserTest {

	private static HumanSupplier randomHumanSupplier;

	@BeforeAll
	static void initSuppliers() {
		CharacteristicsSupplier randomCharacteristicsSupplier = new CharacteristicsSupplier();
		randomHumanSupplier = new HumanSupplier(randomCharacteristicsSupplier);
	}

	@Test
	void pickEnemyDoNotPickMySelf() {
		Human me = randomHumanSupplier.get();
		Human anotherHuman1 = randomHumanSupplier.get();
		Human anotherHuman2 = randomHumanSupplier.get();

		List<Human> fighters = List.of(me, anotherHuman1, anotherHuman2);

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(me, fighters);

		assertTrue(potentialEnemy.isPresent());

		Human enemy = potentialEnemy.get();
		assertNotEquals(me, enemy);
		assertThat(List.of(anotherHuman1, anotherHuman2), hasItems(enemy));
	}

	@Test
	void pickNobody() {
		Human me = randomHumanSupplier.get();

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(me, List.of(me));
		assertFalse(potentialEnemy.isPresent());

		potentialEnemy = new EnemyChooser().pickEnemy(me, Collections.emptyList());
		assertFalse(potentialEnemy.isPresent());
	}
}
