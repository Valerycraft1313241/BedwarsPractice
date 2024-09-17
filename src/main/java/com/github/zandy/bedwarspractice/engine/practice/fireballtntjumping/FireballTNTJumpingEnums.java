package com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping;

import com.github.zandy.bedwarspractice.features.stats.Stats;
import com.github.zandy.bedwarspractice.files.language.Language;

import java.util.UUID;

public class FireballTNTJumpingEnums {
    public enum FireballTNTJumpingWoolType {
        DISABLE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_WOOL_DISABLED),
        ENABLE(Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_WOOL_ENABLED);

        final Language.MessagesEnum displayName;

        FireballTNTJumpingWoolType(Language.MessagesEnum messagesEnum) {
            this.displayName = messagesEnum;
        }

        public String getDisplayName(UUID uuid) {
            return this.displayName.getString(uuid);
        }

    }

    public enum FireballTNTJumpingItemType {
        FIREBALL(Stats.StatsType.FIREBALL_TNT_JUMPING_LONGEST_JUMP_FIREBALL, "Fireball"),
        TNT(Stats.StatsType.FIREBALL_TNT_JUMPING_LONGEST_JUMP_TNT, "TNT");

        final Stats.StatsType value;
        final String valueName;

        FireballTNTJumpingItemType(Stats.StatsType statsType, String valueName) {
            this.value = statsType;
            this.valueName = valueName;
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

        FireballTNTJumpingAmountType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

    }
}
