package com.aedyl.domain.combat;

import java.util.List;
import java.util.stream.Collectors;

public class Round {

	private final int number;
	private final List<CombatStatistics> stats;

	public Round(int number, List<CombatStatistics> stats) {
		this.number = number;
		this.stats = stats;
	}

	public String buildSummary() {
		String prefix = "[Round: " + number + "] ";
		return prefix + "starting:\n" + stats.stream()
				.map(CombatStatistics::buildSummary)
				.map(summary -> prefix + summary)
				.collect(Collectors.joining("\n"));
	}
}
