package com.github.zandy.bedwarspractice.commands;

import com.github.zandy.bamboolib.command.ParentCommand;
import com.github.zandy.bamboolib.database.Database;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.api.events.player.PlayerLanguageChangeEvent;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.Arrays;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedWarsPracticeLanguageCommand extends ParentCommand {
   public BedWarsPracticeLanguageCommand() {
      super("bwpl");
      this.setAliases(Arrays.asList("bwpracticelanguage", "bedwarspracticelanguage", "bwpracticelang", "bedwarspracticelang", "bwplang", "bwplanguage"));
      this.setDescription("Language command for BedWarsPractice plugin.");
   }

   public void sendDefaultMessage(CommandSender var1) {
      if (!(var1 instanceof Player)) {
         var1.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
      }

   }

   public boolean execute(CommandSender var1, String var2, String[] var3) {
      if (!(var1 instanceof Player)) {
         var1.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
         return false;
      } else {
         Player var4 = (Player)var1;
         if (var3.length == 0) {
            this.sendUsage(var4);
            return false;
         } else {
            String var5 = var3[0].toUpperCase();
            if (!Language.getInstance().getLanguageAbbreviations().contains(var5)) {
               var4.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_NOT_FOUND.getPath());
               Language.getInstance().getLanguageAbbreviations().forEach((var1x) -> BambooUtils.sendTextComponent(var4, Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_LIST_FORMAT.getString(var4.getUniqueId()).replace("[languageAbbreviation]", var1x).replace("[languageName]", Language.getInstance().getLocaleFiles().get(var1x).getString(Language.MessagesEnum.PLUGIN_LANGUAGE_DISPLAY.getPath())), "/bwpl " + var1x, Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4.getUniqueId()), Action.RUN_COMMAND));
               return false;
            } else {
               String var6 = Language.getInstance().getPlayerLocale().get(var4.getUniqueId());
               Language.getInstance().getPlayerLocale().put(var4.getUniqueId(), var5);
               Database.getInstance().setString(var4.getUniqueId(), var5, "Language", "Profile");
               var4.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_CHANGED.getString(var4.getUniqueId()).replace("[languageName]", Language.MessagesEnum.PLUGIN_LANGUAGE_DISPLAY.getString(var4.getUniqueId())).replace("[languageAbbreviation]", var5));
               Bukkit.getPluginManager().callEvent(new PlayerLanguageChangeEvent(var4, var6, var5));
               return true;
            }
         }
      }
   }

   private void sendUsage(Player var1) {
      Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_USAGE.getStringList(var1.getUniqueId()).forEach((var1x) -> {
         if (var1x.contains("bwpl")) {
            BambooUtils.sendTextComponent(var1, var1x, "/bwpl ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var1.getUniqueId()), Action.SUGGEST_COMMAND);
         } else {
            var1.sendMessage(var1x);
         }

      });
      Language.getInstance().getLanguageAbbreviations().forEach((var1x) -> BambooUtils.sendTextComponent(var1, Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_LIST_FORMAT.getString(var1.getUniqueId()).replace("[languageAbbreviation]", var1x).replace("[languageName]", Language.getInstance().getLocaleFiles().get(var1x).getString(Language.MessagesEnum.PLUGIN_LANGUAGE_DISPLAY.getPath())), "/bwpl " + var1x, Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var1.getUniqueId()), Action.RUN_COMMAND));
   }

   public String noPermissionMessage(CommandSender var1) {
      return var1 instanceof Player ? Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString(((Player)var1).getUniqueId()) : Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString();
   }
}
