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

    public BridgingData(BridgingInfo var1) {
        this.angleType = var1.getAngleType();
        this.levelType = var1.getLevelType();
        this.blocksType = var1.getBlocksType();
        this.blocksPlaced = var1.getBlocksPlaced();
        this.personalBest = var1.getPersonalBest();
        this.practiceName = var1.formatArena();
        this.statsType = var1.formatStatsType();
        this.duration = var1.getPracticeTime();
        this.placedBlocks = var1.getPlacedBlocks();
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
