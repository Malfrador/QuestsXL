package de.erethon.questsxl.commands;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.questsxl.QuestsXL;
import de.erethon.questsxl.action.recorder.RecordingSession;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCCommand extends DRECommand {

    QuestsXL plugin = QuestsXL.getInstance();

    boolean isSettingUp = false;
    RecordingSession session;

    public NPCCommand() {
        setCommand("npc");
        setAliases("n");
        setMinArgs(0);
        setMaxArgs(4);
        setPlayerCommand(true);
        setHelp("Help.");
        setPermission("qxl.npc");
    }

    @Override
    public void onExecute(String[] args, CommandSender commandSender) {
        Player player = (Player) commandSender;
        if (player.getTargetBlock(10) == null) {
            MessageUtil.sendMessage(player, "&cNo target.");
            return;
        }
        plugin.getNpcManager().createNPC(player, player.getTargetBlock(10).getLocation(), args[1]);
        MessageUtil.sendMessage(player, "&aSpawned NPC named " + args[1]);
    }
}
