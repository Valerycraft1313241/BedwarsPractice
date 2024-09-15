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
   private static PlayerEngine instance = null;

   private PlayerEngine() {
   }

   public void init() {
      BambooUtils.registerEvent(this);
      if (Main.getBedWarsAPI() != null) {
         new PlayerChangeLanguageListener();
      }

      Bukkit.getOnlinePlayers().forEach((var1) -> this.join(var1.getUniqueId(), var1.getName()));
   }

   @EventHandler
   private void onPlayerJoin(PlayerJoinEvent var1) {
      this.join(var1.getPlayer().getUniqueId(), var1.getPlayer().getName());
   }

   private void join(UUID var1, String var2) {
      if (!Database.getInstance().hasAccount(var1, "Profile")) {
         switch(Database.getInstance().getDatabaseType()) {
         case MYSQL:
            Database.getInstance().createPlayer(var1, "Profile", Arrays.asList(new ColumnInfo("Player", var2), new ColumnInfo("UUID", var1)));
            break;
         case FLAT_FILE:
            Database.getInstance().setString(var1, Settings.SettingsEnum.DEFAULT_LANGUAGE.getString().replace("English", "EN"), "Language", "Profile");
         }
      }

      if (!Database.getInstance().hasAccount(var1, Stats.getTableName())) {
         switch(Database.getInstance().getDatabaseType()) {
         case MYSQL:
            Database.getInstance().createPlayer(var1, Stats.getTableName(), Arrays.asList(new ColumnInfo("Player", var2), new ColumnInfo("UUID", var1)));
            break;
         case FLAT_FILE:
            Arrays.stream(Stats.StatsType.values()).forEach((var1x) -> Database.getInstance().setDouble(var1, 0.0D, var1x.name(), Stats.getTableName()));
         }
      }

      if (Database.getInstance().hasAccount(var1, Stats.getTableName())) {
         Stats.getInstance().addPlayer(var1);
      }

      if (!Language.getInstance().getPlayerLocale().containsKey(var1)) {
         if (Main.getBedWarsAPI() != null) {
            Language.getInstance().getPlayerLocale().put(var1, Main.getBedWarsAPI().getPlayerLanguage(Bukkit.getPlayer(var1)).getIso().toUpperCase());
         } else {
            Language.getInstance().getPlayerLocale().put(var1, Database.getInstance().getString(var1, "Language", "Profile"));
         }
      }

      if (!PlayerDataNPC.contains(var1)) {
         new PlayerDataNPC(var1);
      } else {
         PlayerDataNPC.get(var1).flush();
      }

      if (BambooUtils.isPluginEnabled("Citizens")) {
         Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> PracticeNPC.getInstance().respawnNPCs(Bukkit.getPlayer(var1)), 1L);
      }

   }

   public static PlayerEngine getInstance() {
      if (instance == null) {
         instance = new PlayerEngine();
      }

      return instance;
   }
}
