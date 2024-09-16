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

    public void execute(CommandSender var1, String[] var2) {
        if (!(var1 instanceof Player)) {
            var1.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
        } else {
            Player var3 = (Player) var1;
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
            } else {
                boolean var5 = SchematicWorldCreator.isInSchematicWorld(var3.getWorld());
                if (var2.length == 0) {
                    var3.sendMessage(" ");
                    var3.sendMessage(" ");
                    var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_HEADER.getString(var4));
                    if (!var5) {
                        BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_CREATE.getString(var4), "/bwpa schem create", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND);
                    } else {
                        BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LOAD.getString(var4), "/bwpa schem load ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
                        BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LIST.getString(var4), "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND);
                        BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_POS.getString(var4), "/bwpa schem pos ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
                        BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_SAVE.getString(var4), "/bwpa schem save ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
                        BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LEAVE.getString(var4), "/bwpa schem leave", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND);
                    }

                    Sounds.NOTE_PLING.getSound().play(var3, 3.0F, 1.0F);
                } else {
                    String var6 = var2[0].toLowerCase();
                    byte var7 = -1;
                    switch (var6.hashCode()) {
                        case -1352294148:
                            if (var6.equals("create")) {
                                var7 = 0;
                            }
                            break;
                        case 111188:
                            if (var6.equals("pos")) {
                                var7 = 4;
                            }
                            break;
                        case 3322014:
                            if (var6.equals("list")) {
                                var7 = 3;
                            }
                            break;
                        case 3327206:
                            if (var6.equals("load")) {
                                var7 = 2;
                            }
                            break;
                        case 3522941:
                            if (var6.equals("save")) {
                                var7 = 5;
                            }
                            break;
                        case 102846135:
                            if (var6.equals("leave")) {
                                var7 = 1;
                            }
                    }

                    switch (var7) {
                        case 0:
                            if (var5) {
                                var3.sendMessage(" ");
                                var3.sendMessage(" ");
                                var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
                                var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_ALREADY_IN_SCHEMATIC_WORLD.getString(var4));
                                Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
                                return;
                            }

                            SchematicCreateModule.getInstance().execute(var3);
                            break;
                        case 1:
                            if (!var5) {
                                this.notInSchematicWorld(var3);
                                return;
                            }

                            SchematicLeaveModule.getInstance().execute(var3);
                            break;
                        case 2:
                            if (!var5) {
                                this.notInSchematicWorld(var3);
                                return;
                            }

                            if (var2.length < 2) {
                                var3.sendMessage(" ");
                                var3.sendMessage(" ");
                                var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
                                var3.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(var4));
                                BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_USAGE.getString(var4), "/bwpa schem load ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
                                Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
                                return;
                            }

                            SchematicLoadModule.getInstance().execute(var3, var2[1]);
                            break;
                        case 3:
                            if (!var5) {
                                this.notInSchematicWorld(var3);
                                return;
                            }

                            SchematicListModule.getInstance().execute(var3);
                            break;
                        case 4:
                            if (!var5) {
                                this.notInSchematicWorld(var3);
                                return;
                            }

                            if (var2.length < 2) {
                                var3.sendMessage(" ");
                                var3.sendMessage(" ");
                                var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
                                var3.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(var4));
                                BambooUtils.sendTextComponent(var3, Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_USAGE.getString(var4), "/bwpa schem pos ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
                                Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
                                return;
                            }

                            Location var8 = var3.getLocation();
                            if (var2[1].equalsIgnoreCase("wand")) {
                                SchematicPosModule.getInstance().executeWand(var3);
                                return;
                            }

                            SchematicPosModule.getInstance().execute(var3, new Position(Integer.parseInt(var2[1]), var8.getBlockX(), var8.getBlockY(), var8.getBlockZ()), false);
                            break;
                        case 5:
                            if (!var5) {
                                this.notInSchematicWorld(var3);
                                return;
                            }

                            if (var2.length < 2) {
                                var3.sendMessage(" ");
                                var3.sendMessage(" ");
                                var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
                                var3.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(var4));
                                Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_USAGE.getStringList(var4).forEach((var2x) -> {
                                    if (var2x.toLowerCase().contains("/bwpa schem save")) {
                                        BambooUtils.sendTextComponent(var3, var2x, "/bwpa schem save ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var4), Action.SUGGEST_COMMAND);
                                    } else if (var2x.toLowerCase().contains("/bwpa schem list")) {
                                        BambooUtils.sendTextComponent(var3, var2x, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND);
                                    } else {
                                        var3.sendMessage(var2x);
                                    }

                                });
                                Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
                                return;
                            }

                            SchematicSaveModule.getInstance().execute(var3, var2[1]);
                            break;
                        default:
                            var3.sendMessage(" ");
                            var3.sendMessage(" ");
                            var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
                            var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_WRONG_SUBCOMMAND.getString(var4).replace("[subCommand]", var2[0].toLowerCase()));
                            Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
                    }

                }
            }
        }
    }

    public boolean canSee(CommandSender var1) {
        return this.hasPermission(var1);
    }

    private void notInSchematicWorld(Player var1) {
        var1.sendMessage(" ");
        var1.sendMessage(" ");
        var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
        Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_NOT_IN_SCHEMATIC_WORLD.getStringList(var1.getUniqueId()).forEach((var1x) -> {
            if (var1x.contains("/bwpa")) {
                BambooUtils.sendTextComponent(var1, var1x, "/bwpa schem create", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var1.getUniqueId()), Action.RUN_COMMAND);
            } else {
                var1.sendMessage(var1x);
            }

        });
        Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
    }

    public static class SchematicCommandAlias extends SubCommand {
        public SchematicCommandAlias() {
            super("schematic", "", BedWarsPracticeAdminCommand.getPermissions());
        }

        public void execute(CommandSender var1, String[] var2) {
            SchematicCommand.getSchematicCommandInstance().execute(var1, var2);
        }

        public boolean canSee(CommandSender var1) {
            return false;
        }
    }
}
