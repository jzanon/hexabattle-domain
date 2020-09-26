package com.aedyl.domain.characteristics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Trait {
	CRUEL,
	MERCIFUL,
	APATHETIC,
	PASSIONATE,
	VENGEFUL,
	FORGIVING;

	private static final Map<Trait, Set<Trait>> incompatibleTraits = new HashMap<>();

	public Set<Trait> getIncompatibleTraits() {
		return incompatibleTraits.computeIfAbsent(this, this::buildIncompatibleTraits);
	}

	private Set<Trait> buildIncompatibleTraits(Trait trait) {
		return Collections.unmodifiableSet(switch (trait) {
			case CRUEL -> Set.of(MERCIFUL);
			case MERCIFUL -> Set.of(CRUEL);
			case APATHETIC -> Set.of(PASSIONATE);
			case PASSIONATE -> Set.of(APATHETIC);
			case VENGEFUL -> Set.of(FORGIVING);
			case FORGIVING -> Set.of(VENGEFUL);
		});
	}
}
