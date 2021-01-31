package de.erethon.questsxl.objectives;

import de.erethon.questsxl.players.QPlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocationObjective extends QBaseObjective implements QObjective {

    Location location;
    int distance;
    QPlayer player;

    public LocationObjective(Location location, int distance) {
        this.location = location;
        this.distance = distance;
    }

    @Override
    public void setup(QPlayer p) {
        player = p;
        registerEvents(this);
    }

    @Override
    public String getDisplayText() {
        return "Go to " + location.toString();
    }

    @EventHandler
    private void moveEvent(PlayerMoveEvent event) {
        if (event.getFrom().equals(event.getTo())) {
            return;
        }
        if (event.getTo().distance(location) < distance) {
            plugin.getServer().getPluginManager().callEvent(new ObjectiveCompleteEvent(this, player));
            plugin.debug("Obj " + getDisplayText() + " completed for " + player.getPlayer().getName());
        }

    }
}
