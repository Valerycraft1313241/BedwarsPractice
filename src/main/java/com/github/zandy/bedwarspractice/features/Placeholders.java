package com.github.zandy.bedwarspractice.features;

import com.github.zandy.bamboolib.placeholder.PlaceholderManager;
import com.github.zandy.bamboolib.placeholder.utils.Placeholder;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.features.stats.PlayerStats;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import java.util.Arrays;
import java.util.Calendar;

import org.bukkit.entity.Player;

public class Placeholders {
   public Placeholders() {
      PlaceholderManager.setIdentifier("bwp");
      Arrays.stream(Stats.StatsType.values()).forEach((var1) -> PlaceholderManager.getInstance().addPlaceholder(new Placeholder("stats_" + var1.name().toLowerCase()) {
         public String request(Player var1x) {
            return String.valueOf(PlayerStats.get(var1x.getUniqueId()).get(var1));
         }
      }));
      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("server_date") {
         public String request(Player var1) {
            GameEngine var2 = GameEngine.getInstance();
            return "&7" + (var2.getCalendar().get(Calendar.MONTH) + 1) + "/" + var2.getCalendar().get(Calendar.DATE) + "/" + var2.getCalendar().get(Calendar.YEAR);
         }
      });
      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing") {
         public String request(Player var1) {
            return String.valueOf(GameEngine.getInstance().getPracticeTypeMap().size());
         }
      });
      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_bridging") {
         public String request(Player var1) {
            return String.valueOf(BridgingInfo.getBridgingInfoMap().size());
         }
      });
      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_mlg") {
         public String request(Player var1) {
            return String.valueOf(MLGInfo.getMlgInfoMap().size());
         }
      });
      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_fireballtntjumping") {
         public String request(Player var1) {
            return String.valueOf(FireballTNTJumpingInfo.getFireballTntJumpingMap().size());
         }
      });
   }
}
