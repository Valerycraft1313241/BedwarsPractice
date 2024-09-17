package com.github.zandy.bedwarspractice.api.events.practice;

import com.github.zandy.bedwarspractice.engine.GameEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PracticeSwitchEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final GameEngine.PracticeType oldPracticeMode;
    private final GameEngine.PracticeType newPracticeMode;

    public PracticeSwitchEvent(Player player, GameEngine.PracticeType oldPracticeMode, GameEngine.PracticeType newPracticeMode) {
        this.player = player;
        this.oldPracticeMode = oldPracticeMode;
        this.newPracticeMode = newPracticeMode;
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

    public GameEngine.PracticeType getOldPracticeMode() {
        return this.oldPracticeMode;
    }

    public GameEngine.PracticeType getNewPracticeMode() {
        return this.newPracticeMode;
    }
}
