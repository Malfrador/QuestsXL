package de.erethon.questsxl.objectives;

import de.erethon.questsxl.players.QPlayer;
import org.bukkit.event.Listener;

public interface QObjective extends Listener {

    void setup(QPlayer player);

    String getDisplayText();

}
