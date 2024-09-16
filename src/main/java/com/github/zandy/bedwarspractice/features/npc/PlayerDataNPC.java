package com.github.zandy.bedwarspractice.features.npc;

import com.github.zandy.bamboolib.hologram.RefreshableHologram;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataNPC {
   private static final HashMap<UUID, PlayerDataNPC> dataMap = new HashMap<>();
   private final HashMap<Integer, RefreshableHologram> hologramMap = new HashMap<>();

   public PlayerDataNPC(UUID var1) {
      dataMap.put(var1, this);
   }

   public static boolean contains(UUID var0) {
      return dataMap.containsKey(var0);
   }

   public static PlayerDataNPC get(UUID var0) {
      return dataMap.get(var0);
   }

   public void addNPC(Integer var1, RefreshableHologram var2) {
      this.hologramMap.put(var1, var2);
   }

   public void flush() {
      this.hologramMap.clear();
   }

   public void remove(Integer var1) {
      if (this.hologramMap.containsKey(var1)) {
         this.hologramMap.get(var1).remove();
      }

   }

   public void removeAll() {
      this.hologramMap.values().forEach(RefreshableHologram::remove);
      this.flush();
   }

   public static HashMap<UUID, PlayerDataNPC> getDataMap() {
      return dataMap;
   }
}
