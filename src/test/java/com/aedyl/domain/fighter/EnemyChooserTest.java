package com.aedyl.domain.fighter;

import com.aedyl.domain.characteristics.Characteristics;
import com.aedyl.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.domain.characteristics.Trait;
import com.aedyl.domain.characteristics.Traits;
import com.aedyl.domain.combat.AttackResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EnemyChooserTest {

	private static HumanSupplier randomHumanSupplier;

	@BeforeEach
	void initSuppliers() {
		CharacteristicsSupplier randomCharacteristicsSupplier = new CharacteristicsSupplier();
		randomHumanSupplier = new HumanSupplier(randomCharacteristicsSupplier);
	}

	@Test
	void pick_enemy_do_not_pick_myself() {
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
	void pick_nobody() {
		Human me = randomHumanSupplier.get();

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(me, List.of(me));
		assertFalse(potentialEnemy.isPresent());

		potentialEnemy = new EnemyChooser().pickEnemy(me, Collections.emptyList());
		assertFalse(potentialEnemy.isPresent());
	}

	@Test
	void cruel_human_targets_low_life_enemies_first() {
		Human cruelHuman = createHuman(() -> Traits.of(Trait.CRUEL));
		Human humanFullLife1 = new Human(UUID.randomUUID(), "HumanFullLife1", new Characteristics(5, 10, 10, 9, Traits.of(Trait.CRUEL)), new EnemyChooser(), new AttackResolver());
		Human humanWithLowLife = new Human(UUID.randomUUID(), "HumanWithLowLife", new Characteristics(5, 50, 2, 9, Traits.of(Trait.MERCIFUL)), new EnemyChooser(), new AttackResolver());
		Human humanFullLife2 = new Human(UUID.randomUUID(), "HumanFullLife2", new Characteristics(5, 5, 5, 9, Traits.of(Trait.CRUEL)), new EnemyChooser(), new AttackResolver());

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(cruelHuman, List.of(cruelHuman, humanFullLife1, humanWithLowLife, humanFullLife2));

		assertTrue(potentialEnemy.isPresent());
		assertEquals(humanWithLowLife, potentialEnemy.get());

		// No low-life enemy so cruel enemy chose another one who can be full-life
		potentialEnemy = new EnemyChooser().pickEnemy(cruelHuman, List.of(cruelHuman, humanFullLife1));
		assertTrue(potentialEnemy.isPresent());
		assertEquals(humanFullLife1, potentialEnemy.get());
	}

	@Test
	void merciful_human_do_not_targets_low_life_enemies() {
		Human cruelHuman = createHuman(() -> Traits.of(Trait.MERCIFUL));
		Human humanWithLowLife1 = new Human(UUID.randomUUID(), "HumanWithLowLife1", new Characteristics(5, 10, 1, 9, Traits.of(Trait.CRUEL)), new EnemyChooser(), new AttackResolver());
		Human humanWithLowLife2 = new Human(UUID.randomUUID(), "HumanWithLowLife2", new Characteristics(5, 50, 2, 9, Traits.of(Trait.MERCIFUL)), new EnemyChooser(), new AttackResolver());
		Human humanFullLife = new Human(UUID.randomUUID(), "HumanFullLife", new Characteristics(5, 40, 40, 9, Traits.of(Trait.CRUEL)), new EnemyChooser(), new AttackResolver());

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(cruelHuman, List.of(cruelHuman, humanWithLowLife1, humanFullLife, humanWithLowLife2));

		assertTrue(potentialEnemy.isPresent());
		assertEquals(humanFullLife, potentialEnemy.get());

	}

	@Test
	void merciful_human_prefers_target_noOne_than_low_life_enemies() {
		Human cruelHuman = createHuman(() -> Traits.of(Trait.MERCIFUL));
		Human humanWithLowLife1 = new Human(UUID.randomUUID(), "HumanWithLowLife1", new Characteristics(5, 10, 1, 9, Traits.of(Trait.CRUEL)), new EnemyChooser(), new AttackResolver());
		Human humanWithLowLife2 = new Human(UUID.randomUUID(), "HumanWithLowLife2", new Characteristics(5, 50, 2, 9, Traits.of(Trait.MERCIFUL)), new EnemyChooser(), new AttackResolver());

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(cruelHuman, List.of(cruelHuman, humanWithLowLife1, humanWithLowLife2));
		assertTrue(potentialEnemy.isEmpty());
	}

	private Human createHuman(Supplier<Traits> supplier) {
		Supplier<Characteristics> cruelCaracSupplier = new CharacteristicsSupplier().setTraitSupplier(supplier);
		final HumanSupplier cruelHumanSupplier = new HumanSupplier(cruelCaracSupplier);
		return cruelHumanSupplier.get();
	}


}
