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

   public void open(final Player player, final boolean isNew) {
      UUID playerUUID = player.getUniqueId();
      GUI gui = new GUI(player, PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_SLOTS.getInt(), Language.MessagesEnum.MODE_SELECTOR_TITLE.getString(playerUUID));
      gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_BRIDGING_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_BRIDGING_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.MODE_SELECTOR_BRIDGING_LORE.getStringList(playerUUID)).build(), this.bridgingSlot)).addClickAction(new ClickAction() {
         public void onClick(GUIItem var1x, GUI var2x) {
            ModeSelectorGUI.this.clickFunctionality(player, GameEngine.PracticeType.BRIDGING, isNew);
         }
      }));
      gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_MLG_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_MLG_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.MODE_SELECTOR_MLG_LORE.getStringList(playerUUID)).build(), this.mlgSlot)).addClickAction(new ClickAction() {
         public void onClick(GUIItem var1x, GUI var2x) {
            ModeSelectorGUI.this.clickFunctionality(player, GameEngine.PracticeType.MLG, isNew);
         }
      }));
      gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_FIREBALL_TNT_JUMPING_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_FIREBALL_TNT_JUMPING_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.MODE_SELECTOR_FIREBALL_TNT_JUMPING_LORE.getStringList(playerUUID)).build(), this.fireballTntJumpingSlot)).addClickAction(new ClickAction() {
         public void onClick(GUIItem var1x, GUI var2x) {
            ModeSelectorGUI.this.clickFunctionality(player, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, isNew);
         }
      }));
      if (!isNew) {
         gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_CLOSE_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_CLOSE_NAME.getString(playerUUID)).build(), this.closeSlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem var1x, GUI var2) {
               player.closeInventory();
            }
         }));
         gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_RETURN_TO_LOBBY_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_RETURN_TO_LOBBY_NAME.getString(playerUUID)).build(), this.returnToLobbySlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem var1x, GUI var2) {
               if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
                  PracticeIncomingOutgoingProxy.getInstance().connect(player, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString());
               } else {
                  Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(player));
               }

            }
         }));
      } else if (PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_ENABLED.getBoolean()) {
         gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_GO_BACK_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.MODE_SELECTOR_GO_BACK_LORE.getStringList(playerUUID)).build(), this.goBackSlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem var1x, GUI var2) {
               player.closeInventory();
               PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_COMMANDS.getStringList().stream().filter((var0) -> !var0.toLowerCase().contains("dummy_command_")).forEach((var1xx) -> player.performCommand(var1xx.substring(1)));
            }
         }));
      }

      gui.open();
   }

   public void clickFunctionality(Player player, GameEngine.PracticeType practiceType, boolean isNew) {
      final UUID playerUniqueId = player.getUniqueId();
      player.closeInventory();
      if (BWPUtils.isSetupFinished(player)) {
         if (isNew) {
            if (!Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean() && Settings.SettingsEnum.INVENTORY_CACHE.getBoolean()) {
               InventoryCache.getInstance().add(player);
            }

            this.create(player, practiceType);
         } else {
            HashMap<UUID, GameEngine.PracticeType> practiceTypeMap = GameEngine.getInstance().getPracticeTypeMap();
            if (practiceTypeMap.containsKey(playerUniqueId) && practiceTypeMap.get(playerUniqueId).equals(practiceType)) {
               player.sendMessage(Language.MessagesEnum.MODE_SELECTOR_ALREADY_SELECTED.getString(playerUniqueId));
            } else {
               if (this.selectorCooldown.contains(playerUniqueId)) {
                  player.sendMessage(Language.MessagesEnum.MODE_SELECTOR_COOLDOWN.getString(playerUniqueId).replace("[seconds]", String.valueOf(this.cooldownSeconds)));
               } else {
                  this.selectorCooldown.add(playerUniqueId);
                  (new BukkitRunnable() {
                     public void run() {
                        ModeSelectorGUI.this.selectorCooldown.remove(playerUniqueId);
                     }
                  }).runTaskLaterAsynchronously(BambooLib.getPluginInstance(), this.cooldownSecondsTick);
                  Bukkit.getPluginManager().callEvent(new PracticeSwitchEvent(player, practiceTypeMap.get(playerUniqueId), practiceType));
                  this.create(player, practiceType);
               }

            }
         }
      }
   }

   @EventHandler
   private void onPlayerInteract(PlayerInteractEvent event) {
      if(!event.getPlayer().getWorld().getName().equalsIgnoreCase("bedwars_practice")) return;
      if (event.getAction().name().contains("RIGHT")) {
         Player player = event.getPlayer();
         if (player.getItemInHand().getType().equals(this.modeSelectorMaterial)) {
            this.open(player, !GameEngine.getInstance().getPracticeTypeMap().containsKey(player.getUniqueId()));
         }

      }
   }

   public void create(Player var1, GameEngine.PracticeType var2) {
      Sounds.NOTE_PLING.getSound().play(var1, 1.0F, 3.0F);

      BridgingMode bridgingMode = BridgingMode.getInstance();
      bridgingMode.switchClear(var1);

      MLGMode mlgMode = MLGMode.getInstance();
      mlgMode.switchClear(var1);

      FireballTNTJumpingMode fireballTNTJumpingMode = FireballTNTJumpingMode.getInstance();
      fireballTNTJumpingMode.switchClear(var1);

      switch(var2) {
      case BRIDGING:
         bridgingMode.create(var1);
         break;
      case MLG:
         mlgMode.create(var1);
         break;
      case FIREBALL_TNT_JUMPING:
         fireballTNTJumpingMode.create(var1);
      }

   }

   public static ModeSelectorGUI getInstance() {
      if (instance == null) {
         instance = new ModeSelectorGUI();
      }

      return instance;
   }
}
