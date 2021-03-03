package de.erethon.questsxl;

import com.google.gson.Gson;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.compatibility.Internals;
import de.erethon.commons.javaplugin.DREPlugin;
import de.erethon.commons.javaplugin.DREPluginSettings;
import de.erethon.factionsxl.FactionsXL;
import de.erethon.questsxl.commands.QCommandCache;
import de.erethon.questsxl.npc.EntityRegistry;
import de.erethon.questsxl.npc.NPCManager;
import de.erethon.questsxl.players.QPlayer;
import de.erethon.questsxl.players.QPlayerCache;
import de.erethon.questsxl.quest.QQuest;
import de.erethon.questsxl.quest.QuestManager;
import de.erethon.questsxl.tools.ProtocolTools;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

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
        commandCache = new QCommandCache(this);
        setCommandCache(commandCache);
        commandCache.register(this);
        entityRegistry = new EntityRegistry();
        npcManager = new NPCManager();
        getServer().getPluginManager().registerEvents(this, this);
        if (getServer().getPluginManager().isPluginEnabled("FactionsXL")) {
            fxl = (FactionsXL) getServer().getPluginManager().getPlugin("FactionsXL");
        }
        File file = new File(getDataFolder(), "quests/test.yml");
        questManager = new QuestManager();
        questManager.load();

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
