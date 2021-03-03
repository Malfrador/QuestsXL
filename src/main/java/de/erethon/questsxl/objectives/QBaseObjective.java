package de.erethon.questsxl.objectives;

import de.erethon.questsxl.QuestsXL;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class QBaseObjective implements Listener {

    QuestsXL plugin = QuestsXL.getInstance();

    public void registerEvents(QObjective obj) {
        plugin.getServer().getPluginManager().registerEvents(obj, plugin);
    }

    public void unregister(QObjective obj) {
        HandlerList.unregisterAll(obj);
    }


}
