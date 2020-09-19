package com.aedyl.domain.fighter;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;

import java.util.function.Supplier;

public class CharacteristicsSupplier implements Supplier<Characteristics> {
	private final Faker faker = Faker.instance();

	@Override
	public Characteristics get() {
		Number numberSupplier = faker.number();
		Supplier<Integer> randomInitiative = () -> numberSupplier.numberBetween(1, 20);
		Supplier<Integer> randomMaxLife = () -> numberSupplier.numberBetween(1, 20);
		Supplier<Integer> randomStrength = () -> numberSupplier.numberBetween(1, 20);
		return new Characteristics(
				randomInitiative.get(),
				randomMaxLife.get(),
				randomStrength.get()
		);
	}
}
