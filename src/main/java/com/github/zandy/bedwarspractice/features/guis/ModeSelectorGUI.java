package com.github.zandy.bedwarspractice.features.guis;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.gui.ClickAction;
import com.github.zandy.bamboolib.gui.GUI;
import com.github.zandy.bamboolib.gui.GUIItem;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeQuitEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeSwitchEvent;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.proxy.PracticeIncomingOutgoingProxy;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.InventoryCache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ModeSelectorGUI implements Listener {
   private static ModeSelectorGUI instance = null;
   private final List<UUID> selectorCooldown = new ArrayList<>();
   private final int cooldownSeconds;
   private final int cooldownSecondsTick;
   private final Material modeSelectorMaterial;
   private final int bridgingSlot;
   private final int mlgSlot;
   private final int fireballTntJumpingSlot;
   private final int closeSlot;
   private final int returnToLobbySlot;
   private final int goBackSlot;

   private ModeSelectorGUI() {
      this.cooldownSeconds = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_COOLDOWN_SECONDS.getInt();
      this.cooldownSecondsTick = this.cooldownSeconds * 20;
      this.modeSelectorMaterial = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_MATERIAL.getMaterial().getItem().getMaterial();
      this.bridgingSlot = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_BRIDGING_SLOT.getInt() - 1;
      this.mlgSlot = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_MLG_SLOT.getInt() - 1;
      this.fireballTntJumpingSlot = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_FIREBALL_TNT_JUMPING_SLOT.getInt() - 1;
      this.closeSlot = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_CLOSE_SLOT.getInt() - 1;
      this.returnToLobbySlot = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_RETURN_TO_LOBBY_SLOT.getInt() - 1;
      this.goBackSlot = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_SLOT.getInt() - 1;
   }

   public void init() {
      BambooUtils.registerEvent(this);
   }

   public void open(final Player var1, final boolean var2) {
      UUID var3 = var1.getUniqueId();
      GUI var4 = new GUI(var1, PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_SLOTS.getInt(), Language.MessagesEnum.MODE_SELECTOR_TITLE.getString(var3));
      var4.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_BRIDGING_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_BRIDGING_NAME.getString(var3)).setLore(Language.MessagesEnum.MODE_SELECTOR_BRIDGING_LORE.getStringList(var3)).build(), this.bridgingSlot)).addClickAction(new ClickAction() {
         public void onClick(GUIItem var1x, GUI var2x) {
            ModeSelectorGUI.this.clickFunctionality(var1, GameEngine.PracticeType.BRIDGING, var2);
         }
      }));
      var4.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_MLG_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_MLG_NAME.getString(var3)).setLore(Language.MessagesEnum.MODE_SELECTOR_MLG_LORE.getStringList(var3)).build(), this.mlgSlot)).addClickAction(new ClickAction() {
         public void onClick(GUIItem var1x, GUI var2x) {
            ModeSelectorGUI.this.clickFunctionality(var1, GameEngine.PracticeType.MLG, var2);
         }
      }));
      var4.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_FIREBALL_TNT_JUMPING_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_FIREBALL_TNT_JUMPING_NAME.getString(var3)).setLore(Language.MessagesEnum.MODE_SELECTOR_FIREBALL_TNT_JUMPING_LORE.getStringList(var3)).build(), this.fireballTntJumpingSlot)).addClickAction(new ClickAction() {
         public void onClick(GUIItem var1x, GUI var2x) {
            ModeSelectorGUI.this.clickFunctionality(var1, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, var2);
         }
      }));
      if (!var2) {
         var4.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_CLOSE_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_CLOSE_NAME.getString(var3)).build(), this.closeSlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem var1x, GUI var2) {
               var1.closeInventory();
            }
         }));
         var4.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_RETURN_TO_LOBBY_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_RETURN_TO_LOBBY_NAME.getString(var3)).build(), this.returnToLobbySlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem var1x, GUI var2) {
               if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
                  PracticeIncomingOutgoingProxy.getInstance().connect(var1, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString());
               } else {
                  Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(var1));
               }

            }
         }));
      } else if (PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_ENABLED.getBoolean()) {
         var4.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_GO_BACK_NAME.getString(var3)).setLore(Language.MessagesEnum.MODE_SELECTOR_GO_BACK_LORE.getStringList(var3)).build(), this.goBackSlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem var1x, GUI var2) {
               var1.closeInventory();
               PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_COMMANDS.getStringList().stream().filter((var0) -> !var0.toLowerCase().contains("dummy_command_")).forEach((var1xx) -> var1.performCommand(var1xx.substring(1)));
            }
         }));
      }

      var4.open();
   }

   public void clickFunctionality(Player var1, GameEngine.PracticeType var2, boolean var3) {
      final UUID var4 = var1.getUniqueId();
      var1.closeInventory();
      if (BWPUtils.isSetupFinished(var1)) {
         if (var3) {
            if (!Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean() && Settings.SettingsEnum.INVENTORY_CACHE.getBoolean()) {
               InventoryCache.getInstance().add(var1);
            }

            this.create(var1, var2);
         } else {
            HashMap<UUID, GameEngine.PracticeType> var5 = GameEngine.getInstance().getPracticeTypeMap();
            if (var5.containsKey(var4) && var5.get(var4).equals(var2)) {
               var1.sendMessage(Language.MessagesEnum.MODE_SELECTOR_ALREADY_SELECTED.getString(var4));
            } else {
               if (this.selectorCooldown.contains(var4)) {
                  var1.sendMessage(Language.MessagesEnum.MODE_SELECTOR_COOLDOWN.getString(var4).replace("[seconds]", String.valueOf(this.cooldownSeconds)));
               } else {
                  this.selectorCooldown.add(var4);
                  (new BukkitRunnable() {
                     public void run() {
                        ModeSelectorGUI.this.selectorCooldown.remove(var4);
                     }
                  }).runTaskLaterAsynchronously(BambooLib.getPluginInstance(), this.cooldownSecondsTick);
                  Bukkit.getPluginManager().callEvent(new PracticeSwitchEvent(var1, var5.get(var4), var2));
                  this.create(var1, var2);
               }

            }
         }
      }
   }

   @EventHandler
   private void onPlayerInteract(PlayerInteractEvent var1) {
      if(!var1.getPlayer().getWorld().getName().equalsIgnoreCase("bedwars_practice")) return;
      if (var1.getAction().name().contains("RIGHT")) {
         Player var2 = var1.getPlayer();
         if (var2.getItemInHand().getType().equals(this.modeSelectorMaterial)) {
            this.open(var2, !GameEngine.getInstance().getPracticeTypeMap().containsKey(var2.getUniqueId()));
         }

      }
   }

   public void create(Player var1, GameEngine.PracticeType var2) {
      Sounds.NOTE_PLING.getSound().play(var1, 1.0F, 3.0F);
      BridgingMode var3 = BridgingMode.getInstance();
      var3.switchClear(var1);
      MLGMode var4 = MLGMode.getInstance();
      var4.switchClear(var1);
      FireballTNTJumpingMode var5 = FireballTNTJumpingMode.getInstance();
      var5.switchClear(var1);
      switch(var2) {
      case BRIDGING:
         var3.create(var1);
         break;
      case MLG:
         var4.create(var1);
         break;
      case FIREBALL_TNT_JUMPING:
         var5.create(var1);
      }

   }

   public static ModeSelectorGUI getInstance() {
      if (instance == null) {
         instance = new ModeSelectorGUI();
      }

      return instance;
   }
}
