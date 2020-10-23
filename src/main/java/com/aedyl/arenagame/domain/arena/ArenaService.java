package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommandHandler;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEvent;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.Human;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static com.aedyl.arenagame.domain.arena.port.output.ArenaEvent.ArenaCreatedEvent;

public class ArenaService implements ArenaCommandHandler {

	private final ArenaEventPublisher arenaEventPublisher;
	private final ArenaRepository arenaRepository;

	public ArenaService(ArenaEventPublisher arenaEventPublisher,
	                    ArenaRepository arenaRepository) {
		this.arenaEventPublisher = arenaEventPublisher;
		this.arenaRepository = arenaRepository;
	}


	@Override
	public Arena handle(CreateArenaCommand createArenaCommand) {
		return createArena(createArenaCommand.arenaSize(), createArenaCommand.nbRoundMax());
	}

	Arena createArena(int arenaSize, int nbRoundMax) {
		Arena arena = new Arena(arenaSize, nbRoundMax);
		arenaRepository.save(arena);
		arenaEventPublisher.publish(ArenaCreatedEvent.from(arena));
		return arena;
	}

	@Override
	public void handle(AddFighterCommand addFighterCommand) {
		addFighter(addFighterCommand.arenaId(), addFighterCommand.fighter());
	}

	void addFighter(ArenaId arenaId, Human fighter) {
		Arena arena = arenaRepository.findById(arenaId).orElseThrow();
		arena.addFighter(fighter);
		arenaRepository.save(arena);
		arenaEventPublisher.publish(ArenaEvent.HumanJoinedArenaEvent.from(arena.id(), fighter));
	}

	@Override
	public Future<Void> handle(RunArenaCommand runArenaCommand) {
		return run(runArenaCommand.arenaId());
	}

	public Future<Void> run(ArenaId arenaId) {
		final Supplier<Void> arenaRunner = () -> {
			Arena arena = arenaRepository.findById(arenaId).orElseThrow();
			while (!arena.getStatus().equals(ArenaStatus.FINISHED)) {
				final Round round = arena.roundTick();
				arenaEventPublisher.publish(ArenaEvent.RoundCompletedEvent.from(arenaId, round));
			}
			arenaRepository.save(arena);
			arenaEventPublisher.publish(ArenaEvent.ArenaCompletedEvent.from(arena));
			return null;
		};
		return CompletableFuture.supplyAsync(arenaRunner);
	}


}
