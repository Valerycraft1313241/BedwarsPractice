package com.github.zandy.bedwarspractice.support;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.exceptions.BambooException;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class WESupport {
   private static final long SAVE_DELAY_TICKS = 30L;
   private static final long COPY_DELAY_TICKS = 50L;
   private static final long DELETE_DELAY_TICKS = 40L;
   private static final ConcurrentHashMap<World, com.sk89q.worldedit.world.World> worldMap = new ConcurrentHashMap<>();
   private static final WESupport INSTANCE = new WESupport();
   private final String pluginName = BambooUtils.isPluginEnabled("FastAsyncWorldEdit") ? "FastAsyncWorldEdit" : "WorldEdit";

   private WESupport() {
      // Private constructor to prevent instantiation
   }

   public static WESupport getInstance() {
      return INSTANCE;
   }

   public static com.sk89q.worldedit.world.World getWEWorld(World world) {
      return worldMap.computeIfAbsent(world, BukkitWorld::new);
   }

   public void saveSchematic(Player player, String schematicName) {
      player.sendMessage(" ");
      player.sendMessage(" ");
      player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
      player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_STARTED.getString(player.getUniqueId()));
      player.performCommand("/copy");
      JavaPlugin plugin = BambooLib.getPluginInstance();
      Bukkit.getScheduler().runTaskLater(plugin, () -> {
         player.performCommand("/schem save " + schematicName);
         Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
               File sourceFile = new File("plugins/" + BWPUtils.getDirectory() + "/schematics/", schematicName + BWPUtils.getExtension());
               File destinationFile = new File("plugins/BedWarsPractice/Schematics/", schematicName + BWPUtils.getExtension());
               FileUtils.copyFile(sourceFile, destinationFile);
               Bukkit.getScheduler().runTaskLater(plugin, () -> FileUtils.deleteQuietly(sourceFile), DELETE_DELAY_TICKS);
            } catch (Exception e) {
               throw new BambooException(Arrays.asList(
                       "The plugin couldn't copy the schematic '" + schematicName + "' from " + pluginName + "'s folder.",
                       "Please, copy it manually from:",
                       "plugins/" + pluginName + "/schematics",
                       "to:",
                       "plugins/BedWarsPractice/Schematics/"
               ));
            }
         }, COPY_DELAY_TICKS);
      }, SAVE_DELAY_TICKS);
   }
}
