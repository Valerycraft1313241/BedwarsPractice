package com.github.zandy.bedwarspractice.commands.admin.setup;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicWorldCreator;
import com.github.zandy.bedwarspractice.commands.admin.setup.modules.SetupListModule;
import com.github.zandy.bedwarspractice.commands.admin.setup.modules.SetupPosModule;
import com.github.zandy.bedwarspractice.commands.admin.setup.modules.SetupQuitModule;
import com.github.zandy.bedwarspractice.commands.admin.setup.modules.SetupSaveModule;
import com.github.zandy.bedwarspractice.commands.admin.setup.modules.SetupSetModule;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.Position;
import java.util.UUID;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand extends SubCommand {
   public SetupCommand() {
      super("setup", Language.MessagesEnum.COMMAND_ADMIN_SETUP_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
   }

   public void execute(CommandSender var1, String[] var2) {
      if (!(var1 instanceof Player)) {
         var1.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
      } else {
         Player var3 = (Player)var1;
         UUID var4 = var3.getUniqueId();
         if (!Lobby.getInstance().isSet()) {
            var3.sendMessage("");
            var3.sendMessage("");
            var3.sendMessage(Language.MessagesEnum.COMMAND_HEADER_DEFAULT.getString(var4));
            Language.MessagesEnum.PLUGIN_LOBBY_NOT_SET.getStringList(var4).forEach((var2x) -> {
               if (var2x.toLowerCase().contains("bwpa")) {
                  BambooUtils.sendTextComponent(var3, var2x, "/bwpa setSpawn", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
               } else {
                  var3.sendMessage(var2x);
               }

            });
         } else if (!SchematicWorldCreator.isInSchematicWorld(var3.getWorld())) {
            var3.sendMessage(" ");
            var3.sendMessage(" ");
            var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
            Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_NOT_IN_SCHEMATIC_WORLD.getStringList(var4).forEach((var2x) -> {
               if (var2x.contains("/bwpa")) {
                  BambooUtils.sendTextComponent(var3, var2x, "/bwpa schem create", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND);
               } else {
                  var3.sendMessage(var2x);
               }

            });
            Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
         } else if (var2.length == 0) {
            var3.sendMessage(" ");
            var3.sendMessage(" ");
            var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_HEADER.getString(var4));
            if (!SetupSession.exists(var3.getUniqueId())) {
               BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_SET.getString(var4), "/bwpa setup set ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
               BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_LIST.getString(var4), "/bwpa setup list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND);
            } else {
               BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_POS.getString(var4), "/bwpa setup pos ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
               BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_SAVE.getString(var4), "/bwpa setup save", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND);
               BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_QUIT.getString(var4), "/bwpa setup quit", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND);
            }

            Sounds.NOTE_PLING.getSound().play(var3, 3.0F, 1.0F);
         } else {
            String var5 = var2[0].toLowerCase();
            byte var6 = -1;
            switch(var5.hashCode()) {
            case 111188:
               if (var5.equals("pos")) {
                  var6 = 2;
               }
               break;
            case 113762:
               if (var5.equals("set")) {
                  var6 = 0;
               }
               break;
            case 3322014:
               if (var5.equals("list")) {
                  var6 = 1;
               }
               break;
            case 3482191:
               if (var5.equals("quit")) {
                  var6 = 4;
               }
               break;
            case 3522941:
               if (var5.equals("save")) {
                  var6 = 3;
               }
            }

            switch(var6) {
            case 0:
               if (var2.length != 2) {
                  var3.sendMessage(" ");
                  var3.sendMessage(" ");
                  var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
                  var3.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(var4));
                  BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_USAGE.getString(var4), "/bwpa setup set ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
                  Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
                  return;
               }

               SetupSetModule.getInstance().execute(var3, var2[1].toUpperCase());
               break;
            case 1:
               SetupListModule.getInstance().execute(var3);
               break;
            case 2:
               if (!SetupSession.exists(var3.getUniqueId())) {
                  this.noSetupSession(var3);
                  return;
               }

               if (var2.length < 2) {
                  var3.sendMessage(" ");
                  var3.sendMessage(" ");
                  var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
                  var3.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(var4));
                  BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_USAGE.getString(var4), "/bwpa setup pos ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
                  Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
                  return;
               }

               if (var2[1].equalsIgnoreCase("wand")) {
                  SetupPosModule.getInstance().executeWand(var3);
                  return;
               }

               SetupPosModule.getInstance().execute(var3, new Position(Integer.parseInt(var2[1]), var3.getLocation()));
               break;
            case 3:
            case 4:
               if (!SetupSession.exists(var3.getUniqueId())) {
                  this.noSetupSession(var3);
                  return;
               }

               if (var2[0].equalsIgnoreCase("save")) {
                  SetupSaveModule.getInstance().execute(var3);
               } else {
                  SetupQuitModule.getInstance().execute(var3);
               }
               break;
            default:
               var3.sendMessage(" ");
               var3.sendMessage(" ");
               var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
               var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_WRONG_SUBCOMMAND.getString(var4).replace("[subCommand]", var2[0].toLowerCase()));
               Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
            }

         }
      }
   }

   public boolean canSee(CommandSender var1) {
      if (!(var1 instanceof Player)) {
         return this.hasPermission(var1);
      } else {
         return this.hasPermission(var1) && SchematicWorldCreator.isInSchematicWorld(((Player)var1).getWorld());
      }
   }

   public void noSetupSession(Player var1) {
      var1.sendMessage(" ");
      var1.sendMessage(" ");
      var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
      Language.MessagesEnum.COMMAND_ADMIN_SETUP_INVALID_SETUP_SESSION.getStringList(var1.getUniqueId()).forEach((var1x) -> {
         if (var1x.contains("/bwpa")) {
            BambooUtils.sendTextComponent(var1, var1x, "/bwpa setup set ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var1.getUniqueId()), Action.SUGGEST_COMMAND);
         } else {
            var1.sendMessage(var1x);
         }

      });
      Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
   }
}
