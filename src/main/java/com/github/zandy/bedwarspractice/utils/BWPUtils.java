package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.Random;
import java.util.UUID;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.entity.Player;

public class BWPUtils {
   private static final Random random = new Random();
   private static Object isLegacy = null;
   private static String extension = null;
   private static String directory = null;
   private static boolean finished = false;

   public static boolean isLegacy() {
      if (isLegacy == null) {
         isLegacy = BambooUtils.isVersion(8, 12);
      }

      return (Boolean)isLegacy;
   }

   public static int genRandomNumber(int var0) {
      return random.nextInt(var0 + 1);
   }

   public static boolean isSetupFinished(Player var0) {
      if (finished) {
         return true;
      } else {
         UUID var1 = var0.getUniqueId();
         if (!SetupData.getInstance().isSetupDoneSchematics()) {
            var0.sendMessage(" ");
            var0.sendMessage(" ");
            var0.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var0.getUniqueId()));
            Language.MessagesEnum.COMMAND_PLAYER_MENU_SCHEMATICS_NOT_SET.getStringList(var1).forEach((var2) -> {
               if (var2.contains("/bwpa")) {
                  BambooUtils.sendTextComponent(var0, var2, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var1), Action.RUN_COMMAND);
               } else {
                  var0.sendMessage(var2);
               }

            });
            finished = false;
            return false;
         } else if (!SetupData.getInstance().isSetupDoneConfigurations()) {
            var0.sendMessage(" ");
            var0.sendMessage(" ");
            var0.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var0.getUniqueId()));
            Language.MessagesEnum.COMMAND_PLAYER_MENU_CONFIGURATIONS_NOT_SET.getStringList(var1).forEach((var2) -> {
               if (var2.contains("/bwpa")) {
                  BambooUtils.sendTextComponent(var0, var2, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var1), Action.RUN_COMMAND);
               } else {
                  var0.sendMessage(var2);
               }

            });
            finished = false;
            return false;
         } else {
            finished = true;
            return true;
         }
      }
   }

   public static String getExtension() {
      if (extension == null) {
         extension = BambooUtils.isVersion(8, 12) ? ".schematic" : ".schem";
      }

      return extension;
   }

   public static String getDirectory() {
      if (directory == null) {
         if (BambooUtils.isPluginEnabled("FastAsyncWorldEdit") && BambooUtils.isVersion(18, 19)) {
            directory = "FastAsyncWorldEdit";
         } else {
            directory = "WorldEdit";
         }
      }

      return directory;
   }

}
