package com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping;

import com.github.zandy.bedwarspractice.api.utils.data.FireballTNTJumpingData;
import com.github.zandy.bedwarspractice.engine.practice.utils.PlatformGenerator;
import com.github.zandy.bedwarspractice.features.stats.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FireballTNTJumpingInfo {
    private static final HashMap<UUID, FireballTNTJumpingInfo> fireballTntJumpingMap = new HashMap<>();
    private final UUID uuid;
    private final List<Block> blocksPlaced = new ArrayList<>();
    private FireballTNTJumpingEnums.FireballTNTJumpingAmountType amountType;
    private FireballTNTJumpingEnums.FireballTNTJumpingItemType itemType;
    private FireballTNTJumpingEnums.FireballTNTJumpingWoolType woolType;
    private PlatformGenerator platformGenerator;
    private boolean blockPlaced;

    public FireballTNTJumpingInfo(UUID uuid) {
        this.uuid = uuid;
        this.amountType = FireballTNTJumpingEnums.FireballTNTJumpingAmountType.AMOUNT_1;
        this.itemType = FireballTNTJumpingEnums.FireballTNTJumpingItemType.FIREBALL;
        this.woolType = FireballTNTJumpingEnums.FireballTNTJumpingWoolType.DISABLE;
        fireballTntJumpingMap.put(uuid, this);
    }

    public static FireballTNTJumpingInfo get(@NotNull UUID uuid) {
        return fireballTntJumpingMap.get(uuid);
    }

    public static boolean contains(@NotNull UUID uuid) {
        return fireballTntJumpingMap.containsKey(uuid);
    }

    public static void remove(@NotNull UUID uuid) {
        fireballTntJumpingMap.remove(uuid);
    }

    public static HashMap<UUID, FireballTNTJumpingInfo> getFireballTntJumpingMap() {
        return fireballTntJumpingMap;
    }

    public void addBlocksPlaced(Block block) {
        this.blocksPlaced.add(block);
    }

    public void removeBlocksPlaced() {
        this.blocksPlaced.forEach((block) -> block.setType(Material.AIR));
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public int getStatistic() {
        return (int) PlayerStats.get(this.uuid).get(this.itemType.getValue());
    }

    public void setStatistic(int statistic) {
        PlayerStats.get(this.uuid).set(this.itemType.getValue(), statistic);
    }

    public FireballTNTJumpingData toData() {
        return new FireballTNTJumpingData(this);
    }

    public FireballTNTJumpingEnums.FireballTNTJumpingAmountType getAmountType() {
        return this.amountType;
    }

    public void setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType amountType) {
        this.amountType = amountType;
    }

    public FireballTNTJumpingEnums.FireballTNTJumpingItemType getItemType() {
        return this.itemType;
    }

    public void setItemType(FireballTNTJumpingEnums.FireballTNTJumpingItemType itemType) {
        this.itemType = itemType;
    }

    public FireballTNTJumpingEnums.FireballTNTJumpingWoolType getWoolType() {
        return this.woolType;
    }

    public void setWoolType(FireballTNTJumpingEnums.FireballTNTJumpingWoolType woolType) {
        this.woolType = woolType;
    }

    public PlatformGenerator getPlatformGenerator() {
        return this.platformGenerator;
    }

    public void setPlatformGenerator(PlatformGenerator platformGenerator) {
        this.platformGenerator = platformGenerator;
    }

    public List<Block> getBlocksPlaced() {
        return this.blocksPlaced;
    }

    public boolean isBlockPlaced() {
        return this.blockPlaced;
    }

    public void setBlockPlaced(boolean blockPlaced) {
        this.blockPlaced = blockPlaced;
    }
}
