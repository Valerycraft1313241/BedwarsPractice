package com.github.zandy.bedwarspractice.api.utils.creator;

import com.github.zandy.bedwarspractice.api.utils.data.FireballTNTJumpingData;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingEnums;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;

public class FireballTNTJumpingCreator implements PracticeCreator {
    private final FireballTNTJumpingInfo fireballTNTJumpingInfo;

    public FireballTNTJumpingCreator(FireballTNTJumpingInfo var1) {
        this.fireballTNTJumpingInfo = var1;
    }

    public FireballTNTJumpingCreator setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType var1) {
        this.fireballTNTJumpingInfo.setAmountType(var1);
        return this;
    }

    public FireballTNTJumpingCreator setItemType(FireballTNTJumpingEnums.FireballTNTJumpingItemType var1) {
        this.fireballTNTJumpingInfo.setItemType(var1);
        return this;
    }

    public FireballTNTJumpingCreator setWoolType(FireballTNTJumpingEnums.FireballTNTJumpingWoolType var1) {
        this.fireballTNTJumpingInfo.setWoolType(var1);
        return this;
    }

    public void refresh() {
        FireballTNTJumpingMode.getInstance().refresh(this.fireballTNTJumpingInfo.getPlayer(), null);
    }
}
