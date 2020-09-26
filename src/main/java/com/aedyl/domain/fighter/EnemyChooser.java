package com.aedyl.domain.fighter;

import com.aedyl.domain.characteristics.Trait;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class EnemyChooser {
	public static final int LOW_LIFE = 3;
	private final Random random;

	public EnemyChooser() {
		random = new Random();
	}

	public Optional<Human> pickEnemy(Human me, List<Human> fighters) {
		List<Human> enemies;
		if (me.is(Trait.MERCIFUL)) {
			enemies = mercifulChoice(me, fighters);
		} else if (me.is(Trait.CRUEL)) {
			enemies = cruelChoice(me, fighters);
		} else {
			enemies = everyBodyAliveExceptMe(me, fighters);
		}
		if (enemies.isEmpty()) {
			return Optional.empty();
		}
		Human enemy = getRandom(enemies);
		return Optional.of(enemy);
	}

	private Human getRandom(List<Human> enemies) {
		return enemies.get(random.nextInt(enemies.size()));
	}

	private List<Human> cruelChoice(Human me, List<Human> fighters) {
		final List<Human> nearToDeathEnemies = fighters.stream()
				.filter(Human::isAlive)
				.filter(not(me::equals))
				.filter(isNearToDeath())
				.collect(Collectors.toList());
		if (nearToDeathEnemies.isEmpty()) {
			return everyBodyAliveExceptMe(me, fighters);
		}
		return nearToDeathEnemies;
	}

	private Predicate<Human> isNearToDeath() {
		return human -> human.getCharacteristics().life() <= LOW_LIFE;
	}

	private List<Human> mercifulChoice(Human me, List<Human> fighters) {
		return fighters.stream()
				.filter(Human::isAlive)
				.filter(not(me::equals))
				.filter(not(isNearToDeath()))
				.collect(Collectors.toList());
	}

	private List<Human> everyBodyAliveExceptMe(Human me, List<Human> fighters) {
		return fighters.stream()
				.filter(not(me::equals))
				.filter(Human::isAlive)
				.collect(Collectors.toList());
	}

}
