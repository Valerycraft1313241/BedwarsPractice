package com.github.zandy.bedwarspractice.support;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SWMSupport {
   private static SWMSupport instance = null;
   private final SlimePlugin slimePlugin = (SlimePlugin)Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
   private final SlimeLoader loader;

   public SWMSupport() {
      this.loader = this.slimePlugin.getLoader("file");
   }

   public World generateWorld() {
      SlimePropertyMap var1 = new SlimePropertyMap();
      var1.setInt(SlimeProperties.SPAWN_X, 0);
      var1.setInt(SlimeProperties.SPAWN_Y, 100);
      var1.setInt(SlimeProperties.SPAWN_Z, 0);

      try {
         this.slimePlugin.generateWorld(this.slimePlugin.createEmptyWorld(this.loader, "bedwars_practice", true, var1));
      } catch (Exception ignored) {
      }

      return Bukkit.getWorld("bedwars_practice");
   }

   public void removeWorld() {
      try {
         this.loader.deleteWorld("bedwars_practice");
      } catch (Exception ignored) {
      }

   }

   public static SWMSupport getInstance() {
      if (instance == null) {
         instance = new SWMSupport();
      }

      return instance;
   }
}
