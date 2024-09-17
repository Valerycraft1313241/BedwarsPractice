package com.github.zandy.bedwarspractice.commands.admin.schematic;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.commands.admin.schematic.modules.*;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.Position;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SchematicCommand extends SubCommand {
    private static SchematicCommand instance;

    public SchematicCommand() {
        super("schem", Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
        instance = this;
    }

    public static SchematicCommand getSchematicCommandInstance() {
        return instance;
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
            } else {
                boolean isInWorld = SchematicWorldCreator.isInSchematicWorld(player.getWorld());
                if (args.length == 0) {
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_HEADER.getString(uuid));
                    if (!isInWorld) {
                        BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_CREATE.getString(uuid), "/bwpa schem create", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                    } else {
                        BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LOAD.getString(uuid), "/bwpa schem load ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                        BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LIST.getString(uuid), "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                        BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_POS.getString(uuid), "/bwpa schem pos ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                        BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_SAVE.getString(uuid), "/bwpa schem save ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                        BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LEAVE.getString(uuid), "/bwpa schem leave", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                    }

                    Sounds.NOTE_PLING.getSound().play(player, 3.0F, 1.0F);
                } else {
                    String subCommand = args[0].toLowerCase();
                    byte cmd = -1;
                    if (subCommand.equals("create")) {
                        cmd = 0;
                    }
                    if (subCommand.equals("pos")) {
                        cmd = 4;
                    }
                    if (subCommand.equals("list")) {
                        cmd = 3;
                    }
                    if (subCommand.equals("load")) {
                        cmd = 2;
                    }
                    if (subCommand.equals("save")) {
                        cmd = 5;
                    }
                    if (subCommand.equals("leave")) {
                        cmd = 1;
                    }


                    switch (cmd) {
                        case 0:
                            if (isInWorld) {
                                player.sendMessage(" ");
                                player.sendMessage(" ");
                                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_ALREADY_IN_SCHEMATIC_WORLD.getString(uuid));
                                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                                return;
                            }

                            SchematicCreateModule.getInstance().execute(player);
                            break;
                        case 1:
                            if (!isInWorld) {
                                this.notInSchematicWorld(player);
                                return;
                            }

                            SchematicLeaveModule.getInstance().execute(player);
                            break;
                        case 2:
                            if (!isInWorld) {
                                this.notInSchematicWorld(player);
                                return;
                            }

                            if (args.length < 2) {
                                player.sendMessage(" ");
                                player.sendMessage(" ");
                                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                                player.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(uuid));
                                BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_USAGE.getString(uuid), "/bwpa schem load ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                                return;
                            }

                            SchematicLoadModule.getInstance().execute(player, args[1]);
                            break;
                        case 3:
                            if (!isInWorld) {
                                this.notInSchematicWorld(player);
                                return;
                            }

                            SchematicListModule.getInstance().execute(player);
                            break;
                        case 4:
                            if (!isInWorld) {
                                this.notInSchematicWorld(player);
                                return;
                            }

                            if (args.length < 2) {
                                player.sendMessage(" ");
                                player.sendMessage(" ");
                                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                                player.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(uuid));
                                BambooUtils.sendTextComponent(player, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_USAGE.getString(uuid), "/bwpa schem pos ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                                return;
                            }

                            Location playerLocation = player.getLocation();
                            if (args[1].equalsIgnoreCase("wand")) {
                                SchematicPosModule.getInstance().executeWand(player);
                                return;
                            }

                            SchematicPosModule.getInstance().execute(player, new Position(Integer.parseInt(args[1]), playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ()), false);
                            break;
                        case 5:
                            if (!isInWorld) {
                                this.notInSchematicWorld(player);
                                return;
                            }

                            if (args.length < 2) {
                                player.sendMessage(" ");
                                player.sendMessage(" ");
                                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                                player.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(uuid));
                                Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_USAGE.getStringList(uuid).forEach((argsx) -> {
                                    if (argsx.toLowerCase().contains("/bwpa schem save")) {
                                        BambooUtils.sendTextComponent(player, argsx, "/bwpa schem save ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                                    } else if (argsx.toLowerCase().contains("/bwpa schem list")) {
                                        BambooUtils.sendTextComponent(player, argsx, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                                    } else {
                                        player.sendMessage(argsx);
                                    }

                                });
                                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                                return;
                            }

                            SchematicSaveModule.getInstance().execute(player, args[1]);
                            break;
                        default:
                            player.sendMessage(" ");
                            player.sendMessage(" ");
                            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_WRONG_SUBCOMMAND.getString(uuid).replace("[subCommand]", args[0].toLowerCase()));
                            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                    }

                }
            }
        }
    }

    public boolean canSee(CommandSender sender) {
        return this.hasPermission(sender);
    }

    private void notInSchematicWorld(Player sender) {
        sender.sendMessage(" ");
        sender.sendMessage(" ");
        sender.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(sender.getUniqueId()));
        Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_NOT_IN_SCHEMATIC_WORLD.getStringList(sender.getUniqueId()).forEach((senderx) -> {
            if (senderx.contains("/bwpa")) {
                BambooUtils.sendTextComponent(sender, senderx, "/bwpa schem create", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(sender.getUniqueId()), Action.RUN_COMMAND);
            } else {
                sender.sendMessage(senderx);
            }

        });
        Sounds.VILLAGER_NO.getSound().play(sender, 3.0F, 1.0F);
    }

    public static class SchematicCommandAlias extends SubCommand {
        public SchematicCommandAlias() {
            super("schematic", "", BedWarsPracticeAdminCommand.getPermissions());
        }

        public void execute(CommandSender sender, String[] args) {
            SchematicCommand.getSchematicCommandInstance().execute(sender, args);
        }

        public boolean canSee(CommandSender sender) {
            return false;
        }
    }
}
