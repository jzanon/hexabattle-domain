package com.aedyl.arenagame.domain;

import com.aedyl.arenagame.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.arenagame.domain.characteristics.Trait;
import com.aedyl.arenagame.domain.combat.AttackResolver;
import com.aedyl.arenagame.domain.fighter.EnemyChooser;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanSupplier;

import java.util.function.Function;

public class HumanBuilder {
	private final HumanSupplier humanSupplier;
	private final CharacteristicsSupplier characteristicsSupplier;

	public HumanBuilder() {
		this.characteristicsSupplier = new CharacteristicsSupplier();
		this.humanSupplier = new HumanSupplier()
				.setCharacteristicsSupplier(characteristicsSupplier);
	}

	public Human build() {
		return this.humanSupplier.get();
	}

	public HumanBuilder set(EnemyChooser enemyChooser) {
		this.humanSupplier.setEnemyChooser(enemyChooser);
		return this;
	}

	public HumanBuilder set(AttackResolver attackResolver) {
		this.humanSupplier.setAttackResolver(attackResolver);
		return this;
	}

	public HumanBuilder set(CharacteristicsSupplier.InitiativeSupplier supplier) {
		characteristicsSupplier.setSupplier(supplier);
		return this;
	}

	public HumanBuilder set(CharacteristicsSupplier.StrengthSupplier supplier) {
		characteristicsSupplier.setSupplier(supplier);
		return this;
	}

	public HumanBuilder set(CharacteristicsSupplier.TraitSupplier supplier) {
		characteristicsSupplier.setSupplier(supplier);
		return this;
	}

	public HumanBuilder set(CharacteristicsSupplier.LifeSupplier supplier) {
		characteristicsSupplier.setSupplier(supplier);
		return this;
	}

	public HumanBuilder set(Function<Trait, Trait> supplier) {
		characteristicsSupplier.setSecondaryTraitSupplier(supplier);
		return this;
	}
}
