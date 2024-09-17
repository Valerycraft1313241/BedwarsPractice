package com.github.zandy.bedwarspractice.api.events.practice;

import com.github.zandy.bedwarspractice.engine.GameEngine;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PracticeJoinEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final GameEngine.PracticeType practiceType;

    public PracticeJoinEvent(Player player, GameEngine.PracticeType practiceType) {
        this.player = player;
        this.practiceType = practiceType;
        player.setGameMode(GameMode.SURVIVAL);
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

    public GameEngine.PracticeType getPracticeType() {
        return this.practiceType;
    }
}
