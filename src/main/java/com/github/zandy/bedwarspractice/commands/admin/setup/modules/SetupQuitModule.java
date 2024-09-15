package com.github.zandy.bedwarspractice.commands.admin.setup.modules;

import com.github.zandy.bedwarspractice.commands.admin.setup.SetupSession;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.sk89q.worldedit.EditSession;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class SetupQuitModule implements Listener {
   private static SetupQuitModule instance = null;

   private SetupQuitModule() {
   }

   public void execute(Player var1) {
      UUID var2 = var1.getUniqueId();
      if (!SetupSetModule.getSetupNameMap().containsKey(var2)) {
         var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_QUIT_NOT_IN_SETUP.getString(var2));
      } else {
         this.executeRemove(var1, var1.getLocation().getWorld().getSpawnLocation().clone().add(0.5D, 1.0D, 0.5D), (EditSession)SetupSetModule.getSetupEditSessionMap().get(var2));
      }
   }

   @EventHandler
   private void onPlayerQuit(PlayerQuitEvent var1) {
      Player var2 = var1.getPlayer();
      if (SetupSetModule.getSetupEditSessionMap().containsKey(var2.getUniqueId())) {
         this.executeRemove(var2, Lobby.getInstance().get(), SetupSetModule.getSetupEditSessionMap().get(var2.getUniqueId()));
      }
   }

   private void executeRemove(Player var1, Location var2, EditSession var3) {
      SetupSetModule.getSetupNameMap().remove(var1.getUniqueId());
      var1.teleport(var2);
      var3.undo(var3);
      SetupSetModule.getSetupEditSessionMap().remove(var1.getUniqueId());
      SetupSession.remove(var1.getUniqueId());
      SetupSetModule.getInstance().removeOffset(var1.getUniqueId());
      var1.sendMessage(" ");
      var1.sendMessage(" ");
      var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
      List<String> var10000 = Language.MessagesEnum.COMMAND_ADMIN_SETUP_QUIT_SUCCESSFULLY.getStringList(var1.getUniqueId());
      Objects.requireNonNull(var1);
      var10000.forEach(var1::sendMessage);
   }

   public static SetupQuitModule getInstance() {
      if (instance == null) {
         instance = new SetupQuitModule();
      }

      return instance;
   }
}
