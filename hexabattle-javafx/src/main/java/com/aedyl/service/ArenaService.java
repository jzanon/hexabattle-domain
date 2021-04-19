package com.aedyl.service;

import com.aedyl.domain.Arena;
import com.aedyl.domain.Fighter;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface ArenaService {
	@RequestLine("GET /newArena")
	Arena createNewArena();

	@RequestLine("GET /run/{arena-id}")
	void run(@Param("arena-id") String arenaId);


	@RequestLine("GET /arena/{arena-id}/survirors")
	List<Fighter> getSurvivors(@Param("arena-id") String arenaId);
}
