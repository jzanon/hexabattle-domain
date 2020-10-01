package com.aedyl.domain.characteristics;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import com.github.javafaker.Options;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CharacteristicsSupplier implements Supplier<Characteristics> {
	private final Options option;

	public interface LifeSupplier extends Supplier<Integer> {
	}

	public interface StrengthSupplier extends Supplier<Integer> {
	}

	public interface InitiativeSupplier extends Supplier<Integer> {
	}

	public interface TraitSupplier extends Supplier<Trait> {
	}

	private InitiativeSupplier initiativeSupplier;
	private LifeSupplier lifeSupplier;
	private StrengthSupplier strengthSupplier;
	private TraitSupplier primaryTraitSupplier;
	private Function<Trait, Trait> secondaryTraitSupplier;

	public CharacteristicsSupplier() {
		Faker faker = Faker.instance();
		option = faker.options();
		Number numberSupplier = faker.number();
		initiativeSupplier = () -> numberSupplier.numberBetween(1, 20);
		lifeSupplier = () -> numberSupplier.numberBetween(1, 20);
		strengthSupplier = () -> numberSupplier.numberBetween(1, 20);
		primaryTraitSupplier = () -> option.option(Trait.class);
		secondaryTraitSupplier = this::getCompatibleTrait;
	}

	@Override
	public Characteristics get() {
		final Integer maxLife = lifeSupplier.get();
		final Trait primary = primaryTraitSupplier.get();
		return new Characteristics(
				initiativeSupplier.get(),
				maxLife,
				maxLife,
				strengthSupplier.get(),
				primary,
				secondaryTraitSupplier.apply(primary)
		);
	}

	private Trait getCompatibleTrait(Trait primary) {
		final List<Trait> compatibleTraits = Arrays.stream(Trait.values().clone())
				.filter(trait -> !primary.getIncompatibleTraits().contains(trait))
				.collect(Collectors.toList());
		return option.nextElement(compatibleTraits);
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

	public CharacteristicsSupplier setSupplier(TraitSupplier primaryTraitSupplier) {
		this.primaryTraitSupplier = primaryTraitSupplier;
		return this;
	}

	public CharacteristicsSupplier setSecondaryTraitSupplier(Function<Trait, Trait> secondaryTraitSupplier) {
		this.secondaryTraitSupplier = secondaryTraitSupplier;
		return this;
	}
}
