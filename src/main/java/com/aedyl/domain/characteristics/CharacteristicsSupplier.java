package com.aedyl.domain.characteristics;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import com.github.javafaker.Options;

import java.util.function.Supplier;

public class CharacteristicsSupplier implements Supplier<Characteristics> {
	public interface LifeSupplier extends Supplier<Integer> {
	}

	public interface StrengthSupplier extends Supplier<Integer> {
	}

	public interface InitiativeSupplier extends Supplier<Integer> {
	}

	public interface TraitsSupplier extends Supplier<Traits> {
	}

	private TraitsSupplier traitSupplier;
	private InitiativeSupplier initiativeSupplier;
	private LifeSupplier lifeSupplier;
	private StrengthSupplier strengthSupplier;

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

	public CharacteristicsSupplier setSupplier(InitiativeSupplier initiativeSupplier) {
		this.initiativeSupplier = initiativeSupplier;
		return this;
	}

	public CharacteristicsSupplier setSupplier(LifeSupplier lifeSupplier) {
		this.lifeSupplier = lifeSupplier;
		return this;
	}

	public CharacteristicsSupplier setSupplier(StrengthSupplier strengthSupplier) {
		this.strengthSupplier = strengthSupplier;
		return this;
	}

	public CharacteristicsSupplier setSupplier(TraitsSupplier traitSupplier) {
		this.traitSupplier = traitSupplier;
		return this;
	}
}
