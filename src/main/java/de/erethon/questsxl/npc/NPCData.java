package de.erethon.questsxl.npc;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class NPCData {

    // Gets saved in the PersistentDataContainer of the entity

    private String displayname;
    private EntityType displayEntity = EntityType.PLAYER;
    private transient Map<Integer, String> messages = new HashMap<>(); // Chance or Order, messageText
    private transient Map<Integer, NPCPathfinderGoal> pathfinderGoals;
    private boolean orderedMessages;
    private boolean randomTalker;
    private boolean hasAI;
    private boolean invincible;
    private double speed;

    private int faction;
    private byte[] skin;

    public NPCData() {
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public EntityType getDisplayEntity() {
        return displayEntity;
    }

    public Map<Integer, String> getMessages() {
        return messages;
    }

    public Map<Integer, NPCPathfinderGoal> getPathfinderGoals() {
        return pathfinderGoals;
    }

    public boolean isOrderedMessages() {
        return orderedMessages;
    }

    public boolean isRandomTalker() {
        return randomTalker;
    }

    public boolean isHasAI() {
        return hasAI;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public int getFaction() {
        return faction;
    }

    public byte[] getSkin() {
        return skin;
    }
}
