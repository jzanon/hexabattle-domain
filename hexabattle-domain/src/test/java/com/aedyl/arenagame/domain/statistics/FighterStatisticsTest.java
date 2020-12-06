package com.aedyl.arenagame.domain.statistics;

import com.aedyl.arenagame.domain.HumanBuilder;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.arenagame.domain.combat.AttackResult;
import com.aedyl.arenagame.domain.fighter.model.Human;
import com.aedyl.arenagame.domain.fighter.model.HumanId;
import com.aedyl.arenagame.domain.statistics.model.FighterStatistic;
import com.aedyl.arenagame.domain.statistics.model.FighterStatistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FighterStatisticsTest {

	@Test
	void updateAttacker() {
		final Human attacker = new HumanBuilder().build();
		final Human defender = new HumanBuilder().build();

		var battleScenario = buildBattleScenario(attacker, defender);

		FighterStatistics statistics = new FighterStatistics(ArenaId.randomId(), attacker.uniqueId, attacker.name);
		for (AttackResult attackResult : battleScenario) {
			statistics.updateAttacker(attackResult);
		}
		final Human killedFighter = new HumanBuilder().set((CharacteristicsSupplier.LifeSupplier) () -> 0).build();
		statistics.updateAttacker(AttackResult.attack(attacker, killedFighter, 1));
		statistics.survived();

		Collection<FighterStatistic> stats = statistics.getStats();
		Assertions.assertAll("Inconsistent Statistics",
				() -> checkStat(stats, FighterStatistics.FighterStatType.ATTACK, 3),
				() -> checkStat(stats, FighterStatistics.FighterStatType.DEFENSE, 0),
				() -> checkStat(stats, FighterStatistics.FighterStatType.HIT_SUM, 22),
				() -> checkStat(stats, FighterStatistics.FighterStatType.KILL, 1),
				() -> checkStat(stats, FighterStatistics.FighterStatType.MISS, 1),
				() -> checkStat(stats, FighterStatistics.FighterStatType.SUFFERED_HIT_SUM, 0),
				() -> checkStat(stats, FighterStatistics.FighterStatType.NO_ENEMY_FOUND, 1),
				() -> checkStat(stats, FighterStatistics.FighterStatType.SURVIVOR, 1));
	}

	private Collection<AttackResult> buildBattleScenario(Human attacker, Human defender) {
		Collection<AttackResult> attackResults = new ArrayList<>();
		final AttackResult attackResult = AttackResult.attack(attacker, defender, 9);
		attackResults.add(attackResult);
		attackResults.add(AttackResult.attack(attacker, defender, 12));
		attackResults.add(AttackResult.noEnemyFound(attacker));
		attackResults.add(AttackResult.missedAttack(attacker, defender));
		return attackResults;
	}

	private void checkStat(Collection<FighterStatistic> stats, FighterStatistics.FighterStatType statType, int expectedValue) {
		final FighterStatistic statistic = stats.stream()
				.filter(stat -> stat.type().equals(statType))
				.findAny()
				.orElseThrow(() -> new NoSuchElementException(statType + " not found"));
		assertEquals(expectedValue, statistic.value(), "Bad value for: " + statType);
	}

	@Test
	void updateDefender() {
		final Human attacker = new HumanBuilder().build();
		final Human defender = new HumanBuilder().build();

		var battleScenario = buildBattleScenario(attacker, defender);

		FighterStatistics statistics = new FighterStatistics(ArenaId.randomId(), defender.uniqueId, defender.name);
		for (AttackResult attackResult : battleScenario) {
			statistics.updateDefender(attackResult);
		}

		Collection<FighterStatistic> stats = statistics.getStats();
		Assertions.assertAll("Inconsistent Statistics",
				() -> checkStat(stats, FighterStatistics.FighterStatType.ATTACK, 0),
				() -> checkStat(stats, FighterStatistics.FighterStatType.DEFENSE, 2),
				() -> checkStat(stats, FighterStatistics.FighterStatType.HIT_SUM, 0),
				() -> checkStat(stats, FighterStatistics.FighterStatType.KILL, 0),
				() -> checkStat(stats, FighterStatistics.FighterStatType.MISS, 0),
				() -> checkStat(stats, FighterStatistics.FighterStatType.SUFFERED_HIT_SUM, 21),
				() -> checkStat(stats, FighterStatistics.FighterStatType.NO_ENEMY_FOUND, 0),
				() -> checkStat(stats, FighterStatistics.FighterStatType.SURVIVOR, 0));
	}


	@Test
	void survived() {
		FighterStatistics statistics = new FighterStatistics(ArenaId.randomId(), HumanId.randomId(), "");
		checkStat(statistics.getStats(), FighterStatistics.FighterStatType.SURVIVOR, 0);
		statistics.survived();
		checkStat(statistics.getStats(), FighterStatistics.FighterStatType.SURVIVOR, 1);
	}
}
