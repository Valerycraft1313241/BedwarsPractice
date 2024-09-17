package com.github.zandy.bedwarspractice.api.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLanguageChangeEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String oldISO;
    private final String newISO;

    public PlayerLanguageChangeEvent(Player player, String oldISO, String newISO) {
        this.player = player;
        this.oldISO = oldISO;
        this.newISO = newISO;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getOldISO() {
        return this.oldISO;
    }

    public String getNewISO() {
        return this.newISO;
    }
}
