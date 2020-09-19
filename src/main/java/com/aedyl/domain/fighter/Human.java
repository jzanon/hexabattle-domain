package com.aedyl.domain.fighter;

import com.aedyl.domain.combat.CombatStatistics;
import com.aedyl.domain.combat.CombatStatus;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Human {
	public final UUID uniqueId;
	public final String name;
	public final Characteristics characteristics;

	public Human(UUID uniqueId,
	             String name,
	             Characteristics characteristics) {
		this.uniqueId = uniqueId;
		this.name = name;
		this.characteristics = characteristics;
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
				"name='" + name + '\'' +
				", initiative=" + characteristics.initiative +
				", life=" + characteristics.life +
				", strength=" + characteristics.strength +
				'}';
	}

	public boolean isAlive() {
		return characteristics.life > 0;
	}

	public CombatStatistics fight(List<Human> fighters) {
		Optional<Human> enemy = pickEnemy(fighters);
		return enemy
				.map(this::fight)
				.orElse(new CombatStatistics(CombatStatus.NO_ENEMY_FOUND, this, null, null));
	}

	Optional<Human> pickEnemy(List<Human> fighters) {
		List<Human> enemies = fighters.stream()
				.filter(not(this::equals))
				.filter(Human::isAlive)
				.collect(Collectors.toList());
		if (enemies.isEmpty()) {
			return Optional.empty();
		}
		Random rand = new Random();
		Human enemy = enemies.get(rand.nextInt(enemies.size()));
		return Optional.of(enemy);
	}

	CombatStatistics fight(Human enemy) {
		int hit = getAttackPower();
		enemy.isHit(hit);
		if (hit == 0) {
			return new CombatStatistics(CombatStatus.MISSED, this, enemy, hit);
		}
		return new CombatStatistics(CombatStatus.SUCCESS, this, enemy, hit);
	}

	int getAttackPower() {
		return new Random().nextInt(characteristics.strength);
	}

	void isHit(int hit) {
		characteristics.decreaseLife(hit);
	}
}
