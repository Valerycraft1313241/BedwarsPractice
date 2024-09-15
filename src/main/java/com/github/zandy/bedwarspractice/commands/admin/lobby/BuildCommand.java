package com.github.zandy.bedwarspractice.commands.admin.lobby;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand extends SubCommand {
   private static BuildCommand instance = null;
   private final List<UUID> buildList = new ArrayList<>();

   private BuildCommand() {
      super("build", Language.MessagesEnum.COMMAND_ADMIN_BUILD_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
   }

   public void execute(CommandSender var1, String[] var2) {
      if (!(var1 instanceof Player)) {
         var1.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
      } else {
         Player var3 = (Player)var1;
         var3.sendMessage(" ");
         var3.sendMessage(" ");
         var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3.getUniqueId()));
         var3.sendMessage((this.buildList.contains(var3.getUniqueId()) ? Language.MessagesEnum.COMMAND_ADMIN_BUILD_DISABLED : Language.MessagesEnum.COMMAND_ADMIN_BUILD_ENABLED).getString(var3.getUniqueId()));
         if (this.buildList.contains(var3.getUniqueId())) {
            this.buildList.remove(var3.getUniqueId());
         } else {
            this.buildList.add(var3.getUniqueId());
         }

         Sounds.PLAYER_LEVELUP.getSound().play(var3, 3.0F, 3.0F);
      }
   }

   public static BuildCommand getInstance() {
      if (instance == null) {
         instance = new BuildCommand();
      }

      return instance;
   }

   public boolean canSee(CommandSender var1) {
      return this.hasPermission(var1);
   }

   public List<UUID> getBuildList() {
      return this.buildList;
   }
}
