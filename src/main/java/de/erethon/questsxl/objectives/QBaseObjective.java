package de.erethon.questsxl.objectives;

import de.erethon.questsxl.QuestsXL;
import org.bukkit.event.Listener;

public class QBaseObjective implements Listener {

    QuestsXL plugin = QuestsXL.getInstance();

    public void registerEvents(QObjective obj) {
        plugin.getServer().getPluginManager().registerEvents(obj, plugin);

    }


}
