package com.github.zandy.bedwarspractice.features.npc;

import com.github.zandy.bamboolib.hologram.RefreshableHologram;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataNPC {
   private static final HashMap<UUID, PlayerDataNPC> dataMap = new HashMap<>();
   private final HashMap<Integer, RefreshableHologram> hologramMap = new HashMap<>();

   public PlayerDataNPC(UUID playerUUID) {
      dataMap.put(playerUUID, this);
   }

   public static boolean contains(UUID playerUUID) {
      return dataMap.containsKey(playerUUID);
   }

   public static PlayerDataNPC get(UUID playerUUID) {
      return dataMap.get(playerUUID);
   }

   public void addNPC(Integer npcId, RefreshableHologram hologram) {
      this.hologramMap.put(npcId, hologram);
   }

   public void flush() {
      this.hologramMap.clear();
   }

   public void remove(Integer npcId) {
      if (this.hologramMap.containsKey(npcId)) {
         this.hologramMap.get(npcId).remove();
         this.hologramMap.remove(npcId);
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
