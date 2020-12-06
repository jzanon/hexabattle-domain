package com.aedyl.arenagame.domain.fighter.port.output;

import com.aedyl.arenagame.domain.fighter.model.Human;
import com.aedyl.arenagame.domain.fighter.model.HumanId;

import java.util.Optional;

public interface HumanRepository {
	void save(Human human);

	Optional<Human> findById(HumanId humanId);
}
