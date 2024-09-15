package com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping;

import com.github.zandy.bedwarspractice.features.stats.Stats;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.UUID;

public class FireballTNTJumpingEnums {
   public enum FireballTNTJumpingWoolType {
      DISABLE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_WOOL_DISABLED),
      ENABLE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_WOOL_ENABLED);

      final Language.MessagesEnum displayName;

      FireballTNTJumpingWoolType(Language.MessagesEnum var3) {
         this.displayName = var3;
      }

      public String getDisplayName(UUID var1) {
         return this.displayName.getString(var1);
      }

   }

   public enum FireballTNTJumpingItemType {
      FIREBALL(Stats.StatsType.FIREBALL_TNT_JUMPING_LONGEST_JUMP_FIREBALL, "Fireball"),
      TNT(Stats.StatsType.FIREBALL_TNT_JUMPING_LONGEST_JUMP_TNT, "TNT");

      final Stats.StatsType value;
      final String valueName;

      FireballTNTJumpingItemType(Stats.StatsType var3, String var4) {
         this.value = var3;
         this.valueName = var4;
      }

      public Stats.StatsType getValue() {
         return this.value;
      }

      public String getValueName() {
         return this.valueName;
      }

   }

   public enum FireballTNTJumpingAmountType {
      AMOUNT_1(1),
      AMOUNT_2(2),
      AMOUNT_5(5);

      final int value;

      FireballTNTJumpingAmountType(int var3) {
         this.value = var3;
      }

      public int getValue() {
         return this.value;
      }

   }
}
