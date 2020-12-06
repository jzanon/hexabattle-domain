package com.aedyl.arenagame.domain.arena;

import com.aedyl.arenagame.domain.arena.model.Arena;
import com.aedyl.arenagame.domain.arena.model.ArenaId;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.AddFighterCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.CreateArenaCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.RunArenaCommand;
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommandHandler;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEvent;
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher;
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.model.Human;
import com.aedyl.arenagame.domain.fighter.port.output.HumanRepository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static com.aedyl.arenagame.domain.arena.port.output.ArenaEvent.ArenaCreatedEvent;

public class ArenaService implements ArenaCommandHandler {

	private final ArenaEventPublisher arenaEventPublisher;
	private final ArenaRepository arenaRepository;
	private final HumanRepository humanRepository;

	public ArenaService(ArenaEventPublisher arenaEventPublisher,
	                    ArenaRepository arenaRepository,
	                    HumanRepository humanRepository) {
		this.arenaEventPublisher = arenaEventPublisher;
		this.arenaRepository = arenaRepository;
		this.humanRepository = humanRepository;
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
		Arena arena = arenaRepository.findById(addFighterCommand.arenaId()).orElseThrow();
		Human fighter = humanRepository.findById(addFighterCommand.fighterId()).orElseThrow();
		addFighter(arena, fighter);
	}

	void addFighter(Arena arena, Human fighter) {
		if (arena == null || fighter == null) {
			throw new IllegalArgumentException("Arena and fighter cannot be null");
		}
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
