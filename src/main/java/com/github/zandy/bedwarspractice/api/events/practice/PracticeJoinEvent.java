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

   public PracticeJoinEvent(Player var1, GameEngine.PracticeType var2) {
      this.player = var1;
      this.practiceType = var2;
      var1.setGameMode(GameMode.SURVIVAL);
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

   public GameEngine.PracticeType getPracticeType() {
      return this.practiceType;
   }
}
