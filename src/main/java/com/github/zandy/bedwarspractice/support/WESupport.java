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
import java.util.HashMap;

public class WESupport {
    private static final HashMap<World, com.sk89q.worldedit.world.World> worldEditWorldMap = new HashMap<>();
    private static WESupport instance = null;
    private final String pluginName = BambooUtils.isPluginEnabled("FastAsyncWorldEdit") ? "FastAsyncWorldEdit" : "WorldEdit";

    public static com.sk89q.worldedit.world.World getWEWorld(World bukkitWorld) {
        com.sk89q.worldedit.world.World worldEditWorld = worldEditWorldMap.getOrDefault(bukkitWorld, new BukkitWorld(bukkitWorld));
        if (!worldEditWorldMap.containsKey(bukkitWorld)) {
            worldEditWorldMap.put(bukkitWorld, worldEditWorld);
        }

        return worldEditWorld;
    }

    public static WESupport getInstance() {
        if (instance == null) {
            instance = new WESupport();
        }

        return instance;
    }

    public void saveSchematic(Player player, String schematicName) {
        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
        player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_STARTED.getString(player.getUniqueId()));
        player.performCommand("/copy");
        JavaPlugin pluginInstance = BambooLib.getPluginInstance();
        Bukkit.getScheduler().runTaskLater(pluginInstance, () -> {
            player.performCommand("/schem save " + schematicName);
            Bukkit.getScheduler().runTaskLater(pluginInstance, () -> {
                try {
                    File schematicFile = new File("plugins/" + BWPUtils.getDirectory() + "/schematics/", schematicName + BWPUtils.getExtension());
                    FileUtils.copyFile(schematicFile, new File("plugins/BedWarsPractice/Schematics/", schematicName + BWPUtils.getExtension()));
                    Bukkit.getScheduler().runTaskLater(pluginInstance, () -> FileUtils.deleteQuietly(schematicFile), 40L);
                } catch (Exception e) {
                    throw new BambooException(Arrays.asList("The plugin couldn't copy the schematic '" + schematicName + "' from " + this.pluginName + "'s folder.", "Please, copy it manually from:", "plugins/" + this.pluginName + "/schematics", "to:", "plugins/BedWarsPractice/Schematics/"));
                }
            }, 50L);
        }, 30L);
    }
}
