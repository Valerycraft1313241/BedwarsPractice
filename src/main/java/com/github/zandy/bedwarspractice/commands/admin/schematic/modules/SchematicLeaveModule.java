package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.admin.schematic.SchematicWorldCreator;
import com.github.zandy.bedwarspractice.commands.admin.setup.SetupSession;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class SchematicLeaveModule implements Listener {
   private static SchematicLeaveModule instance = null;

   private SchematicLeaveModule() {
   }

   public void execute(Player var1) {
      if (SchematicWorldCreator.isInSchematicWorld(var1.getWorld())) {
         if (SetupSession.exists(var1.getUniqueId())) {
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LEAVE_SETUP_NOT_FINISHED.getStringList(var1.getUniqueId()).forEach((var1x) -> {
               if (var1x.toLowerCase().contains("/bwpa setup quit")) {
                  BambooUtils.sendTextComponent(var1, var1x, "/bwpa setup quit", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var1.getUniqueId()), Action.RUN_COMMAND);
               } else {
                  var1.sendMessage(var1x);
               }

            });
         } else {
            this.unloadModule(var1);
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
            List<String> var10000 = Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LEAVE_SUCCESSFULLY.getStringList(var1.getUniqueId());
            Objects.requireNonNull(var1);
            var10000.forEach(var1::sendMessage);
            Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
         }
      }
   }

   @EventHandler
   private void onPlayerQuit(PlayerQuitEvent var1) {
      if (SchematicWorldCreator.isInSchematicWorld(var1.getPlayer().getWorld())) {
         this.unloadModule(var1.getPlayer());
      }

   }

   private void unloadModule(Player var1) {
      Location var2 = Lobby.getInstance().get();
      (new ArrayList<>(var1.getWorld().getPlayers())).forEach((var1x) -> var1x.teleport(var2));
      SchematicWorldCreator.unload();
   }

   public static SchematicLeaveModule getInstance() {
      if (instance == null) {
         instance = new SchematicLeaveModule();
      }

      return instance;
   }
}
