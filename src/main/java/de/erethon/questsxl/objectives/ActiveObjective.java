package de.erethon.questsxl.objectives;

import de.erethon.questsxl.players.QPlayer;
import de.erethon.questsxl.quest.QStage;

public class ActiveObjective {

    private final QPlayer player;
    private final QObjective objective;
    private final QStage stage;
    private int progress = 0;
    private int maxProgress = 1;
    private boolean completed = false;

    public ActiveObjective(QPlayer player, QStage stage,  QObjective objective) {
        this.player = player;
        this.objective = objective;
        this.stage = stage;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public QPlayer getPlayer() {
        return player;
    }

    public String getMessage() {
        return objective.getDisplayText();
    }

    public QObjective getObjective() {
        return objective;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public QStage getStage() {
        return stage;
    }
}
