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

    public PlatformGenerator create(UUID var1) {
        MLGInfo var2 = MLGInfo.get(var1);
        int[] var3 = BWPLegacyAdapter.getInstance().getCenter(var2.getCuboidRegion());
        int var4 = var3[0];
        int var5 = var3[1];
        int var6 = var3[2];
        if (!var2.getPositionType().equals(MLGEnums.MLGPositionType.CENTER)) {
            List<int[]> var7 = BWPLegacyAdapter.getInstance().getVectorList(var2.getCuboidRegion());
            var4 = (var7.get(BWPUtils.genRandomNumber(var7.size() - 1)))[0];
            var6 = (var7.get(BWPUtils.genRandomNumber(var7.size() - 1)))[2];
        }

        this.blocks = SquareGenerator.getInstance().createSquare(var2.getSizeType(), new Location(WorldEngine.getInstance().getPracticeWorld(), var4, var5 - var2.getHeightType().getValue(), var6), var2.getTallnessType().getValue(), var2.getTallnessType().getMaterials());
        return this;
    }

    public PlatformGenerator create(Location var1, int var2, Materials var3) {
        boolean var4 = BWPUtils.isLegacy();
        this.blocks = new ArrayList<>();

        for (int var5 = 1; var5 <= var2; ++var5) {
            var1.add(0.0D, 0.0D, 1.0D);
            Block var6 = var1.getBlock();
            var6.setType(var3.getItem().getMaterial());
            if (var4) {
                var6.setData((byte) var3.getData());
            }

            this.blocks.add(var6);
        }

        return this;
    }

    public int getStartingZ() {
        return this.blocks.get(0).getZ();
    }

    public void destroy() {
        this.blocks.forEach((var0) -> var0.setType(Material.AIR));
    }
}
