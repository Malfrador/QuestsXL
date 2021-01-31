package de.erethon.questsxl.action.recorder;

import de.erethon.questsxl.QuestsXL;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Recording {

    QuestsXL plugin = QuestsXL.getInstance();

    Player player;
    private final LinkedHashMap<Snapshot, Integer> playlist = new LinkedHashMap<>();
    BukkitRunnable playTask;
    int lastSnapshot;


    public void add(Snapshot snapshot, int delay) {
        playlist.put(snapshot, delay);
        lastSnapshot = plugin.getServer().getCurrentTick();
    }

    public void play(Player player) {
        this.player = player;
        int progress = 0;
        for (Map.Entry<Snapshot, Integer> entry : playlist.entrySet()) {
            progress = progress + entry.getValue();
            delayPlayTask(player, entry.getKey(), progress);
        }
    }

    public void delayPlayTask(Player player,  Snapshot snapshot, int delay) {
        BukkitRunnable asyncDelay = new BukkitRunnable() {
            @Override
            public void run() {
                playSnapshot(player, snapshot);
            }
        };
        asyncDelay.runTaskLaterAsynchronously(plugin, delay);
    }

    public void playSnapshot(Player player, Snapshot snapshot) {
        BukkitRunnable mainThread = new BukkitRunnable() {
            @Override
            public void run() {
                snapshot.restore(player);
            }
        };
        mainThread.run();
    }

    public int getDelay() {
        return plugin.getServer().getCurrentTick() - lastSnapshot;
    }

    public void setLastSnapshot(int lastSnapshot) {
        this.lastSnapshot = lastSnapshot;
    }

    public void stop() {
        playTask.cancel();
    }

}
