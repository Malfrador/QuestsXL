package de.erethon.questsxl.objectives;

import de.erethon.questsxl.players.QPlayer;
import de.erethon.questsxl.quest.ActiveQuest;
import de.erethon.questsxl.quest.QQuest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ObjectiveManager implements Listener {


    public void complete(QPlayer player, QObjective obj) {
        for (ActiveObjective objective : player.getCurrentObjectives()) {
            if (objective.getObjective().equals(obj)) {
                objective.setCompleted(true);
                objective.getStage().checkCompleted(player);
                return;
            }
        }
    }

    @EventHandler
    public void objectiveEvent(ObjectiveCompleteEvent event) {
        complete(event.getPlayer(), event.getObjective());
    }
}
