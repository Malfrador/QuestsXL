package de.erethon.questsxl.quest;

import de.erethon.questsxl.QuestsXL;
import de.erethon.questsxl.players.QPlayer;

public class ActiveQuest {

    QuestsXL plugin = QuestsXL.getInstance();

    QPlayer player;
    QQuest quest;
    QStage currentStage;

    public ActiveQuest(QPlayer player, QQuest quest) {
        this.player = player;
        this.quest = quest;
    }

    public void progress(QPlayer player) {
        QStage next = null;
        int currentID = currentStage.getId();
        for (QStage stage : quest.getStages()) {
            if (stage.getId() == currentID + 1) {
                next = stage;
            }
        }
        if (next == null) {
            finish(player);
            return;
        }
        currentStage = next;
        plugin.debug(player.getPlayer().getName() + " progressed to stage " + currentStage.toString() + " of " + quest.getName() + ".");
    }

    public void finish(QPlayer player) {
        quest.reward(player);
        player.getActiveQuests().remove(this);
        player.getCompletedQuests().put(this.getQuest(), System.currentTimeMillis());
    }

    public QStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(QStage currentStage) {
        this.currentStage = currentStage;
    }

    public QPlayer getPlayer() {
        return player;
    }

    public QQuest getQuest() {
        return quest;
    }
}
