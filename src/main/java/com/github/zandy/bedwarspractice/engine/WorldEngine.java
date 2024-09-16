package com.github.zandy.bedwarspractice.engine;

import com.github.zandy.bamboolib.exceptions.BambooErrorException;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.support.SWMSupport;
import com.github.zandy.bedwarspractice.support.WESupport;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class WorldEngine implements Listener {
    private static WorldEngine instance = null;
    private World world;

    private WorldEngine() {
    }

    public static WorldEngine getInstance() {
        if (instance == null) {
            instance = new WorldEngine();
        }

        return instance;
    }

    public void init() {
        BambooUtils.registerEvent(this);
        this.removeWorld();
        if (BambooUtils.isPluginEnabled("SlimeWorldManager")) {
            this.world = SWMSupport.getInstance().generateWorld();
        } else {
            WorldCreator var1 = new WorldCreator("bedwars_practice");
            var1.type(WorldType.FLAT);
            var1.generateStructures(false);
            if (BWPUtils.isLegacy()) {
                var1.generatorSettings("2;0;1");
            }

            this.world = var1.createWorld();
        }

        this.world.setWeatherDuration(0);
        this.world.setThunderDuration(0);
        this.world.setStorm(false);
        this.world.setThundering(false);
        this.world.setDifficulty(Difficulty.PEACEFUL);
        this.world.setTime(1000L);
        this.world.setAmbientSpawnLimit(0);
        this.world.setMonsterSpawnLimit(0);
        this.world.setWaterAnimalSpawnLimit(0);
        this.world.setAnimalSpawnLimit(0);
        this.world.getWorldBorder().setSize(2.147483647E9D);
        boolean var6 = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_STATIC.getBoolean();
        this.world.setGameRuleValue("doDaylightCycle", String.valueOf(!var6));
        if (var6) {
            String var4 = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_TYPE.getString().toUpperCase();
            byte var5 = -1;
            switch (var4.hashCode()) {
                case 67452:
                    if (var4.equals("DAY")) {
                        var5 = 0;
                    }
                    break;
                case 74279928:
                    if (var4.equals("NIGHT")) {
                        var5 = 1;
                    }
            }

            long var2;
            switch (var5) {
                case 0:
                    var2 = 8000L;
                    break;
                case 1:
                    var2 = 16000L;
                    break;
                default:
                    var2 = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_PRECISE.getInt();
            }

            this.world.setTime(var2);
        }
    }

    public void unload() {
        Bukkit.unloadWorld(this.world, false);
        this.removeWorld();
    }

    public World getPracticeWorld() {
        return this.world;
    }

    public com.sk89q.worldedit.world.World getPracticeWEWorld() {
        return WESupport.getWEWorld(this.world);
    }

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent var1) {
        if (var1.toWeatherState()) {
            var1.setCancelled(true);
        }

    }

    private void removeWorld() {
        if (BambooUtils.isPluginEnabled("SlimeWorldManager")) {
            SWMSupport.getInstance().removeWorld();
        } else {
            File var1 = new File("bedwars_practice");

            try {
                if (var1.exists()) {
                    FileUtils.deleteDirectory(var1);
                }
            } catch (IOException var3) {
                throw new BambooErrorException(var3, this.getClass(), Collections.singletonList("Cannot delete 'bedwars_practice' world."));
            }
        }

    }
}
