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
import java.util.concurrent.ConcurrentHashMap;

public class PracticeSpectator implements Listener {
    private static final ConcurrentHashMap<UUID, PracticeSpectator> practiceSpectators = new ConcurrentHashMap<>();
   private final Player target;

   public PracticeSpectator(Player spectator, Player target) {
      this.target = target;
      spectator.setGameMode(GameMode.SPECTATOR);
      UUID spectatorUUID = spectator.getUniqueId();
      UUID targetUUID = target.getUniqueId();
      practiceSpectators.put(spectatorUUID, this);
      GameEngine gameEngine = GameEngine.getInstance();
      sendSpectatorMessages(spectator, target, gameEngine, spectatorUUID, targetUUID);
      VersionSupport.getInstance().sendTitle(spectator, Language.MessagesEnum.SPECTATE_TITLE.getString(spectatorUUID), Language.MessagesEnum.SPECTATE_SUBTITLE.getString(spectatorUUID).replace("[player_name]", target.getName()));
      spectator.teleport(gameEngine.getPracticeLocationMap().get(targetUUID).clone().add(0.0D, 1.5D, 0.0D));
      gameEngine.getSpectators().get(targetUUID).add(spectatorUUID);
   }

   private void sendSpectatorMessages(Player spectator, Player target, GameEngine gameEngine, UUID spectatorUUID, UUID targetUUID) {
      spectator.sendMessage(" ");
      spectator.sendMessage(" ");
      Language.MessagesEnum.SPECTATE_SPECTATING.getStringList(spectatorUUID).forEach(message -> {
         if (message.contains("[player_name]")) {
            spectator.sendMessage(message.replace("[player_name]", target.getName()));
         }
         if (message.contains("[practice_mode]")) {
            spectator.sendMessage(message.replace("[practice_mode]", gameEngine.getPracticeTypeMap().get(targetUUID).getDisplayName(spectatorUUID)));
         }
         if (message.contains("[current_settings]")) {
            spectator.sendMessage(message.replace("[current_settings]", gameEngine.getCurrentGameSettings(targetUUID, spectatorUUID)));
         }
      });
   }

   public static PracticeSpectator get(UUID uuid) {
      return practiceSpectators.get(uuid);
   }

   public static boolean isSpectating(UUID uuid) {
      return practiceSpectators.containsKey(uuid);
   }

   public static void remove(UUID uuid) {
      PracticeSpectator spectator = get(uuid);
      if (spectator != null) {
         UUID targetUUID = spectator.getTarget().getUniqueId();
         HashMap<UUID, List<UUID>> spectatorsMap = GameEngine.getInstance().getSpectators();
         if (spectatorsMap.containsKey(targetUUID)) {
            spectatorsMap.get(targetUUID).remove(uuid);
         }
         practiceSpectators.remove(uuid);
      }
   }

   public static ConcurrentHashMap<UUID, PracticeSpectator> getPracticeSpectators() {
      return practiceSpectators;
   }

   public Player getTarget() {
      return this.target;
   }
}
