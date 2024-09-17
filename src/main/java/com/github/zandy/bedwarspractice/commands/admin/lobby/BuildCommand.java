package com.github.zandy.bedwarspractice.commands.admin.lobby;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildCommand extends SubCommand {
    private static BuildCommand instance = null;
    private final List<UUID> buildList = new ArrayList<>();

    private BuildCommand() {
        super("build", Language.MessagesEnum.COMMAND_ADMIN_BUILD_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
    }

    public static BuildCommand getInstance() {
        if (instance == null) {
            instance = new BuildCommand();
        }

        return instance;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
        } else {
            Player player = (Player) sender;
            player.sendMessage(" ");
            player.sendMessage(" ");
            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
            player.sendMessage((this.buildList.contains(player.getUniqueId()) ? Language.MessagesEnum.COMMAND_ADMIN_BUILD_DISABLED : Language.MessagesEnum.COMMAND_ADMIN_BUILD_ENABLED).getString(player.getUniqueId()));
            if (this.buildList.contains(player.getUniqueId())) {
                this.buildList.remove(player.getUniqueId());
            } else {
                this.buildList.add(player.getUniqueId());
            }

            Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
        }
    }

    public boolean canSee(CommandSender sender) {
        return this.hasPermission(sender);
    }

    public List<UUID> getBuildList() {
        return this.buildList;
    }
}
