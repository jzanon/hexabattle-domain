package com.aedyl.arenagame.domain.fighter;

import java.util.UUID;

public record HumanId(UUID id) {
	public static HumanId randomId() {
		return new HumanId(UUID.randomUUID());
	}
}
