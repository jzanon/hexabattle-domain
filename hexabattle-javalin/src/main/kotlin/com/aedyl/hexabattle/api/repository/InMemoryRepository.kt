package com.aedyl.hexabattle.api.repository

import com.aedyl.arenagame.domain.arena.model.Arena
import com.aedyl.arenagame.domain.arena.model.ArenaId
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository
import com.aedyl.arenagame.domain.fighter.model.Human
import com.aedyl.arenagame.domain.fighter.model.HumanId
import com.aedyl.arenagame.domain.fighter.port.output.HumanRepository
import com.aedyl.arenagame.domain.statistics.model.ArenaStatistics
import com.aedyl.arenagame.domain.statistics.model.FighterStatistics
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsRepository
import java.util.*

class InMemoryRepository : ArenaRepository, StatisticsRepository, HumanRepository {
    private val arenaMap: MutableMap<ArenaId, Arena> = HashMap()
    private val humanMap: MutableMap<HumanId, Human> = HashMap()
    private val arenaStatsMap: MutableMap<ArenaId, ArenaStatistics> = HashMap()
    override fun save(arena: Arena) {
        arenaMap[arena.id()] = arena
    }

    override fun findById(arenaId: ArenaId): Optional<Arena> {
        return Optional.ofNullable(arenaMap[arenaId])
    }

    override fun find(arenaId: ArenaId): Optional<ArenaStatistics> {
        return Optional.ofNullable(arenaStatsMap[arenaId])
    }

    override fun save(arenaStatistics: ArenaStatistics) {
        arenaStatsMap[arenaStatistics.arenaId] = arenaStatistics
    }

    override fun save(fighterStatistics: FighterStatistics) {
        find(fighterStatistics.arenaId).ifPresent { arenaStatistics: ArenaStatistics ->
            val arenaId = arenaStatistics.arenaId
            val statistics = arenaStatistics.statistics
            arenaStatsMap[arenaId] = ArenaStatistics(arenaId, statistics)
        }
    }

    override fun save(human: Human) {
        humanMap[human.uniqueId] = human
    }

    override fun findById(humanId: HumanId): Optional<Human> {
        return Optional.ofNullable(humanMap[humanId])
    }
}
