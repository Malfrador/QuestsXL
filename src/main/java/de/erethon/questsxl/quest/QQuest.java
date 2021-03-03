package de.erethon.questsxl.quest;

import com.google.gson.Gson;
import de.erethon.commons.config.DREConfig;
import de.erethon.questsxl.players.QPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class QQuest extends DREConfig {


    int id;
    private final Set<QStage> stages = new HashSet<>();
    private final Set<QQuest> requiredQuests = new HashSet<>();
    private final Set<QQuest> forbiddenQuests = new HashSet<>();
    private final Set<Permission> requiredPermissions = new HashSet<>();
    //Map<Job, JobLevel> requiredJobLevels = new HashMap<>();

    String name;

    public QQuest(File file) {
        super(file, 1);
        if (initialize) {
            initialize();
        }
        load();
    }

    public void reward(QPlayer player) {

    }

    public boolean canStartQuest(QPlayer qPlayer) {
        return true;
    }

    public Set<QStage> getStages() {
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

    public String getName() {
        return name;
    }

    public void setName(String nn) {
        name = nn;
    }

    @Override
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        id = Integer.parseInt(file.getName().replace(".yml", ""));
    }
}
