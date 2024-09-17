package com.github.zandy.bedwarspractice.engine.practice.mlg;

import com.github.zandy.bedwarspractice.api.utils.data.MLGData;
import com.github.zandy.bedwarspractice.engine.practice.utils.PlatformGenerator;
import com.github.zandy.bedwarspractice.utils.FinishPositions;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class MLGInfo {
    private static final HashMap<UUID, MLGInfo> mlgInfoMap = new HashMap<>();
    private final UUID uuid;
    private CuboidRegion cuboidRegion;
    private MLGEnums.MLGSizeType sizeType;
    private MLGEnums.MLGHeightType heightType;
    private MLGEnums.MLGPositionType positionType;
    private MLGEnums.MLGTallnessType tallnessType;
    private MLGEnums.MLGItemType itemType;
    private MLGEnums.MLGShuffleType shuffleType;
    private PlatformGenerator platformGenerator;
    private BukkitTask jumpTask = null;
    private Block waterPlaced = null;
    private boolean shuffleItem = false;

    public MLGInfo(UUID uuid) {
        this.uuid = uuid;
        this.sizeType = MLGEnums.MLGSizeType.LARGE;
        this.heightType = MLGEnums.MLGHeightType.HIGH;
        this.positionType = MLGEnums.MLGPositionType.CENTER;
        this.tallnessType = MLGEnums.MLGTallnessType.BLOCKS_1;
        this.itemType = MLGEnums.MLGItemType.WATER;
        this.shuffleType = MLGEnums.MLGShuffleType.NONE;
        mlgInfoMap.put(uuid, this);
    }

    public static MLGInfo get(@NotNull UUID uuid) {
        return mlgInfoMap.get(uuid);
    }

    public static boolean contains(@NotNull UUID uuid) {
        return mlgInfoMap.containsKey(uuid);
    }

    public static void remove(@NotNull UUID uuid) {
        mlgInfoMap.remove(uuid);
    }

    public static HashMap<UUID, MLGInfo> getMlgInfoMap() {
        return mlgInfoMap;
    }

    public void removeJumpTask() {
        if (this.jumpTask != null) {
            this.jumpTask.cancel();
            this.jumpTask = null;
        }
    }

    public void removeWaterPlaced() {
        if (this.waterPlaced != null) {
            this.waterPlaced.setType(Material.AIR);
            this.waterPlaced = null;
        }
    }

    public void removeShuffle() {
        this.shuffleItem = false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public MLGData toData() {
        return new MLGData(this);
    }

    public CuboidRegion getCuboidRegion() {
        return this.cuboidRegion;
    }

    public void setCuboidRegion(Location location) {
        this.cuboidRegion = (new FinishPositions("MLG", location)).getRegion();
    }

    public MLGEnums.MLGSizeType getSizeType() {
        return this.sizeType;
    }

    public void setSizeType(MLGEnums.MLGSizeType sizeType) {
        this.sizeType = sizeType;
    }

    public MLGEnums.MLGHeightType getHeightType() {
        return this.heightType;
    }

    public void setHeightType(MLGEnums.MLGHeightType heightType) {
        this.heightType = heightType;
    }

    public MLGEnums.MLGPositionType getPositionType() {
        return this.positionType;
    }

    public void setPositionType(MLGEnums.MLGPositionType positionType) {
        this.positionType = positionType;
    }

    public MLGEnums.MLGTallnessType getTallnessType() {
        return this.tallnessType;
    }

    public void setTallnessType(MLGEnums.MLGTallnessType tallnessType) {
        this.tallnessType = tallnessType;
    }

    public MLGEnums.MLGItemType getItemType() {
        return this.itemType;
    }

    public void setItemType(MLGEnums.MLGItemType itemType) {
        this.itemType = itemType;
    }

    public MLGEnums.MLGShuffleType getShuffleType() {
        return this.shuffleType;
    }

    public void setShuffleType(MLGEnums.MLGShuffleType shuffleType) {
        this.shuffleType = shuffleType;
    }

    public PlatformGenerator getPlatformGenerator() {
        return this.platformGenerator;
    }

    public void setPlatformGenerator(PlatformGenerator platformGenerator) {
        this.platformGenerator = platformGenerator;
    }

    public BukkitTask getJumpTask() {
        return this.jumpTask;
    }

    public void setJumpTask(BukkitTask jumpTask) {
        this.jumpTask = jumpTask;
    }

    public void setWaterPlaced(Block waterPlaced) {
        this.waterPlaced = waterPlaced;
    }

    public boolean isShuffleItem() {
        return this.shuffleItem;
    }

    public void setShuffleItem(boolean shuffleItem) {
        this.shuffleItem = shuffleItem;
    }
}
