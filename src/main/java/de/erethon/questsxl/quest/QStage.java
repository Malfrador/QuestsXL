package de.erethon.questsxl.quest;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.questsxl.condition.QCondition;
import de.erethon.questsxl.objectives.ActiveObjective;
import de.erethon.questsxl.objectives.QObjective;
import de.erethon.questsxl.players.QPlayer;

import java.util.HashSet;
import java.util.Set;

public class QStage {

    private final QQuest owner;
    private final Set<QObjective> goals = new HashSet<>();
    private Set<QCondition> conditions = new HashSet<>();
    private final int id;
    private String startMessage = "";
    private String completeMessage = "";

    public QStage(QQuest quest,  int id) {
        this.owner = quest;
        this.id = id;
    }

    // gets called whenever an objective is completed
    public void checkCompleted(QPlayer player) {
        if (isCompleted(player)) {

        }
    }

    // Checked before the stage gets started
    public boolean canStart(QPlayer player) {
        Set<QCondition> failed = new HashSet<>();
        boolean canStart = true;
        for (QCondition condition : conditions) {
            if (!condition.check()) {
                canStart = false;
                failed.add(condition);
            }
        }
        player.send(failed.toString());
        return canStart;
    }

    public boolean isCompleted(QPlayer player) {
        boolean done = true;
        for (ActiveObjective activeObjective : player.getCurrentObjectives()) {
            if (goals.contains(activeObjective.getObjective())) {
                if (!activeObjective.isCompleted()) {
                    done = false;
                }
            }
        }
        return done;
    }

    public void addGoal(QObjective goal) {
        goals.add(goal);
    }

    public void addCondition(QCondition condition) {
        conditions.add(condition);
    }

    public boolean hasObjective(QObjective obj) {
        return goals.contains(obj);
    }

    public int getId() {
        return id;
    }

    public String getStartMessage() {
        return startMessage;
    }

    public String getCompleteMessage() {
        return completeMessage;
    }

    public void setStartMessage(String startMessage) {
        this.startMessage = startMessage;
    }

    public void setCompleteMessage(String completeMessage) {
        this.completeMessage = completeMessage;
    }

    public QQuest getQuest() {
        return owner;
    }
}
