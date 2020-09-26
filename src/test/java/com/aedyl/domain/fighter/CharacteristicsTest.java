package com.aedyl.domain.fighter;

import com.aedyl.domain.characteristics.Characteristics;
import com.aedyl.domain.characteristics.Trait;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacteristicsTest {

	@Test
	void decreaseLife() {
		Characteristics characteristics = new Characteristics(5, 7, 7, 9, Set.of(Trait.CRUEL));
		Characteristics updateCharacteristics = characteristics.decreaseLife(3);
		assertEquals(4, updateCharacteristics.life());
	}
}
