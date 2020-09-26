package com.aedyl.domain.fighter;

import com.aedyl.domain.combat.AttackResolver;
import com.aedyl.domain.combat.AttackResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Human {

	public final UUID uniqueId;
	public final String name;
	private Characteristics characteristics;
	private final EnemyChooser enemyChooser;
	private final AttackResolver attackResolver;

	public Human(UUID uniqueId,
	             String name,
	             Characteristics characteristics,
	             EnemyChooser enemyChooser,
	             AttackResolver attackResolver) {
		this.uniqueId = uniqueId;
		this.name = name;
		this.characteristics = characteristics;
		this.enemyChooser = enemyChooser;
		this.attackResolver = attackResolver;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Human human = (Human) o;
		return uniqueId.equals(human.uniqueId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uniqueId);
	}

	@Override
	public String toString() {
		return "Human{" +
				"name='" + name + '}';
	}

	public boolean isAlive() {
		return characteristics.life() > 0;
	}

	public AttackResult fight(List<Human> fighters) {
		Optional<Human> enemy = enemyChooser.pickEnemy(this, fighters);
		return enemy
				.map(this::fight)
				.orElse(AttackResult.noEnemyFound(this));
	}


	public AttackResult fight(Human enemy) {
		return attackResolver.resolveAttack(this, enemy);
	}

	public Human suffersStroke(int hit) {
		final Characteristics newCharac = characteristics.decreaseLife(hit);
		return setCharacteristics(newCharac);
	}

	private Human setCharacteristics(Characteristics characteristics) {
		this.characteristics = characteristics;
		return this;
	}

	public Characteristics getCharacteristics() {
		return characteristics;
	}

}

