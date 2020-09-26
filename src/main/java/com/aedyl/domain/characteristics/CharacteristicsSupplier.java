package com.aedyl.domain.characteristics;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import com.github.javafaker.Options;

import java.util.Set;
import java.util.function.Supplier;

public class CharacteristicsSupplier implements Supplier<Characteristics> {
	private final Faker faker = Faker.instance();

	@Override
	public Characteristics get() {
		final Options option = faker.options();
		Number numberSupplier = faker.number();
		Supplier<Integer> randomInitiative = () -> numberSupplier.numberBetween(1, 20);
		Supplier<Integer> randomMaxLife = () -> numberSupplier.numberBetween(1, 20);
		Supplier<Integer> randomStrength = () -> numberSupplier.numberBetween(1, 20);
		final Integer maxLife = randomMaxLife.get();
		return new Characteristics(
				randomInitiative.get(),
				maxLife,
				maxLife,
				randomStrength.get(),
				Set.of(option.option(Trait.class))
		);
	}
}
