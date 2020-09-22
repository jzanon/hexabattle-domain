package com.aedyl.domain.fighter;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class EnemyChooser {
	private final Random random;

	public EnemyChooser() {
		random = new Random();
	}

	public Optional<Human> pickEnemy(Human me, List<Human> fighters) {
		List<Human> enemies = fighters.stream()
				.filter(not(me::equals))
				.filter(Human::isAlive)
				.collect(Collectors.toList());
		if (enemies.isEmpty()) {
			return Optional.empty();
		}
		Human enemy = enemies.get(random.nextInt(enemies.size()));
		return Optional.of(enemy);
	}

}
