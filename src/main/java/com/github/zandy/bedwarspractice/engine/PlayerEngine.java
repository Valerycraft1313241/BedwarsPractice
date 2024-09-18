package com.github.zandy.bedwarspractice.engine;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.database.Database;
import com.github.zandy.bamboolib.database.utils.ColumnInfo;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.Main;
import com.github.zandy.bedwarspractice.features.bedwars1058.PlayerChangeLanguageListener;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.UUID;

public class PlayerEngine implements Listener {
    private static PlayerEngine instance = null;

    private PlayerEngine() {
    }

    public static PlayerEngine getInstance() {
        if (instance == null) {
            instance = new PlayerEngine();
        }

        return instance;
    }

    public void init() {
        BambooUtils.registerEvent(this);
        if (Main.getBedWarsAPI() != null) {
            new PlayerChangeLanguageListener();
        }

        Bukkit.getOnlinePlayers().forEach((player) -> this.join(player.getUniqueId(), player.getName()));
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        this.join(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

    private void join(UUID playerUUID, String playerName) {
        if (!Database.getInstance().hasAccount(playerUUID, "Profile")) {
            switch (Database.getInstance().getDatabaseType()) {
                case MYSQL:
                    Database.getInstance().createPlayer(playerUUID, "Profile", Arrays.asList(new ColumnInfo("Player", playerName), new ColumnInfo("UUID", playerUUID)));
                    break;
                case FLAT_FILE:
                    Database.getInstance().setString(playerUUID, Settings.SettingsEnum.DEFAULT_LANGUAGE.getString().replace("English", "EN"), "Language", "Profile");
            }
        }

        if (!Database.getInstance().hasAccount(playerUUID, Stats.getTableName())) {
            switch (Database.getInstance().getDatabaseType()) {
                case MYSQL:
                    Database.getInstance().createPlayer(playerUUID, Stats.getTableName(), Arrays.asList(new ColumnInfo("Player", playerName), new ColumnInfo("UUID", playerUUID)));
                    break;
                case FLAT_FILE:
                    Arrays.stream(Stats.StatsType.values()).forEach((statType) -> Database.getInstance().setDouble(playerUUID, 0.0D, statType.name(), Stats.getTableName()));
            }
        }

        if (Database.getInstance().hasAccount(playerUUID, Stats.getTableName())) {
            Stats.getInstance().addPlayer(playerUUID);
        }

        if (!Language.getInstance().getPlayerLocale().containsKey(playerUUID)) {
            if (Main.getBedWarsAPI() != null) {
                Language.getInstance().getPlayerLocale().put(playerUUID, Main.getBedWarsAPI().getPlayerLanguage(Bukkit.getPlayer(playerUUID)).getIso().toUpperCase());
            } else {
                Language.getInstance().getPlayerLocale().put(playerUUID, Database.getInstance().getString(playerUUID, "Language", "Profile"));
            }
        }

    }
}
