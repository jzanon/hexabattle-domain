package com.aedyl.hexabattle.api

import com.aedyl.arenagame.domain.arena.model.ArenaId
import com.aedyl.arenagame.domain.arena.port.input.ArenaCommand.*
import com.aedyl.arenagame.domain.fighter.model.HumanId
import com.aedyl.arenagame.domain.fighter.port.input.FghterCommand
import com.aedyl.hexabattle.api.config.ServiceConfig
import com.aedyl.hexabattle.api.dto.toArena
import com.aedyl.hexabattle.api.dto.toFighterFromArena
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import java.util.*

fun main(args: Array<String>) {


    val numberOfFighter = 1000
    val nbRoundMax = 25

    val config = ServiceConfig()

    val arenaService = config.arenaService
    val fighterService = config.fighterService


    val app = Javalin.create().apply {
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start(7000)

    app.routes {

        // this redirect shouldn't be here, but readers have complained about the 404 for the root path
        get("/") { it.redirect("/newArena") }


        get("/newArena") { ctx ->
            val arena = arenaService.handle(CreateArenaCommand.create(numberOfFighter, nbRoundMax))
            ctx.json(arena.toArena())
            ctx.redirect("/fillArena/" + arena.id().id().toString())
        }

        get("/fillArena/:arena-id") { ctx ->
            val arenaId = ArenaId.from(ctx.pathParam("arena-id"))
            val arenaOpt = config.arenaRepository.findById(arenaId)

            arenaOpt.ifPresentOrElse({ arena ->
                fighterService.handle(FghterCommand.CreateRandomHumansCommand.create(arena.maxSize()))
                    .stream()
                    .map { fighterId: HumanId? -> AddFighterCommand.create(arena.id(), fighterId) }
                    .forEach { addFighterCommand: AddFighterCommand? -> arenaService.handle(addFighterCommand) }
                ctx.json(arena.toArena())
            }, {
                ctx.status(404)
            })
        }

        get("/run/:arena-id") { ctx ->
            val arenaId = ArenaId.from(ctx.pathParam("arena-id"))
            val arenaOpt = config.arenaRepository.findById(arenaId)
            arenaOpt.ifPresentOrElse({ arena ->
                arenaService.handle(RunArenaCommand.create(arena.id()))
            }, {
                ctx.status(404)
            })
        }


        get("/arena/:arena-id/survirors") { ctx ->
            val arenaId = ArenaId.from(ctx.pathParam("arena-id"))
            val arenaOpt = config.arenaRepository.findById(arenaId)
            arenaOpt.ifPresentOrElse({ arena ->
                ctx.json(arena.survivors.toFighterFromArena())
            }, {
                ctx.status(404)
            })
        }

        get("/fighters/:fighter-id") { ctx ->
            val fighterId = HumanId(UUID.fromString(ctx.pathParam("fighter-id")))
            val fighter = config.fighterRepository.findById(fighterId)
            if (fighter != null) {
                ctx.json(fighter)
            } else {
                ctx.status(404)
            }

        }


    }

}

