package com.github.zandy.bedwarspractice.features.npc;

import com.github.zandy.bamboolib.hologram.RefreshableHologram;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataNPC {
    private static final HashMap<UUID, PlayerDataNPC> dataMap = new HashMap<>();
    private final HashMap<Integer, RefreshableHologram> hologramMap = new HashMap<>();

    public PlayerDataNPC(UUID uuid) {
        dataMap.put(uuid, this);
    }

    public static boolean contains(UUID uuid) {
        return dataMap.containsKey(uuid);
    }

    public static PlayerDataNPC get(UUID uuid) {
        return dataMap.get(uuid);
    }

    public static HashMap<UUID, PlayerDataNPC> getDataMap() {
        return dataMap;
    }

    public void addNPC(Integer value, RefreshableHologram hologram) {
        this.hologramMap.put(value, hologram);
    }

    public void flush() {
        this.hologramMap.clear();
    }

    public void remove(Integer value) {
        if (this.hologramMap.containsKey(value)) {
            this.hologramMap.get(value).remove();
        }

    }

    public void removeAll() {
        this.hologramMap.values().forEach(RefreshableHologram::remove);
        this.flush();
    }
}
