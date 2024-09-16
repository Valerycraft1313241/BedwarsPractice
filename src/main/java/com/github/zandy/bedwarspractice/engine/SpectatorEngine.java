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
    private void onPlayerMove(PlayerMoveEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (PracticeSpectator.getPracticeSpectators().containsKey(var2)) {
            Player var3 = var1.getPlayer();
            Location var4 = GameEngine.getInstance().getPracticeLocationMap().get(PracticeSpectator.get(var2).getTarget().getUniqueId());
            Location var5 = var1.getTo();
            if (var5.getBlockX() < var4.getBlockX() - 100 || var5.getBlockX() > var4.getBlockX() + 100) {
                var3.teleport(var4.clone());
                var3.sendMessage(Language.MessagesEnum.PRACTICE_NAME_FIREBALL_TNT_JUMPING.getString(var2));
            }

        }
    }
}
