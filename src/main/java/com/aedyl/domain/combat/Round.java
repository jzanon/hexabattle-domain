package com.aedyl.domain.combat;

import java.util.List;
import java.util.stream.Collectors;

public class Round {

	private final int number;
	private final List<AttackResult> stats;

	public Round(int number, List<AttackResult> stats) {
		this.number = number;
		this.stats = stats;
	}

	public String buildSummary() {
		String prefix = "[Round: " + number + "] ";
		return prefix + "starting:\n" + stats.stream()
				.map(AttackResult::buildSummary)
				.map(summary -> prefix + summary)
				.collect(Collectors.joining("\n"));
	}
}
