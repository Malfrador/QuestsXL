package de.erethon.questsxl.players;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.questsxl.objectives.ActiveObjective;
import de.erethon.questsxl.quest.ActiveQuest;
import de.erethon.questsxl.quest.QQuest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QPlayer {

    Player player;

    private final Map<ActiveQuest, Long> activeQuests = new HashMap<>();
    private final Map<QQuest, Long> startedQuests = new HashMap<>();
    private final Map<QQuest, Long> completedQuests = new HashMap<>();
    private final Set<ActiveObjective> currentObjectives = new HashSet<>();

    public void startQuest(QQuest quest) {
        activeQuests.put(new ActiveQuest(this, quest), System.currentTimeMillis());
        startedQuests.put(quest, System.currentTimeMillis());
    }

    public void completeQuest(ActiveQuest quest) {
        activeQuests.remove(quest);
        completedQuests.put(quest.getQuest(), System.currentTimeMillis());
    }

    public void addObjective(ActiveObjective objective) {
        currentObjectives.add(objective);
    }

    public void send(String msg) {
        MessageUtil.sendMessage(getPlayer(), msg);
    }

    public Map<ActiveQuest, Long> getActiveQuests() {
        return activeQuests;
    }

    public Map<QQuest, Long> getStartedQuests() {
        return startedQuests;
    }

    public Map<QQuest, Long> getCompletedQuests() {
        return completedQuests;
    }

    public Set<ActiveObjective> getCurrentObjectives() {
        return currentObjectives;
    }


    public Player getPlayer() {
        return player;
    }
}
