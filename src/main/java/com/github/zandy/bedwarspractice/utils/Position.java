package com.github.zandy.bedwarspractice.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Position {
   private final int pos;
   private final int x;
   private final int y;
   private final int z;

   public Position(int var1, Location var2) {
      this.pos = var1;
      this.x = var2.getBlockX();
      this.y = var2.getBlockY();
      this.z = var2.getBlockZ();
   }

   public Position(int var1, int var2, int var3, int var4) {
      this.pos = var1;
      this.x = var2;
      this.y = var3;
      this.z = var4;
   }

   public int getPos() {
      return this.pos;
   }

   public Location getLocation(World var1) {
      return new Location(var1, this.x, this.y, this.z);
   }
}
