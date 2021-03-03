package de.erethon.questsxl.action;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.PaperMobGoals;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.questsxl.QuestsXL;
import de.erethon.questsxl.players.QPlayer;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class QCutscene implements QAction{

    QuestsXL plugin = QuestsXL.getInstance();
    List<Location> locations = new ArrayList<>();
    List<String> messages = new ArrayList<>();
    int progress = 0;
    ItemStack headItem = new ItemStack(Material.CARVED_PUMPKIN);

    @Override
    public void play(Player player) {
        //
        Location loc = player.getLocation();
        locations.add(player.getLocation());
        messages.add("This is nothing.");
        messages.add(" \uF808 <rainbow>Dies ist ein sehr langer Text, der in der Actionbar angezeigt wird. Viel Spaß damit. \n Dies ist ein test!");
        locations.add(player.getLocation().add(50,0,50));
        messages.add("\uF802 &oDieser Text ist länger, hat aber eine deutlich langweiligere Farbe. Das ist sehr schön. Dieser Text ist ebenfalls kursiv und das ist toll!");
        locations.add(player.getTargetBlock(50).getLocation());
        messages.add("You have arrived.");

        GameMode previousGamemode = player.getGameMode();
        ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM);
        EntityArmorStand nmsStand= ((CraftArmorStand) stand).getHandle();

        nmsStand.noclip = true;
        stand.setInvisible(true);
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().setItem(EquipmentSlot.HEAD, headItem);
        player.setSpectatorTarget(stand);
        BukkitRunnable mover = new BukkitRunnable() {
            int tick = 1;
            double progressYaw = 0.00;
            double progressPitch = 0.00;
            @Override
            public void run() {
                Location current = stand.getLocation();
                Location target = locations.get(progress);
                Vector vCurrent = current.toVector();
                Vector vTarget = target.toVector();
                Vector direction = vTarget.subtract(vCurrent);
                double currentYaw = current.getYaw();
                double currentPitch = current.getPitch();
                double targetYaw = target.getYaw();
                double targetPitch = target.getPitch();
                double stepSizeYaw = currentYaw / targetYaw;
                double stepSizePitch = currentPitch / targetPitch;
                if (current.distance(target) < 1) {
                    progress++;
                    progressYaw = 0.00;
                    progressPitch = 0.00;
                    tick = 1;
                    if (progress >= locations.size()) {
                        player.setGameMode(previousGamemode);
                        player.getInventory().setItem(EquipmentSlot.HEAD, null);
                        player.getInventory().setItem(EquipmentSlot.HAND, null);
                        stand.damage(1000);
                        cancel();
                    }
                    return;
                }
                //player.sendActionBar(MessageUtil.parse(messages.get(progress)));
                player.sendActionBar("Current: " + current.toString() + " Target: " + target.toString());
                stand.setVelocity(direction.multiply(tick).normalize());
                progressYaw = progressYaw + stepSizeYaw;
                progressPitch = progressPitch + stepSizePitch;
                stand.setRotation((float) progressYaw, (float) progressPitch);
                tick++;

            }
        };
        mover.runTaskTimer(plugin, 5,1);
    }

    private float clampYaw(float yaw) {
        while(yaw < -180.0F) {
            yaw += 360.0F;
        }

        while(yaw >= 180.0F) {
            yaw -= 360.0F;
        }

        return yaw;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void load(ConfigurationSection section) {

    }
}
