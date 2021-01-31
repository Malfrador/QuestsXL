package de.erethon.questsxl.commands;

import de.erethon.commons.command.DRECommandCache;
import de.erethon.commons.javaplugin.DREPlugin;

import java.util.Set;

public class QCommandCache extends DRECommandCache {

    public static final String LABEL = "questsxl";

    DREPlugin plugin;

    public QCommandCache(DREPlugin plugin) {
        super(LABEL, plugin);
        this.plugin = plugin;
        addCommand(new ActionCommand());
        addCommand(new NPCCommand());

    }
}


