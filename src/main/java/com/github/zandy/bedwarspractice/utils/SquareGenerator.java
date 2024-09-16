package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGEnums;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class SquareGenerator {
    private static SquareGenerator instance = null;

    private SquareGenerator() {
    }

    public static SquareGenerator getInstance() {
        if (instance == null) {
            instance = new SquareGenerator();
        }

        return instance;
    }

    public List<Block> createSquare(MLGEnums.MLGSizeType sizeType, Location location, int height, Materials material) {
        ArrayList<Block> blocks = new ArrayList<>();
        byte radius;
        switch (sizeType) {
            case LARGE:
                radius = 2;
                break;
            case MEDIUM:
                radius = 1;
                break;
            case SMALL:
                Block singleBlock = location.getWorld().getBlockAt(location);
                singleBlock.setType(material.getItem().getMaterial());
                if (BWPUtils.isLegacy()) {
                    singleBlock.setData((byte) material.getData());
                }

                blocks.add(singleBlock);
                return blocks;
            default:
                return blocks;
        }

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY(); y <= location.getBlockY() + (height - 1); ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
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
