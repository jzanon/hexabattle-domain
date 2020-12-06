package com.aedyl.hexabattle.api.dto

import com.aedyl.arenagame.domain.arena.ArenaStatus
import com.aedyl.arenagame.domain.characteristics.Characteristics
import com.aedyl.arenagame.domain.fighter.model.Human

data class FighterFromArena(val id: String, val name: String, val characteristics: Characteristics)
data class Arena(val id: String, val survivors: MutableList<FighterFromArena>, val status: ArenaStatus)


fun com.aedyl.arenagame.domain.arena.model.Arena.toArena(): Arena {
    return  Arena(id().toString(), survivors.toFighterFromArena(), status)
}

fun List<Human>.toFighterFromArena(): MutableList<FighterFromArena> {
    val list = mutableListOf<FighterFromArena>()
    for (human in this) {
        list.add(human.toFighterFromArena())
    }
    return list;

}

private fun Human.toFighterFromArena(): FighterFromArena {
    return FighterFromArena(uniqueId.toString(), name, characteristics)
}
