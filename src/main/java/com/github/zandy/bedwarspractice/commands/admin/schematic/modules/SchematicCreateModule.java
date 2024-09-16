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

    public void execute(Player var1) {
        World var2 = (new SchematicWorldCreator()).getWorld();
        Location var3 = var2.getSpawnLocation();
        int var4 = var3.getBlockX();
        int var5 = var3.getBlockY();
        int var6 = var3.getBlockZ();

        for (int var7 = var4 - 1; var7 <= var4 + 1; ++var7) {
            for (int var8 = var6 - 1; var8 <= var6 + 1; ++var8) {
                var2.getBlockAt(var7, var5, var8).setType(Material.BEDROCK);
            }
        }

        var1.teleport(var3.clone().add(0.5D, 1.0D, 0.5D));
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_CREATE_INFO_TELEPORTED.getString(var1.getUniqueId()));

            for (String var2x : Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_CREATE_INFO_TUTORIAL.getStringList(var1.getUniqueId())) {
                var1.sendMessage(var2x.replace("[spigotLink]", "https://bit.ly/3f4lyvF").replace("[youtubeLink]", "https://bit.ly/3BSNJXn"));
            }

            Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
        }, 10L);
    }
}
