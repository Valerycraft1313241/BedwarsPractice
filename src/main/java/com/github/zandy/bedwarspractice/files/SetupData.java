package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SetupData {
    private static final SetupData INSTANCE = new SetupData();
   private boolean setupDoneSchematics = false;
   private boolean setupDoneConfigurations = false;
   private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(true));
   private final List<String> requiredConfigurations = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));

   private SetupData() {
      // Private constructor to prevent instantiation
   }

   public static SetupData getInstance() {
      return INSTANCE;
   }

   public void init() {
      List<String> missingSchematics = new ArrayList<>(this.requiredSchematics);
      File schematicsDir = new File("plugins/BedWarsPractice/Schematics/");
      if (schematicsDir.exists()) {
         Arrays.stream(schematicsDir.listFiles()).forEach(file -> missingSchematics.remove(file.getName().replace(BWPUtils.getExtension(), "")));
      }

      if (missingSchematics.isEmpty()) {
         this.setupDoneSchematics = true;
      }

      List<String> missingConfigurations = new ArrayList<>(this.requiredConfigurations);
      missingConfigurations.remove("FIREBALL-TNT-JUMPING");
      File dataDir = new File("plugins/BedWarsPractice/Data/");
      if (dataDir.exists()) {
         Arrays.stream(dataDir.listFiles()).forEach(file -> missingConfigurations.remove(file.getName().replace(".yml", "")));
      }

      if (missingConfigurations.isEmpty()) {
         this.setupDoneConfigurations = true;
      }
   }

   public boolean isSetupDoneSchematics() {
      return this.setupDoneSchematics;
   }

   public boolean isSetupDoneConfigurations() {
      return this.setupDoneConfigurations;
   }

   public void setSetupDoneSchematics(boolean setupDoneSchematics) {
      this.setupDoneSchematics = setupDoneSchematics;
   }

   public void setSetupDoneConfigurations(boolean setupDoneConfigurations) {
      this.setupDoneConfigurations = setupDoneConfigurations;
   }
}
