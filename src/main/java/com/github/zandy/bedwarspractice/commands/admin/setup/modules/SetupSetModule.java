package com.github.zandy.bedwarspractice.commands.admin.setup.modules;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.utils.RelativeLocation;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.admin.setup.SetupSession;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.support.BWPVector;
import com.github.zandy.bedwarspractice.support.WESupport;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SetupSetModule {
   private static SetupSetModule instance = null;
   private static final HashMap<UUID, String> setupNameMap = new HashMap<>();
   private static final HashMap<UUID, EditSession> setupEditSessionMap = new HashMap<>();
   private final HashMap<UUID, Integer> setupOffsetMap = new HashMap<>();
   private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));
   private final List<Integer> availableOffsets = new ArrayList<>();
   private final Location baseLocation = new Location(Bukkit.getWorld("bedwars_practice_schematic_creator"), 0.0D, 25.0D, 0.0D);
   private int nextOffset = 0;

   private SetupSetModule() {
   }

   public void execute(Player var1, String var2) {
      UUID var3 = var1.getUniqueId();
      var1.sendMessage(" ");
      var1.sendMessage(" ");
      var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var3));
      if (setupNameMap.containsKey(var3)) {
         Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_ALREADY_IN_SETUP.getStringList(var3).forEach((var3x) -> {
            if (var3x.contains("/bwpa")) {
               BambooUtils.sendTextComponent(var1, var3x.replace("[Name]", var2), "/bwpa setup quit", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var3), Action.RUN_COMMAND);
            } else {
               var1.sendMessage(var3x.replace("[Name]", var2));
            }

         });
      } else if (!this.requiredSchematics.contains(var2)) {
         Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_WRONG_PRACTICE_NAME.getStringList(var3).forEach((var2x) -> {
            if (var2x.contains("/bwpa")) {
               BambooUtils.sendTextComponent(var1, var2x, "/bwpa setup list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var3), Action.RUN_COMMAND);
            } else {
               var1.sendMessage(var2x);
            }

         });
         Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
      } else {
         label41: {
            if (BWPUtils.isLegacy()) {
               if (!BWPLegacyAdapter.getInstance().getClipboardCache().containsKey(var2)) {
                  break label41;
               }
            }

            Location var4 = this.getSetupLocation(var3);
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_LOADING_SCHEMATIC.getString(var3).replace("[schemName]", var2));
            double[] var5 = (new BWPVector(var4)).toArray();
            var4.getWorld().getEntities().forEach((var0) -> {
               if (var0.getType().equals(EntityType.DROPPED_ITEM)) {
                  var0.remove();
               }

            });
            World var6 = WESupport.getWEWorld(var1.getWorld());
            getSetupEditSessionMap().put(var1.getUniqueId(), BWPLegacyAdapter.getInstance().pasteSchematic(var2, var6, var5) );
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_SCHEMATIC_LOADED.getString(var3));
            Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
            GameEngine.PracticeType var7 = var2.contains("FIREBALL") ? GameEngine.PracticeType.FIREBALL_TNT_JUMPING : GameEngine.PracticeType.valueOf(var2.split("-")[0]);
            var4.add(0.5D, 0.0D, 0.5D);
            if (GameEngine.getInstance().getPracticeFile().containsKey(var2)) {
               BambooFile var8 = GameEngine.getInstance().getPracticeFile().get(var2);
               new SetupSession(var1, var7, var2, var4, new RelativeLocation((float)var8.getInt("Position-1.X"), (float)var8.getInt("Position-1.Y"), (float)var8.getInt("Position-1.Z")), new RelativeLocation((float)var8.getInt("Position-2.X"), (float)var8.getInt("Position-2.Y"), (float)var8.getInt("Position-2.Z")));
            } else {
               new SetupSession(var1, var7, var2, var4);
            }

            var1.teleport(var4);
            Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_INFO_TUTORIAL.getStringList(var3).forEach((var1x) -> var1.sendMessage(var1x.replace("[spigotLink]", "https://bit.ly/3f4lyvF").replace("[youtubeLink]", "https://bit.ly/3BSNJXn")));
            setupNameMap.put(var1.getUniqueId(), var2);
            return;
         }

         Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_SCHEMATIC_NOT_FOUND.getStringList(var3).forEach((var3x) -> {
            if (var3x.contains("/bwpa")) {
               BambooUtils.sendTextComponent(var1, var3x, "/bwpa schem save", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(var3), Action.SUGGEST_COMMAND);
            } else {
               var1.sendMessage(var3x.replace("[schemName]", var2));
            }

         });
         Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
      }
   }

   public Location getSetupLocation(UUID var1) {
      int var2 = this.getNextOffset();
      if (!this.availableOffsets.isEmpty() && this.availableOffsets.get(0) != null) {
         var2 = this.availableOffsets.get(0);
         this.availableOffsets.remove(var2);
         this.getSetupOffsetMap().put(var1, var2);
      } else {
         short var3 = 400;
         this.nextOffset += var3;
      }

      return this.getBaseLocation().clone().add(var2, 0.0D, 0.0D);
   }

   public void removeOffset(UUID var1) {
      Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
         this.availableOffsets.add(this.getSetupOffsetMap().get(var1));
         this.getSetupOffsetMap().remove(var1);
      }, 500L);
   }

   public static SetupSetModule getInstance() {
      if (instance == null) {
         instance = new SetupSetModule();
      }

      return instance;
   }

   public static HashMap<UUID, String> getSetupNameMap() {
      return setupNameMap;
   }

   public static HashMap<UUID, EditSession> getSetupEditSessionMap() {
      return setupEditSessionMap;
   }

   public HashMap<UUID, Integer> getSetupOffsetMap() {
      return this.setupOffsetMap;
   }

   public Location getBaseLocation() {
      return this.baseLocation;
   }

   public int getNextOffset() {
      return this.nextOffset;
   }
}
