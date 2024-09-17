package com.github.zandy.bedwarspractice.engine.practice.bridging;

import com.github.zandy.bedwarspractice.files.language.Language;

import java.util.UUID;

public class BridgingEnums {
    public enum BridgingAngleType {
        STRAIGHT(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ANGLE_STRAIGHT),
        DIAGONAL(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ANGLE_DIAGONAL);

        private final Language.MessagesEnum displayName;

        BridgingAngleType(Language.MessagesEnum displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName(UUID playerUUID) {
            return this.displayName.getString(playerUUID);
        }

    }

    public enum BridgingLevelType {
        NONE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_NONE),
        SLIGHT(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_SLIGHT),
        STAIRCASE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_STAIRCASE);

        private final Language.MessagesEnum displayName;

        BridgingLevelType(Language.MessagesEnum displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName(UUID playerUUID) {
            return this.displayName.getString(playerUUID);
        }

    }

    public enum BridgingBlocksType {
        BLOCKS_30,
        BLOCKS_50,
        BLOCKS_100,
        BLOCKS_INFINITE;

        public String getDisplayName(UUID playerUUID) {
            String displayName = "";
            switch (this) {
                case BLOCKS_30:
                    displayName = String.valueOf(30);
                    break;
                case BLOCKS_50:
                    displayName = String.valueOf(50);
                    break;
                case BLOCKS_100:
                    displayName = String.valueOf(100);
                    break;
                case BLOCKS_INFINITE:
                    displayName = Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_BLOCKS_INFINITE.getString(playerUUID);
            }

            return displayName;
        }

        public String getDistance() {
            return this.name().replace("BLOCKS_", "");
        }

    }
}
