package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.BambooLib;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryCache {
   private static InventoryCache instance = null;
   private final HashMap<UUID, ItemStack[]> contents = new HashMap<>();
   private final HashMap<UUID, ItemStack[]> armorContents = new HashMap<>();

   public void add(Player var1) {
      this.contents.put(var1.getUniqueId(), var1.getInventory().getContents());
      this.armorContents.put(var1.getUniqueId(), var1.getInventory().getArmorContents());
   }

   public void restore(Player var1) {
      PlayerInventory var2 = var1.getInventory();
      Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
         if (this.contents.containsKey(var1.getUniqueId())) {
            var2.setContents(this.contents.get(var1.getUniqueId()));
         }

         if (this.armorContents.containsKey(var1.getUniqueId())) {
            var2.setArmorContents(this.armorContents.get(var1.getUniqueId()));
         }

         var1.updateInventory();
      }, 5L);
   }

   public void remove(Player var1) {
      this.contents.remove(var1.getUniqueId());
      this.armorContents.remove(var1.getUniqueId());
   }

   public static boolean isInstantiated() {
      return instance != null;
   }

   public static InventoryCache getInstance() {
      if (instance == null) {
         instance = new InventoryCache();
      }

      return instance;
   }
}
