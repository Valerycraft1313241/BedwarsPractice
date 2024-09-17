package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicWorldCreator;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SchematicCreateModule implements Listener {
    private static SchematicCreateModule instance = null;

    private SchematicCreateModule() {
        BambooUtils.registerEvent(this);
    }

    public static SchematicCreateModule getInstance() {
        if (instance == null) {
            instance = new SchematicCreateModule();
        }

        return instance;
    }

    public void execute(Player player) {
        World world = (new SchematicWorldCreator()).getWorld();
        Location spawnLocation = world.getSpawnLocation();
        int blockX = spawnLocation.getBlockX();
        int blockY = spawnLocation.getBlockY();
        int blockZ = spawnLocation.getBlockZ();

        for (int x = blockX - 1; x <= blockX + 1; ++x) {
            for (int z = blockZ - 1; z <= blockZ + 1; ++z) {
                world.getBlockAt(x, blockY, z).setType(Material.BEDROCK);
            }
        }

        player.teleport(spawnLocation.clone().add(0.5D, 1.0D, 0.5D));
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            player.sendMessage(" ");
            player.sendMessage(" ");
            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_CREATE_INFO_TELEPORTED.getString(player.getUniqueId()));

            for (String message : Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_CREATE_INFO_TUTORIAL.getStringList(player.getUniqueId())) {
                player.sendMessage(message.replace("[spigotLink]", "https://bit.ly/3f4lyvF").replace("[youtubeLink]", "https://bit.ly/3BSNJXn"));
            }

            Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
        }, 10L);
    }
}
