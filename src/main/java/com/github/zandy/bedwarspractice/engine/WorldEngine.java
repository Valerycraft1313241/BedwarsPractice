package com.github.zandy.bedwarspractice.engine;

import com.github.zandy.bamboolib.exceptions.BambooErrorException;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.support.SWMSupport;
import com.github.zandy.bedwarspractice.support.WESupport;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldEngine implements Listener {
   private static volatile WorldEngine instance = null;
   private World world;

   private WorldEngine() {
   }

   public static WorldEngine getInstance() {
      if (instance == null) {
         synchronized (WorldEngine.class) {
            if (instance == null) {
               instance = new WorldEngine();
            }
         }
      }
      return instance;
   }

   public void init() {
      BambooUtils.registerEvent(this);
      this.removeWorld();
      if (BambooUtils.isPluginEnabled("SlimeWorldManager")) {
         this.world = SWMSupport.getInstance().generateWorld();
      } else {
         WorldCreator worldCreator = new WorldCreator("bedwars_practice");
         worldCreator.type(WorldType.FLAT);
         worldCreator.generateStructures(false);
         if (BWPUtils.isLegacy()) {
            worldCreator.generatorSettings("2;0;1");
         }
         this.world = worldCreator.createWorld();
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
      boolean isTimeStatic = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_STATIC.getBoolean();
      this.world.setGameRuleValue("doDaylightCycle", String.valueOf(!isTimeStatic));
      if (isTimeStatic) {
         String timeType = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_TYPE.getString().toUpperCase();
         long time;
         switch (timeType) {
            case "DAY":
               time = 8000L;
               break;
            case "NIGHT":
               time = 16000L;
               break;
            default:
               time = PracticeSettings.GameSettingsEnum.SETTINGS_TIME_PRECISE.getInt();
         }
         this.world.setTime(time);
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
   private void onWeatherChange(WeatherChangeEvent event) {
      if (event.toWeatherState()) {
         event.setCancelled(true);
      }
   }

   private void removeWorld() {
      if (BambooUtils.isPluginEnabled("SlimeWorldManager")) {
         SWMSupport.getInstance().removeWorld();
      } else {
         File worldDir = new File("bedwars_practice");
         try {
            if (worldDir.exists()) {
               FileUtils.deleteDirectory(worldDir);
            }
         } catch (IOException e) {
            throw new BambooErrorException(e, this.getClass(), Collections.singletonList("Cannot delete 'bedwars_practice' world."));
         }
      }
   }
}
