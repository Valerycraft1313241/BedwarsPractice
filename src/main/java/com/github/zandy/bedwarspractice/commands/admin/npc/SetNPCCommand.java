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
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SetNPCCommand extends SubCommand implements Listener {
   private static SetNPCCommand instance = null;
   private final List<UUID> toClickList = new ArrayList<>();
   private final HashMap<UUID, NPCStorage.NPCType> npcTypeMap = new HashMap<>();

   private SetNPCCommand() {
      super("setNPC", Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
      BambooUtils.registerEvent(this);
   }

   public void execute(CommandSender var1, String[] var2) {
      if (!(var1 instanceof Player)) {
         var1.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
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
         } else if (var2.length == 0) {
            var3.sendMessage(" ");
            var3.sendMessage(" ");
            var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var4));
            var3.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(var4));
            this.sendWrongUsageMessage(var3);
            Sounds.VILLAGER_NO.getSound().play(var3, 3.0F, 1.0F);
         } else {
            String var6 = var2[0].toLowerCase();
            byte var7 = -1;
            switch(var6.hashCode()) {
            case 108200:
               if (var6.equals("mlg")) {
                  var7 = 2;
               }
               break;
            case 194511366:
               if (var6.equals("bridging")) {
                  var7 = 1;
               }
               break;
            case 1544803905:
               if (var6.equals("default")) {
                  var7 = 0;
               }
               break;
            case 1647511919:
               if (var6.equals("fireballtntjumping")) {
                  var7 = 3;
               }
            }

            NPCStorage.NPCType var5;
            switch(var7) {
            case 0:
            case 1:
            case 2:
               var5 = NPCStorage.NPCType.valueOf(var2[0].toUpperCase());
               break;
            case 3:
               var5 = NPCStorage.NPCType.FIREBALL_TNT_JUMPING;
               break;
            default:
               this.sendWrongUsageMessage(var3);
               return;
            }

            var3.sendMessage(" ");
            var3.sendMessage(" ");
            var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var4));
            var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_CLICK_TO_APPLY.getString(var4));
            this.toClickList.add(var4);
            this.npcTypeMap.put(var4, var5);
            Sounds.PLAYER_LEVELUP.getSound().play(var3, 3.0F, 3.0F);
         }
      }
   }

   private void sendFormattedComponent(Player var1, String var2, String var3) {
      BambooUtils.sendTextComponent(var1, var2, "/bwpa setNPC " + var3, Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var1.getUniqueId()), Action.RUN_COMMAND);
   }

   private void sendWrongUsageMessage(Player var1) {
      Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_WRONG_USAGE.getStringList(var1.getUniqueId()).forEach((var2) -> {
         String var3 = var2.toLowerCase();
         if (var3.contains("default")) {
            this.sendFormattedComponent(var1, var2, "Default");
         } else if (var3.contains("bridging")) {
            this.sendFormattedComponent(var1, var2, "Bridging");
         } else if (var3.contains("mlg")) {
            this.sendFormattedComponent(var1, var2, "MLG");
         } else if (var3.contains("fireball/tnt jumping")) {
            this.sendFormattedComponent(var1, var2, "FireballTntJumping");
         } else {
            var1.sendMessage(var2);
         }

      });
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   private void onNPCRightClick(NPCRightClickEvent var1) {
      var1.setCancelled(this.clickFunction(var1.getClicker(), var1.getClicker().getUniqueId(), var1.getNPC().getId()));
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   private void onNPCLeftClick(NPCLeftClickEvent var1) {
      var1.setCancelled(this.clickFunction(var1.getClicker(), var1.getClicker().getUniqueId(), var1.getNPC().getId()));
   }

   private boolean clickFunction(Player var1, UUID var2, int var3) {
      if (!this.toClickList.contains(var2)) {
         return false;
      } else {
         NPCStorage.NPCType var4 = this.npcTypeMap.get(var2);
         if (NPCStorage.getInstance().contains(var4, var3)) {
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var2));
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_ALREADY_EXISTS.getString(var2));
            Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
            return false;
         } else {
            NPCStorage.getInstance().add(var4, var3);
            PracticeNPC.getInstance().spawnNPC(CitizensAPI.getNPCRegistry().getById(var3), var3, var4);
            this.toClickList.remove(var2);
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var2));
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_ADDED.getString(var2));
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_TYPE_ADDED.getString(var2).replace("[practiceType]", var4.getType().getString(var2)));
            Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
            return true;
         }
      }
   }

   public boolean canSee(CommandSender var1) {
      Lobby var2 = Lobby.getInstance();
      return var1 instanceof Player && this.hasPermission(var1) && var2.isSet() && var2.get().getWorld().equals(((Player)var1).getWorld());
   }

   public static SetNPCCommand getInstance() {
      if (instance == null) {
         instance = new SetNPCCommand();
      }

      return instance;
   }
}
