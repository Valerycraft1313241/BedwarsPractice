package com.github.zandy.bedwarspractice.api;

import com.github.zandy.bedwarspractice.api.utils.creator.PracticeCreator;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;
import com.github.zandy.bedwarspractice.features.stats.PlayerStats;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BedWarsPracticeAPI {
   private static BedWarsPracticeAPI instance = null;

   private BedWarsPracticeAPI() {
   }

   public double getStatistic(UUID var1, Stats.StatsType var2) {
      return PlayerStats.get(var1).get(var2);
   }

   public String getISO(UUID var1) {
      return Language.getInstance().getPlayerLocale().get(var1);
   }

   public double getAPIVersion() {
      return 1.3D;
   }

   public PracticeCreator joinPractice(@NotNull Player var1, @NotNull GameEngine.PracticeType var2) {

       switch(var2) {
      case BRIDGING:
         return BridgingMode.getInstance().create(var1);
      case MLG:
         return MLGMode.getInstance().create(var1);
      case FIREBALL_TNT_JUMPING:
         return FireballTNTJumpingMode.getInstance().create(var1);
      default:
         return null;
      }
   }

   public static BedWarsPracticeAPI getInstance() {
      if (instance == null) {
         instance = new BedWarsPracticeAPI();
      }

      return instance;
   }

}
