package com.aedyl.arenagame.domain.fighter;

import com.aedyl.arenagame.domain.characteristics.Characteristics;
import com.aedyl.arenagame.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.arenagame.domain.combat.AttackResolver;
import com.github.javafaker.Ancient;
import com.github.javafaker.Faker;

import java.util.UUID;
import java.util.function.Supplier;

public class HumanSupplier implements Supplier<Human> {

	private final Faker faker = Faker.instance();
	private Supplier<Characteristics> characteristicsSupplier;
	private EnemyChooser enemyChooser;
	private AttackResolver attackResolver;

	public HumanSupplier() {
		this.characteristicsSupplier = new CharacteristicsSupplier();
		this.enemyChooser = new EnemyChooser();
		this.attackResolver = new AttackResolver();
	}

	public HumanSupplier setEnemyChooser(EnemyChooser enemyChooser) {
		this.enemyChooser = enemyChooser;
		return this;
	}

	public HumanSupplier setAttackResolver(AttackResolver attackResolver) {
		this.attackResolver = attackResolver;
		return this;
	}

	public HumanSupplier setCharacteristicsSupplier(Supplier<Characteristics> characteristicsSupplier) {
		this.characteristicsSupplier = characteristicsSupplier;
		return this;
	}

	@Override
	public Human get() {
		Ancient nameSupplier = faker.ancient();

		return new Human(
				UUID.randomUUID(),
				nameSupplier.hero(),
				characteristicsSupplier.get(),
				enemyChooser,
				attackResolver
		);
	}

}
