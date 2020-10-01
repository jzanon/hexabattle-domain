package com.aedyl.domain.fighter;

import com.aedyl.domain.characteristics.Characteristics;
import com.aedyl.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.domain.characteristics.Trait;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EnemyChooserTest {

	private static HumanSupplier randomHumanSupplier;

	@BeforeEach
	void initSuppliers() {
		randomHumanSupplier = new HumanSupplier();
	}

	@Test
	void pick_enemy_do_not_pick_myself() {
		Human me = createHuman(() -> 20);

		Optional<Human> potentialEnemy = new EnemyChooser(i -> 0).pickEnemy(me, List.of(me));

		assertTrue(potentialEnemy.isEmpty());
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
		Human cruelHuman = createHuman(() -> Trait.CRUEL);
		Human humanFullLife1 = createHuman(() -> 20);
		Human humanWithLowLife = createHuman(() -> 3);
		Human humanFullLife2 = createHuman(() -> 10);

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
		Human cruelHuman = createHuman(() -> Trait.MERCIFUL);
		Human humanWithLowLife1 = createHuman(() -> 1);
		Human humanWithLowLife2 = createHuman(() -> 2);
		Human humanFullLife = createHuman(() -> 50);

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(cruelHuman, List.of(cruelHuman, humanWithLowLife1, humanFullLife, humanWithLowLife2));

		assertTrue(potentialEnemy.isPresent());
		assertEquals(humanFullLife, potentialEnemy.get());

	}


	@Test
	void merciful_human_prefers_target_noOne_than_low_life_enemies() {
		Human cruelHuman = createHuman(() -> Trait.MERCIFUL, (Trait t) -> Trait.MERCIFUL);
		Human humanWithLowLife1 = createHuman(() -> 1);
		Human humanWithLowLife2 = createHuman(() -> 2);

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(cruelHuman, List.of(cruelHuman, humanWithLowLife1, humanWithLowLife2));
		assertTrue(potentialEnemy.isEmpty());
	}

	@Test
	void vengeful_human_target_last_enemy_who_attacked_him() {
		Human me = createHuman(() -> Trait.VENGEFUL);
		Human humanWhoAttacksMe = createHuman(() -> 15);
		Human humanWithLowLifeWhoAttacksMe = createHuman(() -> 1);
		Human anotherLambdaHuman = createHuman(() -> 20);

		humanWhoAttacksMe.fight(me);
		humanWithLowLifeWhoAttacksMe.fight(me);

		Optional<Human> potentialEnemy = new EnemyChooser().pickEnemy(me, List.of(me, humanWhoAttacksMe, humanWithLowLifeWhoAttacksMe, anotherLambdaHuman));

		assertTrue(potentialEnemy.isPresent());
		assertEquals(humanWithLowLifeWhoAttacksMe, potentialEnemy.get());
	}

	@Test
	void primary_trait_have_priority_over_secondary_trait() {
		// Primary trait is MERCIFUL --> I attacked last an enemy who is low life now so I do not pick him,
		// even if my Secondary trait is PASSIONATE:
		Human me = createHuman(() -> Trait.MERCIFUL, (Trait trait) -> Trait.PASSIONATE);
		Human humanIAttackFirst =  createHuman(() -> 1000); // ensure he will not die at first fight
		Human lastHumanIAttackedButWithLowLife = createHuman(() -> 1);

		me.fight(humanIAttackFirst);
		me.fight(lastHumanIAttackedButWithLowLife);

		Optional<Human> potentialEnemy = new EnemyChooser(i -> 0).pickEnemy(me, List.of(me, humanIAttackFirst, lastHumanIAttackedButWithLowLife));

		assertTrue(potentialEnemy.isPresent());
		assertEquals(humanIAttackFirst, potentialEnemy.get());

		// Primary trait is PASSIONATE --> don't care of life level even if Secondary trait is MERCIFUL
		me = createHuman(() -> Trait.PASSIONATE, (Trait trait) -> Trait.MERCIFUL);
		humanIAttackFirst = createHuman(() -> 20);
		lastHumanIAttackedButWithLowLife = createHuman(() -> 1000); // ensure he will not die at first fight

		me.fight(humanIAttackFirst);
		me.fight(lastHumanIAttackedButWithLowLife);
		lastHumanIAttackedButWithLowLife.suffersStroke(me, lastHumanIAttackedButWithLowLife.getCharacteristics().life() - 1);

		potentialEnemy = new EnemyChooser(i -> 0).pickEnemy(me, List.of(me, humanIAttackFirst, lastHumanIAttackedButWithLowLife));

		assertTrue(potentialEnemy.isPresent());
		assertEquals(lastHumanIAttackedButWithLowLife, potentialEnemy.get());
	}


	@Test
	void passionate_human_continue_to_target_same_enemy() {
		Human me = createHuman(() -> Trait.PASSIONATE);
		Human humanIAttack1 =  createHuman(() -> 1000); // ensure he will not die at first fight
		Human humanIAttack2 =  createHuman(() -> 1000); // ensure he will not die at first fight

		me.fight(humanIAttack1);
		me.fight(humanIAttack2);

		Optional<Human> potentialEnemy = new EnemyChooser(i -> 0).pickEnemy(me, List.of(me, humanIAttack2, humanIAttack1));

		assertTrue(potentialEnemy.isPresent());
		assertEquals(humanIAttack2, potentialEnemy.get());
	}


	@Test
	void vengeful_human_choose_another_target_if_last_attacker_is_dead() {
		Human me = createHuman(() -> Trait.VENGEFUL, (Trait t) -> Trait.VENGEFUL);
		Human humanIAttack1 = randomHumanSupplier.get();

		humanIAttack1.fight(me);
		// ensure he is dead
		humanIAttack1.suffersStroke(me, humanIAttack1.getCharacteristics().maxLife());

		Optional<Human> potentialEnemy = new EnemyChooser(i -> 0).pickEnemy(me, List.of(me, humanIAttack1));
		assertTrue(potentialEnemy.isEmpty());

		Human humanIAttack2 = randomHumanSupplier.get();
		potentialEnemy = new EnemyChooser(i -> 0).pickEnemy(me, List.of(me, humanIAttack1, humanIAttack2));
		assertTrue(potentialEnemy.isPresent());
		assertEquals(humanIAttack2, potentialEnemy.get());
	}


	@ParameterizedTest
	@EnumSource(value = Trait.class, mode = EnumSource.Mode.MATCH_ALL)
	void human_do_not_target_dead_enemy(Trait trait) {
		Human me = createHuman(() -> trait);
		Human humanIAttack = randomHumanSupplier.get();

		// ensure he is dead
		humanIAttack.suffersStroke(me, humanIAttack.getCharacteristics().maxLife());

		Optional<Human> potentialEnemy = new EnemyChooser(i -> 0).pickEnemy(me, List.of(me, humanIAttack));
		assertTrue(potentialEnemy.isEmpty());
	}


	private Human createHuman(CharacteristicsSupplier.TraitSupplier supplier) {
		Supplier<Characteristics> caracSupplier = new CharacteristicsSupplier().setSupplier(supplier);
		return getHuman(caracSupplier);
	}

	private Human createHuman(CharacteristicsSupplier.TraitSupplier supplier, Function<Trait, Trait> secondaryTraitSupplier) {
		Supplier<Characteristics> caracSupplier = new CharacteristicsSupplier()
				.setSupplier(supplier)
				.setSecondaryTraitSupplier(secondaryTraitSupplier);
		return getHuman(caracSupplier);
	}

	private Human createHuman(CharacteristicsSupplier.LifeSupplier supplier) {
		Supplier<Characteristics> caracSupplier = new CharacteristicsSupplier().setSupplier(supplier);
		return getHuman(caracSupplier);
	}


	private Human getHuman(Supplier<Characteristics> caracSupplier) {
		final HumanSupplier humanSupplier = new HumanSupplier(caracSupplier);
		return humanSupplier.get();
	}

}
