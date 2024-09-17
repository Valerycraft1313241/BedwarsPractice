package com.github.zandy.bedwarspractice.commands;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.command.ParentCommand;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeQuitEvent;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.features.PracticeSpectator;
import com.github.zandy.bedwarspractice.features.guis.ModeSelectorGUI;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.proxy.PracticeIncomingOutgoingProxy;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BedWarsPracticeCommand extends ParentCommand {
    private final List<UUID> cooldown;
    private final int cooldownDuration;
    private final int cooldownDurationTicks;

    public BedWarsPracticeCommand() {
        super("bwp");
        this.setAliases(Arrays.asList("bwpractice", "bedwarspractice"));
        this.setDescription("Main command for BedWarsPractice plugin.");
        this.cooldown = new ArrayList<>();
        this.cooldownDuration = PracticeSettings.GameSettingsEnum.COMMAND_SPECTATE_COOLDOWN.getInt();
        this.cooldownDurationTicks = this.cooldownDuration * 20;
    }

    public void sendDefaultMessage(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
        }

    }

    public boolean execute(CommandSender sender, String command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
            return false;
        } else {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
                return false;
            } else if (!Lobby.getInstance().isSet()) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_HEADER_DEFAULT.getString(uuid));
                Language.MessagesEnum.PLUGIN_LOBBY_NOT_SET.getStringList(uuid).forEach((s) -> {
                    if (s.toLowerCase().contains("bwpa")) {
                        BambooUtils.sendTextComponent(player, s, "/bwpa setSpawn", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                    } else {
                        player.sendMessage(s);
                    }

                    Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                });
                return false;
            } else if (!BWPUtils.isSetupFinished(player)) {
                return false;
            }
            else {
                String arg0;
                byte cmd = -1; // Initialize cmd here
                if (args.length != 0) {
                    arg0 = args[0].toLowerCase();
                    switch (arg0) {
                        case "quit":
                            cmd = 1;
                            break;
                        case "leave":
                            cmd = 0;
                            break;
                    }


                    switch (cmd) {
                        case 0:
                        case 1:
                            if (GameEngine.getInstance().getPracticeTypeMap().containsKey(uuid)) {
                                if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
                                    PracticeIncomingOutgoingProxy.getInstance().connect(player, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString());
                                } else {
                                    Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(player));
                                }
                            } else {
                                player.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_QUIT.getString(uuid));
                            }

                            return true;
                    }
                }

                if (GameEngine.getInstance().getPracticeTypeMap().containsKey(player.getUniqueId())) {
                    player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                    player.sendMessage(Language.MessagesEnum.COMMAND_NOT_AVAILABLE_IN_PRACTICE.getString(uuid));
                    return false;
                }
                else if (args.length != 0) {
                    arg0 = args[0].toLowerCase();
                    switch (arg0) {
                        case "spectate":
                            cmd = 3;
                            break;
                        case "mlg":
                            cmd = 1;
                            break;
                        case "bridging":
                            cmd = 0;
                            break;
                        case "fireballtntjumping":
                            cmd = 2;
                            break;
                    }


                    switch (cmd) {
                        case 0:
                            ModeSelectorGUI.getInstance().clickFunctionality(player, GameEngine.PracticeType.BRIDGING, true);
                            return true;
                        case 1:
                            ModeSelectorGUI.getInstance().clickFunctionality(player, GameEngine.PracticeType.MLG, true);
                            return true;
                        case 2:
                            ModeSelectorGUI.getInstance().clickFunctionality(player, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, true);
                            return true;
                        case 3:
                            if (!player.hasPermission("bwpractice.spectate")) {
                                player.sendMessage(Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString(uuid));
                                return true;
                            } else if (args.length != 2) {
                                Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_WRONG_USAGE.getStringList(uuid).forEach((s) -> {
                                    if (s.contains("/bwp spectate")) {
                                        BambooUtils.sendTextComponent(player, s, "/bwp spectate ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(uuid), Action.SUGGEST_COMMAND);
                                    } else {
                                        player.sendMessage(s);
                                    }

                                });
                                return true;
                            } else if (!args[1].equalsIgnoreCase("quit") && !args[1].equalsIgnoreCase("leave")) {
                                Player player1 = Bukkit.getPlayer(args[1]);
                                if (player1 == null) {
                                    player.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_TARGET_NULL.getString(uuid).replace("[player_name]", args[1]));
                                    return true;
                                } else if (!GameEngine.getInstance().getPracticeTypeMap().containsKey(player1.getUniqueId())) {
                                    player.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_TARGET_NOT_IN_PRACTICE.getString(uuid).replace("[player_name]", args[1]));
                                    return true;
                                } else if (PracticeSpectator.isSpectating(uuid) && PracticeSpectator.get(uuid).getTarget().getUniqueId().equals(player1.getUniqueId())) {
                                    player.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_ALREADY_SPECTATING.getString(uuid).replace("[player_name]", args[1]));
                                    return true;
                                } else {
                                    if (this.cooldown.contains(uuid)) {
                                        player.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_IN_COOLDOWN.getString(uuid).replace("[seconds]", String.valueOf(this.cooldownDuration)));
                                        return true;
                                    }

                                    this.cooldown.add(uuid);
                                    Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> this.cooldown.remove(uuid), this.cooldownDurationTicks);
                                    new PracticeSpectator(player, player1);
                                    return true;
                                }
                            } else {
                                if (!PracticeSpectator.isSpectating(uuid)) {
                                    player.sendMessage(Language.MessagesEnum.SPECTATE_NOT_SPECTATING.getString(uuid));
                                    return true;
                                }

                                player.getInventory().clear();
                                player.teleport(Lobby.getInstance().get());
                                player.sendMessage(Language.MessagesEnum.SPECTATE_QUIT.getString(uuid).replace("[player_name]", PracticeSpectator.get(uuid).getTarget().getName()));
                                PracticeSpectator.remove(uuid);
                                player.setGameMode(GameMode.SURVIVAL);
                                return true;
                            }
                        default:
                            player.sendMessage(" ");
                            player.sendMessage(" ");
                            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                            player.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(player.getUniqueId()));
                            Language.MessagesEnum.COMMAND_PLAYER_PRACTICE_USAGE.getStringList(uuid).forEach((s) -> {
                                if (s.contains("/bwp bridging")) {
                                    BambooUtils.sendTextComponent(player, s, "/bwp bridging", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                                } else if (s.contains("/bwp mlg")) {
                                    BambooUtils.sendTextComponent(player, s, "/bwp mlg", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                                } else if (s.contains("/bwp fireballtntjumping")) {
                                    BambooUtils.sendTextComponent(player, s, "/bwp fireballtntjumping", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                                } else if (s.contains("/bwp quit")) {
                                    BambooUtils.sendTextComponent(player, s, "/bwp quit", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
                                } else {
                                    player.sendMessage(s);
                                }

                            });
                            return false;
                    }
                } else {
                    ModeSelectorGUI.getInstance().open(player, true);
                    return true;
                }
            }
        }
    }

    public String noPermissionMessage(CommandSender sender) {
        return sender instanceof Player ? Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString(((Player) sender).getUniqueId()) : Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString();
    }
}
