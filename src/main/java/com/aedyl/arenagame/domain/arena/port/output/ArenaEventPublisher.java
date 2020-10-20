package com.aedyl.arenagame.domain.arena.port.output;

public interface ArenaEventPublisher {

	void publish(ArenaEvent arenaEvent);
}
