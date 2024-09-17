package com.github.zandy.bedwarspractice.engine.practice.utils;

import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGEnums;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SquareGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlatformGenerator {
    private List<Block> blocks;

    public PlatformGenerator create(UUID playerUUID) {
        MLGInfo mlgInfo = MLGInfo.get(playerUUID);
        int[] center = BWPLegacyAdapter.getInstance().getCenter(mlgInfo.getCuboidRegion());
        int x = center[0];
        int y = center[1];
        int z = center[2];
        if (!mlgInfo.getPositionType().equals(MLGEnums.MLGPositionType.CENTER)) {
            List<int[]> vectorList = BWPLegacyAdapter.getInstance().getVectorList(mlgInfo.getCuboidRegion());
            x = (vectorList.get(BWPUtils.genRandomNumber(vectorList.size() - 1)))[0];
            z = (vectorList.get(BWPUtils.genRandomNumber(vectorList.size() - 1)))[2];
        }

        this.blocks = SquareGenerator.getInstance().createSquare(mlgInfo.getSizeType(), new Location(WorldEngine.getInstance().getPracticeWorld(), x, y - mlgInfo.getHeightType().getValue(), z), mlgInfo.getTallnessType().getValue(), mlgInfo.getTallnessType().getMaterials());
        return this;
    }

    public PlatformGenerator create(Location location, int length, Materials materials) {
        boolean isLegacy = BWPUtils.isLegacy();
        this.blocks = new ArrayList<>();

        for (int i = 1; i <= length; ++i) {
            location.add(0.0D, 0.0D, 1.0D);
            Block block = location.getBlock();
            block.setType(materials.getItem().getMaterial());
            if (isLegacy) {
                block.setData((byte) materials.getData());
            }

            this.blocks.add(block);
        }

        return this;
    }

    public int getStartingZ() {
        return this.blocks.get(0).getZ();
    }

    public void destroy() {
        this.blocks.forEach((block) -> block.setType(Material.AIR));
    }
}
