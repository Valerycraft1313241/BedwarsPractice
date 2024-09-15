package com.github.zandy.bedwarspractice.commands.admin.schematic;

import com.github.zandy.bamboolib.exceptions.BambooException;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class SchematicWorldCreator implements Listener {
   private static World world = null;

   public SchematicWorldCreator() {
      BambooUtils.registerEvent(this);
   }

   public static boolean isWorldGenerated() {
      return world != null;
   }

   public static boolean isInSchematicWorld(World var0) {
      return isWorldGenerated() && world == var0;
   }

   public static void unload() {
      if (world != null) {

          for (Player var1 : world.getPlayers()) {
              var1.teleport(Lobby.getInstance().get());
          }

         Bukkit.unloadWorld(world, false);
         world = null;
         File var3 = new File("bedwars_practice_schematic_creator");

         try {
            if (var3.exists()) {
               FileUtils.deleteDirectory(var3);
            }

         } catch (IOException var2) {
            throw new BambooException(Arrays.asList("Could not remove 'bedwars_practice_schematic_creator' world.", "Please remove it manually once the server is closed!"));
         }
      }
   }

   public World getWorld() {
       if (world == null) {
           WorldCreator var1 = new WorldCreator("bedwars_practice_schematic_creator");
           var1.type(WorldType.FLAT);
           var1.generateStructures(false);
           if (BWPUtils.isLegacy()) {
               var1.generatorSettings("2;0;1");
           }

           world = var1.createWorld();
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
   private void onWeatherChange(WeatherChangeEvent var1) {
      if (var1.toWeatherState()) {
         var1.setCancelled(true);
      }

   }
}
