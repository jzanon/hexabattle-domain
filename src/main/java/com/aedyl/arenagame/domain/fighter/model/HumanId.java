package com.aedyl.arenagame.domain.fighter.model;

import java.util.UUID;

public record HumanId(UUID id) {
	public static HumanId randomId() {
		return new HumanId(UUID.randomUUID());
	}
}
