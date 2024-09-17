package com.github.zandy.bedwarspractice.api.utils.creator;

import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingEnums;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;

public class BridgingCreator implements PracticeCreator {
    private final BridgingInfo bridgingInfo;

    public BridgingCreator(BridgingInfo bridgingInfo) {
        this.bridgingInfo = bridgingInfo;
    }

    public BridgingCreator setBlocksType(BridgingEnums.BridgingBlocksType blocksType) {
        this.bridgingInfo.setBlocksType(blocksType);
        return this;
    }

    public BridgingCreator setLevelType(BridgingEnums.BridgingLevelType levelType) {
        this.bridgingInfo.setLevelType(levelType);
        return this;
    }

    public BridgingCreator setAngleType(BridgingEnums.BridgingAngleType angleType) {
        this.bridgingInfo.setAngleType(angleType);
        return this;
    }

    public void refresh() {
        BridgingMode.getInstance().refresh(this.bridgingInfo.getPlayer(), null);
    }
}
