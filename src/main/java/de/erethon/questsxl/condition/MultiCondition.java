package de.erethon.questsxl.condition;

import de.erethon.questsxl.players.QPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Condition is fulfilled if all conditions are fulfilled.
 */
public class MultiCondition implements QCondition {

    List<QCondition> conditions = new ArrayList<>();

    @Override
    public boolean check(QPlayer player) {
        boolean check = true;
        for (QCondition condition : conditions) {
            if (!condition.check(player)) {
                check = false;
            }
        }
        return check;
    }
}
