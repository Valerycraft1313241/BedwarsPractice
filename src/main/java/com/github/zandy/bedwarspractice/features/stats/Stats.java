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

    public static void createTable() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add((new Column("Player")).setType(Column.ColumnType.VARCHAR).setLength(25));
        columns.add((new Column("UUID")).setType(Column.ColumnType.VARCHAR).setLength(50).setPrimaryKey());
        Arrays.stream(Stats.StatsType.values()).forEach((statsType) -> columns.add((new Column(statsType.name())).setType(Column.ColumnType.DOUBLE).setDefault(0)));
        Database.getInstance().createTable("Statistics", columns);
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

    public void addPlayer(UUID playerUUID) {
        HashMap<StatsType, Double> statsMap = new HashMap<>();
        BambooResultSet resultSet = Database.getInstance().getResultSet(playerUUID, tableName);
        Arrays.stream(Stats.StatsType.values()).forEach((statsType) -> {
            double statValue;
            try {
                statValue = resultSet.getDouble(statsType.name());
            } catch (Exception e) {
                statValue = 0.0D;
            }

            statsMap.put(statsType, statValue);
        });
        resultSet.close();
        new PlayerStats(playerUUID, statsMap);
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
