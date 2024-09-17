package com.github.zandy.bedwarspractice.engine;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.features.PracticeSpectator;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class SpectatorEngine implements Listener {
    private static SpectatorEngine instance = null;

    public static SpectatorEngine getInstance() {
        if (instance == null) {
            instance = new SpectatorEngine();
        }

        return instance;
    }

    public void init() {
        BambooUtils.registerEvent(this);
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (PracticeSpectator.getPracticeSpectators().containsKey(playerUUID)) {
            Player player = event.getPlayer();
            Location targetLocation = GameEngine.getInstance().getPracticeLocationMap().get(PracticeSpectator.get(playerUUID).getTarget().getUniqueId());
            Location toLocation = event.getTo();
            if (toLocation.getBlockX() < targetLocation.getBlockX() - 100 || toLocation.getBlockX() > targetLocation.getBlockX() + 100) {
                player.teleport(targetLocation.clone());
                player.sendMessage(Language.MessagesEnum.PRACTICE_NAME_FIREBALL_TNT_JUMPING.getString(playerUUID));
            }
        }
    }
}
