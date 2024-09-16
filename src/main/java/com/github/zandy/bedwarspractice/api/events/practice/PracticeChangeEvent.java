package com.github.zandy.bedwarspractice.api.events.practice;

import com.github.zandy.bedwarspractice.api.utils.data.PracticeData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PracticeChangeEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final PracticeData oldPracticeData;
    private final PracticeData newPracticeData;

    public PracticeChangeEvent(Player var1, PracticeData var2, PracticeData var3) {
        this.player = var1;
        this.oldPracticeData = var2;
        this.newPracticeData = var3;
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

    public PracticeData getOldPracticeData() {
        return this.oldPracticeData;
    }

    public PracticeData getNewPracticeData() {
        return this.newPracticeData;
    }
}
