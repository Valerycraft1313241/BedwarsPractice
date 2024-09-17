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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ScoreboardEngine implements Listener {
    private static ScoreboardEngine instance = null;
    private final HashMap<UUID, Scoreboard> sidebarMap = new HashMap<>();

    private ScoreboardEngine() {
    }

    public static ScoreboardEngine getInstance() {
        if (instance == null) {
            instance = new ScoreboardEngine();
        }

        return instance;
    }

    public void init() {
        BambooUtils.registerEvent(this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> (new HashMap<>(this.sidebarMap)).forEach((playerUUID, scoreboard) -> {
            if (GameEngine.getInstance().getPracticeTypeMap().containsKey(playerUUID)) {
                scoreboard.refresh();
            }

        }), 0L, Settings.SettingsEnum.SCOREBOARD_REFRESH_TICK.getInt());
    }

    public void sendSidebar(Player player, GameEngine.PracticeType practiceType) {
        if (Main.getBedWarsAPI() != null) {
            Main.getBedWarsAPI().getScoreboardUtil().removePlayerScoreboard(player);
        }

        UUID playerUUID = player.getUniqueId();
        if (this.sidebarMap.containsKey(playerUUID)) {
            this.sidebarMap.get(playerUUID).destroy();
        }

        List<String> scoreboardLines;
        HashMap<String, Callable<String>> placeholders;
        switch (practiceType) {
            case BRIDGING:
                BridgingMode bridgingMode = BridgingMode.getInstance();
                scoreboardLines = bridgingMode.getScoreboardLines(playerUUID);
                placeholders = bridgingMode.getPlaceholders(playerUUID);
                break;
            case MLG:
                MLGMode mlgMode = MLGMode.getInstance();
                scoreboardLines = mlgMode.getScoreboardLines(playerUUID);
                placeholders = mlgMode.getPlaceholders(playerUUID);
                break;
            case FIREBALL_TNT_JUMPING:
                FireballTNTJumpingMode fireballTNTJumpingMode = FireballTNTJumpingMode.getInstance();
                scoreboardLines = fireballTNTJumpingMode.getScoreboardLines(playerUUID);
                placeholders = fireballTNTJumpingMode.getPlaceholders(playerUUID);
                break;
            default:
                return;
        }

        this.sidebarMap.put(playerUUID, new Scoreboard(player, Language.MessagesEnum.GAME_SCOREBOARD_TITLE.getString(playerUUID), scoreboardLines, placeholders));
    }

    @EventHandler
    private void onPracticeQuit(PracticeQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (this.sidebarMap.containsKey(playerUUID)) {
            Scoreboard scoreboard = this.sidebarMap.get(playerUUID);
            scoreboard.destroy();
            this.sidebarMap.remove(playerUUID);
            if (Main.getBedWarsAPI() != null) {
                Main.getBedWarsAPI().getScoreboardUtil().givePlayerScoreboard(event.getPlayer(), true);
            }
        }
    }

    public void unload(UUID playerUUID) {
        if (this.sidebarMap.containsKey(playerUUID)) {
            this.sidebarMap.get(playerUUID).destroy();
        }
    }
}
