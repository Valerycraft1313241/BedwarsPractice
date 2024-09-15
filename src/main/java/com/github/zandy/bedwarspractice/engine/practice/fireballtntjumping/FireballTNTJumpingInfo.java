package com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping;

import com.github.zandy.bedwarspractice.api.utils.data.FireballTNTJumpingData;
import com.github.zandy.bedwarspractice.engine.practice.utils.PlatformGenerator;
import com.github.zandy.bedwarspractice.features.stats.PlayerStats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FireballTNTJumpingInfo {
   private static final HashMap<UUID, FireballTNTJumpingInfo> fireballTntJumpingMap = new HashMap<>();
   private final UUID uuid;
   private FireballTNTJumpingEnums.FireballTNTJumpingAmountType amountType;
   private FireballTNTJumpingEnums.FireballTNTJumpingItemType itemType;
   private FireballTNTJumpingEnums.FireballTNTJumpingWoolType woolType;
   private PlatformGenerator platformGenerator;
   private final List<Block> blocksPlaced = new ArrayList<>();
   private boolean blockPlaced;

   public FireballTNTJumpingInfo(UUID var1) {
      this.uuid = var1;
      this.amountType = FireballTNTJumpingEnums.FireballTNTJumpingAmountType.AMOUNT_1;
      this.itemType = FireballTNTJumpingEnums.FireballTNTJumpingItemType.FIREBALL;
      this.woolType = FireballTNTJumpingEnums.FireballTNTJumpingWoolType.DISABLE;
      fireballTntJumpingMap.put(var1, this);
   }

   public void addBlocksPlaced(Block var1) {
      this.blocksPlaced.add(var1);
   }

   public void removeBlocksPlaced() {
      this.blocksPlaced.forEach((var0) -> var0.setType(Material.AIR));
   }

   public Player getPlayer() {
      return Bukkit.getPlayer(this.uuid);
   }

   public int getStatistic() {
      return (int)PlayerStats.get(this.uuid).get(this.itemType.getValue());
   }

   public void setStatistic(int var1) {
      PlayerStats.get(this.uuid).set(this.itemType.getValue(), var1);
   }

   public FireballTNTJumpingData toData() {
      return new FireballTNTJumpingData(this);
   }

   public static FireballTNTJumpingInfo get(@NotNull UUID var0) {
       return fireballTntJumpingMap.get(var0);
   }

   public static boolean contains(@NotNull UUID var0) {
       return fireballTntJumpingMap.containsKey(var0);
   }

   public static void remove(@NotNull UUID var0) {
       fireballTntJumpingMap.remove(var0);
   }

   public static HashMap<UUID, FireballTNTJumpingInfo> getFireballTntJumpingMap() {
      return fireballTntJumpingMap;
   }

   public void setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType var1) {
      this.amountType = var1;
   }

   public FireballTNTJumpingEnums.FireballTNTJumpingAmountType getAmountType() {
      return this.amountType;
   }

   public void setItemType(FireballTNTJumpingEnums.FireballTNTJumpingItemType var1) {
      this.itemType = var1;
   }

   public FireballTNTJumpingEnums.FireballTNTJumpingItemType getItemType() {
      return this.itemType;
   }

   public void setWoolType(FireballTNTJumpingEnums.FireballTNTJumpingWoolType var1) {
      this.woolType = var1;
   }

   public FireballTNTJumpingEnums.FireballTNTJumpingWoolType getWoolType() {
      return this.woolType;
   }

   public void setPlatformGenerator(PlatformGenerator var1) {
      this.platformGenerator = var1;
   }

   public PlatformGenerator getPlatformGenerator() {
      return this.platformGenerator;
   }

   public List<Block> getBlocksPlaced() {
      return this.blocksPlaced;
   }

   public boolean isBlockPlaced() {
      return this.blockPlaced;
   }

   public void setBlockPlaced(boolean var1) {
      this.blockPlaced = var1;
   }

}
