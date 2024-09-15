package com.github.zandy.bedwarspractice.support;

import com.github.zandy.bamboolib.utils.BambooUtils;
import java.io.File;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class BWPPatcher {
   static File paperSpigotFile = new File("paper.yml");

   public static void patch(@NotNull BWPPatcher.PatchType var0) {

       try {
         if (var0.isPatchable()) {
            boolean var1 = true;
            File var2;
            YamlConfiguration var3;
            switch(var0) {
            case REAL_SCOREBOARD:
               var2 = new File("plugins/RealScoreboard/", "config.yml");
               var3 = YamlConfiguration.loadConfiguration(var2);
               List<String> var4 = var3.getStringList("Config.Bypass-Worlds");
               if (!var4.contains("bedwars_practice")) {
                  var1 = false;
                  BambooUtils.consolePrint("&cRealScoreboard found! Applying patch...");
                  var4.add("bedwars_practice");
                  var3.set("Config.Bypass-Worlds", var4);
                  var3.save(var2);
               }
               break;
            case PAPER:
               YamlConfiguration var9 = YamlConfiguration.loadConfiguration(paperSpigotFile);
               if (var9.getBoolean("warnWhenSettingExcessiveVelocity")) {
                  var1 = false;
                  BambooUtils.consolePrint("&cPaperMc found! Applying patch...");
                  var9.set("warnWhenSettingExcessiveVelocity", false);

                  try {
                     var9.save(paperSpigotFile);
                  } catch (Exception ignored) {
                  }
               }
               break;
            case FAWE:
               var2 = new File("plugins/FastAsyncWorldEdit/", "config.yml");
               var3 = YamlConfiguration.loadConfiguration(var2);
               if (var3.getBoolean("paths.per-player-schematics")) {
                  var1 = false;
                  BambooUtils.consolePrint("&cFastAsyncWorldEdit found! Applying patch...");
                  var3.set("paths.per-player-schematics", false);

                  try {
                     var3.save(var2);
                  } catch (Exception ignored) {
                  }
               }
               break;
            case VIA_VERSION:
               var2 = new File("plugins/ViaVersion/", "config.yml");
               var3 = YamlConfiguration.loadConfiguration(var2);
               if (!var3.getBoolean("suppress-conversion-warnings") || !var3.getBoolean("suppress-metadata-errors")) {
                  var1 = false;
                  BambooUtils.consolePrint("&cViaVersion found! Applying patch...");
                  var3.set("suppress-conversion-warnings", true);
                  var3.set("suppress-metadata-errors", true);

                  try {
                     var3.save(var2);
                  } catch (Exception ignored) {
                  }
               }
            }

            if (!var1) {
               BambooUtils.consolePrint("&c&lThe patch was applied, please restart!");
            }

         }
      } catch (Throwable var8) {
         var8.printStackTrace();
      }
   }


   public enum PatchType {
      REAL_SCOREBOARD((new File("plugins/RealScoreboard/", "config.yml")).exists() && !YamlConfiguration.loadConfiguration(new File("plugins/RealScoreboard/", "config.yml")).getStringList("Config.Bypass-Worlds").contains("bedwars_practice")),
      PAPER(BWPPatcher.paperSpigotFile.exists()),
      FAWE(BambooUtils.isPluginEnabled("FastAsyncWorldEdit")),
      VIA_VERSION(BambooUtils.isPluginEnabled("ViaVersion") && BambooUtils.isVersion(8));

      final boolean patchable;

      PatchType(boolean var3) {
         this.patchable = var3;
      }

      public boolean isPatchable() {
         return this.patchable;
      }

   }
}
