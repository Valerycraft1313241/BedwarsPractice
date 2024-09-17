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

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
        } else {
            Player player = (Player) sender;
            Lobby.getInstance().set(player.getLocation());
            player.sendMessage(" ");
            player.sendMessage(" ");
            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SET_LOBBY_SUCCEEDED.getString(player.getUniqueId()));
            Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
        }
    }

    public boolean canSee(CommandSender sender) {
        return !(sender instanceof Player) ? this.hasPermission(sender) : this.hasPermission(sender) && !SchematicWorldCreator.isInSchematicWorld(((Player) sender).getWorld());
    }
}
