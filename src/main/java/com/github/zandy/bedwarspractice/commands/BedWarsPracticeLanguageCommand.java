package com.github.zandy.bedwarspractice.commands;

import com.github.zandy.bamboolib.command.ParentCommand;
import com.github.zandy.bamboolib.database.Database;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.api.events.player.PlayerLanguageChangeEvent;
import com.github.zandy.bedwarspractice.files.language.Language;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BedWarsPracticeLanguageCommand extends ParentCommand {
    public BedWarsPracticeLanguageCommand() {
        super("bwpl");
        this.setAliases(Arrays.asList("bwpracticelanguage", "bedwarspracticelanguage", "bwpracticelang", "bedwarspracticelang", "bwplang", "bwplanguage"));
        this.setDescription("Language command for BedWarsPractice plugin.");
    }

    public void sendDefaultMessage(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
        }
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
            return false;
        } else {
            Player player = (Player) sender;
            if (args.length == 0) {
                this.sendUsage(player);
                return false;
            } else {
                String languageAbbreviation = args[0].toUpperCase();
                if (!Language.getInstance().getLanguageAbbreviations().contains(languageAbbreviation)) {
                    player.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_NOT_FOUND.getPath());
                    Language.getInstance().getLanguageAbbreviations().forEach((abbreviation) -> BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_LIST_FORMAT.getString(player.getUniqueId()).replace("[languageAbbreviation]", abbreviation).replace("[languageName]", Language.getInstance().getLocaleFiles().get(abbreviation).getString(Language.MessagesEnum.PLUGIN_LANGUAGE_DISPLAY.getPath())), "/bwpl " + abbreviation, Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(player.getUniqueId()), Action.RUN_COMMAND));
                    return false;
                } else {
                    String oldLanguage = Language.getInstance().getPlayerLocale().get(player.getUniqueId());
                    Language.getInstance().getPlayerLocale().put(player.getUniqueId(), languageAbbreviation);
                    Database.getInstance().setString(player.getUniqueId(), languageAbbreviation, "Language", "Profile");
                    player.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_CHANGED.getString(player.getUniqueId()).replace("[languageName]", Language.MessagesEnum.PLUGIN_LANGUAGE_DISPLAY.getString(player.getUniqueId())).replace("[languageAbbreviation]", languageAbbreviation));
                    Bukkit.getPluginManager().callEvent(new PlayerLanguageChangeEvent(player, oldLanguage, languageAbbreviation));
                    return true;
                }
            }
        }
    }

    private void sendUsage(Player player) {
        Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_USAGE.getStringList(player.getUniqueId()).forEach((message) -> {
            if (message.contains("bwpl")) {
                BambooUtils.sendTextComponent(player, message, "/bwpl ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(player.getUniqueId()), Action.SUGGEST_COMMAND);
            } else {
                player.sendMessage(message);
            }
        });
        Language.getInstance().getLanguageAbbreviations().forEach((abbreviation) -> BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_LIST_FORMAT.getString(player.getUniqueId()).replace("[languageAbbreviation]", abbreviation).replace("[languageName]", Language.getInstance().getLocaleFiles().get(abbreviation).getString(Language.MessagesEnum.PLUGIN_LANGUAGE_DISPLAY.getPath())), "/bwpl " + abbreviation, Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(player.getUniqueId()), Action.RUN_COMMAND));
    }

    public String noPermissionMessage(CommandSender sender) {
        return sender instanceof Player ? Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString(((Player) sender).getUniqueId()) : Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString();
    }
}
