package com.github.zandy.bedwarspractice.api.events.practice;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PracticeQuitEvent extends Event {
   public static final HandlerList handlers = new HandlerList();
   private final Player player;

   public PracticeQuitEvent(Player var1) {
      this.player = var1;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public Player getPlayer() {
      return this.player;
   }
}
