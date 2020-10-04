package com.aedyl.arenagame.domain.combat;

import com.aedyl.arenagame.domain.fighter.Human;

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

}
