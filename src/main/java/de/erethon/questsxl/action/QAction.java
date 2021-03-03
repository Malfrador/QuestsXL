package de.erethon.questsxl.action;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;

public interface QAction {

    void play(Player player);

    void cancel();

    void load(ConfigurationSection section);

}
