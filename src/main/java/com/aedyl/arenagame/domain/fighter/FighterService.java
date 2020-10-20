package com.aedyl.arenagame.domain.fighter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FighterService {

	private final HumanSupplier humanSupplier;

	public FighterService() {
		this.humanSupplier = new HumanSupplier();
	}

	public List<Human> createRandomFighters(int nbOfFighter) {
		return IntStream.range(0, nbOfFighter)
				.mapToObj(value -> humanSupplier.get())
				.collect(Collectors.toUnmodifiableList());
	}
}
