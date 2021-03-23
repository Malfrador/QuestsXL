package de.erethon.questsxl.objectives;

import de.erethon.questsxl.players.QPlayer;
import de.erethon.questsxl.quest.QuestMarkerType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocationBasedObjective extends QBaseObjective implements QObjective {

    char LEFT = '←';
    char FORWARD_RIGHT = '⬈';
    char RIGHT = '→';
    char FORWARD_LEFT = '⬉';
    char BACKWARD = '↓';
    char BACKWARD_RIGHT = '⬊';
    char FORWARD = '↑';
    char BACKWARD_LEFT = '⬋';
    char UNKNOWN = '-';
    char UP = '▲';
    char DOWN = '▼';
    char SAME = '◆';

    Location location;
    int distance = 2;
    double lineProgress = 2;
    QuestMarkerType markerType;

    @Override
    public void setup(QPlayer player) {
    }

    public void setMarkerType(QuestMarkerType type) {
        this.markerType = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getDistance() {
        return distance;
    }

    public char getDirectionalMarker(Player player) {
        Location fromLoc = player.getLocation().clone();
        Location toLoc = location.clone();
        if (location.getWorld() != fromLoc.getWorld()) {
            return UNKNOWN;
        }
        Vector toVector = toLoc.clone().subtract(fromLoc).toVector().normalize();
        Vector fromVector = fromLoc.getDirection();
        double x1 = toVector.getX();
        double z1 = toVector.getZ();
        double x2 = fromVector.getX();
        double z2 = fromVector.getZ();
        double angle = Math.atan2(x1*z2-z1*x2, x1*x2+z1*z2)* 180 / Math.PI;

        if (angle >= -22.5 && angle <= 22.5) {
            return FORWARD;
        }
        if (angle >= 22.5 && angle <= 67.5) {
            // Looking forward right, target is more on the left
            return FORWARD_LEFT;
        }
        if (angle <= -22.5 && angle >= -67.5) {
            // Looking forward left
            return FORWARD_RIGHT;
        }
        if (angle >= 67.5 && angle <= 112.5) {
            // Looking right
            return LEFT;
        }
        if (angle <= -67.5 && angle >= -112.5) {
            // Looking left
            return RIGHT;
        }
        if (angle <= -112.5 && angle >= -157.5) {
            return BACKWARD_RIGHT;
        }
        if (angle >= 112.5 && angle <= 157.5) {
            return BACKWARD_LEFT;
        }
        if (angle >= 157.5 || angle <= -157.5) {
            return BACKWARD;
        }
        return UNKNOWN;
    }

    public char getVerticalMarker(Player player) {
        Location fromLoc = player.getLocation().clone();
        Location toLoc = location.clone();
        int fromY = fromLoc.getBlockY();
        int toY = toLoc.getBlockY();
        if (fromY < toY) {
            return UP;
        }
        // toY + 1 because of jumping
        if (fromY > toY + 1) {
            return DOWN;
        }
        return SAME;
    }

    public void showDirectLine(Player player) {
        Location fromLoc = player.getLocation().clone();
        Location toLoc = location.clone();
        if (location.getWorld() != fromLoc.getWorld()) {
            return;
        }
        Vector toVector = toLoc.clone().subtract(fromLoc).toVector().normalize();

        // Reset faster and further in front when sprinting to keep animation in FOV
        if (player.isSprinting() && lineProgress >= 10) {
            lineProgress = 6;
        }
        if (!player.isSprinting() && lineProgress >= 10) {
            lineProgress = 2;
        }
        if (lineProgress > fromLoc.distance(toLoc)) {
            lineProgress = fromLoc.distance(toLoc);
        }Vector partVector = toVector.clone().multiply(lineProgress);
        Location partLoc = fromLoc.clone().add(partVector);
        partLoc = partLoc.toHighestLocation();
        partLoc = partLoc.add(0, 1.05,0);
        player.spawnParticle(Particle.VILLAGER_HAPPY, partLoc, 1);
        lineProgress = lineProgress + 2;
    }


    @Override
    public String getDisplayText() {
        return null;
    }
}
