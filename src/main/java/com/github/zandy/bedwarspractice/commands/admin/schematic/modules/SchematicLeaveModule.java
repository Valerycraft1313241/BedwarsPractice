package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicWorldCreator;
import com.github.zandy.bedwarspractice.commands.admin.setup.SetupSession;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.language.Language;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SchematicLeaveModule implements Listener {
    private static SchematicLeaveModule instance = null;

    private SchematicLeaveModule() {
    }

    public static SchematicLeaveModule getInstance() {
        if (instance == null) {
            instance = new SchematicLeaveModule();
        }

        return instance;
    }

    public void execute(Player player) {
        if (SchematicWorldCreator.isInSchematicWorld(player.getWorld())) {
            if (SetupSession.exists(player.getUniqueId())) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LEAVE_SETUP_NOT_FINISHED.getStringList(player.getUniqueId()).forEach((message) -> {
                    if (message.toLowerCase().contains("/bwpa setup quit")) {
                        BambooUtils.sendTextComponent(player, message, "/bwpa setup quit", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(player.getUniqueId()), Action.RUN_COMMAND);
                    } else {
                        player.sendMessage(message);
                    }

                });
            } else {
                this.unloadModule(player);
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
                List<String> messages = Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LEAVE_SUCCESSFULLY.getStringList(player.getUniqueId());
                Objects.requireNonNull(player);
                messages.forEach(player::sendMessage);
                Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
            }
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        if (SchematicWorldCreator.isInSchematicWorld(event.getPlayer().getWorld())) {
            this.unloadModule(event.getPlayer());
        }

    }

    private void unloadModule(Player player) {
        Location lobbyLocation = Lobby.getInstance().get();
        (new ArrayList<>(player.getWorld().getPlayers())).forEach((worldPlayer) -> worldPlayer.teleport(lobbyLocation));
        SchematicWorldCreator.unload();
    }
}
