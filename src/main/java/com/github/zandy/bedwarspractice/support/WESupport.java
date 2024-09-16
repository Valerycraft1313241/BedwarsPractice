package com.github.zandy.bedwarspractice.support;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.exceptions.BambooException;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WESupport {
   private static WESupport instance = null;
   private static final HashMap<World, com.sk89q.worldedit.world.World> worldMap = new HashMap<>();
   private final String pluginName = BambooUtils.isPluginEnabled("FastAsyncWorldEdit") ? "FastAsyncWorldEdit" : "WorldEdit";

   public static com.sk89q.worldedit.world.World getWEWorld(World var0) {
      com.sk89q.worldedit.world.World var1 = worldMap.getOrDefault(var0, new BukkitWorld(var0));
      if (!worldMap.containsKey(var0)) {
         worldMap.put(var0, var1);
      }

      return var1;
   }

   public void saveSchematic(Player var1, String var2) {
      var1.sendMessage(" ");
      var1.sendMessage(" ");
      var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
      var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_STARTED.getString(var1.getUniqueId()));
      var1.performCommand("/copy");
      JavaPlugin var3 = BambooLib.getPluginInstance();
      Bukkit.getScheduler().runTaskLater(var3, () -> {
         var1.performCommand("/schem save " + var2);
         Bukkit.getScheduler().runTaskLater(var3, () -> {
            try {
               File var3x = new File("plugins/" + BWPUtils.getDirectory() + "/schematics/", var2 + BWPUtils.getExtension());
               FileUtils.copyFile(var3x, new File("plugins/BedWarsPractice/Schematics/", var2 + BWPUtils.getExtension()));
               Bukkit.getScheduler().runTaskLater(var3, () -> FileUtils.deleteQuietly(var3x), 40L);
            } catch (Exception var4) {
               throw new BambooException(Arrays.asList("The plugin couldn't copy the schematic '" + var2 + "' from " + this.pluginName + "'s folder.", "Please, copy it manually from:", "plugins/" + this.pluginName + "/schematics", "to:", "plugins/BedWarsPractice/Schematics/"));
            }
         }, 50L);
      }, 30L);
   }

   public static WESupport getInstance() {
      if (instance == null) {
         instance = new WESupport();
      }

      return instance;
   }
}
