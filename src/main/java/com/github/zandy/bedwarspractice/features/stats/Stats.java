package com.github.zandy.bedwarspractice.features.stats;

import com.github.zandy.bamboolib.database.Database;
import com.github.zandy.bamboolib.database.utils.BambooResultSet;
import com.github.zandy.bamboolib.database.utils.Column;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Stats {
   private static final String TABLE_NAME = "Statistics";
   private static Stats instance = null;

   private Stats() {
      // Private constructor to prevent instantiation
   }

   public static synchronized Stats getInstance() {
      if (instance == null) {
         instance = new Stats();
      }
      return instance;
   }

   public void addPlayer(UUID playerUUID) {
      HashMap<StatsType, Double> statsMap = new HashMap<>();
      BambooResultSet resultSet = null;
      try {
         resultSet = Database.getInstance().getResultSet(playerUUID, TABLE_NAME);
         BambooResultSet finalResultSet = resultSet;
         Arrays.stream(StatsType.values()).forEach(statsType -> {
            double value;
            try {
               value = finalResultSet.getDouble(statsType.name());
            } catch (Exception e) {
               value = 0.0;
            }
            statsMap.put(statsType, value);
         });
      } finally {
         if (resultSet != null) {
            resultSet.close();
         }
      }
      new PlayerStats(playerUUID, statsMap);
   }

   public static void createTable() {
      ArrayList<Column> columns = new ArrayList<>();
      columns.add(new Column("Player").setType(Column.ColumnType.VARCHAR).setLength(25));
      columns.add(new Column("UUID").setType(Column.ColumnType.VARCHAR).setLength(50).setPrimaryKey());
      Arrays.stream(StatsType.values()).forEach(statsType ->
              columns.add(new Column(statsType.name()).setType(Column.ColumnType.DOUBLE).setDefault(0))
      );
      Database.getInstance().createTable(TABLE_NAME, columns);
   }

   public static String getTableName() {
      return TABLE_NAME;
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
