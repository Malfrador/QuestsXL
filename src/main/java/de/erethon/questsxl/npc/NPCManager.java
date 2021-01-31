package de.erethon.questsxl.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.questsxl.QuestsXL;
import de.erethon.questsxl.tools.packetwrapper.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.entity.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class NPCManager implements Listener {

    QuestsXL plugin = QuestsXL.getInstance();
    Gson gson = new Gson();

    ProtocolManager protocol = ProtocolLibrary.getProtocolManager();
    NamespacedKey key = new NamespacedKey(plugin, "npc");
    Set<UUID> uuids = new HashSet<>();

    public NPCManager() {
        MessageUtil.log("Adding packet listener...");
        protocol.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerSpawnEntityLiving wrapper = new WrapperPlayServerSpawnEntityLiving(event.getPacket());
                Player player = event.getPlayer();
                Entity entity = wrapper.getEntity(event);
                if (!entity.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    return;
                };
                String raw = entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                NPCData data = null;
                try {
                    data = gson.fromJson(raw, NPCData.class);
                } catch (Exception ignored) {
                }
                if (data == null) {
                    return;
                }
                MessageUtil.log("Data: " + data.getDisplayname());
                wrapper.setType(data.getDisplayEntity());
                entity.setCustomName(ChatColor.GREEN + data.getDisplayname());
                if (data.getDisplayEntity() == EntityType.PLAYER) {
                    spawnPlayerEntity(event.getPlayer(), wrapper.getUniqueId(), wrapper.getEntityID(), wrapper.getEntity(event).getLocation());
                    uuids.add(entity.getUniqueId());
                    destroyEntity(event.getPlayer(), entity.getUniqueId(), entity.getEntityId());
                    event.setCancelled(true);

                    WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
                    info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                    List<PlayerInfoData> dataList = new ArrayList<>(info.getData());
                    for (UUID uuid : uuids) {
                        if (player.getWorld().getEntity(uuid) == null) {
                            continue;
                        }
                        WrappedGameProfile profile = new WrappedGameProfile(uuid, data.getDisplayname());
                        // Set skin
                        PlayerInfoData playerInfoData = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(""));
                        dataList.add(playerInfoData);
                    }
                    info.setData(dataList);
                    info.sendPacket(player);
                    return;
                }
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.setAI(data.isHasAI());
            }
        });
        protocol.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                event.setPacket(sendInfo(event.getPlayer(), event.getPacket()).getHandle());
            }
        });
        protocol.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_SOUND) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerEntitySound packet = new WrapperPlayServerEntitySound(event.getPacket());
                if (uuids.contains(packet.getEntity(event).getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        });
    }


    public void createNPC(Player player, Location location, String name) {
        Entity entity = location.getWorld().spawnEntity(location, EntityType.PIG, CreatureSpawnEvent.SpawnReason.CUSTOM);
        entity.setCustomName(name);
        entity.setSilent(true);
        NPCData data = new NPCData();
        data.setDisplayname(name);
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, gson.toJson(data));
        uuids.add(entity.getUniqueId());
        destroyEntity(player, entity.getUniqueId(), entity.getEntityId());

        WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
        info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        List<PlayerInfoData> dataList = new ArrayList<>(info.getData());
        for (UUID uuid : uuids) {
            if (location.getWorld().getEntity(uuid) == null) {
                continue;
            }
            String n = location.getWorld().getEntity(uuid).getCustomName();
            PlayerInfoData playerInfoData = new PlayerInfoData(new WrappedGameProfile(uuid, n), 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(""));
            dataList.add(playerInfoData);
        }
        info.setData(dataList);
        info.sendPacket(player);
        spawnPlayerEntity(player, entity.getUniqueId(), entity.getEntityId(), location);

    }

    public void destroyEntity(Player player, UUID uuid, int entityID) {
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        int[] ids = {entityID, entityID};
        packet.setEntityIds(ids);
        packet.sendPacket(player);
    }

    public WrapperPlayServerPlayerInfo sendInfo(Player player, PacketContainer packetContainer) {
        WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(packetContainer);
        info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        List<PlayerInfoData> dataList = new ArrayList<>(info.getData());
        for (UUID uuid : uuids) {
            if (player.getWorld().getEntity(uuid) == null) {
                continue;
            }
            String n = player.getWorld().getEntity(uuid).getCustomName();
            PlayerInfoData playerInfoData = new PlayerInfoData(new WrappedGameProfile(uuid, n), 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(""));
            dataList.add(playerInfoData);
        }
        info.setData(dataList);
        return info;
    }

    public void spawnPlayerEntity(Player player, UUID uuid, int entityID, Location loc) {
        WrapperPlayServerNamedEntitySpawn npc = new WrapperPlayServerNamedEntitySpawn();
        npc.setPlayerUUID(uuid);
        npc.setX(loc.getX());
        npc.setY(loc.getY());
        npc.setZ(loc.getZ());
        npc.setEntityID(entityID);
        npc.sendPacket(player);
    }
}
