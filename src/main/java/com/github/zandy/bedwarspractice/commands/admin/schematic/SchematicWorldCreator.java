package com.github.zandy.bedwarspractice.commands.admin.schematic;

import com.github.zandy.bamboolib.exceptions.BambooException;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SchematicWorldCreator implements Listener {
    private static World world = null;

    public SchematicWorldCreator() {
        BambooUtils.registerEvent(this);
    }

    public static boolean isWorldGenerated() {
        return world != null;
    }

    public static boolean isInSchematicWorld(World world) {
        return isWorldGenerated() && SchematicWorldCreator.world == world;
    }

    public static void unload() {
        if (world != null) {
            for (Player player : world.getPlayers()) {
                player.teleport(Lobby.getInstance().get());
            }

            Bukkit.unloadWorld(world, false);
            world = null;
            File worldDirectory = new File("bedwars_practice_schematic_creator");

            try {
                if (worldDirectory.exists()) {
                    FileUtils.deleteDirectory(worldDirectory);
                }
            } catch (IOException e) {
                throw new BambooException(Arrays.asList("Could not remove 'bedwars_practice_schematic_creator' world.", "Please remove it manually once the server is closed!"));
            }
        }
    }

    public World getWorld() {
        if (world == null) {
            WorldCreator worldCreator = new WorldCreator("bedwars_practice_schematic_creator");
            worldCreator.type(WorldType.FLAT);
            worldCreator.generateStructures(false);
            if (BWPUtils.isLegacy()) {
                worldCreator.generatorSettings("2;0;1");
            }

            world = worldCreator.createWorld();
            world.setStorm(false);
            world.setWeatherDuration(0);
            world.setThunderDuration(0);
            world.setStorm(false);
            world.setThundering(false);
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setTime(1000L);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setTime(8000L);
            world.setAmbientSpawnLimit(0);
            world.setMonsterSpawnLimit(0);
            world.setWaterAnimalSpawnLimit(0);
            world.setAnimalSpawnLimit(0);
        }
        return world;
    }

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
}
