package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchematicListModule {
    private static SchematicListModule instance = null;
    private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(true));

    private SchematicListModule() {
    }

    public static SchematicListModule getInstance() {
        if (instance == null) {
            instance = new SchematicListModule();
        }

        return instance;
    }

    public void execute(Player player) {
        ArrayList<String> missingSchematics = new ArrayList<>(this.requiredSchematics);
        File schematicsFolder = new File("plugins/BedWarsPractice/Schematics");
        if (schematicsFolder.exists()) {
            Arrays.stream(schematicsFolder.listFiles()).forEach((file) -> missingSchematics.remove(file.getName().replace(BWPUtils.getExtension(), "")));
        }

        player.sendMessage(" ");
        player.sendMessage(" ");
        if (missingSchematics.isEmpty()) {
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_ALL_CREATED.getString(player.getUniqueId()));
            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
        } else {
            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_REQUIRED_SCHEMATICS.getString(player.getUniqueId()));
            String enumerationMessage = Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_ENUMERATION.getString(player.getUniqueId());
            missingSchematics.forEach((schematicName) -> player.sendMessage(enumerationMessage.replace("[schemName]", schematicName)));
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_INFO.getString(player.getUniqueId()));
            Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
        }
    }
}
