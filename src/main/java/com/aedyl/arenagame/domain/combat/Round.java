package com.aedyl.arenagame.domain.combat;

import java.util.List;

public record Round(int number,
                    List<AttackResult> stats) {


}
