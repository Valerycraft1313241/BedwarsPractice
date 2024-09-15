package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;

public class SchematicListModule {
   private static SchematicListModule instance = null;
   private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(true));

   private SchematicListModule() {
   }

   public void execute(Player var1) {
      ArrayList<String> var2 = new ArrayList<>(this.requiredSchematics);
      File var3 = new File("plugins/BedWarsPractice/Schematics");
      if (var3.exists()) {
         Arrays.stream(var3.listFiles()).forEach((var1x) -> var2.remove(var1x.getName().replace(BWPUtils.getExtension(), "")));
      }

      var1.sendMessage(" ");
      var1.sendMessage(" ");
      if (var2.isEmpty()) {
         var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_ALL_CREATED.getString(var1.getUniqueId()));
         Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
      } else {
         var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
         var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_REQUIRED_SCHEMATICS.getString(var1.getUniqueId()));
         String var4 = Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_ENUMERATION.getString(var1.getUniqueId());
         var2.forEach((var2x) -> var1.sendMessage(var4.replace("[schemName]", var2x)));
         var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_INFO.getString(var1.getUniqueId()));
         Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
      }
   }

   public static SchematicListModule getInstance() {
      if (instance == null) {
         instance = new SchematicListModule();
      }

      return instance;
   }
}
