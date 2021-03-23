package de.erethon.questsxl.players;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.questsxl.QuestsXL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class QPlayerCache implements Listener {

    QuestsXL plugin = QuestsXL.getInstance();

    private final Set<QPlayer> players = new HashSet<>();

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

    @EventHandler
    public void loginEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (get(player) != null) {
            MessageUtil.log("Player already loaded.");
            return;
        }
        players.add(new QPlayer(player));
        MessageUtil.log("Loaded data for " + player.getName());
    }

    @EventHandler
    public void logoffEvent(PlayerQuitEvent event) {
        players.remove(get(event.getPlayer()));
    }

    public static File getFile(UUID uuid) {
        return new File(QuestsXL.PLAYERS, uuid.toString() + ".yml");
    }
}
