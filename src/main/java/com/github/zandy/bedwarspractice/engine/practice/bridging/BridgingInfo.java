package com.github.zandy.bedwarspractice.engine.practice.bridging;

import com.github.zandy.bedwarspractice.api.utils.data.BridgingData;
import com.github.zandy.bedwarspractice.features.stats.PlayerStats;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.FinishPositions;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BridgingInfo {
    private static final HashMap<UUID, BridgingInfo> bridgingInfoMap = new HashMap<>();
    private final UUID uuid;
    private CuboidRegion cuboidRegion;
    private BridgingEnums.BridgingBlocksType blocksType;
    private BridgingEnums.BridgingLevelType levelType;
    private BridgingEnums.BridgingAngleType angleType;
    private double averageSpeed;
    private List<Block> blocksPlaced = null;
    private double practiceTime = 0.0D;
    private int placedBlocks = 0;
    private boolean infiniteBefore = false;

    public BridgingInfo(UUID uuid) {
        this.uuid = uuid;
        this.blocksType = BridgingEnums.BridgingBlocksType.BLOCKS_30;
        this.levelType = BridgingEnums.BridgingLevelType.NONE;
        this.angleType = BridgingEnums.BridgingAngleType.STRAIGHT;
        bridgingInfoMap.put(uuid, this);
    }

    public static BridgingInfo get(@NotNull UUID uuid) {
        return bridgingInfoMap.get(uuid);
    }

    public static boolean contains(@NotNull UUID uuid) {
        return bridgingInfoMap.containsKey(uuid);
    }

    public static void remove(@NotNull UUID uuid) {
        bridgingInfoMap.remove(uuid);
    }

    public static HashMap<UUID, BridgingInfo> getBridgingInfoMap() {
        return bridgingInfoMap;
    }

    public String getAverageSpeed() {
        String speed = String.valueOf(this.averageSpeed);
        return speed.equals("null") ? "0.0" : speed;
    }

    public void setAverageSpeed(String speed) {
        this.averageSpeed = Double.parseDouble(speed.replace(",", "."));
    }

    public String getPersonalBest() {
        String best = String.valueOf(PlayerStats.get(this.uuid).get(get(this.uuid).formatStatsType()));
        return best.equals("0.0") ? Language.MessagesEnum.GAME_SCOREBOARD_PERSONAL_BEST_NONE.getString(this.uuid) : best;
    }

    public void addBlockPlaced(Block block) {
        if (this.blocksPlaced == null) {
            this.blocksPlaced = new ArrayList<>();
        }
        this.blocksPlaced.add(block);
    }

    public void removeBlocksPlaced(boolean remove) {
        if (this.blocksPlaced != null) {
            if (remove) {
                this.blocksPlaced.forEach((block) -> block.setType(Material.AIR));
            }
            this.blocksPlaced = null;
        }
    }

    public void addPracticeTime() {
        this.practiceTime += 0.05D;
    }

    public void removePracticeTime() {
        this.practiceTime = 0.0D;
    }

    public void addPlacedBlocks() {
        ++this.placedBlocks;
    }

    public void removePlacedBlocks() {
        this.placedBlocks = 0;
    }

    public String formatArena() {
        return "BRIDGING-" + this.getAngleType() + "-" + this.getLevelType() + "-" + this.getBlocksType().getDistance();
    }

    public Stats.StatsType formatStatsType() {
        String prefix = "BRIDGING_";
        return Stats.StatsType.valueOf(prefix + (this.isInfinite() ? this.getBlocksType() : this.getBlocksType() + "_" + this.getLevelType() + "_" + this.getAngleType()));
    }

    public boolean isInfinite() {
        return this.getBlocksType().name().contains("INFINITE");
    }

    public double getStatistic() {
        return PlayerStats.get(this.uuid).get(this.formatStatsType());
    }

    public void setStatistic(double statistic) {
        PlayerStats.get(this.uuid).set(this.formatStatsType(), statistic);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public BridgingData toData() {
        return new BridgingData(this);
    }

    public CuboidRegion getCuboidRegion() {
        return this.cuboidRegion;
    }

    public void setCuboidRegion(Location location) {
        this.cuboidRegion = (new FinishPositions(this.formatArena(), location)).getRegion();
    }

    public BridgingEnums.BridgingBlocksType getBlocksType() {
        return this.blocksType;
    }

    public void setBlocksType(BridgingEnums.BridgingBlocksType blocksType) {
        this.blocksType = blocksType;
    }

    public BridgingEnums.BridgingLevelType getLevelType() {
        return this.levelType;
    }

    public void setLevelType(BridgingEnums.BridgingLevelType levelType) {
        this.levelType = levelType;
    }

    public BridgingEnums.BridgingAngleType getAngleType() {
        return this.angleType;
    }

    public void setAngleType(BridgingEnums.BridgingAngleType angleType) {
        this.angleType = angleType;
    }

    public List<Block> getBlocksPlaced() {
        return this.blocksPlaced;
    }

    public double getPracticeTime() {
        return this.practiceTime;
    }

    public int getPlacedBlocks() {
        return this.placedBlocks;
    }

    public boolean isInfiniteBefore() {
        return this.infiniteBefore;
    }

    public void setInfiniteBefore(boolean infiniteBefore) {
        this.infiniteBefore = infiniteBefore;
    }
}
