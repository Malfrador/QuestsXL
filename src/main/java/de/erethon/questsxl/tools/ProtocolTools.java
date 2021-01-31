package de.erethon.questsxl.tools;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import de.erethon.questsxl.QuestsXL;
import de.erethon.questsxl.tools.packetwrapper.*;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ProtocolTools {

    public static void spawnFakeEntity(EntityType type, Location location, Player player) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        WrapperPlayServerSpawnEntityLiving entity = new WrapperPlayServerSpawnEntityLiving();
        entity.setEntityID((int)(Math.random() * Integer.MAX_VALUE));
        entity.setUniqueId(UUID.randomUUID());
        entity.setType(EntityType.VILLAGER);
        entity.setX(location.getX());
        entity.setY(location.add(0, 1,0).getY());
        entity.setZ(location.getZ());

        try {
            Bukkit.getLogger().info("Sending packet. Spawning at " + location.toString());
            manager.sendServerPacket(player, entity.getHandle());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void spawnFakePlayer(Player player, Location location) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        UUID uuid = UUID.randomUUID();
        int id = (int)(Math.random() * Integer.MAX_VALUE);

        WrapperPlayServerNamedEntitySpawn npc = new WrapperPlayServerNamedEntitySpawn();
        npc.setPlayerUUID(uuid);
        npc.setEntityID(id);
        npc.setX(location.getX());
        npc.setY(location.add(0, 1,0).getY());
        npc.setZ(location.getZ());
        npc.setYaw(player.getLocation().getYaw());
        npc.setPitch(player.getLocation().getPitch());

        WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
        info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        PlayerInfoData data = new PlayerInfoData(new WrappedGameProfile(uuid, "Test"), 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText("Test"));
        List<PlayerInfoData> dataList = new ArrayList<>();
        dataList.add(data);
        info.setData(dataList);

        QuestsXL.getInstance().getEntityRegistry().add(id);

        try {
            manager.sendServerPacket(player, info.getHandle());
            manager.sendServerPacket(player, npc.getHandle());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static WrapperPlayServerPlayerInfo addFakePlayer(UUID uuid) {
        WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
        info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        PlayerInfoData data = new PlayerInfoData(new WrappedGameProfile(uuid, "Test"), 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText("Test"));
        List<PlayerInfoData> dataList = new ArrayList<>();
        dataList.add(data);
        info.setData(dataList);
        return info;
    }

    public static void updateEntityLocation(Player player, Location loc, int id) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        WrapperPlayServerRelEntityMoveLook packet = new WrapperPlayServerRelEntityMoveLook();
        packet.setEntityID(id);
        packet.setDx(loc.getX());
        packet.setDy(loc.getY());
        packet.setDz(loc.getZ());
        packet.setPitch(loc.getPitch());
        packet.setYaw(loc.getYaw());
        try {
            manager.sendServerPacket(player, packet.getHandle());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
