package com.github.zandy.bedwarspractice.engine.practice.mlg;

import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.files.language.Language;

import java.util.UUID;

public class MLGEnums {
    public enum MLGShuffleType {
        NONE,
        SHUFFLED
    }

    public enum MLGItemType {
        WATER,
        LADDER
    }

    public enum MLGTallnessType {
        BLOCKS_1(1, PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_MATERIAL.getMaterial()),
        BLOCKS_2(2, PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_MATERIAL.getMaterial()),
        BLOCKS_3(3, PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_MATERIAL.getMaterial());

        final int value;
        final Materials materials;

        MLGTallnessType(int value, Materials materials) {
            this.value = value;
            this.materials = materials;
        }

        public int getValue() {
            return this.value;
        }

        public Materials getMaterials() {
            return this.materials;
        }

    }

    public enum MLGPositionType {
        CENTER(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_MLG_POSITION_CENTER),
        RANDOM(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_MLG_POSITION_RANDOM);

        private final Language.MessagesEnum displayName;

        MLGPositionType(Language.MessagesEnum displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName(UUID playerUUID) {
            return this.displayName.getString(playerUUID);
        }

    }

    public enum MLGHeightType {
        HIGH(PracticeSettings.GameSettingsEnum.MLG_HEIGHT_HIGH.getInt(), Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_MLG_HEIGHT_HIGH),
        MEDIUM(PracticeSettings.GameSettingsEnum.MLG_HEIGHT_MEDIUM.getInt(), Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_MLG_HEIGHT_MEDIUM),
        LOW(PracticeSettings.GameSettingsEnum.MLG_HEIGHT_LOW.getInt(), Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_MLG_HEIGHT_LOW);

        final int value;
        final Language.MessagesEnum displayName;

        MLGHeightType(int value, Language.MessagesEnum displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        public int getValue() {
            return this.value;
        }

        public String getDisplayName(UUID playerUUID) {
            return this.displayName.getString(playerUUID);
        }

    }

    public enum MLGSizeType {
        LARGE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_MLG_SIZE_LARGE),
        MEDIUM(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_MLG_SIZE_MEDIUM),
        SMALL(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_MLG_SIZE_SMALL);

        private final Language.MessagesEnum displayName;

        MLGSizeType(Language.MessagesEnum displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName(UUID playerUUID) {
            return this.displayName.getString(playerUUID);
        }

    }
}
