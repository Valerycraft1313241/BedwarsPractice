package com.github.zandy.bedwarspractice.api.utils.data;

import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingEnums;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import org.bukkit.block.Block;

import java.util.List;

public class BridgingData implements PracticeData {
    private final BridgingEnums.BridgingAngleType angleType;
    private final BridgingEnums.BridgingLevelType levelType;
    private final BridgingEnums.BridgingBlocksType blocksType;
    private final List<Block> blocksPlaced;
    private final String personalBest;
    private final String practiceName;
    private final Stats.StatsType statsType;
    private final double duration;
    private final int placedBlocks;

    public BridgingData(BridgingInfo bridgingInfo) {
        this.angleType = bridgingInfo.getAngleType();
        this.levelType = bridgingInfo.getLevelType();
        this.blocksType = bridgingInfo.getBlocksType();
        this.blocksPlaced = bridgingInfo.getBlocksPlaced();
        this.personalBest = bridgingInfo.getPersonalBest();
        this.practiceName = bridgingInfo.formatArena();
        this.statsType = bridgingInfo.formatStatsType();
        this.duration = bridgingInfo.getPracticeTime();
        this.placedBlocks = bridgingInfo.getPlacedBlocks();
    }

    public BridgingEnums.BridgingAngleType getAngleType() {
        return this.angleType;
    }

    public BridgingEnums.BridgingLevelType getLevelType() {
        return this.levelType;
    }

    public BridgingEnums.BridgingBlocksType getBlocksType() {
        return this.blocksType;
    }

    public List<Block> getBlocksPlaced() {
        return this.blocksPlaced;
    }

    public String getPersonalBest() {
        return this.personalBest;
    }

    public String getPracticeName() {
        return this.practiceName;
    }

    public Stats.StatsType getStatsType() {
        return this.statsType;
    }

    public double getDuration() {
        return this.duration;
    }

    public int getPlacedBlocks() {
        return this.placedBlocks;
    }
}
