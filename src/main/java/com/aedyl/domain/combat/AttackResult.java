package com.aedyl.domain.combat;

import com.aedyl.domain.fighter.Human;

public record AttackResult(AttackStatus status,
                           Human assailant,
                           Human defender,
                           Integer hit) {


	public static AttackResult attack(Human attacker, Human defender, int hit) {
		return new AttackResult(AttackStatus.SUCCESS, attacker, defender, hit);
	}

	public static AttackResult missedAttack(Human attacker, Human defender) {
		return new AttackResult(AttackStatus.MISSED, attacker, defender, 0);
	}

	public static AttackResult noEnemyFound(Human attacker) {
		return new AttackResult(AttackStatus.NO_ENEMY_FOUND, attacker, null, null);
	}

	public String getConsequenceOnDefender() {
		if (defender == null) {
			return "";
		}
		String consequenceOnDefender;
		if (defender.isAlive()) {
			consequenceOnDefender = String.format("%s's life: %s/%s", defender.name,
					defender.getCharacteristics().life(),
					defender.getCharacteristics().maxLife());
		} else {
			consequenceOnDefender = String.format("%s is dead", defender.name);
		}
		return consequenceOnDefender;
	}

	public String buildSummary() {
		final String name = assailant.name;
		switch (status) {
			case SUCCESS -> {
				String consequenceOnDefender = getConsequenceOnDefender();
				return String.format("%s hit %s : %s damages. %s", name, defender.name, hit, consequenceOnDefender);
			}
			case NO_ENEMY_FOUND -> {
				return String.format("%s did not found enemy", name);
			}
			case MISSED -> {
				return String.format("%s missed %s", name, defender.name);
			}
			default -> throw new IllegalStateException("Combat status not managed: " + status);
		}
	}
}
