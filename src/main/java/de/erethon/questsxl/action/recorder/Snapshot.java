package de.erethon.questsxl.action.recorder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Snapshot {

    private final Map<Location, Material> blockSnapshot = new HashMap<>();
    private final Map<Location, EntityType> entitySnapshot = new HashMap<>();

    public void restore(Player player) {
        for (Map.Entry<Location, Material> entry : blockSnapshot.entrySet()) {
            player.sendBlockChange(entry.getKey(), entry.getValue().createBlockData());
        }
        for (Map.Entry<Location, EntityType> entry : entitySnapshot.entrySet()) {
        }
    }

    public void add(Block block) {
        blockSnapshot.put(block.getLocation(), block.getType());
    }

    public void add(Entity entity) {
        entitySnapshot.put(entity.getLocation(), entity.getType());
    }

    public Map<Location, Material> getBlockSnapshot() {
        return blockSnapshot;
    }

    public Map<Location, EntityType> getEntitySnapshot() {
        return entitySnapshot;
    }
}
