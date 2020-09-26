package com.aedyl.domain.characteristics;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import com.github.javafaker.Options;

import java.util.function.Supplier;

public class CharacteristicsSupplier implements Supplier<Characteristics> {
	private Supplier<Traits> traitSupplier;
	private Supplier<Integer> initiativeSupplier;
	private Supplier<Integer> lifeSupplier;
	private Supplier<Integer> strengthSupplier;

	public CharacteristicsSupplier() {
		Faker faker = Faker.instance();
		Options option = faker.options();
		Number numberSupplier = faker.number();
		initiativeSupplier = () -> numberSupplier.numberBetween(1, 20);
		lifeSupplier = () -> numberSupplier.numberBetween(1, 20);
		strengthSupplier = () -> numberSupplier.numberBetween(1, 20);
		traitSupplier = () -> Traits.of(option.option(Trait.class));
	}

	@Override
	public Characteristics get() {
		final Integer maxLife = lifeSupplier.get();
		return new Characteristics(
				initiativeSupplier.get(),
				maxLife,
				maxLife,
				strengthSupplier.get(),
				traitSupplier.get()
		);
	}

	public CharacteristicsSupplier setInitiativeSupplier(Supplier<Integer> initiativeSupplier) {
		this.initiativeSupplier = initiativeSupplier;
		return this;
	}

	public CharacteristicsSupplier setLifeSupplier(Supplier<Integer> lifeSupplier) {
		this.lifeSupplier = lifeSupplier;
		return this;
	}

	public CharacteristicsSupplier setStrengthSupplier(Supplier<Integer> strengthSupplier) {
		this.strengthSupplier = strengthSupplier;
		return this;
	}

	public CharacteristicsSupplier setTraitSupplier(Supplier<Traits> traitSupplier) {
		this.traitSupplier = traitSupplier;
		return this;
	}
}
