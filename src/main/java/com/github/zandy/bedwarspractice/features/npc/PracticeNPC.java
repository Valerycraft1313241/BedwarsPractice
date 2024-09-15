package com.github.zandy.bedwarspractice.features.npc;

import com.github.zandy.bamboolib.hologram.RefreshableHologram;
import com.github.zandy.bamboolib.placeholder.utils.RefreshablePlaceholder;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.api.events.player.PlayerLanguageChangeEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeQuitEvent;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.features.guis.ModeSelectorGUI;
import com.github.zandy.bedwarspractice.files.NPCStorage;
import com.github.zandy.bedwarspractice.files.Settings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PracticeNPC implements Listener {
   private static PracticeNPC instance = null;
   private boolean init = false;

   public void init() {
      BambooUtils.registerEvent(this);
      this.init = true;
   }

   @EventHandler
   private void onNPCRightClick(NPCRightClickEvent var1) {
      this.clickFunction(var1.getClicker(), var1.getNPC().getId());
   }

   @EventHandler
   private void onNPCLeftClick(NPCLeftClickEvent var1) {
      this.clickFunction(var1.getClicker(), var1.getNPC().getId());
   }

   @EventHandler
   private void onPracticeLeave(PracticeQuitEvent var1) {
      if (var1.getPlayer().isOnline()) {
         this.respawnNPCs(var1.getPlayer());
      }

   }

   @EventHandler
   private void onLanguageChange(PlayerLanguageChangeEvent var1) {
      PlayerDataNPC.get(var1.getPlayer().getUniqueId()).removeAll();
      this.respawnNPCs(var1.getPlayer());
   }

   private void clickFunction(Player var1, int var2) {
      Arrays.stream(NPCStorage.NPCType.values()).forEach((var2x) -> {
         if (NPCStorage.getInstance().contains(var2x, var2)) {
            switch(var2x) {
            case DEFAULT:
               ModeSelectorGUI.getInstance().open(var1, true);
               break;
            case BRIDGING:
               ModeSelectorGUI.getInstance().clickFunctionality(var1, GameEngine.PracticeType.BRIDGING, true);
               break;
            case MLG:
               ModeSelectorGUI.getInstance().clickFunctionality(var1, GameEngine.PracticeType.MLG, true);
               break;
            case FIREBALL_TNT_JUMPING:
               ModeSelectorGUI.getInstance().clickFunctionality(var1, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, true);
            }
         }

      });
   }

   public void spawnNPC(NPC var1, Integer var2, NPCStorage.NPCType var3) {
      var1.setAlwaysUseNameHologram(false);
      Entity var4 = var1.getEntity();
      Location var5 = var4.getLocation().clone().add(0.0D, 1.55D, 0.0D);
      List<RefreshablePlaceholder> var6 = this.getRefreshablePlaceholders(var3);
      int var7 = Settings.SettingsEnum.NPC_REFRESH_TICK.getInt();
      Bukkit.getOnlinePlayers().forEach((var5x) -> PlayerDataNPC.get(var5x.getUniqueId()).addNPC(var2, new RefreshableHologram(var5x, "npc_" + var2 + "_" + var5x.getUniqueId(), var5, var3.getMessagesEnum().getStringList(var5x.getUniqueId()), var6, var7)));
   }

   public void respawnNPCs(Player var1) {
      int var2 = Settings.SettingsEnum.NPC_REFRESH_TICK.getInt();
      Arrays.stream(NPCStorage.NPCType.values()).forEach((var3) -> NPCStorage.getInstance().getIDList(var3).forEach((var4) -> {
         Entity var5 = CitizensAPI.getNPCRegistry().getById(var4).getEntity();
         Location var6 = var5.getLocation().clone().add(0.0D, 1.55D, 0.0D);
         List<RefreshablePlaceholder> var7 = this.getRefreshablePlaceholders(var3);
         PlayerDataNPC.get(var1.getUniqueId()).addNPC(var4, new RefreshableHologram(var1, "npc_" + var4 + "_" + var1.getUniqueId(), var6, var3.getMessagesEnum().getStringList(var1.getUniqueId()), var7, var2));
      }));
   }

   public void removeNPC(Integer var1) {
      PlayerDataNPC.getDataMap().values().forEach((var1x) -> var1x.remove(var1));
   }

   public void despawnNPCs() {
      PlayerDataNPC.getDataMap().values().forEach(PlayerDataNPC::removeAll);
   }

   private List<RefreshablePlaceholder> getRefreshablePlaceholders(final NPCStorage.NPCType var1) {
      ArrayList<RefreshablePlaceholder> var2 = new ArrayList<>();
      var2.add(new RefreshablePlaceholder(var1.getPlaceholder(), Settings.SettingsEnum.NPC_REFRESH_TICK.getInt()) {
         public String refresh() {
            int var1x = 0;
            switch(var1) {
            case DEFAULT:
               var1x = GameEngine.getInstance().getPracticeTypeMap().size();
               break;
            case BRIDGING:
               var1x = BridgingInfo.getBridgingInfoMap().size();
               break;
            case MLG:
               var1x = MLGInfo.getMlgInfoMap().size();
               break;
            case FIREBALL_TNT_JUMPING:
               var1x = FireballTNTJumpingInfo.getFireballTntJumpingMap().size();
            }

            return String.valueOf(var1x);
         }
      });
      return var2;
   }

   public static PracticeNPC getInstance() {
      if (instance == null) {
         instance = new PracticeNPC();
      }

      return instance;
   }

   public boolean isInit() {
      return this.init;
   }
}
