package com.aedyl.arenagame.domain.fighter;

import com.aedyl.arenagame.domain.fighter.model.Human;
import com.aedyl.arenagame.domain.fighter.model.HumanId;
import com.aedyl.arenagame.domain.fighter.model.HumanSupplier;
import com.aedyl.arenagame.domain.fighter.port.input.FghterCommand;
import com.aedyl.arenagame.domain.fighter.port.input.FighterCommandHandler;
import com.aedyl.arenagame.domain.fighter.port.output.HumanRepository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FighterService implements FighterCommandHandler {

	private final HumanSupplier humanSupplier;
	private final HumanRepository repository;

	public FighterService(HumanRepository repository) {
		this.repository = repository;
		this.humanSupplier = new HumanSupplier();
	}

	Set<HumanId> createRandomFighters(int nbOfFighter) {
		return IntStream.range(0, nbOfFighter)
				.mapToObj(value -> humanSupplier.get())
				.map(this::save)
				.collect(Collectors.toUnmodifiableSet());
	}

	private HumanId save(Human human) {
		repository.save(human);
		return human.uniqueId;
	}

	@Override
	public Set<HumanId> handle(FghterCommand.CreateRandomHumansCommand createRandomHumansCommand) {
		return createRandomFighters(createRandomHumansCommand.nbHumans());
	}
}
