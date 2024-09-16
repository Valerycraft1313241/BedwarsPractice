package com.github.zandy.bedwarspractice.commands;

import com.github.zandy.bamboolib.command.ParentCommand;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.admin.lobby.BuildCommand;
import com.github.zandy.bedwarspractice.commands.admin.lobby.SetLobbyCommand;
import com.github.zandy.bedwarspractice.commands.admin.npc.DeleteNPCCommand;
import com.github.zandy.bedwarspractice.commands.admin.npc.SetNPCCommand;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicCommand;
import com.github.zandy.bedwarspractice.commands.admin.setup.SetupCommand;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BedWarsPracticeAdminCommand extends ParentCommand {
    public BedWarsPracticeAdminCommand() {
        super("bwpa");
        this.setAliases(Arrays.asList("bwpracticeadmin", "bedwarspracticeadmin", "bwpadmin"));
        this.setDescription("Admin command for BedWarsPractice plugin.");
        this.addSubCommand(new SetLobbyCommand());
        this.addSubCommand(new SchematicCommand());
        this.addSubCommand(new SchematicCommand.SchematicCommandAlias());
        this.addSubCommand(new SetupCommand());
        if (BambooUtils.isPluginEnabled("Citizens")) {
            this.addSubCommand(SetNPCCommand.getInstance());
            this.addSubCommand(DeleteNPCCommand.getInstance());
        }

        if (!Settings.SettingsEnum.LOBBY_ALLOW_BLOCK_BREAK.getBoolean()) {
            this.addSubCommand(BuildCommand.getInstance());
        }

    }

    public static String[] getPermissions() {
        return Arrays.asList("bwpractice.admin", "bedwarspractice.admin", "bwpractice.*", "bedwarspractice.*").toArray(new String[0]);
    }

    public void sendDefaultMessage(CommandSender var1) {
        if (var1 instanceof Player) {
            Player var2 = (Player) var1;
            var2.sendMessage("");
            var2.sendMessage("");
            var2.sendMessage(Language.MessagesEnum.COMMAND_HEADER_ADMIN.getString(var2.getUniqueId()));
            this.showCommandsList(var2);
            Sounds.NOTE_PLING.getSound().play(var2, 3.0F, 1.0F);
        }
    }

    public String noPermissionMessage(CommandSender var1) {
        return var1 instanceof Player ? Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString(((Player) var1).getUniqueId()) : Language.MessagesEnum.PLUGIN_NO_PERMISSION.getString();
    }
}
