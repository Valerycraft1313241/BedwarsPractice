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
import java.util.HashMap;
import java.util.List;

public class BWPLegacyAdapter {
    private static BWPLegacyAdapter instance = null;
    private final HashMap<String, CuboidClipboard> clipboardCache = new HashMap<>();

    public static BWPLegacyAdapter getInstance() {
        if (instance == null) {
            instance = new BWPLegacyAdapter();
        }

        return instance;
    }

    public CuboidClipboard getSchematic(File schematicFile) {
        try {
            return SchematicFormat.getFormat(schematicFile).load(schematicFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public EditSession pasteSchematic(String schematicName, World world, double[] coordinates) {
        Bukkit.getWorld(world.getName()).getEntities().forEach((entity) -> {
            if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
                entity.remove();
            }
        });

        try {
            EditSession editSession = new EditSession(LocalWorldAdapter.adapt(world), Integer.MAX_VALUE);
            editSession.enableQueue();
            this.getClipboardCache().get(schematicName).paste(editSession, new Vector(coordinates[0], coordinates[1], coordinates[2]), true);
            editSession.flushQueue();
            return editSession;
        } catch (Exception e) {
            return null;
        }
    }

    public List<int[]> getVectorList(CuboidRegion cuboidRegion) {
        Vector minPoint = cuboidRegion.getMinimumPoint();
        Vector maxPoint = cuboidRegion.getMaximumPoint();
        ArrayList<int[]> vectorList = new ArrayList<>();
        int minX = Math.min(minPoint.getBlockX(), maxPoint.getBlockX());
        int minY = Math.min(minPoint.getBlockY(), maxPoint.getBlockY());
        int minZ = Math.min(minPoint.getBlockZ(), maxPoint.getBlockZ());
        int maxX = Math.max(minPoint.getBlockX(), maxPoint.getBlockX());
        int maxY = Math.max(minPoint.getBlockY(), maxPoint.getBlockY());
        int maxZ = Math.max(minPoint.getBlockZ(), maxPoint.getBlockZ());

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    vectorList.add(new int[]{x, y, z});
                }
            }
        }

        return vectorList;
    }

    public CuboidRegion getCuboidRegion(World world, double[] minCoordinates, double[] maxCoordinates) {
        return new CuboidRegion(world, new Vector(minCoordinates[0], minCoordinates[1], minCoordinates[2]), new Vector(maxCoordinates[0], maxCoordinates[1], maxCoordinates[2]));
    }

    public boolean cuboidRegionContains(CuboidRegion cuboidRegion, double[] coordinates) {
        return cuboidRegion.contains(new Vector(coordinates[0], coordinates[1], coordinates[2]));
    }

    public int[] getCenter(CuboidRegion cuboidRegion) {
        Vector center = cuboidRegion.getCenter();
        return new int[]{center.getBlockX(), center.getBlockY(), center.getBlockZ()};
    }

    public void addSchematicToCache(String schematicName, File schematicFile) {
        this.clipboardCache.put(schematicName.replace(".schematic", ""), this.getSchematic(schematicFile));
    }

    public HashMap<String, CuboidClipboard> getClipboardCache() {
        return this.clipboardCache;
    }
}
