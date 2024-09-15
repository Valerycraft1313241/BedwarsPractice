package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.BambooLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryCache {
   private static final long RESTORE_DELAY_TICKS = 5L;
   private static final InventoryCache INSTANCE = new InventoryCache();
   private final ConcurrentHashMap<UUID, ItemStack[]> contents = new ConcurrentHashMap<>();
   private final ConcurrentHashMap<UUID, ItemStack[]> armorContents = new ConcurrentHashMap<>();

   private InventoryCache() {
      // Private constructor to prevent instantiation
   }

   public static InventoryCache getInstance() {
      return INSTANCE;
   }

   public void add(Player player) {
      contents.put(player.getUniqueId(), player.getInventory().getContents());
      armorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
   }

   public void restore(Player player) {
      PlayerInventory inventory = player.getInventory();
      Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
         if (contents.containsKey(player.getUniqueId())) {
            inventory.setContents(contents.get(player.getUniqueId()));
         }

         if (armorContents.containsKey(player.getUniqueId())) {
            inventory.setArmorContents(armorContents.get(player.getUniqueId()));
         }

         player.updateInventory();
      }, RESTORE_DELAY_TICKS);
   }

   public void remove(Player player) {
      contents.remove(player.getUniqueId());
      armorContents.remove(player.getUniqueId());
   }

   public static boolean isInstantiated() {
      return INSTANCE != null;
   }
}
