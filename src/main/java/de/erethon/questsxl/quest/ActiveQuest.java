package de.erethon.questsxl.quest;

import de.erethon.questsxl.players.QPlayer;

public class ActiveQuest {

    QPlayer player;
    QQuest quest;
    QStage currentStage;

    public ActiveQuest(QPlayer player, QQuest quest) {
        this.player = player;
        this.quest = quest;
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
