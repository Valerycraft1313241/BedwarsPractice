package com.github.zandy.bedwarspractice.api.utils.creator;

import com.github.zandy.bedwarspractice.api.utils.data.BridgingData;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingEnums;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;

public class BridgingCreator implements PracticeCreator {
   private final BridgingInfo bridgingInfo;

   public BridgingCreator(BridgingInfo var1) {
      this.bridgingInfo = var1;
   }

   public BridgingCreator setBlocksType(BridgingEnums.BridgingBlocksType var1) {
      this.bridgingInfo.setBlocksType(var1);
      return this;
   }

   public BridgingCreator setLevelType(BridgingEnums.BridgingLevelType var1) {
      this.bridgingInfo.setLevelType(var1);
      return this;
   }

   public BridgingCreator setAngleType(BridgingEnums.BridgingAngleType var1) {
      this.bridgingInfo.setAngleType(var1);
      return this;
   }

   public void refresh() {
      BridgingMode.getInstance().refresh(this.bridgingInfo.getPlayer(), null);
   }
}
