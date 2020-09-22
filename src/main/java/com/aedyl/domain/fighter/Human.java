package com.aedyl.domain.fighter;

import com.aedyl.domain.combat.CombatStatistics;
import com.aedyl.domain.combat.CombatStatus;

import java.util.*;

public record Human(UUID uniqueId,
                    String name,
                    Characteristics characteristics,
                    EnemyChooser enemyChooser,
                    AttackResolver attackResolver) {


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
		Optional<Human> enemy = enemyChooser.pickEnemy(this, fighters);
		return enemy
				.map(this::fight)
				.orElse(new CombatStatistics(CombatStatus.NO_ENEMY_FOUND, this, null, null));
	}


	public CombatStatistics fight(Human enemy) {
		return attackResolver.resolveAttack(this, enemy);
	}

	public Human suffersStroke(int hit) {
		final Characteristics newCharac = this.characteristics.decreaseLife(hit);
		return new Human(uniqueId, name, newCharac, enemyChooser, attackResolver);
	}
}
