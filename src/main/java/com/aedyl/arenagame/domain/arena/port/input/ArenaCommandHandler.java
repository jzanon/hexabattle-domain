package com.aedyl.arenagame.domain.arena.port.input;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.AddFighterCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.CreateArenaCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.RunArenaCommand;

import java.util.concurrent.Future;

/**
 * Exposed service to deal with arena management.
 */
public interface ArenaCommandHandler {

	Arena handle(CreateArenaCommand createArenaCommand);

	void handle(AddFighterCommand addFighterCommand);

	Future<Void> handle(RunArenaCommand addFighterCommand);

}
