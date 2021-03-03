package de.erethon.questsxl.quest;

import de.erethon.questsxl.QuestsXL;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class QuestManager {

    QuestsXL plugin = QuestsXL.getInstance();

    Set<QQuest> quests = new HashSet<>();

    public void load() {
        for (File file : plugin.QUESTS.listFiles()){
            quests.add(new QQuest(file));
        }
    }

    public void save() {
        for (QQuest quest : quests) {
            quest.save();
        }
    }

    public QQuest createNew() {
        File file = new File(plugin.QUESTS, getNewID() + ".yml");
        QQuest quest = new QQuest(file);
        quests.add(quest);
        return quest;
    }

    public int getNewID() {
        return plugin.QUESTS.listFiles().length + 1;
    }
}
