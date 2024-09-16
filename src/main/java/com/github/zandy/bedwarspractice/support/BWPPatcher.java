package com.github.zandy.bedwarspractice.support;

import com.github.zandy.bamboolib.utils.BambooUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class BWPPatcher {
    static File paperSpigotFile = new File("paper.yml");

    public static void patch(@NotNull BWPPatcher.PatchType patchType) {

        try {
            if (patchType.isPatchable()) {
                boolean patchApplied = true;
                File configFile;
                YamlConfiguration config;
                switch (patchType) {
                    case REAL_SCOREBOARD:
                        configFile = new File("plugins/RealScoreboard/", "config.yml");
                        config = YamlConfiguration.loadConfiguration(configFile);
                        List<String> bypassWorlds = config.getStringList("Config.Bypass-Worlds");
                        if (!bypassWorlds.contains("bedwars_practice")) {
                            patchApplied = false;
                            BambooUtils.consolePrint("&cRealScoreboard found! Applying patch...");
                            bypassWorlds.add("bedwars_practice");
                            config.set("Config.Bypass-Worlds", bypassWorlds);
                            config.save(configFile);
                        }
                        break;
                    case PAPER:
                        YamlConfiguration paperConfig = YamlConfiguration.loadConfiguration(paperSpigotFile);
                        if (paperConfig.getBoolean("warnWhenSettingExcessiveVelocity")) {
                            patchApplied = false;
                            BambooUtils.consolePrint("&cPaperMc found! Applying patch...");
                            paperConfig.set("warnWhenSettingExcessiveVelocity", false);

                            try {
                                paperConfig.save(paperSpigotFile);
                            } catch (Exception ignored) {
                            }
                        }
                        break;
                    case FAWE:
                        configFile = new File("plugins/FastAsyncWorldEdit/", "config.yml");
                        config = YamlConfiguration.loadConfiguration(configFile);
                        if (config.getBoolean("paths.per-player-schematics")) {
                            patchApplied = false;
                            BambooUtils.consolePrint("&cFastAsyncWorldEdit found! Applying patch...");
                            config.set("paths.per-player-schematics", false);

                            try {
                                config.save(configFile);
                            } catch (Exception ignored) {
                            }
                        }
                        break;
                    case VIA_VERSION:
                        configFile = new File("plugins/ViaVersion/", "config.yml");
                        config = YamlConfiguration.loadConfiguration(configFile);
                        if (!config.getBoolean("suppress-conversion-warnings") || !config.getBoolean("suppress-metadata-errors")) {
                            patchApplied = false;
                            BambooUtils.consolePrint("&cViaVersion found! Applying patch...");
                            config.set("suppress-conversion-warnings", true);
                            config.set("suppress-metadata-errors", true);

                            try {
                                config.save(configFile);
                            } catch (Exception ignored) {
                            }
                        }
                }

                if (!patchApplied) {
                    BambooUtils.consolePrint("&c&lThe patch was applied, please restart!");
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public enum PatchType {
        REAL_SCOREBOARD((new File("plugins/RealScoreboard/", "config.yml")).exists() && !YamlConfiguration.loadConfiguration(new File("plugins/RealScoreboard/", "config.yml")).getStringList("Config.Bypass-Worlds").contains("bedwars_practice")),
        PAPER(BWPPatcher.paperSpigotFile.exists()),
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
