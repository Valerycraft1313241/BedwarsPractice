package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchematicChecker {
    private static final String bridgingSchematicFormat = "BRIDGING-[ANGLE]-[LEVEL]-[DISTANCE]";

    public static List<String> getRequiredSchematics(boolean isInfinite) {
        ArrayList<String> requiredSchematics = new ArrayList<>();
        Arrays.stream(BridgingEnums.BridgingAngleType.values()).forEach((angleType) ->
                Arrays.stream(BridgingEnums.BridgingLevelType.values()).forEach((levelType) ->
                        Arrays.stream(BridgingEnums.BridgingBlocksType.values()).forEach((blocksType) -> {
                            if (isInfinite) {
                                requiredSchematics.add(formatBridging(angleType, levelType, blocksType));
                            } else if (!blocksType.equals(BridgingEnums.BridgingBlocksType.BLOCKS_INFINITE)) {
                                requiredSchematics.add(formatBridging(angleType, levelType, blocksType));
                            }
                        })
                )
        );
        requiredSchematics.add("MLG");
        requiredSchematics.add("FIREBALL-TNT-JUMPING");
        return requiredSchematics;
    }

    private static String formatBridging(BridgingEnums.BridgingAngleType angleType, BridgingEnums.BridgingLevelType levelType, BridgingEnums.BridgingBlocksType blocksType) {
        return bridgingSchematicFormat
                .replace("[ANGLE]", angleType.name())
                .replace("[LEVEL]", levelType.name())
                .replace("[DISTANCE]", blocksType.name().replace("BLOCKS_", ""));
    }
}
