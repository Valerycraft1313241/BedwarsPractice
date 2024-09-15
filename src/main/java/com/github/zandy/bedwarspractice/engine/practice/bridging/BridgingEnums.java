package com.github.zandy.bedwarspractice.engine.practice.bridging;

import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.UUID;

public class BridgingEnums {
   public enum BridgingAngleType {
      STRAIGHT(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ANGLE_STRAIGHT),
      DIAGONAL(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ANGLE_DIAGONAL);

      private final Language.MessagesEnum displayName;

      BridgingAngleType(Language.MessagesEnum var3) {
         this.displayName = var3;
      }

      public String getDisplayName(UUID var1) {
         return this.displayName.getString(var1);
      }

   }

   public enum BridgingLevelType {
      NONE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_NONE),
      SLIGHT(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_SLIGHT),
      STAIRCASE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_STAIRCASE);

      private final Language.MessagesEnum displayName;

      BridgingLevelType(Language.MessagesEnum var3) {
         this.displayName = var3;
      }

      public String getDisplayName(UUID var1) {
         return this.displayName.getString(var1);
      }

   }

   public enum BridgingBlocksType {
      BLOCKS_30,
      BLOCKS_50,
      BLOCKS_100,
      BLOCKS_INFINITE;

      public String getDisplayName(UUID var1) {
         String var2 = "";
         switch(this) {
         case BLOCKS_30:
            var2 = String.valueOf(30);
            break;
         case BLOCKS_50:
            var2 = String.valueOf(50);
            break;
         case BLOCKS_100:
            var2 = String.valueOf(100);
            break;
         case BLOCKS_INFINITE:
            var2 = Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_BRIDGING_BLOCKS_INFINITE.getString(var1);
         }

         return var2;
      }

      public String getDistance() {
         return this.name().replace("BLOCKS_", "");
      }

   }
}
