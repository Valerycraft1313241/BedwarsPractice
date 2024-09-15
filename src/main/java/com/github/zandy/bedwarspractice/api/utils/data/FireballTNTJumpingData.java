package com.github.zandy.bedwarspractice.api.utils.data;

import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingEnums;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import java.util.List;
import org.bukkit.block.Block;

public class FireballTNTJumpingData implements PracticeData {
   private final FireballTNTJumpingEnums.FireballTNTJumpingAmountType amountType;
   private final FireballTNTJumpingEnums.FireballTNTJumpingItemType itemType;
   private final FireballTNTJumpingEnums.FireballTNTJumpingWoolType woolType;
   private final List<Block> blocksPlaced;

   public FireballTNTJumpingData(FireballTNTJumpingInfo var1) {
      this.amountType = var1.getAmountType();
      this.itemType = var1.getItemType();
      this.woolType = var1.getWoolType();
      this.blocksPlaced = var1.getBlocksPlaced();
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
