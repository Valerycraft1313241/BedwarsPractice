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

      Arrays.stream(Stats.StatsType.values()).forEach(statsType ->
              PlaceholderManager.getInstance().addPlaceholder(new Placeholder("stats_" + statsType.name().toLowerCase()) {
                 @Override
                 public String request(Player player) {
                    return String.valueOf(PlayerStats.get(player.getUniqueId()).get(statsType));
                 }
              })
      );

      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("server_date") {
         @Override
         public String request(Player player) {
            Calendar calendar = GameEngine.getInstance().getCalendar();
            return "&7" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.YEAR);
         }
      });

      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing") {
         @Override
         public String request(Player player) {
            return String.valueOf(GameEngine.getInstance().getPracticeTypeMap().size());
         }
      });

      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_bridging") {
         @Override
         public String request(Player player) {
            return String.valueOf(BridgingInfo.getBridgingInfoMap().size());
         }
      });

      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_mlg") {
         @Override
         public String request(Player player) {
            return String.valueOf(MLGInfo.getMlgInfoMap().size());
         }
      });

      PlaceholderManager.getInstance().addPlaceholder(new Placeholder("playing_fireballtntjumping") {
         @Override
         public String request(Player player) {
            return String.valueOf(FireballTNTJumpingInfo.getFireballTntJumpingMap().size());
         }
      });
   }
}
