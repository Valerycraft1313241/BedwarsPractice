package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetupData {
   private static SetupData instance = null;
   private boolean setupDoneSchematics = false;
   private boolean setupDoneConfigurations = false;
   private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(true));
   private final List<String> requiredConfigurations = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));

   private SetupData() {
   }

   public void init() {
      ArrayList<String> var1 = new ArrayList<>(this.requiredSchematics);
      File var2 = new File("plugins/BedWarsPractice/Schematics/");
      if (var2.exists()) {
         Arrays.stream(var2.listFiles()).forEach((var1x) -> var1.remove(var1x.getName().replace(BWPUtils.getExtension(), "")));
      }

      if (var1.isEmpty()) {
         this.setupDoneSchematics = true;
      }

      ArrayList<String> var3 = new ArrayList<>(this.requiredConfigurations);
      var3.remove("FIREBALL-TNT-JUMPING");
      File var4 = new File("plugins/BedWarsPractice/Data/");
      if (var4.exists()) {
         Arrays.stream(var4.listFiles()).forEach((var1x) -> var3.remove(var1x.getName().replace(".yml", "")));
      }

      if (var3.isEmpty()) {
         this.setupDoneConfigurations = true;
      }

   }

   public static SetupData getInstance() {
      if (instance == null) {
         instance = new SetupData();
      }

      return instance;
   }

   public boolean isSetupDoneSchematics() {
      return this.setupDoneSchematics;
   }

   public boolean isSetupDoneConfigurations() {
      return this.setupDoneConfigurations;
   }

   public void setSetupDoneSchematics(boolean var1) {
      this.setupDoneSchematics = var1;
   }

   public void setSetupDoneConfigurations(boolean var1) {
      this.setupDoneConfigurations = var1;
   }
}
