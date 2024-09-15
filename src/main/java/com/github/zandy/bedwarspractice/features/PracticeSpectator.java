package com.github.zandy.bedwarspractice.features;

import com.github.zandy.bamboolib.versionsupport.VersionSupport;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PracticeSpectator implements Listener {
   private static final HashMap<UUID, PracticeSpectator> practiceSpectators = new HashMap<>();
   private final Player target;

   public PracticeSpectator(Player var1, Player var2) {
      this.target = var2;
      var1.setGameMode(GameMode.SPECTATOR);
      UUID var3 = var1.getUniqueId();
      UUID var4 = var2.getUniqueId();
      practiceSpectators.put(var3, this);
      GameEngine var5 = GameEngine.getInstance();
      var1.sendMessage(" ");
      var1.sendMessage(" ");
      Language.MessagesEnum.SPECTATE_SPECTATING.getStringList(var3).forEach((var5x) -> {
         if (var5x.contains("[player_name]")) {
            var1.sendMessage(var5x.replace("[player_name]", var2.getName()));
         }

         if (var5x.contains("[practice_mode]")) {
            var1.sendMessage(var5x.replace("[practice_mode]", var5.getPracticeTypeMap().get(var4).getDisplayName(var3)));
         }

         if (var5x.contains("[current_settings]")) {
            var1.sendMessage(var5x.replace("[current_settings]", var5.getCurrentGameSettings(var4, var3)));
         }

      });
      VersionSupport.getInstance().sendTitle(var1, Language.MessagesEnum.SPECTATE_TITLE.getString(var3), Language.MessagesEnum.SPECTATE_SUBTITLE.getString(var3).replace("[player_name]", var2.getName()));
      var1.teleport(var5.getPracticeLocationMap().get(var4).clone().add(0.0D, 1.5D, 0.0D));
      var5.getSpectators().get(var4).add(var3);
   }

   public static PracticeSpectator get(UUID var0) {
      return practiceSpectators.get(var0);
   }

   public static boolean isSpectating(UUID var0) {
      return practiceSpectators.containsKey(var0);
   }

   public static void remove(UUID var0) {
      UUID var1 = get(var0).getTarget().getUniqueId();
      HashMap<UUID, List<UUID>> var2 = GameEngine.getInstance().getSpectators();
      if (var2.containsKey(var1)) {
         var2.get(var1).remove(var0);
      }

      practiceSpectators.remove(var0);
   }

   public static HashMap<UUID, PracticeSpectator> getPracticeSpectators() {
      return practiceSpectators;
   }

   public Player getTarget() {
      return this.target;
   }
}
