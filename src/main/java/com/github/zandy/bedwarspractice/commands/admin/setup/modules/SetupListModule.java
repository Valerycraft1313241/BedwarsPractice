package com.github.zandy.bedwarspractice.commands.admin.setup.modules;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SetupListModule {
    private static SetupListModule instance = null;
    private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));

    private SetupListModule() {
    }

    public static SetupListModule getInstance() {
        if (instance == null) {
            instance = new SetupListModule();
        }

        return instance;
    }

    public void execute(Player player) {
        ArrayList<String> remainingSchematics = new ArrayList<>(this.requiredSchematics);
        remainingSchematics.remove("FIREBALL-TNT-JUMPING");
        File dataFolder = new File("plugins/BedWarsPractice/Data");
        if (dataFolder.exists()) {
            Arrays.stream(dataFolder.listFiles()).forEach((file) -> remainingSchematics.remove(file.getName().replace(".yml", "")));
        }

        player.sendMessage(" ");
        player.sendMessage(" ");
        UUID playerUUID = player.getUniqueId();
        if (remainingSchematics.isEmpty()) {
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_ALL_CREATED.getString(playerUUID));
            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
        } else {
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_REQUIRED_SETUPS.getString(playerUUID));
            String enumerationMessage = Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_ENUMERATION.getString(playerUUID);
            remainingSchematics.forEach((schematic) -> BambooUtils.sendTextComponent(player, enumerationMessage.replace("[practiceName]", schematic), "/bwpa setup set " + schematic, Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(playerUUID), Action.RUN_COMMAND));
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_INFO.getString(playerUUID));
            Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
        }
    }
}
