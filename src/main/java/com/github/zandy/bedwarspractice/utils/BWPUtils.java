package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class BWPUtils {
    private static final Random random = new Random();
    private static Object legacyVersion = null;
    private static String fileExtension = null;
    private static String pluginDirectory = null;
    private static boolean setupFinished = false;

    public static boolean isLegacy() {
        if (legacyVersion == null) {
            legacyVersion = BambooUtils.isVersion(8, 12);
        }

        return (Boolean) legacyVersion;
    }

    public static int genRandomNumber(int max) {
        return random.nextInt(max + 1);
    }

    public static boolean isSetupFinished(Player player) {
        if (setupFinished) {
            return true;
        } else {
            UUID playerUUID = player.getUniqueId();
            if (!SetupData.getInstance().isSetupDoneSchematics()) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                Language.MessagesEnum.COMMAND_PLAYER_MENU_SCHEMATICS_NOT_SET.getStringList(playerUUID).forEach((message) -> {
                    if (message.contains("/bwpa")) {
                        BambooUtils.sendTextComponent(player, message, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(playerUUID), Action.RUN_COMMAND);
                    } else {
                        player.sendMessage(message);
                    }
                });
                setupFinished = false;
                return false;
            } else if (!SetupData.getInstance().isSetupDoneConfigurations()) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                Language.MessagesEnum.COMMAND_PLAYER_MENU_CONFIGURATIONS_NOT_SET.getStringList(playerUUID).forEach((message) -> {
                    if (message.contains("/bwpa")) {
                        BambooUtils.sendTextComponent(player, message, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(playerUUID), Action.RUN_COMMAND);
                    } else {
                        player.sendMessage(message);
                    }
                });
                setupFinished = false;
                return false;
            } else {
                setupFinished = true;
                return true;
            }
        }
    }

    public static String getExtension() {
        if (fileExtension == null) {
            fileExtension = BambooUtils.isVersion(8, 12) ? ".schematic" : ".schem";
        }

        return fileExtension;
    }

    public static String getDirectory() {
        if (pluginDirectory == null) {
            if (BambooUtils.isPluginEnabled("FastAsyncWorldEdit") && BambooUtils.isVersion(18, 19)) {
                pluginDirectory = "FastAsyncWorldEdit";
            } else {
                pluginDirectory = "WorldEdit";
            }
        }

        return pluginDirectory;
    }
}
