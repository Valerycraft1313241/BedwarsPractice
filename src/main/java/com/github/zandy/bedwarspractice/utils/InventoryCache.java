package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.BambooLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.UUID;

public class InventoryCache {
    private static InventoryCache instance = null;
    private final HashMap<UUID, ItemStack[]> inventoryContents = new HashMap<>();
    private final HashMap<UUID, ItemStack[]> armorContents = new HashMap<>();

    public static boolean isInstantiated() {
        return instance != null;
    }

    public static InventoryCache getInstance() {
        if (instance == null) {
            instance = new InventoryCache();
        }

        return instance;
    }

    public void add(Player player) {
        this.inventoryContents.put(player.getUniqueId(), player.getInventory().getContents());
        this.armorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
    }

    public void restore(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            if (this.inventoryContents.containsKey(player.getUniqueId())) {
                playerInventory.setContents(this.inventoryContents.get(player.getUniqueId()));
            }

            if (this.armorContents.containsKey(player.getUniqueId())) {
                playerInventory.setArmorContents(this.armorContents.get(player.getUniqueId()));
            }

            player.updateInventory();
        }, 5L);
    }

    public void remove(Player player) {
        this.inventoryContents.remove(player.getUniqueId());
        this.armorContents.remove(player.getUniqueId());
    }
}
