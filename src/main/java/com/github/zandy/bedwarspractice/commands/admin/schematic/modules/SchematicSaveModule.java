package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.support.WESupport;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchematicSaveModule {
    private static SchematicSaveModule instance = null;
    private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(true));

    private SchematicSaveModule() {
    }

    public static SchematicSaveModule getInstance() {
        if (instance == null) {
            instance = new SchematicSaveModule();
        }

        return instance;
    }

    public void execute(Player player, String schematicName) {
        player.sendMessage(" ");
        player.sendMessage(" ");
        if (!this.requiredSchematics.contains(schematicName)) {
            Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_WRONG_SCHEMATIC_NAME.getStringList(player.getUniqueId()).forEach((message) -> {
                if (!message.toLowerCase().contains("/bwpa")) {
                    player.sendMessage(message);
                } else {
                    BambooUtils.sendTextComponent(player, message, "/bwpa schem list", Action.RUN_COMMAND);
                }
            });
            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
        } else {
            WESupport.getInstance().saveSchematic(player, schematicName);
            Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_SUCCESSFULLY.getString(player.getUniqueId()).replace("[schemName]", schematicName));
                Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
                ArrayList<String> remainingSchematics = new ArrayList<>(this.requiredSchematics);
                File schematicsFolder = new File("plugins/BedWarsPractice/Schematics");
                if (schematicsFolder.exists()) {
                    Arrays.stream(schematicsFolder.listFiles()).forEach((file) -> remainingSchematics.remove(file.getName().replace(BWPUtils.getExtension(), "")));
                }

                if (remainingSchematics.isEmpty()) {
                    SetupData.getInstance().setSetupDoneSchematics(true);
                }

                GameEngine.getInstance().rebuildSchematicCache();
            }, 120L);
        }
    }
}
