package com.aedyl.domain.fighter;

import com.aedyl.domain.combat.CombatStatistics;
import com.aedyl.domain.combat.CombatStatus;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public record Human(UUID uniqueId,
                    String name,
                    Characteristics characteristics) {

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
		Human enemyAfterFight = enemy.suffersStroke(hit);
		if (hit == 0) {
			return new CombatStatistics(CombatStatus.MISSED, this, enemyAfterFight, hit);
		}
		return new CombatStatistics(CombatStatus.SUCCESS, this, enemyAfterFight, hit);
	}

	int getAttackPower() {
		return new Random().nextInt(characteristics.strength());
	}

	Human suffersStroke(int hit) {
		final Characteristics newCharac = this.characteristics.decreaseLife(hit);
		return new Human(uniqueId, name, newCharac);
	}
}
