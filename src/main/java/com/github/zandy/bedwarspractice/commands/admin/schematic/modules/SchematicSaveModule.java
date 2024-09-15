package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.support.WESupport;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SchematicSaveModule {
   private static SchematicSaveModule instance = null;
   private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(true));

   private SchematicSaveModule() {
   }

   public void execute(Player var1, String var2) {
      var1.sendMessage(" ");
      var1.sendMessage(" ");
      if (!this.requiredSchematics.contains(var2)) {
         Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_WRONG_SCHEMATIC_NAME.getStringList(var1.getUniqueId()).forEach((var1x) -> {
            if (!var1x.toLowerCase().contains("/bwpa")) {
               var1.sendMessage(var1x);
            } else {
               BambooUtils.sendTextComponent(var1, var1x, "/bwpa schem list", Action.RUN_COMMAND);
            }

         });
         Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
      } else {
         WESupport.getInstance().saveSchematic(var1, var2);
         Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_SUCCESSFULLY.getString(var1.getUniqueId()).replace("[schemName]", var2));
            Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
            ArrayList<String> var3 = new ArrayList<>(this.requiredSchematics);
            File var4 = new File("plugins/BedWarsPractice/Schematics");
            if (var4.exists()) {
               Arrays.stream(var4.listFiles()).forEach((var1x) -> var3.remove(var1x.getName().replace(BWPUtils.getExtension(), "")));
            }

            if (var3.isEmpty()) {
               SetupData.getInstance().setSetupDoneSchematics(true);
            }

            GameEngine.getInstance().rebuildSchematicCache();
         }, 120L);
      }
   }

   public static SchematicSaveModule getInstance() {
      if (instance == null) {
         instance = new SchematicSaveModule();
      }

      return instance;
   }
}
