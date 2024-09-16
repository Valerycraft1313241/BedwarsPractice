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
            Player var4 = (Player) var1;
            UUID var5 = var4.getUniqueId();
            if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
                return false;
            } else if (!Lobby.getInstance().isSet()) {
                var4.sendMessage(" ");
                var4.sendMessage(" ");
                var4.sendMessage(Language.MessagesEnum.COMMAND_HEADER_DEFAULT.getString(var5));
                Language.MessagesEnum.PLUGIN_LOBBY_NOT_SET.getStringList(var5).forEach((var2x) -> {
                    if (var2x.toLowerCase().contains("bwpa")) {
                        BambooUtils.sendTextComponent(var4, var2x, "/bwpa setSpawn", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var5), Action.SUGGEST_COMMAND);
                    } else {
                        var4.sendMessage(var2x);
                    }

                    Sounds.VILLAGER_NO.getSound().play(var4, 3.0F, 1.0F);
                });
                return false;
            } else if (!BWPUtils.isSetupFinished(var4)) {
                return false;
            } else {
                String var6;
                byte var7;
                if (var3.length != 0) {
                    var6 = var3[0].toLowerCase();
                    var7 = -1;
                    switch (var6.hashCode()) {
                        case 3482191:
                            if (var6.equals("quit")) {
                                var7 = 1;
                            }
                            break;
                        case 102846135:
                            if (var6.equals("leave")) {
                                var7 = 0;
                            }
                    }

                    switch (var7) {
                        case 0:
                        case 1:
                            if (GameEngine.getInstance().getPracticeTypeMap().containsKey(var5)) {
                                if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
                                    PracticeIncomingOutgoingProxy.getInstance().connect(var4, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString());
                                } else {
                                    Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(var4));
                                }
                            } else {
                                var4.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_QUIT.getString(var5));
                            }

                            return true;
                    }
                }

                if (GameEngine.getInstance().getPracticeTypeMap().containsKey(var4.getUniqueId())) {
                    var4.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var4.getUniqueId()));
                    var4.sendMessage(Language.MessagesEnum.COMMAND_NOT_AVAILABLE_IN_PRACTICE.getString(var5));
                    return false;
                } else if (var3.length != 0) {
                    var6 = var3[0].toLowerCase();
                    var7 = -1;
                    switch (var6.hashCode()) {
                        case -2132551719:
                            if (var6.equals("spectate")) {
                                var7 = 3;
                            }
                            break;
                        case 108200:
                            if (var6.equals("mlg")) {
                                var7 = 1;
                            }
                            break;
                        case 194511366:
                            if (var6.equals("bridging")) {
                                var7 = 0;
                            }
                            break;
                        case 1647511919:
                            if (var6.equals("fireballtntjumping")) {
                                var7 = 2;
                            }
                    }

                    switch (var7) {
                        case 0:
                            ModeSelectorGUI.getInstance().clickFunctionality(var4, GameEngine.PracticeType.BRIDGING, true);
                            return true;
                        case 1:
                            ModeSelectorGUI.getInstance().clickFunctionality(var4, GameEngine.PracticeType.MLG, true);
                            return true;
                        case 2:
                            ModeSelectorGUI.getInstance().clickFunctionality(var4, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, true);
                            return true;
                        case 3:
                            if (!var4.hasPermission("bwpractice.spectate")) {
                                var4.sendMessage(Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString(var5));
                                return true;
                            } else if (var3.length != 2) {
                                Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_WRONG_USAGE.getStringList(var5).forEach((var2x) -> {
                                    if (var2x.contains("/bwp spectate")) {
                                        BambooUtils.sendTextComponent(var4, var2x, "/bwp spectate ", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var5), Action.SUGGEST_COMMAND);
                                    } else {
                                        var4.sendMessage(var2x);
                                    }

                                });
                                return true;
                            } else if (!var3[1].equalsIgnoreCase("quit") && !var3[1].equalsIgnoreCase("leave")) {
                                Player var8 = Bukkit.getPlayer(var3[1]);
                                if (var8 == null) {
                                    var4.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_TARGET_NULL.getString(var5).replace("[player_name]", var3[1]));
                                    return true;
                                } else if (!GameEngine.getInstance().getPracticeTypeMap().containsKey(var8.getUniqueId())) {
                                    var4.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_TARGET_NOT_IN_PRACTICE.getString(var5).replace("[player_name]", var3[1]));
                                    return true;
                                } else if (PracticeSpectator.isSpectating(var5) && PracticeSpectator.get(var5).getTarget().getUniqueId().equals(var8.getUniqueId())) {
                                    var4.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_ALREADY_SPECTATING.getString(var5).replace("[player_name]", var3[1]));
                                    return true;
                                } else {
                                    if (this.cooldown.contains(var5)) {
                                        var4.sendMessage(Language.MessagesEnum.COMMAND_PLAYER_SPECTATE_IN_COOLDOWN.getString(var5).replace("[seconds]", String.valueOf(this.cooldownDuration)));
                                        return true;
                                    }

                                    this.cooldown.add(var5);
                                    Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> this.cooldown.remove(var5), this.cooldownDurationTicks);
                                    new PracticeSpectator(var4, var8);
                                    return true;
                                }
                            } else {
                                if (!PracticeSpectator.isSpectating(var5)) {
                                    var4.sendMessage(Language.MessagesEnum.SPECTATE_NOT_SPECTATING.getString(var5));
                                    return true;
                                }

                                var4.getInventory().clear();
                                var4.teleport(Lobby.getInstance().get());
                                var4.sendMessage(Language.MessagesEnum.SPECTATE_QUIT.getString(var5).replace("[player_name]", PracticeSpectator.get(var5).getTarget().getName()));
                                PracticeSpectator.remove(var5);
                                var4.setGameMode(GameMode.SURVIVAL);
                                return true;
                            }
                        default:
                            var4.sendMessage(" ");
                            var4.sendMessage(" ");
                            var4.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var4.getUniqueId()));
                            var4.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(var4.getUniqueId()));
                            Language.MessagesEnum.COMMAND_PLAYER_PRACTICE_USAGE.getStringList(var5).forEach((var2x) -> {
                                if (var2x.contains("/bwp bridging")) {
                                    BambooUtils.sendTextComponent(var4, var2x, "/bwp bridging", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var5), Action.RUN_COMMAND);
                                } else if (var2x.contains("/bwp mlg")) {
                                    BambooUtils.sendTextComponent(var4, var2x, "/bwp mlg", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var5), Action.RUN_COMMAND);
                                } else if (var2x.contains("/bwp fireballtntjumping")) {
                                    BambooUtils.sendTextComponent(var4, var2x, "/bwp fireballtntjumping", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var5), Action.RUN_COMMAND);
                                } else if (var2x.contains("/bwp quit")) {
                                    BambooUtils.sendTextComponent(var4, var2x, "/bwp quit", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var5), Action.RUN_COMMAND);
                                } else {
                                    var4.sendMessage(var2x);
                                }

                            });
                            return false;
                    }
                } else {
                    ModeSelectorGUI.getInstance().open(var4, true);
                    return true;
                }
            }
        }
    }

    public String noPermissionMessage(CommandSender var1) {
        return var1 instanceof Player ? Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString(((Player) var1).getUniqueId()) : Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString();
    }
}
