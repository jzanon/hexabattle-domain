package com.aedyl.domain;

import java.util.ArrayList;
import java.util.List;

public class Arena {
	private String id = "";
	private List<com.aedyl.domain.Fighter> survivors = new ArrayList<>();
	private ArenaStatus status = ArenaStatus.UNKNOWN;

	public String getId() {
		return id;
	}

	public Arena setId(String id) {
		this.id = id;
		return this;
	}

	public List<com.aedyl.domain.Fighter> getSurvivors() {
		return survivors;
	}

	public Arena setSurvivors(List<com.aedyl.domain.Fighter> survivors) {
		this.survivors = survivors;
		return this;
	}

	public ArenaStatus getStatus() {
		return status;
	}

	public Arena setStatus(ArenaStatus status) {
		this.status = status;
		return this;
	}
}
