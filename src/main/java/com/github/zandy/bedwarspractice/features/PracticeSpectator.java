package com.github.zandy.bedwarspractice.features;

import com.github.zandy.bamboolib.versionsupport.VersionSupport;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PracticeSpectator implements Listener {
    private static final HashMap<UUID, PracticeSpectator> practiceSpectators = new HashMap<>();
    private final Player target;

    public PracticeSpectator(Player player, Player targetPlayer) {
        this.target = targetPlayer;
        player.setGameMode(GameMode.SPECTATOR);
        UUID playerUUID = player.getUniqueId();
        UUID targetUUID = targetPlayer.getUniqueId();
        practiceSpectators.put(playerUUID, this);
        GameEngine gameEngine = GameEngine.getInstance();
        player.sendMessage(" ");
        player.sendMessage(" ");
        Language.MessagesEnum.SPECTATE_SPECTATING.getStringList(playerUUID).forEach((message) -> {
            if (message.contains("[player_name]")) {
                player.sendMessage(message.replace("[player_name]", targetPlayer.getName()));
            }

            if (message.contains("[practice_mode]")) {
                player.sendMessage(message.replace("[practice_mode]", gameEngine.getPracticeTypeMap().get(targetUUID).getDisplayName(playerUUID)));
            }

            if (message.contains("[current_settings]")) {
                player.sendMessage(message.replace("[current_settings]", gameEngine.getCurrentGameSettings(targetUUID, playerUUID)));
            }

        });
        VersionSupport.getInstance().sendTitle(player, Language.MessagesEnum.SPECTATE_TITLE.getString(playerUUID), Language.MessagesEnum.SPECTATE_SUBTITLE.getString(playerUUID).replace("[player_name]", targetPlayer.getName()));
        player.teleport(gameEngine.getPracticeLocationMap().get(targetUUID).clone().add(0.0D, 1.5D, 0.0D));
        gameEngine.getSpectators().get(targetUUID).add(playerUUID);
    }

    public static PracticeSpectator get(UUID uuid) {
        return practiceSpectators.get(uuid);
    }

    public static boolean isSpectating(UUID uuid) {
        return practiceSpectators.containsKey(uuid);
    }

    public static void remove(UUID uuid) {
        UUID targetUUID = get(uuid).getTarget().getUniqueId();
        HashMap<UUID, List<UUID>> spectatorsMap = GameEngine.getInstance().getSpectators();
        if (spectatorsMap.containsKey(targetUUID)) {
            spectatorsMap.get(targetUUID).remove(uuid);
        }

        practiceSpectators.remove(uuid);
    }

    public static HashMap<UUID, PracticeSpectator> getPracticeSpectators() {
        return practiceSpectators;
    }

    public Player getTarget() {
        return this.target;
    }
}
