package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGEnums;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class SquareGenerator {
   private static SquareGenerator instance = null;

   private SquareGenerator() {
   }

   public List<Block> createSquare(MLGEnums.MLGSizeType var1, Location var2, int var3, Materials var4) {
      ArrayList<Block> var5 = new ArrayList<>();
      byte var6;
      switch(var1) {
      case LARGE:
         var6 = 2;
         break;
      case MEDIUM:
         var6 = 1;
         break;
      case SMALL:
         Block var7 = var2.getWorld().getBlockAt(var2);
         var7.setType(var4.getItem().getMaterial());
         if (BWPUtils.isLegacy()) {
            var7.setData((byte)var4.getData());
         }

         var5.add(var7);
         return var5;
      default:
         return var5;
      }

      for(int var11 = var2.getBlockX() - var6; var11 <= var2.getBlockX() + var6; ++var11) {
         for(int var8 = var2.getBlockY(); var8 <= var2.getBlockY() + (var3 - 1); ++var8) {
            for(int var9 = var2.getBlockZ() - var6; var9 <= var2.getBlockZ() + var6; ++var9) {
               Block var10 = var2.getWorld().getBlockAt(var11, var8, var9);
               var10.setType(var4.getItem().getMaterial());
               if (BWPUtils.isLegacy()) {
                  var10.setData((byte)var4.getData());
               }

               var5.add(var10);
            }
         }
      }

      return var5;
   }

   public static SquareGenerator getInstance() {
      if (instance == null) {
         instance = new SquareGenerator();
      }

      return instance;
   }
}
