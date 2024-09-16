package com.github.zandy.bedwarspractice.api.utils.creator;

import com.github.zandy.bedwarspractice.api.utils.data.MLGData;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGEnums;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;

public class MLGCreator implements PracticeCreator {
    private final MLGInfo mlgInfo;

    public MLGCreator(MLGInfo var1) {
        this.mlgInfo = var1;
    }

    public MLGCreator setSizeType(MLGEnums.MLGSizeType var1) {
        this.mlgInfo.setSizeType(var1);
        return this;
    }

    public MLGCreator setHeightType(MLGEnums.MLGHeightType var1) {
        this.mlgInfo.setHeightType(var1);
        return this;
    }

    public MLGCreator setPositionType(MLGEnums.MLGPositionType var1) {
        this.mlgInfo.setPositionType(var1);
        return this;
    }

    public MLGCreator setTallnessType(MLGEnums.MLGTallnessType var1) {
        this.mlgInfo.setTallnessType(var1);
        return this;
    }

    public MLGCreator setItemType(MLGEnums.MLGItemType var1) {
        this.mlgInfo.setItemType(var1);
        return this;
    }

    public MLGCreator setShuffleType(MLGEnums.MLGShuffleType var1) {
        this.mlgInfo.setShuffleType(var1);
        return this;
    }

    public void refresh() {
        MLGMode.getInstance().refresh(this.mlgInfo.getPlayer(), null);
    }
}
