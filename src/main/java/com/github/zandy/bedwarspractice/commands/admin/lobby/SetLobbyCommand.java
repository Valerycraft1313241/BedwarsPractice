package com.github.zandy.bedwarspractice.commands.admin.lobby;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicWorldCreator;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand extends SubCommand {
    public SetLobbyCommand() {
        super("setLobby", Language.MessagesEnum.COMMAND_ADMIN_SET_LOBBY_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
    }

    public void execute(CommandSender var1, String[] var2) {
        if (!(var1 instanceof Player)) {
            var1.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
        } else {
            Player var3 = (Player) var1;
            Lobby.getInstance().set(var3.getLocation());
            var3.sendMessage(" ");
            var3.sendMessage(" ");
            var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
            var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SET_LOBBY_SUCCEEDED.getString(var3.getUniqueId()));
            Sounds.PLAYER_LEVELUP.getSound().play(var3, 3.0F, 3.0F);
        }
    }

    public boolean canSee(CommandSender var1) {
        return !(var1 instanceof Player) ? this.hasPermission(var1) : this.hasPermission(var1) && !SchematicWorldCreator.isInSchematicWorld(((Player) var1).getWorld());
    }
}
