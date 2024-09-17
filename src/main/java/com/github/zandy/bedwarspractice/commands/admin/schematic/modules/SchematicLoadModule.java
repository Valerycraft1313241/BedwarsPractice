package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.support.BWPVector;
import com.github.zandy.bedwarspractice.support.WESupport;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

public class SchematicLoadModule {
    private static SchematicLoadModule instance = null;

    private SchematicLoadModule() {
    }

    public static SchematicLoadModule getInstance() {
        if (instance == null) {
            instance = new SchematicLoadModule();
        }

        return instance;
    }

    public void execute(Player player, String schematicName) {
        File schematicFile = new File("plugins/BedWarsPractice/Schematics/", schematicName + BWPUtils.getExtension());
        player.sendMessage(" ");
        player.sendMessage(" ");
        if (!schematicFile.exists()) {
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_FILE_NOT_FOUND.getString(player.getUniqueId()).replace("[schemName]", schematicName));
            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
        } else {
            Location playerLocation = player.getLocation();
            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_LOADING.getString(player.getUniqueId()).replace("[schemName]", schematicName));
            World world = WESupport.getWEWorld(player.getWorld());
            double[] coordinates = (new BWPVector(playerLocation)).toArray();
            if (BWPUtils.isLegacy()) {
                BWPLegacyAdapter.getInstance().pasteSchematic(schematicName, world, coordinates);
            }
            Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_LOADED_AND_PASTED.getString(player.getUniqueId()));
        }
    }
}
