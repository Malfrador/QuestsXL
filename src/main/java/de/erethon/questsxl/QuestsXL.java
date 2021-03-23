package de.erethon.questsxl;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.compatibility.Internals;
import de.erethon.commons.javaplugin.DREPlugin;
import de.erethon.commons.javaplugin.DREPluginSettings;
import de.erethon.factionsxl.FactionsXL;
import de.erethon.questsxl.commands.QCommandCache;
import de.erethon.questsxl.npc.EntityRegistry;
import de.erethon.questsxl.npc.NPCManager;
import de.erethon.questsxl.objectives.LocationBasedObjective;
import de.erethon.questsxl.players.QPlayerCache;
import de.erethon.questsxl.quest.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public final class QuestsXL extends DREPlugin implements Listener {

    static QuestsXL instance;

    public static File QUESTS;
    public static File PLAYERS;

    QCommandCache commandCache;
    QPlayerCache qPlayerCache;
    EntityRegistry entityRegistry;
    NPCManager npcManager;
    QuestManager questManager;
    FactionsXL fxl;

    public QuestsXL() {
        settings = DREPluginSettings.builder()
                .paper(true)
                .economy(true)
                .internals(Internals.v1_16_R3)
                .build();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (!compat.isPaper()) {
            MessageUtil.log("Please use Paper. https://papermc.io/");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        instance = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        QUESTS = new File(getDataFolder(), "quests");
        if (!QUESTS.exists()) {
            QUESTS.mkdir();
        }
        PLAYERS = new File(getDataFolder(), "players");
        if (!PLAYERS.exists()) {
            PLAYERS.mkdir();
        }
        qPlayerCache = new QPlayerCache();
        commandCache = new QCommandCache(this);
        setCommandCache(commandCache);
        commandCache.register(this);
        entityRegistry = new EntityRegistry();
        npcManager = new NPCManager();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(qPlayerCache, this);
        if (getServer().getPluginManager().isPluginEnabled("FactionsXL")) {
            fxl = (FactionsXL) getServer().getPluginManager().getPlugin("FactionsXL");
        }
        File file = new File(getDataFolder(), "quests/test.yml");
        questManager = new QuestManager();
        questManager.load();

        LocationBasedObjective objective = new LocationBasedObjective();
        objective.setLocation(new Location(Bukkit.getWorld("Saragandes"), 111, 4, 111));
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    BossBar bar = qPlayerCache.get(player).getBar();
                    bar.setTitle("§6§l" + objective.getDirectionalMarker(player) + " " + objective.getVerticalMarker(player));
                }
            }
        };
        runnable.runTaskTimer(this, 20, 5);

    }

    @Override
    public void onDisable() {
        questManager.save();
    }

    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public FactionsXL getFXL() {
        return fxl;
    }

    public static QuestsXL getInstance() {
        return instance;
    }

    public void debug(String msg) {
         // check for debug mode here
        MessageUtil.log(msg);
    }
}
