package com.github.zandy.bedwarspractice.api.utils.data;

import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGEnums;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;

public class MLGData implements PracticeData {
   private final MLGEnums.MLGSizeType sizeType;
   private final MLGEnums.MLGHeightType heightType;
   private final MLGEnums.MLGPositionType positionType;
   private final MLGEnums.MLGTallnessType tallnessType;
   private final MLGEnums.MLGItemType itemType;
   private final MLGEnums.MLGShuffleType shuffleType;

   public MLGData(MLGInfo var1) {
      this.sizeType = var1.getSizeType();
      this.heightType = var1.getHeightType();
      this.positionType = var1.getPositionType();
      this.tallnessType = var1.getTallnessType();
      this.itemType = var1.getItemType();
      this.shuffleType = var1.getShuffleType();
   }

   public MLGEnums.MLGSizeType getSizeType() {
      return this.sizeType;
   }

   public MLGEnums.MLGHeightType getHeightType() {
      return this.heightType;
   }

   public MLGEnums.MLGPositionType getPositionType() {
      return this.positionType;
   }

   public MLGEnums.MLGTallnessType getTallnessType() {
      return this.tallnessType;
   }

   public MLGEnums.MLGItemType getItemType() {
      return this.itemType;
   }

   public MLGEnums.MLGShuffleType getShuffleType() {
      return this.shuffleType;
   }
}
