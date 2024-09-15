package com.github.zandy.bedwarspractice.support;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.logging.Logger;

public class SWMSupport {
   private static final Logger LOGGER = Logger.getLogger(SWMSupport.class.getName());
   private static final String WORLD_NAME = "bedwars_practice";
   private static final SWMSupport INSTANCE = new SWMSupport();
   private final SlimePlugin slimePlugin;
   private final SlimeLoader loader;

   private SWMSupport() {
      this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
      this.loader = this.slimePlugin.getLoader("file");
   }

   public static SWMSupport getInstance() {
      return INSTANCE;
   }

   public World generateWorld() {
      SlimePropertyMap properties = new SlimePropertyMap();
      properties.setInt(SlimeProperties.SPAWN_X, 0);
      properties.setInt(SlimeProperties.SPAWN_Y, 100);
      properties.setInt(SlimeProperties.SPAWN_Z, 0);

      try {
         this.slimePlugin.generateWorld(this.slimePlugin.createEmptyWorld(this.loader, WORLD_NAME, true, properties));
      } catch (Exception e) {
         LOGGER.severe("Failed to generate world: " + e.getMessage());
      }

      return Bukkit.getWorld(WORLD_NAME);
   }

   public void removeWorld() {
      try {
         this.loader.deleteWorld(WORLD_NAME);
      } catch (Exception e) {
         LOGGER.severe("Failed to delete world: " + e.getMessage());
      }
   }
}
