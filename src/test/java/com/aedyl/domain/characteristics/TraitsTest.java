package com.aedyl.domain.characteristics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TraitsTest {

	@Test
	void add_incompatible_trait_fails() {
		assertThrows(IllegalArgumentException.class, () -> {
			final Traits traits = Traits.of(Trait.MERCIFUL);
			traits.add(Trait.CRUEL);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			Traits.of(Trait.FORGIVING, Trait.VENGEFUL);
		});
	}
}
