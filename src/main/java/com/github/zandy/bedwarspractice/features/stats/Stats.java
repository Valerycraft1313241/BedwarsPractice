package com.github.zandy.bedwarspractice.features.stats;

import com.github.zandy.bamboolib.database.Database;
import com.github.zandy.bamboolib.database.utils.BambooResultSet;
import com.github.zandy.bamboolib.database.utils.Column;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Stats {
   private static final String tableName = "Statistics";
   private static Stats instance = null;

   public void addPlayer(UUID var1) {
      HashMap<StatsType, Double> var2 = new HashMap<>();
      BambooResultSet var3 = Database.getInstance().getResultSet(var1, tableName);
      Arrays.stream(Stats.StatsType.values()).forEach((var2x) -> {
         double var3x;
         try {
            var3x = var3.getDouble(var2x.name());
         } catch (Exception var6) {
            var3x = 0.0D;
         }

         var2.put(var2x, var3x);
      });
      var3.close();
      new PlayerStats(var1, var2);
   }

   public static void createTable() {
      ArrayList<Column> var0 = new ArrayList<>();
      var0.add((new Column("Player")).setType(Column.ColumnType.VARCHAR).setLength(25));
      var0.add((new Column("UUID")).setType(Column.ColumnType.VARCHAR).setLength(50).setPrimaryKey());
      Arrays.stream(Stats.StatsType.values()).forEach((var1) -> var0.add((new Column(var1.name())).setType(Column.ColumnType.DOUBLE).setDefault(0)));
      Database.getInstance().createTable("Statistics", var0);
   }

   public static Stats getInstance() {
      if (instance == null) {
         instance = new Stats();
      }

      return instance;
   }

   public static String getTableName() {
      return "Statistics";
   }

   public enum StatsType {
      BRIDGING_BLOCKS_30_NONE_STRAIGHT,
      BRIDGING_BLOCKS_50_NONE_STRAIGHT,
      BRIDGING_BLOCKS_100_NONE_STRAIGHT,
      BRIDGING_BLOCKS_30_SLIGHT_STRAIGHT,
      BRIDGING_BLOCKS_50_SLIGHT_STRAIGHT,
      BRIDGING_BLOCKS_100_SLIGHT_STRAIGHT,
      BRIDGING_BLOCKS_30_STAIRCASE_STRAIGHT,
      BRIDGING_BLOCKS_50_STAIRCASE_STRAIGHT,
      BRIDGING_BLOCKS_100_STAIRCASE_STRAIGHT,
      BRIDGING_BLOCKS_30_NONE_DIAGONAL,
      BRIDGING_BLOCKS_50_NONE_DIAGONAL,
      BRIDGING_BLOCKS_100_NONE_DIAGONAL,
      BRIDGING_BLOCKS_30_SLIGHT_DIAGONAL,
      BRIDGING_BLOCKS_50_SLIGHT_DIAGONAL,
      BRIDGING_BLOCKS_100_SLIGHT_DIAGONAL,
      BRIDGING_BLOCKS_30_STAIRCASE_DIAGONAL,
      BRIDGING_BLOCKS_50_STAIRCASE_DIAGONAL,
      BRIDGING_BLOCKS_100_STAIRCASE_DIAGONAL,
      BRIDGING_BLOCKS_INFINITE,
      FIREBALL_TNT_JUMPING_LONGEST_JUMP_FIREBALL,
      FIREBALL_TNT_JUMPING_LONGEST_JUMP_TNT

   }
}
