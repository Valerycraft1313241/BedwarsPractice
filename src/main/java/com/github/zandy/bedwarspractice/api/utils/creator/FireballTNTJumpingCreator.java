package com.github.zandy.bedwarspractice.api.utils.creator;

import com.github.zandy.bedwarspractice.api.utils.data.FireballTNTJumpingData;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingEnums;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;

public class FireballTNTJumpingCreator implements PracticeCreator {
    private final FireballTNTJumpingInfo fireballTNTJumpingInfo;

    public FireballTNTJumpingCreator(FireballTNTJumpingInfo fireballTNTJumpingInfo) {
        this.fireballTNTJumpingInfo = fireballTNTJumpingInfo;
    }

    public FireballTNTJumpingCreator setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType amountType) {
        this.fireballTNTJumpingInfo.setAmountType(amountType);
        return this;
    }

    public FireballTNTJumpingCreator setItemType(FireballTNTJumpingEnums.FireballTNTJumpingItemType itemType) {
        this.fireballTNTJumpingInfo.setItemType(itemType);
        return this;
    }

    public FireballTNTJumpingCreator setWoolType(FireballTNTJumpingEnums.FireballTNTJumpingWoolType woolType) {
        this.fireballTNTJumpingInfo.setWoolType(woolType);
        return this;
    }

    public void refresh() {
        FireballTNTJumpingMode.getInstance().refresh(this.fireballTNTJumpingInfo.getPlayer(), null);
    }
}
