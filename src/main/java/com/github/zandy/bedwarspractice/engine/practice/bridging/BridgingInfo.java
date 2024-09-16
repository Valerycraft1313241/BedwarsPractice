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

    public BridgingInfo(UUID var1) {
        this.uuid = var1;
        this.blocksType = BridgingEnums.BridgingBlocksType.BLOCKS_30;
        this.levelType = BridgingEnums.BridgingLevelType.NONE;
        this.angleType = BridgingEnums.BridgingAngleType.STRAIGHT;
        bridgingInfoMap.put(var1, this);
    }

    public static BridgingInfo get(@NotNull UUID var0) {

        return bridgingInfoMap.get(var0);
    }

    public static boolean contains(@NotNull UUID var0) {

        return bridgingInfoMap.containsKey(var0);
    }

    public static void remove(@NotNull UUID var0) {

        bridgingInfoMap.remove(var0);
    }

    public static HashMap<UUID, BridgingInfo> getBridgingInfoMap() {
        return bridgingInfoMap;
    }

    public String getAverageSpeed() {
        String var1 = String.valueOf(this.averageSpeed);
        return var1.equals("null") ? "0.0" : var1;
    }

    public void setAverageSpeed(String var1) {
        this.averageSpeed = Double.parseDouble(var1.replace(",", "."));
    }

    public String getPersonalBest() {
        String var1 = String.valueOf(PlayerStats.get(this.uuid).get(get(this.uuid).formatStatsType()));
        return var1.equals("0.0") ? Language.MessagesEnum.GAME_SCOREBOARD_PERSONAL_BEST_NONE.getString(this.uuid) : var1;
    }

    public void addBlockPlaced(Block var1) {
        if (this.blocksPlaced == null) {
            this.blocksPlaced = new ArrayList<>();
        }

        this.blocksPlaced.add(var1);
    }

    public void removeBlocksPlaced(boolean var1) {
        if (this.blocksPlaced != null) {
            if (var1) {
                this.blocksPlaced.forEach((var0) -> var0.setType(Material.AIR));
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
        String var1 = "BRIDGING_";
        return Stats.StatsType.valueOf(var1 + (this.isInfinite() ? this.getBlocksType() : this.getBlocksType() + "_" + this.getLevelType() + "_" + this.getAngleType()));
    }

    public boolean isInfinite() {
        return this.getBlocksType().name().contains("INFINITE");
    }

    public double getStatistic() {
        return PlayerStats.get(this.uuid).get(this.formatStatsType());
    }

    public void setStatistic(double var1) {
        PlayerStats.get(this.uuid).set(this.formatStatsType(), var1);
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

    public void setCuboidRegion(Location var1) {
        this.cuboidRegion = (new FinishPositions(this.formatArena(), var1)).getRegion();
    }

    public BridgingEnums.BridgingBlocksType getBlocksType() {
        return this.blocksType;
    }

    public void setBlocksType(BridgingEnums.BridgingBlocksType var1) {
        this.blocksType = var1;
    }

    public BridgingEnums.BridgingLevelType getLevelType() {
        return this.levelType;
    }

    public void setLevelType(BridgingEnums.BridgingLevelType var1) {
        this.levelType = var1;
    }

    public BridgingEnums.BridgingAngleType getAngleType() {
        return this.angleType;
    }

    public void setAngleType(BridgingEnums.BridgingAngleType var1) {
        this.angleType = var1;
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

    public void setInfiniteBefore(boolean var1) {
        this.infiniteBefore = var1;
    }

}
