package com.github.zandy.bedwarspractice;

import com.andrei1058.bedwars.api.BedWars;
import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.VersionSupport;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeCommand;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeLanguageCommand;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicWorldCreator;
import com.github.zandy.bedwarspractice.engine.*;
import com.github.zandy.bedwarspractice.features.Placeholders;
import com.github.zandy.bedwarspractice.features.chat.ChatHandler;
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

public class Main extends JavaPlugin {
    private static BedWars bedWarsAPI = null;

    public static BedWars getBedWarsAPI() {
        return bedWarsAPI;
    }

    public void onEnable() {
        BambooLib.setPluginInstance(this);
        BambooUtils.consolePrint("&m--------------------------");
        BambooUtils.consolePrint("Initializing BedWars Practice " + this.getDescription().getVersion());
        if (!BambooUtils.isVersion(8, 12, 18, 19)) {
            BambooUtils.consolePrint("&c&lCan't run on: " + VersionSupport.getInstance().getVersion());
            BambooUtils.consolePrint("&f&m--------------------------");
            this.setEnabled(false);
        } else {
            BambooUtils.consolePrint("Running on: " + VersionSupport.getInstance().getVersion());
            BambooUtils.consolePrint("Loading Settings...");
            Settings.init();
            BambooUtils.consolePrint("Initializing Database & Profiles...");
            new Database();
            BambooUtils.consolePrint("Database type: " + BambooUtils.capitalizeFirstLetter(com.github.zandy.bamboolib.database.Database.getInstance().getDatabaseType().name().toLowerCase()).replace("_", " "));
            PlayerEngine.getInstance().init();
            BambooUtils.consolePrint("Loading Languages...");
            org.bukkit.plugin.Plugin bedWarsPlugin = Bukkit.getPluginManager().getPlugin("BedWars1058");
            if (bedWarsPlugin != null) {
                String pluginVersion = bedWarsPlugin.getDescription().getVersion().replace("-SNAPSHOT", "");
                if (pluginVersion.contains("${gitVer}")) {
                    pluginVersion = pluginVersion.replace("${gitVer}", "");
                }

                String[] versionParts = pluginVersion.split("\\.");
                pluginVersion = versionParts[0] + "." + versionParts[1];
                if (Double.parseDouble(pluginVersion) >= 22.01D) {
                    BambooUtils.consolePrint("BedWars1058 hook found! Hooking languages...");
                    bedWarsAPI = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
                } else {
                    BambooUtils.consolePrint("BedWars1058 hook found! &cYou're using an unsupported BedWars1058 version!");
                    BambooUtils.consolePrint("Required BedWars1058 version: 22.01 or greater");
                }
            }

            Language.getInstance().init();
            BambooUtils.consolePrint("Loading Engines...");
            ScoreboardEngine.getInstance().init();
            WorldEngine.getInstance().init();
            GameEngine.getInstance().init();
            SpectatorEngine.getInstance().init();
            BambooUtils.consolePrint("Loading Commands & Setup Data...");
            VersionSupport.getInstance().registerCommand(new BedWarsPracticeCommand());
            VersionSupport.getInstance().registerCommand(new BedWarsPracticeAdminCommand());
            if (bedWarsAPI == null) {
                VersionSupport.getInstance().registerCommand(new BedWarsPracticeLanguageCommand());
            }

            SetupData.getInstance().init();
            BambooUtils.consolePrint("Loading GUIs...");
            ModeSelectorGUI.getInstance().init();
            GameSettingsGUI.getInstance().init();
            BambooUtils.consolePrint("Loading Functions & Placeholders...");
            if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
                BambooUtils.consolePrint("Hooking to the proxy...");
                PracticeIncomingOutgoingProxy.getInstance().init();
            }

            if (BambooUtils.isPluginEnabled("Citizens")) {
                PracticeNPC.getInstance().init();
            }

            File spigotConfigFile = new File("spigot.yml");
            YamlConfiguration spigotConfig = YamlConfiguration.loadConfiguration(spigotConfigFile);
            spigotConfig.set("world-settings.default.verbose", Settings.SettingsEnum.ENABLE_VERBOSE.getBoolean());
            spigotConfig.set("world-settings.default.anti-xray.enabled", false);

            try {
                spigotConfig.save(spigotConfigFile);
            } catch (Exception ignored) {
            }

            Arrays.stream(BWPPatcher.PatchType.values()).forEach(BWPPatcher::patch);
            new Placeholders();
            new ChatHandler();
            BambooUtils.consolePrint("&aBedWars Practice loaded successfully!");
            BambooUtils.consolePrint("&m--------------------------");
        }
    }

    public void onDisable() {
        BambooUtils.consolePrint("&m--------------------------");
        BambooUtils.consolePrint("&cDisabling BedWars Practice " + this.getDescription().getVersion());
        if (PracticeNPC.getInstance().isInit()) {
            PracticeNPC.getInstance().despawnNPCs();
        }

        if (com.github.zandy.bamboolib.database.Database.getInstance().getDatabaseType().equals(com.github.zandy.bamboolib.database.Database.DatabaseType.MYSQL)) {
            BambooUtils.consolePrint("Disabling Database...");
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
            BambooUtils.consolePrint("Unhooking from the proxy...");
            PracticeIncomingOutgoingProxy.getInstance().unregister();
        }

        BambooUtils.consolePrint("&aBedWars Practice unloaded successfully!");
        BambooUtils.consolePrint("&m--------------------------");
    }
}
