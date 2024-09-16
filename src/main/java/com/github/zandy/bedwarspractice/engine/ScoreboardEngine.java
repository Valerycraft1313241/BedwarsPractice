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
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> (new HashMap<>(this.sidebarMap)).forEach((var0, var1) -> {
            if (GameEngine.getInstance().getPracticeTypeMap().containsKey(var0)) {
                var1.refresh();
            }

        }), 0L, Settings.SettingsEnum.SCOREBOARD_REFRESH_TICK.getInt());
    }

    public void sendSidebar(Player var1, GameEngine.PracticeType var2) {
        if (Main.getBedWarsAPI() != null) {
            Main.getBedWarsAPI().getScoreboardUtil().removePlayerScoreboard(var1);
        }

        UUID var3 = var1.getUniqueId();
        if (this.sidebarMap.containsKey(var3)) {
            this.sidebarMap.get(var3).destroy();
        }

        List<String> var4;
        HashMap<String, Callable<String>> var5;
        switch (var2) {
            case BRIDGING:
                BridgingMode var8 = BridgingMode.getInstance();
                var4 = var8.getScoreboardLines(var3);
                var5 = var8.getPlaceholders(var3);
                break;
            case MLG:
                MLGMode var7 = MLGMode.getInstance();
                var4 = var7.getScoreboardLines(var3);
                var5 = var7.getPlaceholders(var3);
                break;
            case FIREBALL_TNT_JUMPING:
                FireballTNTJumpingMode var6 = FireballTNTJumpingMode.getInstance();
                var4 = var6.getScoreboardLines(var3);
                var5 = var6.getPlaceholders(var3);
                break;
            default:
                return;
        }

        this.sidebarMap.put(var3, new Scoreboard(var1, Language.MessagesEnum.GAME_SCOREBOARD_TITLE.getString(var3), var4, var5));
    }

    @EventHandler
    private void onPracticeQuit(PracticeQuitEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (this.sidebarMap.containsKey(var2)) {
            Scoreboard var3 = this.sidebarMap.get(var2);
            var3.destroy();
            this.sidebarMap.remove(var2);
            if (Main.getBedWarsAPI() != null) {
                Main.getBedWarsAPI().getScoreboardUtil().givePlayerScoreboard(var1.getPlayer(), true);
            }


        }
    }

    public void unload(UUID var1) {
        if (this.sidebarMap.containsKey(var1)) {
            this.sidebarMap.get(var1).destroy();
        }

    }
}
