package com.github.zandy.bedwarspractice.commands.admin.setup.modules;

import com.github.zandy.bedwarspractice.commands.admin.setup.SetupSession;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.sk89q.worldedit.EditSession;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SetupQuitModule implements Listener {
    private static SetupQuitModule instance = null;

    private SetupQuitModule() {
    }

    public static SetupQuitModule getInstance() {
        if (instance == null) {
            instance = new SetupQuitModule();
        }

        return instance;
    }

    public void execute(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (!SetupSetModule.getSetupNameMap().containsKey(playerUUID)) {
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_QUIT_NOT_IN_SETUP.getString(playerUUID));
        } else {
            this.executeRemove(player, player.getLocation().getWorld().getSpawnLocation().clone().add(0.5D, 1.0D, 0.5D), SetupSetModule.getSetupEditSessionMap().get(playerUUID));
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (SetupSetModule.getSetupEditSessionMap().containsKey(player.getUniqueId())) {
            this.executeRemove(player, Lobby.getInstance().get(), SetupSetModule.getSetupEditSessionMap().get(player.getUniqueId()));
        }
    }

    private void executeRemove(Player player, Location spawnLocation, EditSession editSession) {
        SetupSetModule.getSetupNameMap().remove(player.getUniqueId());
        player.teleport(spawnLocation);
        editSession.undo(editSession);
        SetupSetModule.getSetupEditSessionMap().remove(player.getUniqueId());
        SetupSession.remove(player.getUniqueId());
        SetupSetModule.getInstance().removeOffset(player.getUniqueId());
        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
        List<String> messages = Language.MessagesEnum.COMMAND_ADMIN_SETUP_QUIT_SUCCESSFULLY.getStringList(player.getUniqueId());
        Objects.requireNonNull(player);
        messages.forEach(player::sendMessage);
    }
}
