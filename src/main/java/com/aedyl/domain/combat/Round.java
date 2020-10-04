package com.aedyl.domain.combat;

import java.util.List;

public record Round(int number,
                    List<AttackResult> stats) {


}
