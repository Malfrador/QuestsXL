package de.erethon.questsxl.action.recorder;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.questsxl.QuestsXL;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashSet;
import java.util.Set;

public class RecordingSession implements Listener {

    QuestsXL plugin = QuestsXL.getInstance();
    private final Player player;
    private Location pos1;
    private Location pos2;

    private boolean isRecording = false;
    private final Recording recording;

    Set<Block> state = new HashSet<>();

    public RecordingSession(Player player) {
        this.player = player;
        recording = new Recording();
    }

    public void start() {
        if (isRecording) {
            return;
        }
        isRecording = true;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        recording.setLastSnapshot(plugin.getServer().getCurrentTick());
        snap();
    }

    public void stop() {
        isRecording = false;
    }

    public void snap() {
        MessageUtil.log("Updating state....");
        state = getBlocks(pos1.getWorld());
        Snapshot snapshot = new Snapshot();
        for (Block block : state) {
            snapshot.add(block);
        }
        MessageUtil.log("Adding new Snapshot after " + recording.getDelay());
        recording.add(snapshot, recording.getDelay());
    }

    @EventHandler
    public void breakEvent(BlockBreakEvent event) {
        if (!isRecording) {
            return;
        }
        if (!isInArea(event.getBlock().getLocation())) {
            return;
        }
        snap();
    }

    @EventHandler
    public void destroyEvent(BlockDestroyEvent event) {
        if (!isRecording) {
            return;
        }
        if (!isInArea(event.getBlock().getLocation())) {
            return;
        }
        snap();
    }

    @EventHandler
    public void placeEvent(BlockPlaceEvent event) {
        if (!isRecording) {
            return;
        }
        if (!isInArea(event.getBlock().getLocation())) {
            return;
        }
        snap();
    }


    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Recording getRecording() {
        return recording;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Block> getBlocks(World world) {
        Set<Block> blockList = new HashSet<>();
        Set<Location> result = new HashSet<>();
        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());
        for (double x = minX; x <= maxX; x+=1) {
            for (double y = minY; y <= maxY; y+=1) {
                for (double z = minZ; z <= maxZ; z+=1) {
                    result.add(new Location(world, x, y, z));
                }
            }
        }
        for (Location location : result) {
            blockList.add(world.getBlockAt(location));
        }

        return blockList;
    }


    public boolean isInArea(Location location) {
        double xp = location.getX();
        double yp = location.getY();
        double zp = location.getZ();
        double x1 = pos1.getX();
        double y1 = pos1.getY();
        double z1 = pos1.getZ();
        double x2 = pos2.getX();
        double y2 = pos2.getY();
        double z2 = pos2.getZ();
        return new IntRange(x1, x2).containsDouble(xp) && new IntRange(y1, y2).containsDouble(yp) && new IntRange(z1, z2).containsDouble(zp);
    }
}
