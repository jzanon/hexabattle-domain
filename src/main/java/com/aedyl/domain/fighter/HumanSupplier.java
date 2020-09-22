package com.aedyl.domain.fighter;

import com.aedyl.domain.combat.AttackResolver;
import com.github.javafaker.Ancient;
import com.github.javafaker.Faker;

import java.util.UUID;
import java.util.function.Supplier;

public class HumanSupplier implements Supplier<Human> {

	private final Faker faker = Faker.instance();
	private final Supplier<Characteristics> characteristicsSupplier;

	public HumanSupplier(Supplier<Characteristics> charactSupplier) {
		this.characteristicsSupplier = charactSupplier;
	}

	@Override
	public Human get() {
		Ancient nameSupplier = faker.ancient();
		return new Human(
				UUID.randomUUID(),
				nameSupplier.hero(),
				characteristicsSupplier.get(),
				new EnemyChooser(),
				new AttackResolver()
		);
	}
}
