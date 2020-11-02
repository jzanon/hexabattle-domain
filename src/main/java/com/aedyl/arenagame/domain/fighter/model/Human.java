package com.aedyl.arenagame.domain.fighter.model;

import com.aedyl.arenagame.domain.characteristics.Characteristics;
import com.aedyl.arenagame.domain.characteristics.HumanMemory;
import com.aedyl.arenagame.domain.combat.AttackResolver;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.combat.AttackStatus;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Human {

	public final HumanId uniqueId;
	public final String name;
	private Characteristics characteristics;
	private final EnemyChooser enemyChooser;
	private final AttackResolver attackResolver;
	private final HumanMemory memory = new HumanMemory();

	public Human(HumanId uniqueId,
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
				"name='" + name +
				"', traits='" + characteristics.primary() + "','" + characteristics.secondary() + "'" +
				", life=" + characteristics.life() + "/" + characteristics.maxLife() +
				"}";
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
		final AttackResult attackResult = attackResolver.resolveAttack(this, enemy);
		if (!attackResult.status().equals(AttackStatus.NO_ENEMY_FOUND)) {
			memory.setLastHumanIAttacked(enemy);
		}
		return attackResult;
	}

	public Human suffersStroke(Human attacker, int hit) {
		memory.setLastHumanWhoAttackedMe(attacker);
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

	public Optional<Human> getLastHumanWhoAttackedMe() {
		return memory.getLastHumanWhoAttackedMe();
	}

	public Optional<Human> getLastHumanIAttacked() {
		return memory.getLastHumanIAttacked();
	}
}

