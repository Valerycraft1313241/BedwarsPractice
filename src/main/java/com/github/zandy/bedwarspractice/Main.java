package com.github.zandy.bedwarspractice;

import com.andrei1058.bedwars.api.BedWars;
import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.VersionSupport;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeCommand;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeLanguageCommand;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicWorldCreator;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.PlayerEngine;
import com.github.zandy.bedwarspractice.engine.ScoreboardEngine;
import com.github.zandy.bedwarspractice.engine.SpectatorEngine;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.features.Placeholders;
import com.github.zandy.bedwarspractice.features.guis.GameSettingsGUI;
import com.github.zandy.bedwarspractice.features.guis.ModeSelectorGUI;
import com.github.zandy.bedwarspractice.features.npc.PracticeNPC;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.proxy.PracticeIncomingOutgoingProxy;
import com.github.zandy.bedwarspractice.storage.Database;
import com.github.zandy.bedwarspractice.support.BWPPatcher;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
   private static BedWars bedWarsAPI = null;
   private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

   @Override
   public void onEnable() {
      BambooLib.setPluginInstance(this);
      logInfo("--------------------------");
      logInfo("Initializing BedWars Practice " + getDescription().getVersion());

      if (!BambooUtils.isVersion(8, 12, 18, 19)) {
         logError("Can't run on: " + VersionSupport.getInstance().getVersion());
         logInfo("--------------------------");
         setEnabled(false);
         return;
      }

      logInfo("Running on: " + VersionSupport.getInstance().getVersion());
      logInfo("Loading Settings...");
      Settings.init();

      logInfo("Initializing Database & Profiles...");
      new Database();
      logInfo("Database type: " + BambooUtils.capitalizeFirstLetter(com.github.zandy.bamboolib.database.Database.getInstance().getDatabaseType().name().toLowerCase()).replace("_", " "));
      PlayerEngine.getInstance().init();

      logInfo("Loading Languages...");
      hookBedWarsAPI();

      Language.getInstance().init();
      logInfo("Loading Engines...");
      ScoreboardEngine.getInstance().init();
      WorldEngine.getInstance().init();
      GameEngine.getInstance().init();
      SpectatorEngine.getInstance().init();

      logInfo("Loading Commands & Setup Data...");
      registerCommands();

      SetupData.getInstance().init();
      logInfo("Loading GUIs...");
      ModeSelectorGUI.getInstance().init();
      GameSettingsGUI.getInstance().init();

      logInfo("Loading Functions & Placeholders...");
      if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
         logInfo("Hooking to the proxy...");
         PracticeIncomingOutgoingProxy.getInstance().init();
      }

      if (BambooUtils.isPluginEnabled("Citizens")) {
         PracticeNPC.getInstance().init();
      }

      configureSpigot();

      Arrays.stream(BWPPatcher.PatchType.values()).forEach(BWPPatcher::patch);
      new Placeholders();

      logInfo("BedWars Practice loaded successfully!");
      logInfo("--------------------------");
   }

   @Override
   public void onDisable() {
      logInfo("--------------------------");
      logInfo("Disabling BedWars Practice " + getDescription().getVersion());

      if (PracticeNPC.getInstance().isInitialized()) {
         PracticeNPC.getInstance().despawnNPCs();
      }

      if (com.github.zandy.bamboolib.database.Database.getInstance().getDatabaseType().equals(com.github.zandy.bamboolib.database.Database.DatabaseType.MYSQL)) {
         logInfo("Disabling Database...");
         com.github.zandy.bamboolib.database.Database.getInstance().close();
      }

      if (GameEngine.getInstance() != null) {
         GameEngine.getInstance().serverReload();
      }

      if (WorldEngine.getInstance() != null) {
         WorldEngine.getInstance().unload();
      }

      SchematicWorldCreator.unload();

      if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
         logInfo("Unhooking from the proxy...");
         PracticeIncomingOutgoingProxy.getInstance().unregister();
      }

      logInfo("BedWars Practice unloaded successfully!");
      logInfo("--------------------------");
   }

   private void hookBedWarsAPI() {
      org.bukkit.plugin.Plugin plugin = Bukkit.getPluginManager().getPlugin("BedWars1058");
      if (plugin != null) {
         String version = plugin.getDescription().getVersion().replace("-SNAPSHOT", "");
         if (version.contains("${gitVer}")) {
            version = version.replace("${gitVer}", "");
         }

         String[] versionParts = version.split("\\.");
         version = versionParts[0] + "." + versionParts[1];
         if (Double.parseDouble(version) >= 22.01D) {
            logInfo("BedWars1058 hook found! Hooking languages...");
            bedWarsAPI = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
         } else {
            logError("BedWars1058 hook found! You're using an unsupported BedWars1058 version!");
            logError("Required BedWars1058 version: 22.01 or greater");
         }
      }
   }

   private void registerCommands() {
      VersionSupport.getInstance().registerCommand(new BedWarsPracticeCommand());
      VersionSupport.getInstance().registerCommand(new BedWarsPracticeAdminCommand());
      if (bedWarsAPI == null) {
         VersionSupport.getInstance().registerCommand(new BedWarsPracticeLanguageCommand());
      }
   }

   private void configureSpigot() {
      File spigotConfig = new File("spigot.yml");
      YamlConfiguration config = YamlConfiguration.loadConfiguration(spigotConfig);
      config.set("world-settings.default.verbose", Settings.SettingsEnum.ENABLE_VERBOSE.getBoolean());
      config.set("world-settings.default.anti-xray.enabled", false);

      try {
         config.save(spigotConfig);
      } catch (Exception e) {
         logError("Failed to save spigot.yml configuration", e);
      }
   }

   private void logInfo(String message) {
      LOGGER.info(message);
   }

   private void logError(String message) {
      LOGGER.severe(message);
   }

   private void logError(String message, Throwable throwable) {
      LOGGER.log(Level.SEVERE, message, throwable);
   }

   public static BedWars getBedWarsAPI() {
      return bedWarsAPI;
   }
}
