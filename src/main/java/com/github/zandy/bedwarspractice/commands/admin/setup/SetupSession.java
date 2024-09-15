package com.github.zandy.bedwarspractice.commands.admin.setup;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.utils.RelativeLocation;
import com.github.zandy.bedwarspractice.commands.admin.setup.modules.SetupSetModule;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import com.sk89q.worldedit.EditSession;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetupSession {
   private final GameEngine.PracticeType pType;
   private final String name;
   private final Location spawnLocation;
   private final UUID uuid;
   private RelativeLocation relativePosition1;
   private RelativeLocation relativePosition2;
   private static final HashMap<UUID, SetupSession> setupSessionMap = new HashMap<>();
   private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));

   public SetupSession(Player var1, GameEngine.PracticeType var2, String var3, Location var4) {
      this.pType = var2;
      this.name = var3;
      this.spawnLocation = var4;
      this.uuid = var1.getUniqueId();
      setupSessionMap.put(this.uuid, this);
   }

   public SetupSession(Player var1, GameEngine.PracticeType var2, String var3, Location var4, RelativeLocation var5, RelativeLocation var6) {
      this.pType = var2;
      this.name = var3;
      this.spawnLocation = var4;
      this.uuid = var1.getUniqueId();
      this.relativePosition1 = var5;
      this.relativePosition2 = var6;
      setupSessionMap.put(this.uuid, this);
   }

   public static boolean exists(UUID var0) {
      return setupSessionMap.containsKey(var0);
   }

   public static SetupSession get(UUID var0) {
      return setupSessionMap.get(var0);
   }

   public static void remove(UUID var0) {
      setupSessionMap.remove(var0);
   }

   public void setRelativePosition(int var1, RelativeLocation var2) {
      if (var1 == 1) {
         this.relativePosition1 = var2;
      } else {
         this.relativePosition2 = var2;
      }

   }

   public boolean containsData() {
      Player var1 = Bukkit.getPlayer(this.uuid);
      if (!this.pType.equals(GameEngine.PracticeType.BRIDGING) && !this.pType.equals(GameEngine.PracticeType.MLG)) {
         return true;
      } else {
         byte var2;
         if (this.relativePosition1 == null) {
            var2 = 1;
         } else if (this.relativePosition2 == null) {
            var2 = 2;
         } else {
             var2 = 0;
         }

          if (var2 == 0) {
            return true;
         } else {
            Language.MessagesEnum.COMMAND_ADMIN_SETUP_SAVE_POSITION_NOT_SET.getStringList(this.uuid).forEach((var3) -> {
               String var4 = var3.replace("[posNumber]", String.valueOf(var2));
               if (var3.contains("/bwpa setup pos")) {
                  BambooUtils.sendTextComponent(var1, var4, "/bwpa setup pos", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(this.uuid), Action.SUGGEST_COMMAND);
               } else {
                  var1.sendMessage(var4);
               }

            });
            return false;
         }
      }
   }

   public boolean save(EditSession var1) {
      if (!this.containsData()) {
         return false;
      } else {
         BambooFile var2 = new BambooFile(this.getName(), "Data");
         var2.set("Position-1.X", this.relativePosition1.getRelativeX());
         var2.set("Position-1.Y", this.relativePosition1.getRelativeY());
         var2.set("Position-1.Z", this.relativePosition1.getRelativeZ());
         var2.set("Position-2.X", this.relativePosition2.getRelativeX());
         var2.set("Position-2.Y", this.relativePosition2.getRelativeY());
         var2.set("Position-2.Z", this.relativePosition2.getRelativeZ());
         Player var3 = Bukkit.getPlayer(this.uuid);
         var3.sendMessage(" ");
         var3.sendMessage(" ");
         var3.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(this.uuid));
         var3.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SAVE_SUCCESSFULLY.getString(this.uuid).replace("[practiceName]", this.name));
         var1.undo(var1);
         setupSessionMap.remove(this.uuid);
         ArrayList<String> var4 = new ArrayList<>(this.requiredSchematics);
         var4.remove("FIREBALL-TNT-JUMPING");
         File var5 = new File("plugins/BedWarsPractice/Data");
         if (var5.exists()) {
            Arrays.stream(var5.listFiles()).forEach((var1x) -> var4.remove(var1x.getName().replace(".yml", "")));
         }

         if (var4.isEmpty()) {
            SetupData.getInstance().setSetupDoneConfigurations(true);
         }

         SetupSetModule.getSetupNameMap().remove(this.uuid);
         SetupSetModule.getInstance().removeOffset(this.uuid);
         var3.getWorld().getSpawnLocation().clone().add(0.5D, 1.0D, 0.5D);
         return true;
      }
   }

   public String getName() {
      return this.name;
   }

   public Location getSpawnLocation() {
      return this.spawnLocation;
   }
}
