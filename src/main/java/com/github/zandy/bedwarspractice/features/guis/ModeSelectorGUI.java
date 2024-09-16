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
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.proxy.PracticeIncomingOutgoingProxy;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.InventoryCache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    public static ModeSelectorGUI getInstance() {
        if (instance == null) {
            instance = new ModeSelectorGUI();
        }

        return instance;
    }

    public void init() {
        BambooUtils.registerEvent(this);
    }

    public void open(final Player player, final boolean bool) {
        UUID uuid = player.getUniqueId();
        GUI gui = new GUI(player, PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_SLOTS.getInt(), Language.MessagesEnum.MODE_SELECTOR_TITLE.getString(uuid));
        gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_BRIDGING_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_BRIDGING_NAME.getString(uuid)).setLore(Language.MessagesEnum.MODE_SELECTOR_BRIDGING_LORE.getStringList(uuid)).build(), this.bridgingSlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem guiItem, GUI gui) {
                ModeSelectorGUI.this.clickFunctionality(player, GameEngine.PracticeType.BRIDGING, bool);
            }
        }));
        gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_MLG_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_MLG_NAME.getString(uuid)).setLore(Language.MessagesEnum.MODE_SELECTOR_MLG_LORE.getStringList(uuid)).build(), this.mlgSlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem guiItem, GUI gui) {
                ModeSelectorGUI.this.clickFunctionality(player, GameEngine.PracticeType.MLG, bool);
            }
        }));
        gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_FIREBALL_TNT_JUMPING_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_FIREBALL_TNT_JUMPING_NAME.getString(uuid)).setLore(Language.MessagesEnum.MODE_SELECTOR_FIREBALL_TNT_JUMPING_LORE.getStringList(uuid)).build(), this.fireballTntJumpingSlot)).addClickAction(new ClickAction() {
            public void onClick(GUIItem guiItem, GUI gui) {
                ModeSelectorGUI.this.clickFunctionality(player, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, bool);
            }
        }));
        if (!bool) {
            gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_CLOSE_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_CLOSE_NAME.getString(uuid)).build(), this.closeSlot)).addClickAction(new ClickAction() {
                public void onClick(GUIItem guiItem, GUI gui) {
                    player.closeInventory();
                }
            }));
            gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_RETURN_TO_LOBBY_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_RETURN_TO_LOBBY_NAME.getString(uuid)).build(), this.returnToLobbySlot)).addClickAction(new ClickAction() {
                public void onClick(GUIItem guiItem, GUI gui) {
                    if (Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
                        PracticeIncomingOutgoingProxy.getInstance().connect(player, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString());
                    } else {
                        Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(player));
                    }

                }
            }));
        } else if (PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_ENABLED.getBoolean()) {
            gui.addItem((new GUIItem(PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_MATERIAL.getMaterial().getItem().setDisplayName(Language.MessagesEnum.MODE_SELECTOR_GO_BACK_NAME.getString(uuid)).setLore(Language.MessagesEnum.MODE_SELECTOR_GO_BACK_LORE.getStringList(uuid)).build(), this.goBackSlot)).addClickAction(new ClickAction() {
                public void onClick(GUIItem guiItem, GUI gui) {
                    player.closeInventory();
                    PracticeSettings.GameSettingsEnum.MODE_SELECTOR_GUI_GO_BACK_COMMANDS.getStringList().stream().filter((command) -> !command.toLowerCase().contains("dummy_command_")).forEach((guiItemx) -> player.performCommand(guiItemx.substring(1)));
                }
            }));
        }

        gui.open();
    }

    public void clickFunctionality(Player player, GameEngine.PracticeType practiceType, boolean inventoryCache) {
        final UUID uuid = player.getUniqueId();
        player.closeInventory();
        if (BWPUtils.isSetupFinished(player)) {
            if (inventoryCache) {
                if (!Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean() && Settings.SettingsEnum.INVENTORY_CACHE.getBoolean()) {
                    InventoryCache.getInstance().add(player);
                }

                this.create(player, practiceType);
            } else {
                HashMap<UUID, GameEngine.PracticeType> practiceTypeMap = GameEngine.getInstance().getPracticeTypeMap();
                if (practiceTypeMap.containsKey(uuid) && practiceTypeMap.get(uuid).equals(practiceType)) {
                    player.sendMessage(Language.MessagesEnum.MODE_SELECTOR_ALREADY_SELECTED.getString(uuid));
                } else {
                    if (this.selectorCooldown.contains(uuid)) {
                        player.sendMessage(Language.MessagesEnum.MODE_SELECTOR_COOLDOWN.getString(uuid).replace("[seconds]", String.valueOf(this.cooldownSeconds)));
                    } else {
                        this.selectorCooldown.add(uuid);
                        (new BukkitRunnable() {
                            public void run() {
                                ModeSelectorGUI.this.selectorCooldown.remove(uuid);
                            }
                        }).runTaskLaterAsynchronously(BambooLib.getPluginInstance(), this.cooldownSecondsTick);
                        Bukkit.getPluginManager().callEvent(new PracticeSwitchEvent(player, practiceTypeMap.get(uuid), practiceType));
                        this.create(player, practiceType);
                    }

                }
            }
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent e) {
        if(!WorldEngine.getInstance().getPracticeWorld().equals(e.getPlayer().getWorld())) return;
        if (e.getAction().name().contains("RIGHT")) {
            Player player = e.getPlayer();
            if (player.getItemInHand().getType().equals(this.modeSelectorMaterial)) {
                this.open(player, !GameEngine.getInstance().getPracticeTypeMap().containsKey(player.getUniqueId()));
            }

        }
    }

    public void create(Player player, GameEngine.PracticeType practiceType) {
        Sounds.NOTE_PLING.getSound().play(player, 1.0F, 3.0F);
        BridgingMode bridgingMode = BridgingMode.getInstance();
        bridgingMode.switchClear(player);
        MLGMode mlgMode = MLGMode.getInstance();
        mlgMode.switchClear(player);
        FireballTNTJumpingMode fireballTNTJumpingMode = FireballTNTJumpingMode.getInstance();
        fireballTNTJumpingMode.switchClear(player);
        switch (practiceType) {
            case BRIDGING:
                bridgingMode.create(player);
                break;
            case MLG:
                mlgMode.create(player);
                break;
            case FIREBALL_TNT_JUMPING:
                fireballTNTJumpingMode.create(player);
        }

    }
}
