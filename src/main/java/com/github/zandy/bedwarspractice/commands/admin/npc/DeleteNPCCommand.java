package com.github.zandy.bedwarspractice.commands.admin.npc;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.features.npc.PracticeNPC;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.NPCStorage;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DeleteNPCCommand extends SubCommand implements Listener {
   private static DeleteNPCCommand instance = null;
   private final List<UUID> toClickList = new ArrayList<>();
   private final HashMap<UUID, NPCStorage.NPCType> npcTypeMap = new HashMap<>();

   private DeleteNPCCommand() {
      super("deleteNPC", Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
      BambooUtils.registerEvent(this);
   }

   public void execute(CommandSender var1, String[] var2) {
      if (!(var1 instanceof Player)) {
         var1.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getPath());
      } else {
         Player var3 = (Player)var1;
         UUID var4 = var3.getUniqueId();
         if (!Lobby.getInstance().isSet()) {
            var3.sendMessage(" ");
            var3.sendMessage(" ");
            var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var4));
            List<String> var10000 = Language.MessagesEnum.PLUGIN_LOBBY_NOT_SET.getStringList(var4);
            Objects.requireNonNull(var3);
            var10000.forEach(var3::sendMessage);
            Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
         } else {
            boolean var5 = true;
            NPCStorage.NPCType[] var6 = NPCStorage.NPCType.values();

             for (NPCStorage.NPCType var9 : var6) {
                 if (!NPCStorage.getInstance().isEmpty(var9)) {
                     var5 = false;
                 }
             }

            if (var5) {
               var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var4));
               var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_NO_NPCS_SET.getString());
            } else {
               var3.sendMessage(" ");
               var3.sendMessage(" ");
               var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var4));
               var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_CLICK_TO_DELETE.getString(var4));
               this.toClickList.add(var4);
               Sounds.PLAYER_LEVELUP.getSound().play(var3, 3.0F, 3.0F);
            }
         }
      }
   }

   @EventHandler
   private void onNPCRightClick(NPCRightClickEvent var1) {
      this.clickFunction(var1.getClicker(), var1.getClicker().getUniqueId(), var1.getNPC().getId());
   }

   @EventHandler
   private void onNPCLeftClick(NPCLeftClickEvent var1) {
      this.clickFunction(var1.getClicker(), var1.getClicker().getUniqueId(), var1.getNPC().getId());
   }

   private void clickFunction(Player var1, UUID var2, int var3) {
      if (this.toClickList.contains(var2)) {
         boolean var4 = false;
         NPCStorage.NPCType var5 = null;
         NPCStorage.NPCType[] var6 = NPCStorage.NPCType.values();

          for (NPCStorage.NPCType var9 : var6) {
              if (NPCStorage.getInstance().getIDList(var9).contains(var3)) {
                  var4 = true;
                  var5 = var9;
                  break;
              }
          }

         if (!var4) {
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var2));
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_NOT_PRACTICE.getString(var2));
            Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
         } else {
            NPCStorage.getInstance().remove(var5, var3);
            PracticeNPC.getInstance().removeNPC(var3);
            this.toClickList.remove(var2);
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var2));
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_DELETED.getString(var2));
            Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
         }
      }
   }

   public boolean canSee(CommandSender var1) {
      Lobby var2 = Lobby.getInstance();
      return var1 instanceof Player && this.hasPermission(var1) && var2.isSet() && var2.get().getWorld().equals(((Player)var1).getWorld());
   }

   public static DeleteNPCCommand getInstance() {
      if (instance == null) {
         instance = new DeleteNPCCommand();
      }

      return instance;
   }
}
