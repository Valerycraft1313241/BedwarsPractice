package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetupData {
    private static SetupData instance = null;
    private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(true));
    private final List<String> requiredConfigurations = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));
    private boolean setupDoneSchematics = false;
    private boolean setupDoneConfigurations = false;

    private SetupData() {
    }

    public static SetupData getInstance() {
        if (instance == null) {
            instance = new SetupData();
        }

        return instance;
    }

    public void init() {
        ArrayList<String> missingSchematics = new ArrayList<>(this.requiredSchematics);
        File schematicsDirectory = new File("plugins/BedWarsPractice/Schematics/");
        if (schematicsDirectory.exists()) {
            Arrays.stream(schematicsDirectory.listFiles()).forEach((file) -> missingSchematics.remove(file.getName().replace(BWPUtils.getExtension(), "")));
        }

        if (missingSchematics.isEmpty()) {
            this.setupDoneSchematics = true;
        }

        ArrayList<String> missingConfigurations = new ArrayList<>(this.requiredConfigurations);
        missingConfigurations.remove("FIREBALL-TNT-JUMPING");
        File configurationsDirectory = new File("plugins/BedWarsPractice/Data/");
        if (configurationsDirectory.exists()) {
            Arrays.stream(configurationsDirectory.listFiles()).forEach((file) -> missingConfigurations.remove(file.getName().replace(".yml", "")));
        }

        if (missingConfigurations.isEmpty()) {
            this.setupDoneConfigurations = true;
        }
    }

    public boolean isSetupDoneSchematics() {
        return this.setupDoneSchematics;
    }

    public void setSetupDoneSchematics(boolean setupDoneSchematics) {
        this.setupDoneSchematics = setupDoneSchematics;
    }

    public boolean isSetupDoneConfigurations() {
        return this.setupDoneConfigurations;
    }

    public void setSetupDoneConfigurations(boolean setupDoneConfigurations) {
        this.setupDoneConfigurations = setupDoneConfigurations;
    }
}
