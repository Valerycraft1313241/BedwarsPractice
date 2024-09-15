package com.github.zandy.bedwarspractice.engine;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.database.Database;
import com.github.zandy.bamboolib.database.utils.ColumnInfo;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.Main;
import com.github.zandy.bedwarspractice.features.bedwars1058.PlayerChangeLanguageListener;
import com.github.zandy.bedwarspractice.features.npc.PlayerDataNPC;
import com.github.zandy.bedwarspractice.features.npc.PracticeNPC;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEngine implements Listener {
   private static volatile PlayerEngine instance = null;

   private PlayerEngine() {
   }

   public static PlayerEngine getInstance() {
      if (instance == null) {
         synchronized (PlayerEngine.class) {
            if (instance == null) {
               instance = new PlayerEngine();
            }
         }
      }
      return instance;
   }

   public void init() {
      BambooUtils.registerEvent(this);
      if (Main.getBedWarsAPI() != null) {
         new PlayerChangeLanguageListener();
      }

      Bukkit.getOnlinePlayers().forEach(player -> this.join(player.getUniqueId(), player.getName()));
   }

   @EventHandler
   private void onPlayerJoin(PlayerJoinEvent event) {
      this.join(event.getPlayer().getUniqueId(), event.getPlayer().getName());
   }

   private void join(UUID uuid, String playerName) {
      Database database = Database.getInstance();
      if (!database.hasAccount(uuid, "Profile")) {
         switch (database.getDatabaseType()) {
            case MYSQL:
               database.createPlayer(uuid, "Profile", Arrays.asList(new ColumnInfo("Player", playerName), new ColumnInfo("UUID", uuid)));
               break;
            case FLAT_FILE:
               database.setString(uuid, Settings.SettingsEnum.DEFAULT_LANGUAGE.getString().replace("English", "EN"), "Language", "Profile");
         }
      }

      if (!database.hasAccount(uuid, Stats.getTableName())) {
         switch (database.getDatabaseType()) {
            case MYSQL:
               database.createPlayer(uuid, Stats.getTableName(), Arrays.asList(new ColumnInfo("Player", playerName), new ColumnInfo("UUID", uuid)));
               break;
            case FLAT_FILE:
               Arrays.stream(Stats.StatsType.values()).forEach(stat -> database.setDouble(uuid, 0.0D, stat.name(), Stats.getTableName()));
         }
      }

      if (database.hasAccount(uuid, Stats.getTableName())) {
         Stats.getInstance().addPlayer(uuid);
      }

      Language language = Language.getInstance();
      if (!language.getPlayerLocale().containsKey(uuid)) {
         if (Main.getBedWarsAPI() != null) {
            language.getPlayerLocale().put(uuid, Main.getBedWarsAPI().getPlayerLanguage(Bukkit.getPlayer(uuid)).getIso().toUpperCase());
         } else {
            language.getPlayerLocale().put(uuid, database.getString(uuid, "Language", "Profile"));
         }
      }

      if (!PlayerDataNPC.contains(uuid)) {
         new PlayerDataNPC(uuid);
      } else {
         PlayerDataNPC.get(uuid).flush();
      }

      if (BambooUtils.isPluginEnabled("Citizens")) {
         Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> PracticeNPC.getInstance().respawnNPCs(Bukkit.getPlayer(uuid)), 1L);
      }
   }
}
