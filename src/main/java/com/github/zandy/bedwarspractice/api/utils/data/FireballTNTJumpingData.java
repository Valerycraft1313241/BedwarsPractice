package com.github.zandy.bedwarspractice.api.utils.data;

import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingEnums;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import org.bukkit.block.Block;

import java.util.List;

public class FireballTNTJumpingData implements PracticeData {
    private final FireballTNTJumpingEnums.FireballTNTJumpingAmountType amountType;
    private final FireballTNTJumpingEnums.FireballTNTJumpingItemType itemType;
    private final FireballTNTJumpingEnums.FireballTNTJumpingWoolType woolType;
    private final List<Block> blocksPlaced;

    public FireballTNTJumpingData(FireballTNTJumpingInfo fireballTNTJumpingInfo) {
        this.amountType = fireballTNTJumpingInfo.getAmountType();
        this.itemType = fireballTNTJumpingInfo.getItemType();
        this.woolType = fireballTNTJumpingInfo.getWoolType();
        this.blocksPlaced = fireballTNTJumpingInfo.getBlocksPlaced();
    }

    public FireballTNTJumpingEnums.FireballTNTJumpingAmountType getAmountType() {
        return this.amountType;
    }

    public FireballTNTJumpingEnums.FireballTNTJumpingItemType getItemType() {
        return this.itemType;
    }

    public FireballTNTJumpingEnums.FireballTNTJumpingWoolType getWoolType() {
        return this.woolType;
    }

    public List<Block> getBlocksPlaced() {
        return this.blocksPlaced;
    }
}
