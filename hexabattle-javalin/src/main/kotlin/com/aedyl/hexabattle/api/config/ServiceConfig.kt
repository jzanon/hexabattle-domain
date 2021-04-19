package com.aedyl.hexabattle.api.config

import com.aedyl.arenagame.domain.arena.ArenaService
import com.aedyl.arenagame.domain.arena.port.output.ArenaEventPublisher
import com.aedyl.arenagame.domain.arena.port.output.ArenaRepository
import com.aedyl.arenagame.domain.fighter.FighterService
import com.aedyl.arenagame.domain.fighter.port.output.HumanRepository
import com.aedyl.arenagame.domain.statistics.StatisticsService
import com.aedyl.arenagame.domain.statistics.port.input.StatisticsAdapter
import com.aedyl.arenagame.domain.statistics.port.output.StatisticsPublisher
import com.aedyl.hexabattle.api.repository.InMemoryRepository

class ServiceConfig {
    private val repository: InMemoryRepository = InMemoryRepository()


    private val statisticsRepository: InMemoryRepository
        get() = repository


    val fighterRepository: HumanRepository
        get() = repository
    val arenaRepository: ArenaRepository
        get() = repository
    val fighterService: FighterService
        get() = FighterService(fighterRepository)
    val arenaService: ArenaService
        get() = ArenaService(arenaEventPublisher, arenaRepository, fighterRepository)
    private val arenaEventPublisher: ArenaEventPublisher
        get() = ArenaEventPublisher { arenaEvent ->
            statisticsAdapter.publish(arenaEvent)
        }
    private val statisticsAdapter: StatisticsAdapter
        get() = StatisticsAdapter(statisticsService)
    val statisticsService: StatisticsService
        get() {
            val statisticsPublisher = StatisticsPublisher {
            }
            return StatisticsService(statisticsPublisher, statisticsRepository)
        }
}
