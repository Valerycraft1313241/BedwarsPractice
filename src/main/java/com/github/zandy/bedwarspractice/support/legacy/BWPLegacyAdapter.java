package com.github.zandy.bedwarspractice.support.legacy;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class BWPLegacyAdapter {
   private static final Logger LOGGER = Logger.getLogger(BWPLegacyAdapter.class.getName());
   private static final BWPLegacyAdapter INSTANCE = new BWPLegacyAdapter();
   private final ConcurrentHashMap<String, CuboidClipboard> clipboardCache = new ConcurrentHashMap<>();

   private BWPLegacyAdapter() {
      // Private constructor to prevent instantiation
   }

   public static BWPLegacyAdapter getInstance() {
      return INSTANCE;
   }

   public CuboidClipboard getSchematic(File file) {
      try {
         return SchematicFormat.getFormat(file).load(file);
      } catch (Exception e) {
         LOGGER.severe("Failed to load schematic: " + e.getMessage());
         return null;
      }
   }

   public EditSession pasteSchematic(String schematicName, World world, double[] coordinates) {
      Bukkit.getWorld(world.getName()).getEntities().forEach(entity -> {
         if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
            entity.remove();
         }
      });

      try {
         EditSession editSession = new EditSession(LocalWorldAdapter.adapt(world), Integer.MAX_VALUE);
         editSession.enableQueue();
         clipboardCache.get(schematicName).paste(editSession, new Vector(coordinates[0], coordinates[1], coordinates[2]), true);
         editSession.flushQueue();
         return editSession;
      } catch (Exception e) {
         LOGGER.severe("Failed to paste schematic: " + e.getMessage());
         return null;
      }
   }

   public List<int[]> getVectorList(CuboidRegion region) {
      Vector min = region.getMinimumPoint();
      Vector max = region.getMaximumPoint();
      List<int[]> vectors = new ArrayList<>();
      for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
         for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
               vectors.add(new int[]{x, y, z});
            }
         }
      }
      return vectors;
   }

   public CuboidRegion getCuboidRegion(World world, double[] minCoords, double[] maxCoords) {
      return new CuboidRegion(world, new Vector(minCoords[0], minCoords[1], minCoords[2]), new Vector(maxCoords[0], maxCoords[1], maxCoords[2]));
   }

   public boolean cuboidRegionContains(CuboidRegion region, double[] coords) {
      return region.contains(new Vector(coords[0], coords[1], coords[2]));
   }

   public int[] getCenter(CuboidRegion region) {
      Vector center = region.getCenter();
      return new int[]{center.getBlockX(), center.getBlockY(), center.getBlockZ()};
   }

   public void addSchematicToCache(String name, File file) {
      clipboardCache.put(name.replace(".schematic", ""), getSchematic(file));
   }

   public ConcurrentHashMap<String, CuboidClipboard> getClipboardCache() {
      return clipboardCache;
   }
}
