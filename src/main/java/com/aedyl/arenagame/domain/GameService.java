package com.aedyl.arenagame.domain;

import com.aedyl.arenagame.domain.arena.*;
import com.aedyl.arenagame.domain.combat.Round;
import com.aedyl.arenagame.domain.fighter.Human;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.aedyl.arenagame.domain.arena.ArenaEvent.ArenaCreatedEvent;

public class GameService {

	private final ArenaEventPublisher arenaEventPublisher;
	private final Supplier<Human> humanSupplier;
	private final ArenaRepository arenaRepository;

	public GameService(ArenaEventPublisher arenaEventPublisher,
	                   Supplier<Human> fighterSupplier,
	                   ArenaRepository arenaRepository) {
		this.arenaEventPublisher = arenaEventPublisher;
		this.humanSupplier = fighterSupplier;
		this.arenaRepository = arenaRepository;
	}

	public Arena createArena(int arenaSize, int nbRoundMax) {
		Arena arena = new Arena(arenaSize, nbRoundMax);
		arenaRepository.save(arena);
		arenaEventPublisher.publish(ArenaCreatedEvent.from(arena));
		return arena;
	}

	public List<Human> fillArenaWithRandomFighters(UUID arenaId) {
		Arena arena = arenaRepository.findById(arenaId).orElseThrow();

		final List<Human> fighters = IntStream.range(0, arena.maxSize())
				.mapToObj(value -> humanSupplier.get())
				.collect(Collectors.toUnmodifiableList());

		fighters.forEach(fighter -> {
			arena.addFighter(fighter);
			arenaRepository.save(arena);
			arenaEventPublisher.publish(ArenaEvent.HumanJoinedArenaEvent.from(arena.id(), fighter));
		});
		return fighters;
	}

	public Future<Void> run(UUID arenaId) {
		final Supplier<Void> arenaRunner = () -> {
			Arena arena = arenaRepository.findById(arenaId).orElseThrow();
			while (!arena.getStatus().equals(ArenaStatus.FINISHED)) {
				final Round round = arena.roundTick();
				arenaEventPublisher.publish(ArenaEvent.RoundCompletedEvent.from(round));
			}
			arenaRepository.save(arena);
			arenaEventPublisher.publish(ArenaEvent.ArenaCompletedEvent.from(arena));
			return null;
		};
		return CompletableFuture.supplyAsync(arenaRunner);
	}

}
