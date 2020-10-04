package com.aedyl.arenagame.domain;

import com.aedyl.arenagame.domain.characteristics.Characteristics;
import com.aedyl.arenagame.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.arenagame.domain.characteristics.Trait;
import com.aedyl.arenagame.domain.fighter.Human;
import com.aedyl.arenagame.domain.fighter.HumanSupplier;

import java.util.function.Function;
import java.util.function.Supplier;

public class HumanFactoryForTests {

	public HumanFactoryForTests() {

	}

	public Human createHuman(CharacteristicsSupplier.TraitSupplier supplier) {
		Supplier<Characteristics> caracSupplier = new CharacteristicsSupplier().setSupplier(supplier);
		return getHuman(caracSupplier);
	}

	public Human createHuman(CharacteristicsSupplier.TraitSupplier supplier, Function<Trait, Trait> secondaryTraitSupplier) {
		Supplier<Characteristics> caracSupplier = new CharacteristicsSupplier()
				.setSupplier(supplier)
				.setSecondaryTraitSupplier(secondaryTraitSupplier);
		return getHuman(caracSupplier);
	}

	public Human createHuman(CharacteristicsSupplier.LifeSupplier supplier) {
		Supplier<Characteristics> caracSupplier = new CharacteristicsSupplier().setSupplier(supplier);
		return getHuman(caracSupplier);
	}

	public Human getHuman(Supplier<Characteristics> caracSupplier) {
		final HumanSupplier humanSupplier = new HumanSupplier(caracSupplier);
		return humanSupplier.get();
	}

	public Human createRandomHuman() {
		return new HumanSupplier(new CharacteristicsSupplier()).get();
	}

	public Human createHuman(CharacteristicsSupplier.InitiativeSupplier supplier) {
		Supplier<Characteristics> caracSupplier = new CharacteristicsSupplier().setSupplier(supplier);
		return getHuman(caracSupplier);
	}

	public Human createHuman(CharacteristicsSupplier.StrengthSupplier supplier) {
		Supplier<Characteristics> caracSupplier = new CharacteristicsSupplier().setSupplier(supplier);
		return getHuman(caracSupplier);
	}
}
