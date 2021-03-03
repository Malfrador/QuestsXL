package de.erethon.questsxl.players;

import de.erethon.questsxl.QuestsXL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class QPlayerCache {


    QuestsXL plugin = QuestsXL.getInstance();


    private Set<QPlayer> players = new HashSet<>();

    public QPlayerCache() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(new QPlayer(player));
        }
    }

    public Set<QPlayer> getPlayers() {
        return players;
    }

    public QPlayer get(Player player) {
        for (QPlayer player1 : players) {
            if (player1.getPlayer() == player) {
                return player1;
            }
        }
        return null;
    }

    public static File getFile(UUID uuid) {
        return new File(QuestsXL.PLAYERS, uuid.toString() + ".yml");
    }
}
