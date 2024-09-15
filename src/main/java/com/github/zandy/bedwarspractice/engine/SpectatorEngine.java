package com.github.zandy.bedwarspractice.engine;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.features.PracticeSpectator;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpectatorEngine implements Listener {
   private static SpectatorEngine instance;

   public void init() {
      BambooUtils.registerEvent(this);
   }

   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event) {
      Player player = event.getPlayer();
      UUID playerId = player.getUniqueId();

      if (PracticeSpectator.getPracticeSpectators().containsKey(playerId)) {
         Location targetLocation = GameEngine.getInstance()
                 .getPracticeLocationMap()
                 .get(PracticeSpectator.get(playerId).getTarget().getUniqueId());
         Location newLocation = event.getTo();

         if (isOutOfBounds(newLocation, targetLocation)) {
            player.teleport(targetLocation.clone());
            player.sendMessage(Language.MessagesEnum.PRACTICE_NAME_FIREBALL_TNT_JUMPING.getString(playerId));
         }
      }
   }

   private boolean isOutOfBounds(Location newLocation, Location targetLocation) {
      return newLocation.getBlockX() < targetLocation.getBlockX() - 100 ||
              newLocation.getBlockX() > targetLocation.getBlockX() + 100;
   }

   public static SpectatorEngine getInstance() {
      if (instance == null) {
         instance = new SpectatorEngine();
      }
      return instance;
   }
}
