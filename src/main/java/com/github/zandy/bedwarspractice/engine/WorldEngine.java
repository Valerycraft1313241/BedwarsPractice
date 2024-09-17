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
    private World practiceWorld;

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
            this.practiceWorld = SWMSupport.getInstance().generateWorld();
        } else {
            WorldCreator worldCreator = new WorldCreator("bedwars_practice");
            worldCreator.type(WorldType.FLAT);
            worldCreator.generateStructures(false);
            if (BWPUtils.isLegacy()) {
                worldCreator.generatorSettings("2;0;1");
            }

            this.practiceWorld = worldCreator.createWorld();
        }

        this.practiceWorld.setWeatherDuration(0);
        this.practiceWorld.setThunderDuration(0);
        this.practiceWorld.setStorm(false);
        this.practiceWorld.setThundering(false);
        this.practiceWorld.setDifficulty(Difficulty.PEACEFUL);
        this.practiceWorld.setTime(1000L);
        this.practiceWorld.setAmbientSpawnLimit(0);
        this.practiceWorld.setMonsterSpawnLimit(0);
        this.practiceWorld.setWaterAnimalSpawnLimit(0);
        this.practiceWorld.setAnimalSpawnLimit(0);
        this.practiceWorld.getWorldBorder().setSize(2.147483647E9D);
        boolean isTimeStatic = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_STATIC.getBoolean();
        this.practiceWorld.setGameRuleValue("doDaylightCycle", String.valueOf(!isTimeStatic));
        if (isTimeStatic) {
            String timeType = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_TYPE.getString().toUpperCase();
            byte timeTypeByte = -1;
            switch (timeType.hashCode()) {
                case 67452:
                    if (timeType.equals("DAY")) {
                        timeTypeByte = 0;
                    }
                    break;
                case 74279928:
                    if (timeType.equals("NIGHT")) {
                        timeTypeByte = 1;
                    }
            }

            long time;
            switch (timeTypeByte) {
                case 0:
                    time = 8000L;
                    break;
                case 1:
                    time = 16000L;
                    break;
                default:
                    time = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_PRECISE.getInt();
            }

            this.practiceWorld.setTime(time);
        }
    }

    public void unload() {
        Bukkit.unloadWorld(this.practiceWorld, false);
        this.removeWorld();
    }

    public World getPracticeWorld() {
        return this.practiceWorld;
    }

    public com.sk89q.worldedit.world.World getPracticeWEWorld() {
        return WESupport.getWEWorld(this.practiceWorld);
    }

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    private void removeWorld() {
        if (BambooUtils.isPluginEnabled("SlimeWorldManager")) {
            SWMSupport.getInstance().removeWorld();
        } else {
            File worldDirectory = new File("bedwars_practice");

            try {
                if (worldDirectory.exists()) {
                    FileUtils.deleteDirectory(worldDirectory);
                }
            } catch (IOException e) {
                throw new BambooErrorException(e, this.getClass(), Collections.singletonList("Cannot delete 'bedwars_practice' world."));
            }
        }
    }
}
