package com.aedyl.domain.characteristics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Traits {

	private final Set<Trait> traits = new HashSet<>();

	public static Traits of(Trait... traitList) {
		Traits traits = new Traits();
		if (traitList != null) {
			for (Trait trait : traitList) {
				traits = traits.add(trait);
			}
		}
		return traits;
	}

	public Traits add(Trait trait) {
		final Set<Trait> incompatibleTraits = trait.getIncompatibleTraits();
		if (!Collections.disjoint(traits, incompatibleTraits)) {
			throw new IllegalArgumentException(trait.name() + " is not compatible with current traits: " + this.toString());
		}
		traits.add(trait);
		return this;
	}

	public boolean contains(Trait trait) {
		return traits.contains(trait);
	}


	@Override
	public String toString() {
		return traits.stream()
				.map(Trait::name)
				.collect(Collectors.joining(",", "[", "]"));
	}
}
