package com.github.zandy.bedwarspractice.engine.practice.mlg;

import com.github.zandy.bedwarspractice.api.utils.data.MLGData;
import com.github.zandy.bedwarspractice.engine.practice.utils.PlatformGenerator;
import com.github.zandy.bedwarspractice.utils.FinishPositions;
import com.sk89q.worldedit.regions.CuboidRegion;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

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

   public MLGInfo(UUID var1) {
      this.uuid = var1;
      this.sizeType = MLGEnums.MLGSizeType.LARGE;
      this.heightType = MLGEnums.MLGHeightType.HIGH;
      this.positionType = MLGEnums.MLGPositionType.CENTER;
      this.tallnessType = MLGEnums.MLGTallnessType.BLOCKS_1;
      this.itemType = MLGEnums.MLGItemType.WATER;
      this.shuffleType = MLGEnums.MLGShuffleType.NONE;
      mlgInfoMap.put(var1, this);
   }

   public void setCuboidRegion(Location var1) {
      this.cuboidRegion = (new FinishPositions("MLG", var1)).getRegion();
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

   public static MLGInfo get(@NotNull UUID var0) {

       return mlgInfoMap.get(var0);
   }

   public static boolean contains(@NotNull UUID var0) {

       return mlgInfoMap.containsKey(var0);
   }

   public static void remove(@NotNull UUID var0) {

       mlgInfoMap.remove(var0);
   }

   public static HashMap<UUID, MLGInfo> getMlgInfoMap() {
      return mlgInfoMap;
   }

   public CuboidRegion getCuboidRegion() {
      return this.cuboidRegion;
   }

   public void setSizeType(MLGEnums.MLGSizeType var1) {
      this.sizeType = var1;
   }

   public MLGEnums.MLGSizeType getSizeType() {
      return this.sizeType;
   }

   public void setHeightType(MLGEnums.MLGHeightType var1) {
      this.heightType = var1;
   }

   public MLGEnums.MLGHeightType getHeightType() {
      return this.heightType;
   }

   public void setPositionType(MLGEnums.MLGPositionType var1) {
      this.positionType = var1;
   }

   public MLGEnums.MLGPositionType getPositionType() {
      return this.positionType;
   }

   public void setTallnessType(MLGEnums.MLGTallnessType var1) {
      this.tallnessType = var1;
   }

   public MLGEnums.MLGTallnessType getTallnessType() {
      return this.tallnessType;
   }

   public void setItemType(MLGEnums.MLGItemType var1) {
      this.itemType = var1;
   }

   public MLGEnums.MLGItemType getItemType() {
      return this.itemType;
   }

   public void setShuffleType(MLGEnums.MLGShuffleType var1) {
      this.shuffleType = var1;
   }

   public MLGEnums.MLGShuffleType getShuffleType() {
      return this.shuffleType;
   }

   public void setPlatformGenerator(PlatformGenerator var1) {
      this.platformGenerator = var1;
   }

   public PlatformGenerator getPlatformGenerator() {
      return this.platformGenerator;
   }

   public void setJumpTask(BukkitTask var1) {
      this.jumpTask = var1;
   }

   public BukkitTask getJumpTask() {
      return this.jumpTask;
   }

   public void setWaterPlaced(Block var1) {
      this.waterPlaced = var1;
   }

   public void setShuffleItem(boolean var1) {
      this.shuffleItem = var1;
   }

   public boolean isShuffleItem() {
      return this.shuffleItem;
   }

}
