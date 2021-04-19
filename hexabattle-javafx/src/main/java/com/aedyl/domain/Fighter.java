package com.aedyl.domain;

public class Fighter {
	private String id;
	private String name;
	private Characteristics characteristics;

	public String getId() {
		return id;
	}

	public Fighter setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Fighter setName(String name) {
		this.name = name;
		return this;
	}

	public Characteristics getCharacteristics() {
		return characteristics;
	}

	public Fighter setCharacteristics(Characteristics characteristics) {
		this.characteristics = characteristics;
		return this;
	}

	@Override
	public String toString() {
		return "Fighter{" +
				"name='" + name + '\'' +
				'}';
	}
}
