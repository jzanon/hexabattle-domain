package com.aedyl.arenagame.domain.fighter.port.input;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.AddFighterCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.CreateArenaCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.RunArenaCommand;
import com.aedyl.arenagame.domain.fighter.model.Human;
import com.aedyl.arenagame.domain.fighter.model.HumanId;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Exposed service to deal with fighter management.
 */
public interface FighterCommandHandler {

	Set<HumanId> handle(FghterCommand.CreateRandomHumansCommand createRandomHumansCommand);

}
