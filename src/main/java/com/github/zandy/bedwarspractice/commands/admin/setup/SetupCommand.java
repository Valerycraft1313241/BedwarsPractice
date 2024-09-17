package com.github.zandy.bedwarspractice.commands.admin.setup;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicWorldCreator;
import com.github.zandy.bedwarspractice.commands.admin.setup.modules.*;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.Position;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SetupCommand extends SubCommand {
    public SetupCommand() {
        super("setup", Language.MessagesEnum.COMMAND_ADMIN_SETUP_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
        } else {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if (!Lobby.getInstance().isSet()) {
                player.sendMessage("");
                player.sendMessage("");
                player.sendMessage(Language.MessagesEnum.COMMAND_HEADER_DEFAULT.getString(uuid));
                Language.MessagesEnum.PLUGIN_LOBBY_NOT_SET.getStringList(uuid).forEach((argsx) -> {
                    if (argsx.toLowerCase().contains("bwpa")) {
                        BambooUtils.sendTextComponent(player, argsx, "/bwpa setSpawn", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                    } else {
                        player.sendMessage(argsx);
                    }

                });
            } else if (!SchematicWorldCreator.isInSchematicWorld(player.getWorld())) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_NOT_IN_SCHEMATIC_WORLD.getStringList(uuid).forEach((argsx) -> {
                    if (argsx.contains("/bwpa")) {
                        BambooUtils.sendTextComponent(player, argsx, "/bwpa schem create", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                    } else {
                        player.sendMessage(argsx);
                    }

                });
                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
            } else if (args.length == 0) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_HEADER.getString(uuid));
                if (!SetupSession.exists(player.getUniqueId())) {
                    BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_SET.getString(uuid), "/bwpa setup set ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                    BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_LIST.getString(uuid), "/bwpa setup list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                } else {
                    BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_POS.getString(uuid), "/bwpa setup pos ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                    BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_SAVE.getString(uuid), "/bwpa setup save", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                    BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_QUIT.getString(uuid), "/bwpa setup quit", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                }

                Sounds.NOTE_PLING.getSound().play(player, 3.0F, 1.0F);
            } else {
                String args0 = args[0].toLowerCase();
                byte cmd = -1;
                switch (args0.hashCode()) {
                    case 111188:
                        if (args0.equals("pos")) {
                            cmd = 2;
                        }
                        break;
                    case 113762:
                        if (args0.equals("set")) {
                            cmd = 0;
                        }
                        break;
                    case 3322014:
                        if (args0.equals("list")) {
                            cmd = 1;
                        }
                        break;
                    case 3482191:
                        if (args0.equals("quit")) {
                            cmd = 4;
                        }
                        break;
                    case 3522941:
                        if (args0.equals("save")) {
                            cmd = 3;
                        }
                }

                switch (cmd) {
                    case 0:
                        if (args.length != 2) {
                            player.sendMessage(" ");
                            player.sendMessage(" ");
                            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                            player.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(uuid));
                            BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_USAGE.getString(uuid), "/bwpa setup set ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                            return;
                        }

                        SetupSetModule.getInstance().execute(player, args[1].toUpperCase());
                        break;
                    case 1:
                        SetupListModule.getInstance().execute(player);
                        break;
                    case 2:
                        if (!SetupSession.exists(player.getUniqueId())) {
                            this.noSetupSession(player);
                            return;
                        }

                        if (args.length < 2) {
                            player.sendMessage(" ");
                            player.sendMessage(" ");
                            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                            player.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(uuid));
                            BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_USAGE.getString(uuid), "/bwpa setup pos ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                            return;
                        }

                        if (args[1].equalsIgnoreCase("wand")) {
                            SetupPosModule.getInstance().executeWand(player);
                            return;
                        }

                        SetupPosModule.getInstance().execute(player, new Position(Integer.parseInt(args[1]), player.getLocation()));
                        break;
                    case 3:
                    case 4:
                        if (!SetupSession.exists(player.getUniqueId())) {
                            this.noSetupSession(player);
                            return;
                        }

                        if (args[0].equalsIgnoreCase("save")) {
                            SetupSaveModule.getInstance().execute(player);
                        } else {
                            SetupQuitModule.getInstance().execute(player);
                        }
                        break;
                    default:
                        player.sendMessage(" ");
                        player.sendMessage(" ");
                        player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                        player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_WRONG_SUBCOMMAND.getString(uuid).replace("[subCommand]", args[0].toLowerCase()));
                        Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                }

            }
        }
    }

    public boolean canSee(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return this.hasPermission(sender);
        } else {
            return this.hasPermission(sender) && SchematicWorldCreator.isInSchematicWorld(((Player) sender).getWorld());
        }
    }

    public void noSetupSession(Player sender) {
        sender.sendMessage(" ");
        sender.sendMessage(" ");
        sender.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(sender.getUniqueId()));
        Language.MessagesEnum.COMMAND_ADMIN_SETUP_INVALID_SETUP_SESSION.getStringList(sender.getUniqueId()).forEach((senderx) -> {
            if (senderx.contains("/bwpa")) {
                BambooUtils.sendTextComponent(sender, senderx, "/bwpa setup set ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(sender.getUniqueId()), Action.SUGGEST_COMMAND);
            } else {
                sender.sendMessage(senderx);
            }

        });
        Sounds.VILLAGER_NO.getSound().play(sender, 3.0F, 1.0F);
    }
}
