package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class BWPUtils {

   private static final Random RANDOM = new Random();
   private static Boolean isLegacy = null;
   private static String extension = null;
   private static String directory = null;
   private static boolean finished = false;

   public static boolean isLegacy() {
      // Lazy initialization of isLegacy
      if (isLegacy == null) {
         isLegacy = BambooUtils.isVersion(8, 12);
      }
      return isLegacy;
   }

   public static int genRandomNumber(int upperBound) {
      return RANDOM.nextInt(upperBound + 1);
   }

   public static boolean isSetupFinished(Player player) {
      if (finished) {
         return true;
      }

      UUID playerUUID = player.getUniqueId();
      SetupData setupData = SetupData.getInstance();

      // Check if schematics setup is done
      if (!setupData.isSetupDoneSchematics()) {
         sendSetupMessage(player, playerUUID, Language.MessagesEnum.COMMAND_PLAYER_MENU_SCHEMATICS_NOT_SET);
         finished = false;
         return false;
      }

      // Check if configurations setup is done
      if (!setupData.isSetupDoneConfigurations()) {
         sendSetupMessage(player, playerUUID, Language.MessagesEnum.COMMAND_PLAYER_MENU_CONFIGURATIONS_NOT_SET);
         finished = false;
         return false;
      }

      finished = true;
      return true;
   }

   // Helper method to avoid code duplication when sending setup messages
   private static void sendSetupMessage(Player player, UUID playerUUID, Language.MessagesEnum messageEnum) {
      player.sendMessage(" ");
      player.sendMessage(" ");
      player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));

      messageEnum.getStringList(playerUUID).forEach(message -> {
         if (message.contains("/bwpa")) {
            BambooUtils.sendTextComponent(
                    player, message, "/bwpa schem list",
                    Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(playerUUID), Action.RUN_COMMAND
            );
         } else {
            player.sendMessage(message);
         }
      });
   }

   public static String getExtension() {
      // Lazy initialization of extension
      if (extension == null) {
         extension = BambooUtils.isVersion(8, 12) ? ".schematic" : ".schem";
      }
      return extension;
   }

   public static String getDirectory() {
      // Lazy initialization of directory
      if (directory == null) {
         directory = (BambooUtils.isPluginEnabled("FastAsyncWorldEdit") && BambooUtils.isVersion(18, 19))
                 ? "FastAsyncWorldEdit"
                 : "WorldEdit";
      }
      return directory;
   }
}
