package com.github.zandy.bedwarspractice.engine;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.scoreboard.Scoreboard;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.Main;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeQuitEvent;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ScoreboardEngine implements Listener {
   private static volatile ScoreboardEngine instance = null;
   private final HashMap<UUID, Scoreboard> sidebarMap = new HashMap<>();

   private ScoreboardEngine() {
   }

   public static ScoreboardEngine getInstance() {
      if (instance == null) {
         synchronized (ScoreboardEngine.class) {
            if (instance == null) {
               instance = new ScoreboardEngine();
            }
         }
      }
      return instance;
   }

   public void init() {
      BambooUtils.registerEvent(this);
      Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> {
         new HashMap<>(this.sidebarMap).forEach((uuid, scoreboard) -> {
            if (GameEngine.getInstance().getPracticeTypeMap().containsKey(uuid)) {
               scoreboard.refresh();
            }
         });
      }, 0L, Settings.SettingsEnum.SCOREBOARD_REFRESH_TICK.getInt());
   }

   public void sendSidebar(Player player, GameEngine.PracticeType practiceType) {
      if (Main.getBedWarsAPI() != null) {
         Main.getBedWarsAPI().getScoreboardUtil().removePlayerScoreboard(player);
      }

      UUID uuid = player.getUniqueId();
      if (this.sidebarMap.containsKey(uuid)) {
         this.sidebarMap.get(uuid).destroy();
      }

      List<String> lines;
      HashMap<String, Callable<String>> placeholders;
      switch (practiceType) {
         case BRIDGING:
            BridgingMode bridgingMode = BridgingMode.getInstance();
            lines = bridgingMode.getScoreboardLines(uuid);
            placeholders = bridgingMode.getPlaceholders(uuid);
            break;
         case MLG:
            MLGMode mlgMode = MLGMode.getInstance();
            lines = mlgMode.getScoreboardLines(uuid);
            placeholders = mlgMode.getPlaceholders(uuid);
            break;
         case FIREBALL_TNT_JUMPING:
            FireballTNTJumpingMode fireballTNTJumpingMode = FireballTNTJumpingMode.getInstance();
            lines = fireballTNTJumpingMode.getScoreboardLines(uuid);
            placeholders = fireballTNTJumpingMode.getPlaceholders(uuid);
            break;
         default:
            return;
      }

      this.sidebarMap.put(uuid, new Scoreboard(player, Language.MessagesEnum.GAME_SCOREBOARD_TITLE.getString(uuid), lines, placeholders));
   }

   @EventHandler
   private void onPracticeQuit(PracticeQuitEvent event) {
      UUID uuid = event.getPlayer().getUniqueId();
      if (this.sidebarMap.containsKey(uuid)) {
         Scoreboard scoreboard = this.sidebarMap.get(uuid);
         scoreboard.destroy();
         this.sidebarMap.remove(uuid);
         if (Main.getBedWarsAPI() != null) {
            Main.getBedWarsAPI().getScoreboardUtil().givePlayerScoreboard(event.getPlayer(), true);
         }
      }
   }

   public void unload(UUID uuid) {
      if (this.sidebarMap.containsKey(uuid)) {
         this.sidebarMap.get(uuid).destroy();
      }
   }
}
