package com.aedyl.domain.fighter;

import com.aedyl.domain.characteristics.Characteristics;
import com.aedyl.domain.characteristics.Trait;
import com.aedyl.domain.characteristics.Traits;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacteristicsTest {

	@Test
	void decreaseLife() {
		Characteristics characteristics = new Characteristics(5, 7, 7, 9, Traits.of(Trait.MERCIFUL));
		Characteristics updateCharacteristics = characteristics.decreaseLife(3);
		assertEquals(4, updateCharacteristics.life());
	}
}
