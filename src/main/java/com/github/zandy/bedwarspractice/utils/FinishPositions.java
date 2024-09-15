package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.support.BWPVector;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;

public class FinishPositions {
   private final BWPVector position1;
   private final BWPVector position2;

   public FinishPositions(String var1, Location var2) {
      BambooFile var3 = GameEngine.getInstance().getPracticeFile().get(var1);
      this.position1 = this.getPosition(1, var2, var3);
      this.position2 = this.getPosition(2, var2, var3);
   }

   private BWPVector getPosition(int var1, Location var2, BambooFile var3) {
      return new BWPVector(new Location(var2.getWorld(), var3.getInt("Position-" + var1 + ".X") + var2.getBlockX(), var3.getInt("Position-" + var1 + ".Y") + var2.getBlockY(), var3.getInt("Position-" + var1 + ".Z") + var2.getBlockZ()));
   }

   public CuboidRegion getRegion() {
      World var1 = WorldEngine.getInstance().getPracticeWEWorld();
      return BWPLegacyAdapter.getInstance().getCuboidRegion(var1, this.position1.toArray(), this.position2.toArray());
   }
}
