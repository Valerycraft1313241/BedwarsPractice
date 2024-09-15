package com.github.zandy.bedwarspractice.support;

import org.bukkit.Location;

public class BWPVector {
   private final double x;
   private final double y;
   private final double z;

   public BWPVector(Location var1) {
      this.x = var1.getX();
      this.y = var1.getY();
      this.z = var1.getZ();
   }

   public double[] toArray() {
      return new double[]{this.x, this.y, this.z};
   }
}
