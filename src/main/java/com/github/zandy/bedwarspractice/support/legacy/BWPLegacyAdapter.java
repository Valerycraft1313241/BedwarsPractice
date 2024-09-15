package com.github.zandy.bedwarspractice.support.legacy;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.World;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

public class BWPLegacyAdapter {
   private static BWPLegacyAdapter instance = null;
   private final HashMap<String, CuboidClipboard> clipboardCache = new HashMap<>();

   public CuboidClipboard getSchematic(File var1) {
      try {
         return SchematicFormat.getFormat(var1).load(var1);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public EditSession pasteSchematic(String var1, World var2, double[] var3) {
      Bukkit.getWorld(var2.getName()).getEntities().forEach((var0) -> {
         if (var0.getType().equals(EntityType.DROPPED_ITEM)) {
            var0.remove();
         }

      });

      try {
         EditSession var4 = new EditSession(LocalWorldAdapter.adapt(var2), Integer.MAX_VALUE);
         var4.enableQueue();
         this.getClipboardCache().get(var1).paste(var4, new Vector(var3[0], var3[1], var3[2]), true);
         var4.flushQueue();
         return var4;
      } catch (Exception var5) {
         return null;
      }
   }

   public List<int[]> getVectorList(CuboidRegion var1) {
      Vector var2 = var1.getMinimumPoint();
      Vector var3 = var1.getMaximumPoint();
      ArrayList<int[]> var4 = new ArrayList<>();
      int var5 = Math.min(var2.getBlockX(), var3.getBlockX());
      int var6 = Math.min(var2.getBlockY(), var3.getBlockY());
      int var7 = Math.min(var2.getBlockZ(), var3.getBlockZ());
      int var8 = Math.max(var2.getBlockX(), var3.getBlockX());
      int var9 = Math.max(var2.getBlockY(), var3.getBlockY());
      int var10 = Math.max(var2.getBlockZ(), var3.getBlockZ());

      for(int var11 = var5; var11 <= var8; ++var11) {
         for(int var12 = var6; var12 <= var9; ++var12) {
            for(int var13 = var7; var13 <= var10; ++var13) {
               var4.add(new int[]{var11, var12, var13});
            }
         }
      }

      return var4;
   }

   public CuboidRegion getCuboidRegion(World var1, double[] var2, double[] var3) {
      return new CuboidRegion(var1, new Vector(var2[0], var2[1], var2[2]), new Vector(var3[0], var3[1], var3[2]));
   }

   public boolean cuboidRegionContains(CuboidRegion var1, double[] var2) {
      return var1.contains(new Vector(var2[0], var2[1], var2[2]));
   }

   public int[] getCenter(CuboidRegion var1) {
      Vector var2 = var1.getCenter();
      return new int[]{var2.getBlockX(), var2.getBlockY(), var2.getBlockZ()};
   }

   public void addSchematicToCache(String var1, File var2) {
      this.clipboardCache.put(var1.replace(".schematic", ""), this.getSchematic(var2));
   }

   public static BWPLegacyAdapter getInstance() {
      if (instance == null) {
         instance = new BWPLegacyAdapter();
      }

      return instance;
   }

   public HashMap<String, CuboidClipboard> getClipboardCache() {
      return this.clipboardCache;
   }
}
