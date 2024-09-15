package com.github.zandy.bedwarspractice.support;

import com.github.zandy.bamboolib.utils.BambooUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class BWPPatcher {
   private static final Logger LOGGER = Logger.getLogger(BWPPatcher.class.getName());
   private static final File PAPER_SPIGOT_FILE = new File("paper.yml");
   private static final ConcurrentHashMap<PatchType, Boolean> patchStatus = new ConcurrentHashMap<>();

   public static void patch(@NotNull PatchType patchType) {
      try {
         if (patchType.isPatchable()) {
            boolean patchApplied = false;
            File configFile;
            YamlConfiguration config;

            switch (patchType) {
               case REAL_SCOREBOARD:
                  configFile = new File("plugins/RealScoreboard/", "config.yml");
                  config = YamlConfiguration.loadConfiguration(configFile);
                  List<String> bypassWorlds = config.getStringList("Config.Bypass-Worlds");
                  if (!bypassWorlds.contains("bedwars_practice")) {
                     patchApplied = true;
                     LOGGER.info("RealScoreboard found! Applying patch...");
                     bypassWorlds.add("bedwars_practice");
                     config.set("Config.Bypass-Worlds", bypassWorlds);
                     config.save(configFile);
                  }
                  break;
               case PAPER:
                  config = YamlConfiguration.loadConfiguration(PAPER_SPIGOT_FILE);
                  if (config.getBoolean("warnWhenSettingExcessiveVelocity")) {
                     patchApplied = true;
                     LOGGER.info("PaperMc found! Applying patch...");
                     config.set("warnWhenSettingExcessiveVelocity", false);
                     config.save(PAPER_SPIGOT_FILE);
                  }
                  break;
               case FAWE:
                  configFile = new File("plugins/FastAsyncWorldEdit/", "config.yml");
                  config = YamlConfiguration.loadConfiguration(configFile);
                  if (config.getBoolean("paths.per-player-schematics")) {
                     patchApplied = true;
                     LOGGER.info("FastAsyncWorldEdit found! Applying patch...");
                     config.set("paths.per-player-schematics", false);
                     config.save(configFile);
                  }
                  break;
               case VIA_VERSION:
                  configFile = new File("plugins/ViaVersion/", "config.yml");
                  config = YamlConfiguration.loadConfiguration(configFile);
                  if (!config.getBoolean("suppress-conversion-warnings") || !config.getBoolean("suppress-metadata-errors")) {
                     patchApplied = true;
                     LOGGER.info("ViaVersion found! Applying patch...");
                     config.set("suppress-conversion-warnings", true);
                     config.set("suppress-metadata-errors", true);
                     config.save(configFile);
                  }
                  break;
            }

            if (patchApplied) {
               LOGGER.info("The patch was applied, please restart!");
               patchStatus.put(patchType, true);
            }
         }
      } catch (Exception e) {
         LOGGER.severe("Error applying patch: " + e.getMessage());
         e.printStackTrace();
      }
   }

   public enum PatchType {
      REAL_SCOREBOARD(new File("plugins/RealScoreboard/", "config.yml").exists() && !YamlConfiguration.loadConfiguration(new File("plugins/RealScoreboard/", "config.yml")).getStringList("Config.Bypass-Worlds").contains("bedwars_practice")),
      PAPER(PAPER_SPIGOT_FILE.exists()),
      FAWE(BambooUtils.isPluginEnabled("FastAsyncWorldEdit")),
      VIA_VERSION(BambooUtils.isPluginEnabled("ViaVersion") && BambooUtils.isVersion(8));

      final boolean patchable;

      PatchType(boolean patchable) {
         this.patchable = patchable;
      }

      public boolean isPatchable() {
         return this.patchable;
      }
   }
}
