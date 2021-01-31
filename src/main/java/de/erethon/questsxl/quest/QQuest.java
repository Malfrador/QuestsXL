package de.erethon.questsxl.quest;

import de.erethon.questsxl.players.QPlayer;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QQuest {

    private final Map<Integer, QStage> stages = new HashMap<>();
    private final Set<QQuest> requiredQuests = new HashSet<>();
    private final Set<QQuest> forbiddenQuests = new HashSet<>();
    private final Set<Permission> requiredPermissions = new HashSet<>();
    //Map<Job, JobLevel> requiredJobLevels = new HashMap<>();

    public boolean canStartQuest(QPlayer qPlayer) {
        return true;
    }

    public Map<Integer, QStage> getStages() {
        return stages;
    }

    public Set<QQuest> getRequiredQuests() {
        return requiredQuests;
    }

    public Set<QQuest> getForbiddenQuests() {
        return forbiddenQuests;
    }

    public Set<Permission> getRequiredPermissions() {
        return requiredPermissions;
    }
}
