package com.aedyl.domain.combat;

import com.aedyl.domain.fighter.Human;

public record AttackResult(AttackStatus status,
                           Attacker assailant,
                           Defender defender,
                           Integer hit) {


	public static AttackResult attack(Human humanAttacker, Human humanDefender, int hit) {
		Attacker attacker = Attacker.fromHuman(humanAttacker);
		Defender defender = Defender.fromHuman(humanDefender);
		return new AttackResult(AttackStatus.SUCCESS, attacker, defender, hit);
	}

	public static AttackResult missedAttack(Human humanAttacker, Human humanDefender) {
		Attacker attacker = Attacker.fromHuman(humanAttacker);
		Defender defender = Defender.fromHuman(humanDefender);
		return new AttackResult(AttackStatus.MISSED, attacker, defender, 0);
	}

	public static AttackResult noEnemyFound(Human humanAttacker) {
		Attacker attacker = Attacker.fromHuman(humanAttacker);
		return new AttackResult(AttackStatus.NO_ENEMY_FOUND, attacker, null, null);
	}

	public String getConsequenceOnDefender() {
		if (defender == null) {
			return "";
		}
		return String.format("%s's life: %s/%s%s", defender.name(),
				defender.characteristics().life(),
				defender.characteristics().maxLife(),
				defender.isAlive() ? "" : " (dead)");
	}

	public String buildSummary() {
		final String name = assailant.name();
		switch (status) {
			case SUCCESS -> {
				String consequenceOnDefender = getConsequenceOnDefender();
				return String.format("%s hit %s : %s damages. %s", name, defender.name(), hit, consequenceOnDefender);
			}
			case NO_ENEMY_FOUND -> {
				return String.format("%s did not found enemy", name);
			}
			case MISSED -> {
				return String.format("%s missed %s", name, defender.name());
			}
			default -> throw new IllegalStateException("Combat status not managed: " + status);
		}
	}
}
