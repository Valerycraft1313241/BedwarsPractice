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
    private static boolean bedwars1058 = false;
    private static boolean bedwars2023 = false;
    public static Main instance;

    public static boolean isBedwars1058() {
        return bedwars1058;
    }

    public static boolean isBedwars2023() {
        return bedwars2023;
    }

    public void onEnable() {
        BambooLib.setPluginInstance(this);
        instance = this;
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

            bedwars1058 = BambooUtils.isPluginEnabled("BedWars1058");
            bedwars2023 = BambooUtils.isPluginEnabled("BedWars2023");

            Language.getInstance().init();
            BambooUtils.consolePrint("Loading Engines...");
            ScoreboardEngine.getInstance().init();
            WorldEngine.getInstance().init();
            GameEngine.getInstance().init();
            SpectatorEngine.getInstance().init();
            BambooUtils.consolePrint("Loading Commands & Setup Data...");
            VersionSupport.getInstance().registerCommand(new BedWarsPracticeCommand());
            VersionSupport.getInstance().registerCommand(new BedWarsPracticeAdminCommand());
            if (!bedwars1058 && !bedwars2023) {
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
