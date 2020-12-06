package com.aedyl.arenagame.domain.fighter.model;

import com.aedyl.arenagame.domain.characteristics.Trait;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class EnemyChooser {
	public static final int LOW_LIFE = 3;
	private final Function<Integer, Integer> randomFunction;

	public EnemyChooser() {
		Random random = new Random();
		randomFunction = random::nextInt;
	}

	public EnemyChooser(Function<Integer, Integer> randomFunction) {
		this.randomFunction = randomFunction;
	}


	public Optional<Human> pickEnemy(Human me, List<Human> fighters) {
		final Trait primaryTrait = me.getCharacteristics().primary();
		List<Human> enemies = getEnemies(primaryTrait, me, fighters);

		if (enemies.isEmpty()) {
			final Trait secondaryTrait = me.getCharacteristics().secondary();
			enemies = getEnemies(secondaryTrait, me, fighters);
		}

		if (enemies.isEmpty()) {
			return Optional.empty();
		}

		Human enemy = getRandom(enemies);
		return Optional.of(enemy);
	}

	private List<Human> vengefulChoice(Human me, List<Human> fighters) {
		return me.getLastHumanWhoAttackedMe()
				.filter(Human::isAlive)
				.map(List::of)
				.orElseGet(() -> everyBodyAliveExceptMe(me, fighters));
	}

	private List<Human> getEnemies(Trait criteria, Human me, List<Human> fighters) {
		return switch (criteria) {
			case CRUEL -> cruelChoice(me, fighters);
			case MERCIFUL -> mercifulChoice(me, fighters);
			case APATHETIC, FORGIVING -> Collections.emptyList();
			case PASSIONATE -> {
				final Optional<Human> lastHumanIAttacked = me.getLastHumanIAttacked();
				yield lastHumanIAttacked
						.filter(Human::isAlive)
						.map(List::of)
						.orElseGet(Collections::emptyList);
			}
			case VENGEFUL -> vengefulChoice(me, fighters);
		};
	}

	private Human getRandom(List<Human> enemies) {
		final int bound = enemies.size();
		return enemies.get(randomFunction.apply(bound));
	}

	private List<Human> cruelChoice(Human me, List<Human> fighters) {
		final List<Human> nearToDeathEnemies = fighters.stream()
				.filter(Human::isAlive)
				.filter(not(me::equals))
				.filter(nearToDeath())
				.collect(Collectors.toList());
		if (nearToDeathEnemies.isEmpty()) {
			return everyBodyAliveExceptMe(me, fighters);
		}
		return nearToDeathEnemies;
	}

	private Predicate<Human> nearToDeath() {
		return human -> human.getCharacteristics().life() <= LOW_LIFE;
	}

	private List<Human> mercifulChoice(Human me, List<Human> fighters) {
		return fighters.stream()
				.filter(Human::isAlive)
				.filter(not(me::equals))
				.filter(not(nearToDeath()))
				.collect(Collectors.toList());
	}

	private List<Human> everyBodyAliveExceptMe(Human me, List<Human> fighters) {
		return fighters.stream()
				.filter(not(me::equals))
				.filter(Human::isAlive)
				.collect(Collectors.toList());
	}

}
