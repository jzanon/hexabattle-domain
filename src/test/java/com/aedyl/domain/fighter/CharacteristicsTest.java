package com.aedyl.domain.fighter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacteristicsTest {

	@Test
	void decreaseLife() {
		Characteristics characteristics = new Characteristics(5, 7, 7, 9);
		Characteristics updateCharacteristics = characteristics.decreaseLife(3);
		assertEquals(4, updateCharacteristics.life());
	}
}
