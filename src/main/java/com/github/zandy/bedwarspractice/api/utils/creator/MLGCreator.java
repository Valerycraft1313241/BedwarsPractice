package com.github.zandy.bedwarspractice.api.utils.creator;

import com.github.zandy.bedwarspractice.api.utils.data.MLGData;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGEnums;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;

public class MLGCreator implements PracticeCreator {
    private final MLGInfo mlgInfo;

    public MLGCreator(MLGInfo mlgInfo) {
        this.mlgInfo = mlgInfo;
    }

    public MLGCreator setSizeType(MLGEnums.MLGSizeType sizeType) {
        this.mlgInfo.setSizeType(sizeType);
        return this;
    }

    public MLGCreator setHeightType(MLGEnums.MLGHeightType heightType) {
        this.mlgInfo.setHeightType(heightType);
        return this;
    }

    public MLGCreator setPositionType(MLGEnums.MLGPositionType positionType) {
        this.mlgInfo.setPositionType(positionType);
        return this;
    }

    public MLGCreator setTallnessType(MLGEnums.MLGTallnessType tallnessType) {
        this.mlgInfo.setTallnessType(tallnessType);
        return this;
    }

    public MLGCreator setItemType(MLGEnums.MLGItemType itemType) {
        this.mlgInfo.setItemType(itemType);
        return this;
    }

    public MLGCreator setShuffleType(MLGEnums.MLGShuffleType shuffleType) {
        this.mlgInfo.setShuffleType(shuffleType);
        return this;
    }

    public void refresh() {
        MLGMode.getInstance().refresh(this.mlgInfo.getPlayer(), null);
    }
}
