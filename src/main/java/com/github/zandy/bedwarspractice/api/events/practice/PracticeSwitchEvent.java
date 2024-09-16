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

    public PracticeSwitchEvent(Player var1, GameEngine.PracticeType var2, GameEngine.PracticeType var3) {
        this.player = var1;
        this.oldPracticeMode = var2;
        this.newPracticeMode = var3;
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
