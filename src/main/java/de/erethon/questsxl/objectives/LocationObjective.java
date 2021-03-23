package de.erethon.questsxl.objectives;

import de.erethon.questsxl.players.QPlayer;
import de.erethon.questsxl.quest.QuestMarkerType;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocationObjective extends LocationBasedObjective implements QObjective {

    QPlayer player;

    public LocationObjective(Location location) {
        this.location = location;
    }

    public LocationObjective(Location location, int distance) {
        this.location = location;
        this.distance = distance;
    }

    @Override
    public void setup(QPlayer p) {
        player = p;
        registerEvents(this);
    }

    public Location getLocation() {
        return location;
    }

    public int getDistance() {
        return distance;
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
            unregister(this);
        }

    }
}
