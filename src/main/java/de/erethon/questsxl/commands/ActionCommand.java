package de.erethon.questsxl.commands;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.questsxl.QuestsXL;
import de.erethon.questsxl.action.recorder.RecordingSession;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ActionCommand extends DRECommand implements Listener {

    QuestsXL plugin = QuestsXL.getInstance();

    boolean isSettingUp = false;
    RecordingSession session;

    public ActionCommand() {
        setCommand("action");
        setAliases("a");
        setMinArgs(0);
        setMaxArgs(4);
        setPlayerCommand(true);
        setHelp("Help.");
        setPermission("qxl.action");
    }

    @Override
    public void onExecute(String[] args, CommandSender commandSender) {
        Player player = (Player) commandSender;
        if (args[1].equals("record") || args[1].equals("r")) {
            if (args.length >= 3 && (args[2].equals("f") || args[2].equals("finish"))) {
                isSettingUp = false;
                MessageUtil.sendMessage(player, "&aArea erfolgreich erstellt. Gib /q a r s ein, um zu starten.");
                return;
            }
            if (args.length >= 3 && (args[2].equals("s") || args[2].equals("start"))) {
                session.start();
                MessageUtil.sendMessage(player, "&aSession gestartet. /q a r stop zum stoppen.");
                return;
            }
            if (args.length >= 3 && (args[2].equals("st") || args[2].equals("stop"))) {
                MessageUtil.sendMessage(player, "&cAufnahme gestoppt. /q a r p zum abspielen.");
                session.stop();
                return;
            }
            if (args.length >= 3 && (args[2].equals("pa") || args[2].equals("pause"))) {
                MessageUtil.sendMessage(player, "&cAbspielen gestoppt");
                session.getRecording().stop();
                return;
            }
            if (args.length >= 3 && (args[2].equals("p") || args[2].equals("play"))) {
                MessageUtil.sendMessage(player, "&aPlaying...");
                session.getRecording().play(player);
                return;
            } else if (args.length == 2) {
                session = null;
                session = new RecordingSession(player);
                isSettingUp = true;
                plugin.getServer().getPluginManager().registerEvents(this, plugin);
                MessageUtil.sendMessage(player, "&7&oRechts-/Linksklick um Area zu erstellen.");
                return;
            }
        }
    }


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!isSettingUp) {
            return;
        }
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            session.setPos1(event.getClickedBlock().getLocation());
            MessageUtil.sendMessage(player, "&aPos1 set to " + session.getPos1().toString());
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            session.setPos2(event.getClickedBlock().getLocation());
            MessageUtil.sendMessage(player, "&aPos2 set to " + session.getPos2().toString());
        }
    }


}
