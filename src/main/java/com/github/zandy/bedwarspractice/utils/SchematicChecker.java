package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingEnums;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchematicChecker {
   private static final String BRIDGING_SCHEMATIC_FORMAT = "BRIDGING-[ANGLE]-[LEVEL]-[DISTANCE]";
   private static final String MLG_SCHEMATIC = "MLG";
   private static final String FIREBALL_TNT_JUMPING_SCHEMATIC = "FIREBALL-TNT-JUMPING";

   public static List<String> getRequiredSchematics(boolean includeInfiniteBlocks) {
      // Use flatMap to reduce nested loops and streamline code
      List<String> schematics = Stream.of(BridgingEnums.BridgingAngleType.values())
              .flatMap(angleType -> Stream.of(BridgingEnums.BridgingLevelType.values())
                      .flatMap(levelType -> Stream.of(BridgingEnums.BridgingBlocksType.values())
                              .filter(blocksType -> includeInfiniteBlocks || !blocksType.equals(BridgingEnums.BridgingBlocksType.BLOCKS_INFINITE))
                              .map(blocksType -> formatBridging(angleType, levelType, blocksType))))
              .collect(Collectors.toList());

      // Add additional schematics
      schematics.add(MLG_SCHEMATIC);
      schematics.add(FIREBALL_TNT_JUMPING_SCHEMATIC);

      return schematics;
   }

   private static String formatBridging(BridgingEnums.BridgingAngleType angleType, BridgingEnums.BridgingLevelType levelType, BridgingEnums.BridgingBlocksType blocksType) {
      // Use StringBuilder for more efficient string formatting
      return new StringBuilder(BRIDGING_SCHEMATIC_FORMAT)
              .replace(BRIDGING_SCHEMATIC_FORMAT.indexOf("[ANGLE]"), BRIDGING_SCHEMATIC_FORMAT.indexOf("[ANGLE]") + "[ANGLE]".length(), angleType.name())
              .replace(BRIDGING_SCHEMATIC_FORMAT.indexOf("[LEVEL]"), BRIDGING_SCHEMATIC_FORMAT.indexOf("[LEVEL]") + "[LEVEL]".length(), levelType.name())
              .replace(BRIDGING_SCHEMATIC_FORMAT.indexOf("[DISTANCE]"), BRIDGING_SCHEMATIC_FORMAT.indexOf("[DISTANCE]") + "[DISTANCE]".length(), blocksType.name().replace("BLOCKS_", ""))
              .toString();
   }
}
