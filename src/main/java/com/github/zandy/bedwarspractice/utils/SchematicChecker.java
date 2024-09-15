package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingEnums;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchematicChecker {
   private static final String bridgingSchematicFormat = "BRIDGING-[ANGLE]-[LEVEL]-[DISTANCE]";

   public static List<String> getRequiredSchematics(boolean var0) {
      ArrayList<String> var1 = new ArrayList<>();
      Arrays.stream(BridgingEnums.BridgingAngleType.values()).forEach((var2) -> Arrays.stream(BridgingEnums.BridgingLevelType.values()).forEach((var3) -> Arrays.stream(BridgingEnums.BridgingBlocksType.values()).forEach((var4) -> {
         if (var0) {
            var1.add(formatBridging(var2, var3, var4));
         } else if (!var4.equals(BridgingEnums.BridgingBlocksType.BLOCKS_INFINITE)) {
            var1.add(formatBridging(var2, var3, var4));
         }

      })));
      var1.add("MLG");
      var1.add("FIREBALL-TNT-JUMPING");
      return var1;
   }

   private static String formatBridging(BridgingEnums.BridgingAngleType var0, BridgingEnums.BridgingLevelType var1, BridgingEnums.BridgingBlocksType var2) {
      return bridgingSchematicFormat.replace("[ANGLE]", var0.name()).replace("[LEVEL]", var1.name()).replace("[DISTANCE]", var2.name().replace("BLOCKS_", ""));
   }
}
