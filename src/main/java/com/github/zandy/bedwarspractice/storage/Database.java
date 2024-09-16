package com.github.zandy.bedwarspractice.storage;

import com.github.zandy.bamboolib.database.utils.Column;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import com.github.zandy.bedwarspractice.files.Settings;

import java.util.ArrayList;

public class Database {
    public Database() {
        com.github.zandy.bamboolib.database.Database.init();
        ArrayList<Column> columns = new ArrayList<>();
        columns.add((new Column("UUID")).setType(Column.ColumnType.VARCHAR).setLength(40).setNotNull().setPrimaryKey());
        columns.add((new Column("Player")).setType(Column.ColumnType.VARCHAR).setLength(25).setNotNull());
        columns.add((new Column("Language")).setType(Column.ColumnType.VARCHAR).setLength(2).setDefault(Settings.SettingsEnum.DEFAULT_LANGUAGE.getString().replace("English", "EN")));
        com.github.zandy.bamboolib.database.Database.getInstance().createTable("Profile", columns);
        Stats.createTable();
    }
}
