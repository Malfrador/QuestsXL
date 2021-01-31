package de.erethon.questsxl.objectives;

import de.erethon.questsxl.players.QPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ObjectiveCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final QObjective objective;
    private final QPlayer qPlayer;

    public ObjectiveCompleteEvent(QObjective obj, QPlayer qPlayer) {
        this.objective = obj;
        this.qPlayer = qPlayer;
    }

    public QObjective getObjective() {
        return objective;
    }

    public QPlayer getPlayer() {
        return qPlayer;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
