package com.github.zandy.bedwarspractice.features;

import com.github.zandy.bamboolib.placeholder.PlaceholderManager;
import com.github.zandy.bamboolib.placeholder.utils.Placeholder;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.features.stats.PlayerStats;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Calendar;

public class Placeholders {
    public Placeholders() {
        PlaceholderManager.setIdentifier("bwp");
        Arrays.stream(Stats.StatsType.values()).forEach((statsType) -> PlaceholderManager.getInstance().addPlaceholder(new Placeholder("stats_" + statsType.name().toLowerCase()) {
            public String request(Player player) {
                return String.valueOf(PlayerStats.get(player.getUniqueId()).get(statsType));
            }
        }));
        PlaceholderManager.getInstance().addPlaceholder(new Placeholder("server_date") {
            public String request(Player player) {
                GameEngine gameEngine = GameEngine.getInstance();
                return "&7" + (gameEngine.getCalendar().get(Calendar.MONTH) + 1) + "/" + gameEngine.getCalendar().get(Calendar.DATE) + "/" + gameEngine.getCalendar().get(Calendar.YEAR);
            }
        });
        PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing") {
            public String request(Player player) {
                return String.valueOf(GameEngine.getInstance().getPracticeTypeMap().size());
            }
        });
        PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_bridging") {
            public String request(Player player) {
                return String.valueOf(BridgingInfo.getBridgingInfoMap().size());
            }
        });
        PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_mlg") {
            public String request(Player player) {
                return String.valueOf(MLGInfo.getMlgInfoMap().size());
            }
        });
        PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_fireballtntjumping") {
            public String request(Player player) {
                return String.valueOf(FireballTNTJumpingInfo.getFireballTntJumpingMap().size());
            }
        });
    }
}
