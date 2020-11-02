package com.aedyl.arenagame.domain;

import com.aedyl.arenagame.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.arenagame.domain.characteristics.Trait;
import com.aedyl.arenagame.domain.fighter.model.Human;

import java.util.function.Function;

public class HumanFactoryForTests {

	public Human createHuman(CharacteristicsSupplier.TraitSupplier supplier) {
		return new HumanBuilder()
				.set(supplier)
				.build();
	}

	public Human createHuman(CharacteristicsSupplier.TraitSupplier supplier, Function<Trait, Trait> secondaryTraitSupplier) {
		return new HumanBuilder()
				.set(supplier)
				.set(secondaryTraitSupplier)
				.build();
	}

	public Human createHuman(CharacteristicsSupplier.LifeSupplier supplier) {
		return new HumanBuilder().set(supplier).build();
	}

	public Human createRandomHuman() {
		return new HumanBuilder().build();
	}

	public Human createHuman(CharacteristicsSupplier.InitiativeSupplier supplier) {
		return new HumanBuilder().set(supplier).build();
	}

	public Human createHuman(CharacteristicsSupplier.StrengthSupplier supplier) {
		return new HumanBuilder().set(supplier).build();
	}

}
