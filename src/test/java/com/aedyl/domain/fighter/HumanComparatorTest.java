package com.aedyl.domain.fighter;

import com.aedyl.domain.characteristics.CharacteristicsSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.Inet4Address;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HumanComparatorTest {


	CharacteristicsSupplier characteristicSupplier;
	HumanSupplier humanSupplier;

	@BeforeEach
	public void init() {
		characteristicSupplier = new CharacteristicsSupplier();
		humanSupplier = new HumanSupplier(characteristicSupplier);
	}

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

	@ParameterizedTest(name="#{index} - Test initiative comparison with Argument={0},{1}")
	@MethodSource("provideInitiativesForComparison")
	void compareByInitiative(int firstInit, int secondInit) {
		final Human human1 = getHuman(firstInit);
		final Human human2 = getHuman(secondInit);

		final int comparisonResult = HumanComparator.compareByInitiative(human1, human2);

		assertEquals(Integer.compare(secondInit, firstInit), comparisonResult);
	}

	private Human getHuman(final int init) {
		characteristicSupplier.setSupplier((CharacteristicsSupplier.InitiativeSupplier) () -> init);
		return humanSupplier.get();
	}
}
