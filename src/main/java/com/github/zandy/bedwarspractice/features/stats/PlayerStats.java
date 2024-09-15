package com.github.zandy.bedwarspractice.features.stats;

import com.github.zandy.bamboolib.database.Database;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStats {
   private static final HashMap<UUID, PlayerStats> playerStatsMap = new HashMap<>();
   private final UUID uuid;
   private final HashMap<Stats.StatsType, Double> stats;

   public PlayerStats(UUID var1, HashMap<Stats.StatsType, Double> var2) {
      this.stats = var2;
      this.uuid = var1;
      playerStatsMap.put(var1, this);
   }

   public static PlayerStats get(UUID var0) {
      return playerStatsMap.get(var0);
   }

   public double get(Stats.StatsType var1) {
      return this.stats.get(var1);
   }

   public void set(Stats.StatsType var1, double var2) {
      if (var1 != null) {
         double var4 = Double.parseDouble(String.format("%,.2f", var2).replace(",", "."));
         this.stats.put(var1, var4);
         Database.getInstance().setDouble(this.uuid, var4, var1.name(), Stats.getTableName());
      }
   }
}
