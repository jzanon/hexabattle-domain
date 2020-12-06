package com.aedyl.arenagame.domain.fighter;

import com.aedyl.arenagame.domain.HumanFactoryForTests;
import com.aedyl.arenagame.domain.characteristics.CharacteristicsSupplier;
import com.aedyl.arenagame.domain.fighter.model.Human;
import com.aedyl.arenagame.domain.fighter.model.HumanComparator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HumanComparatorTest {


	private final HumanFactoryForTests factory = new HumanFactoryForTests();


	private static Stream<Arguments> provideInitiativesForComparison() {
		return Stream.of(
				Arguments.of(0, 0),
				Arguments.of(0, 1),
				Arguments.of(1, 0),
				Arguments.of(12, 21),
				Arguments.of(Integer.MAX_VALUE, 10),
				Arguments.of(Integer.MIN_VALUE, 10)
		);
	}

	@ParameterizedTest(name = "#{index} - Test initiative comparison with Argument={0},{1}")
	@MethodSource("provideInitiativesForComparison")
	void compareByInitiative(int firstInit, int secondInit) {
		final Human human1 = factory.createHuman((CharacteristicsSupplier.InitiativeSupplier) () -> firstInit);
		final Human human2 = factory.createHuman((CharacteristicsSupplier.InitiativeSupplier) () -> secondInit);

		final int comparisonResult = HumanComparator.compareByInitiative(human1, human2);

		assertEquals(Integer.compare(secondInit, firstInit), comparisonResult);
	}

}
