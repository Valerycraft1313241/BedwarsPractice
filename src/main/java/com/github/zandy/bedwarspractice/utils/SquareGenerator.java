package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGEnums;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class SquareGenerator {

   // Lazy-loaded singleton pattern (thread-safe)
   private static SquareGenerator instance;

   private SquareGenerator() {
   }

   public static SquareGenerator getInstance() {
      if (instance == null) {
         instance = new SquareGenerator();
      }
      return instance;
   }

   public List<Block> createSquare(MLGEnums.MLGSizeType sizeType, Location center, int height, Materials material) {
      List<Block> blocks = new ArrayList<>();
      final int sizeRadius;

      // Determine size radius based on MLG size type
      switch (sizeType) {
         case LARGE:
            sizeRadius = 2;
            break;
         case MEDIUM:
            sizeRadius = 1;
            break;
         case SMALL:
            Block singleBlock = center.getWorld().getBlockAt(center);
            singleBlock.setType(material.getItem().getMaterial());
            if (BWPUtils.isLegacy()) {
               singleBlock.setData((byte) material.getData());
            }
            blocks.add(singleBlock);
            return blocks;
         default:
            return blocks;
      }

      // Cache location values to avoid repeated method calls
      final int centerX = center.getBlockX();
      final int centerY = center.getBlockY();
      final int centerZ = center.getBlockZ();

      // Create the square of blocks around the center
      for (int x = centerX - sizeRadius; x <= centerX + sizeRadius; x++) {
         for (int y = centerY; y <= centerY + height - 1; y++) {
            for (int z = centerZ - sizeRadius; z <= centerZ + sizeRadius; z++) {
               Block block = center.getWorld().getBlockAt(x, y, z);
               block.setType(material.getItem().getMaterial());
               if (BWPUtils.isLegacy()) {
                  block.setData((byte) material.getData());
               }
               blocks.add(block);
            }
         }
      }

      return blocks;
   }
}
