package de.erethon.questsxl.npc;

import java.util.HashSet;
import java.util.Set;

public class EntityRegistry {

    Set<Integer> loadedEntities = new HashSet<>();

    public void add(int id) {
        loadedEntities.add(id);
    }

    public Set<Integer> getLoadedEntities() {
        return loadedEntities;
    }
}
