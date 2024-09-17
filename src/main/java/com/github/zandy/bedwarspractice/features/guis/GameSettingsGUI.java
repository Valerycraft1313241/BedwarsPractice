package com.github.zandy.bedwarspractice.features.guis;

import com.github.zandy.bamboolib.gui.ClickAction;
import com.github.zandy.bamboolib.gui.GUI;
import com.github.zandy.bamboolib.gui.GUIItem;
import com.github.zandy.bamboolib.item.ItemBuilder;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingEnums;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingEnums;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGEnums;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GameSettingsGUI implements Listener {
    private static GameSettingsGUI instance = new GameSettingsGUI();
    private final int inventorySlots;
    private final Material gameSettingsMaterial;
    private final Materials bridging30Material;
    private final Materials bridging50Material;
    private final Materials bridging100Material;
    private final Materials bridgingInfiniteMaterial;
    private final Materials bridgingNoneMaterial;
    private final Materials bridgingSlightMaterial;
    private final Materials bridgingStaircaseMaterial;
    private final Materials bridgingStraightMaterial;
    private final Materials bridgingDiagonalMaterial;
    private final Materials mlgSizeLargeMaterial;
    private final Materials mlgSizeMediumMaterial;
    private final Materials mlgSizeSmallMaterial;
    private final Materials mlgHeightHighMaterial;
    private final Materials mlgHeightMediumMaterial;
    private final Materials mlgHeightLowMaterial;
    private final Materials mlgPositionCenteredMaterial;
    private final Materials mlgPositionRandomMaterial;
    private final Materials mlgTallness1Material;
    private final Materials mlgTallness2Material;
    private final Materials mlgTallness3Material;
    private final Materials mlgShuffleNoneMaterial;
    private final Materials mlgShuffleShuffledMaterial;
    private final Materials jumpItems1Material;
    private final Materials jumpItems2Material;
    private final Materials jumpItems5Material;
    private final Materials jumpWoolDisableMaterial;
    private final Materials jumpWoolEnableMaterial;
    private final int bridging30Slot;
    private final int bridging50Slot;
    private final int bridging100Slot;
    private final int bridgingInfiniteSlot;
    private final int bridgingNoneSlot;
    private final int bridgingSlightSlot;
    private final int bridgingStaircaseSlot;
    private final int bridgingStraightSlot;
    private final int bridgingDiagonalSlot;
    private final int mlgSizeLargeSlot;
    private final int mlgSizeMediumSlot;
    private final int mlgSizeSmallSlot;
    private final int mlgHeightHighSlot;
    private final int mlgHeightMediumSlot;
    private final int mlgHeightLowSlot;
    private final int mlgPositionCenteredSlot;
    private final int mlgPositionRandomSlot;
    private final int mlgTallness1Slot;
    private final int mlgTallness2Slot;
    private final int mlgTallness3Slot;
    private final int mlgItemWaterBucketSlot;
    private final int mlgItemLadderSlot;
    private final int mlgShuffleNoneSlot;
    private final int mlgShuffleShuffledSlot;
    private final int jumpItems1Slot;
    private final int jumpItems2Slot;
    private final int jumpItems5Slot;
    private final int jumpItemFireballSlot;
    private final int jumpItemTNTSlot;
    private final int jumpWoolDisableSlot;
    private final int jumpWoolEnableSlot;
    private final List<Integer> slotsList;

    private GameSettingsGUI() {
        this.inventorySlots = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_SLOTS.getInt();
        this.gameSettingsMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_MATERIAL.getMaterial().getItem().getMaterial();
        this.bridging30Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_MATERIAL.getMaterial();
        this.bridging50Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_MATERIAL.getMaterial();
        this.bridging100Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_MATERIAL.getMaterial();
        this.bridgingInfiniteMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_MATERIAL.getMaterial();
        this.bridgingNoneMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_MATERIAL.getMaterial();
        this.bridgingSlightMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_MATERIAL.getMaterial();
        this.bridgingStaircaseMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_MATERIAL.getMaterial();
        this.bridgingStraightMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_MATERIAL.getMaterial();
        this.bridgingDiagonalMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_MATERIAL.getMaterial();
        this.mlgSizeLargeMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SIZE_LARGE_MATERIAL.getMaterial();
        this.mlgSizeMediumMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_MATERIAL.getMaterial();
        this.mlgSizeSmallMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SIZE_SMALL_MATERIAL.getMaterial();
        this.mlgHeightHighMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_MATERIAL.getMaterial();
        this.mlgHeightMediumMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_MATERIAL.getMaterial();
        this.mlgHeightLowMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_MATERIAL.getMaterial();
        this.mlgPositionCenteredMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_MATERIAL.getMaterial();
        this.mlgPositionRandomMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_MATERIAL.getMaterial();
        this.mlgTallness1Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_MATERIAL.getMaterial();
        this.mlgTallness2Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_MATERIAL.getMaterial();
        this.mlgTallness3Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_MATERIAL.getMaterial();
        this.mlgShuffleNoneMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_NONE_MATERIAL.getMaterial();
        this.mlgShuffleShuffledMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_MATERIAL.getMaterial();
        this.jumpItems1Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_MATERIAL.getMaterial();
        this.jumpItems2Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_MATERIAL.getMaterial();
        this.jumpItems5Material = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_MATERIAL.getMaterial();
        this.jumpWoolDisableMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_MATERIAL.getMaterial();
        this.jumpWoolEnableMaterial = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_MATERIAL.getMaterial();
        this.bridging30Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_SLOT.getInt() - 1;
        this.bridging50Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_SLOT.getInt() - 1;
        this.bridging100Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_SLOT.getInt() - 1;
        this.bridgingInfiniteSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_SLOT.getInt() - 1;
        this.bridgingNoneSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_SLOT.getInt() - 1;
        this.bridgingSlightSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_SLOT.getInt() - 1;
        this.bridgingStaircaseSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_SLOT.getInt() - 1;
        this.bridgingStraightSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_SLOT.getInt() - 1;
        this.bridgingDiagonalSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_SLOT.getInt() - 1;
        this.mlgSizeLargeSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SIZE_LARGE_SLOT.getInt() - 1;
        this.mlgSizeMediumSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_SLOT.getInt() - 1;
        this.mlgSizeSmallSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SIZE_SMALL_SLOT.getInt() - 1;
        this.mlgHeightHighSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_SLOT.getInt() - 1;
        this.mlgHeightMediumSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_SLOT.getInt() - 1;
        this.mlgHeightLowSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_SLOT.getInt() - 1;
        this.mlgPositionCenteredSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_SLOT.getInt() - 1;
        this.mlgPositionRandomSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_SLOT.getInt() - 1;
        this.mlgTallness1Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_SLOT.getInt() - 1;
        this.mlgTallness2Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_SLOT.getInt() - 1;
        this.mlgTallness3Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_SLOT.getInt() - 1;
        this.mlgItemWaterBucketSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_SLOT.getInt() - 1;
        this.mlgItemLadderSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_ITEM_LADDER_SLOT.getInt() - 1;
        this.mlgShuffleNoneSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_NONE_SLOT.getInt() - 1;
        this.mlgShuffleShuffledSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_SLOT.getInt() - 1;
        this.jumpItems1Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_SLOT.getInt() - 1;
        this.jumpItems2Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_SLOT.getInt() - 1;
        this.jumpItems5Slot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_SLOT.getInt() - 1;
        this.jumpItemFireballSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_SLOT.getInt() - 1;
        this.jumpItemTNTSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_SLOT.getInt() - 1;
        this.jumpWoolDisableSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_SLOT.getInt() - 1;
        this.jumpWoolEnableSlot = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_SLOT.getInt() - 1;
        this.slotsList = new ArrayList<>(Arrays.asList(this.bridging30Slot, this.bridging50Slot, this.bridging100Slot, this.bridgingInfiniteSlot, this.bridgingNoneSlot, this.bridgingNoneSlot, this.bridgingSlightSlot, this.bridgingStaircaseSlot, this.bridgingStraightSlot, this.bridgingDiagonalSlot));
    }

    public static GameSettingsGUI getInstance() {
        if (instance == null) {
            instance = new GameSettingsGUI();
        }

        return instance;
    }

    public void init() {
        BambooUtils.registerEvent(this);
    }

    public void open(Player player) {
        UUID uuid = player.getUniqueId();
        GameEngine gameEngine = GameEngine.getInstance();
        if (gameEngine.getPracticeTypeMap().containsKey(uuid)) {
            boolean validSlot = false;

            for (int slotIndex = 1; slotIndex <= 6; ++slotIndex) {
                if ((double) this.inventorySlots / 9.0D == (double) slotIndex) {
                    validSlot = true;
                    break;
                }
            }

            if (!validSlot) {
                for (String message : Language.MessagesEnum.GAME_SETTINGS_WRONG_SLOTS_NUMBER.getStringList(uuid)) {
                    player.sendMessage(message);
                }
            } else {
                boolean invalidSlot = false;

                for (Integer slot : this.slotsList) {
                    if (slot > this.inventorySlots - 1) {
                        invalidSlot = true;
                        break;
                    }
                }

                if (invalidSlot) {
                    Language.MessagesEnum.GAME_SETTINGS_WRONG_SLOTS_ITEM.getStringList(uuid).forEach((message) ->
                            player.sendMessage(message.replace("[inventorySlots]", String.valueOf(this.inventorySlots)))
                    );
                } else {
                    ItemBuilder itemBuilder1;
                    ItemBuilder itemBuilder2;
                    ItemBuilder itemBuilder3;
                    ItemBuilder itemBuilder4;
                    ItemBuilder itemBuilder5;
                    ItemBuilder itemBuilder6;
                    ItemBuilder itemBuilder7;
                    ItemBuilder itemBuilder8;
                    ItemBuilder itemBuilder9;
                    GUI gui;
                    switch (gameEngine.getPracticeTypeMap().get(uuid)) {
                        case BRIDGING:
                            gui = new GUI(player, this.inventorySlots, Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_TITLE.getString(uuid));
                            itemBuilder8 = this.bridging30Material.getItem();
                            itemBuilder1 = this.bridging50Material.getItem();
                            itemBuilder2 = this.bridging100Material.getItem();
                            itemBuilder3 = this.bridgingInfiniteMaterial.getItem();
                            itemBuilder4 = this.bridgingNoneMaterial.getItem();
                            itemBuilder5 = this.bridgingSlightMaterial.getItem();
                            itemBuilder6 = this.bridgingStaircaseMaterial.getItem();
                            itemBuilder9 = this.bridgingStraightMaterial.getItem();
                            itemBuilder7 = this.bridgingDiagonalMaterial.getItem();
                            final BridgingInfo bridgingInfo = BridgingInfo.get(uuid);
                            switch (bridgingInfo.getBlocksType()) {
                                case BLOCKS_30:
                                    itemBuilder8 = this.createEnchantAura(itemBuilder8);
                                    break;
                                case BLOCKS_50:
                                    itemBuilder1 = this.createEnchantAura(itemBuilder1);
                                    break;
                                case BLOCKS_100:
                                    itemBuilder2 = this.createEnchantAura(itemBuilder2);
                                    break;
                                case BLOCKS_INFINITE:
                                    itemBuilder3 = this.createEnchantAura(itemBuilder3);
                            }

                            switch (bridgingInfo.getLevelType()) {
                                case NONE:
                                    itemBuilder4 = this.createEnchantAura(itemBuilder4);
                                    break;
                                case SLIGHT:
                                    itemBuilder5 = this.createEnchantAura(itemBuilder5);
                                    break;
                                case STAIRCASE:
                                    itemBuilder6 = this.createEnchantAura(itemBuilder6);
                            }

                            switch (bridgingInfo.getAngleType()) {
                                case STRAIGHT:
                                    itemBuilder9 = this.createEnchantAura(itemBuilder9);
                                    break;
                                case DIAGONAL:
                                    itemBuilder7 = this.createEnchantAura(itemBuilder7);
                            }

                            gui.addItem((new GUIItem(itemBuilder8.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_LORE.getStringList(uuid)).setAmount(30).build(), this.bridging30Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setBlocksType(BridgingEnums.BridgingBlocksType.BLOCKS_30);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder1.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_LORE.getStringList(uuid)).setAmount(50).build(), this.bridging50Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setBlocksType(BridgingEnums.BridgingBlocksType.BLOCKS_50);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder2.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_LORE.getStringList(uuid)).setAmount(64).build(), this.bridging100Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setBlocksType(BridgingEnums.BridgingBlocksType.BLOCKS_100);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder3.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_LORE.getStringList(uuid)).build(), this.bridgingInfiniteSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setBlocksType(BridgingEnums.BridgingBlocksType.BLOCKS_INFINITE);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder4.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_LORE.getStringList(uuid)).build(), this.bridgingNoneSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setLevelType(BridgingEnums.BridgingLevelType.NONE);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder5.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_LORE.getStringList(uuid)).build(), this.bridgingSlightSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setLevelType(BridgingEnums.BridgingLevelType.SLIGHT);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder6.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_LORE.getStringList(uuid)).build(), this.bridgingStaircaseSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setLevelType(BridgingEnums.BridgingLevelType.STAIRCASE);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder9.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_LORE.getStringList(uuid)).build(), this.bridgingStraightSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setAngleType(BridgingEnums.BridgingAngleType.STRAIGHT);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder7.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_LORE.getStringList(uuid)).build(), this.bridgingDiagonalSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    bridgingInfo.setAngleType(BridgingEnums.BridgingAngleType.DIAGONAL);
                                }
                            }));
                            gui.getGUIItems().forEach((guiItem) -> guiItem.addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItemx, GUI gui2x) {
                                    player.closeInventory();
                                    BridgingMode.getInstance().refresh(player, bridgingInfo.toData());
                                }
                            }));
                            break;
                        case MLG:
                            final MLGInfo mlgInfo = MLGInfo.get(uuid);
                            gui = new GUI(player, this.inventorySlots, Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TITLE.getString(uuid));
                            itemBuilder2 = this.mlgSizeLargeMaterial.getItem();
                            itemBuilder3 = this.mlgSizeMediumMaterial.getItem();
                            itemBuilder4 = this.mlgSizeSmallMaterial.getItem();
                            itemBuilder5 = this.mlgHeightHighMaterial.getItem();
                            itemBuilder6 = this.mlgHeightMediumMaterial.getItem();
                            itemBuilder9 = this.mlgHeightLowMaterial.getItem();
                            itemBuilder7 = this.mlgPositionCenteredMaterial.getItem();
                            ItemBuilder itemBuilder10 = this.mlgPositionRandomMaterial.getItem();
                            ItemBuilder itemBuilder11 = this.mlgTallness1Material.getItem();
                            ItemBuilder itemBuilder12 = this.mlgTallness2Material.getItem();
                            ItemBuilder itemBuilder13 = this.mlgTallness3Material.getItem();
                            ItemBuilder itemBuilder14 = this.mlgShuffleNoneMaterial.getItem();
                            ItemBuilder itemBuilder15 = this.mlgShuffleShuffledMaterial.getItem();
                            switch (mlgInfo.getSizeType()) {
                                case LARGE:
                                    itemBuilder2 = this.createEnchantAura(itemBuilder2);
                                    break;
                                case MEDIUM:
                                    itemBuilder3 = this.createEnchantAura(itemBuilder3);
                                    break;
                                case SMALL:
                                    itemBuilder4 = this.createEnchantAura(itemBuilder4);
                            }

                            switch (mlgInfo.getHeightType()) {
                                case HIGH:
                                    itemBuilder5 = this.createEnchantAura(itemBuilder5);
                                    break;
                                case MEDIUM:
                                    itemBuilder6 = this.createEnchantAura(itemBuilder6);
                                    break;
                                case LOW:
                                    itemBuilder9 = this.createEnchantAura(itemBuilder9);
                            }

                            switch (mlgInfo.getPositionType()) {
                                case CENTER:
                                    itemBuilder7 = this.createEnchantAura(itemBuilder7);
                                    break;
                                case RANDOM:
                                    itemBuilder10 = this.createEnchantAura(itemBuilder10);
                            }

                            switch (mlgInfo.getTallnessType()) {
                                case BLOCKS_1:
                                    itemBuilder11 = this.createEnchantAura(itemBuilder11);
                                    break;
                                case BLOCKS_2:
                                    itemBuilder12 = this.createEnchantAura(itemBuilder12);
                                    break;
                                case BLOCKS_3:
                                    itemBuilder13 = this.createEnchantAura(itemBuilder13);
                            }

                            switch (mlgInfo.getShuffleType()) {
                                case NONE:
                                    itemBuilder14 = this.createEnchantAura(itemBuilder14);
                                    break;
                                case SHUFFLED:
                                    itemBuilder15 = this.createEnchantAura(itemBuilder15);
                            }

                            gui.addItem((new GUIItem(itemBuilder2.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_LARGE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_LARGE_LORE.getStringList(uuid)).build(), this.mlgSizeLargeSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setSizeType(MLGEnums.MLGSizeType.LARGE);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder3.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_LORE.getStringList(uuid)).build(), this.mlgSizeMediumSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setSizeType(MLGEnums.MLGSizeType.MEDIUM);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder4.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_SMALL_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_SMALL_LORE.getStringList(uuid)).build(), this.mlgSizeSmallSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setSizeType(MLGEnums.MLGSizeType.SMALL);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder5.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_LORE.getStringList(uuid)).build(), this.mlgHeightHighSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setHeightType(MLGEnums.MLGHeightType.HIGH);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder6.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_LORE.getStringList(uuid)).build(), this.mlgHeightMediumSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setHeightType(MLGEnums.MLGHeightType.MEDIUM);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder9.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_LORE.getStringList(uuid)).build(), this.mlgHeightLowSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setHeightType(MLGEnums.MLGHeightType.LOW);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder7.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_LORE.getStringList(uuid)).build(), this.mlgPositionCenteredSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setPositionType(MLGEnums.MLGPositionType.CENTER);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder10.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_LORE.getStringList(uuid)).build(), this.mlgPositionRandomSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setPositionType(MLGEnums.MLGPositionType.RANDOM);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder11.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_LORE.getStringList(uuid)).build(), this.mlgTallness1Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setTallnessType(MLGEnums.MLGTallnessType.BLOCKS_1);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder12.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_LORE.getStringList(uuid)).build(), this.mlgTallness2Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setTallnessType(MLGEnums.MLGTallnessType.BLOCKS_2);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder13.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_LORE.getStringList(uuid)).build(), this.mlgTallness3Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setTallnessType(MLGEnums.MLGTallnessType.BLOCKS_3);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder14.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_NONE_NAME.getString(uuid)).build(), this.mlgShuffleNoneSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setShuffleType(MLGEnums.MLGShuffleType.NONE);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder15.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_LORE.getStringList(uuid)).build(), this.mlgShuffleShuffledSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setShuffleType(MLGEnums.MLGShuffleType.SHUFFLED);
                                }
                            }));
                            gui.addItem((new GUIItem(Materials.WATER_BUCKET.getItem().setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_LORE.getStringList(uuid)).build(), this.mlgItemWaterBucketSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setItemType(MLGEnums.MLGItemType.WATER);
                                }
                            }));
                            gui.addItem((new GUIItem(Materials.LADDER.getItem().setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_LADDER_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_LADDER_LORE.getStringList(uuid)).build(), this.mlgItemLadderSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    mlgInfo.setItemType(MLGEnums.MLGItemType.LADDER);
                                }
                            }));
                            gui.getGUIItems().forEach((guiItem) -> guiItem.addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItemx, GUI gui2x) {
                                    player.closeInventory();
                                    player.sendMessage(Language.MessagesEnum.GAME_UPDATED_YOUR_SETTINGS.getString(player.getUniqueId()));
                                    MLGMode.getInstance().refresh(player, mlgInfo.toData());
                                }
                            }));
                            break;
                        case FIREBALL_TNT_JUMPING:
                            gui = new GUI(player, this.inventorySlots, Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_TITLE.getString(uuid));
                            itemBuilder8 = this.jumpItems1Material.getItem();
                            itemBuilder1 = this.jumpItems2Material.getItem();
                            itemBuilder2 = this.jumpItems5Material.getItem();
                            itemBuilder3 = Materials.FIRE_CHARGE.getItem();
                            itemBuilder4 = Materials.TNT.getItem();
                            itemBuilder5 = this.jumpWoolDisableMaterial.getItem();
                            itemBuilder6 = this.jumpWoolEnableMaterial.getItem();
                            final FireballTNTJumpingInfo guiItem4 = FireballTNTJumpingInfo.get(uuid);
                            switch (guiItem4.getAmountType()) {
                                case AMOUNT_1:
                                    itemBuilder8 = this.createEnchantAura(itemBuilder8);
                                    break;
                                case AMOUNT_2:
                                    itemBuilder1 = this.createEnchantAura(itemBuilder1);
                                    break;
                                case AMOUNT_5:
                                    itemBuilder2 = this.createEnchantAura(itemBuilder2);
                            }

                            switch (guiItem4.getItemType()) {
                                case FIREBALL:
                                    itemBuilder3 = this.createEnchantAura(itemBuilder3);
                                    break;
                                case TNT:
                                    itemBuilder4 = this.createEnchantAura(itemBuilder4);
                            }

                            switch (guiItem4.getWoolType()) {
                                case DISABLE:
                                    itemBuilder5 = this.createEnchantAura(itemBuilder5);
                                    break;
                                case ENABLE:
                                    itemBuilder6 = this.createEnchantAura(itemBuilder6);
                            }

                            gui.addItem((new GUIItem(itemBuilder8.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_LORE.getStringList(uuid)).build(), this.jumpItems1Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    guiItem4.setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType.AMOUNT_1);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder1.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_LORE.getStringList(uuid)).setAmount(2).build(), this.jumpItems2Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    guiItem4.setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType.AMOUNT_2);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder2.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_LORE.getStringList(uuid)).setAmount(5).build(), this.jumpItems5Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    guiItem4.setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType.AMOUNT_5);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder3.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_LORE.getStringList(uuid)).build(), this.jumpItemFireballSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    guiItem4.setItemType(FireballTNTJumpingEnums.FireballTNTJumpingItemType.FIREBALL);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder4.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_LORE.getStringList(uuid)).build(), this.jumpItemTNTSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    guiItem4.setItemType(FireballTNTJumpingEnums.FireballTNTJumpingItemType.TNT);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder5.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_LORE.getStringList(uuid)).build(), this.jumpWoolDisableSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    guiItem4.setWoolType(FireballTNTJumpingEnums.FireballTNTJumpingWoolType.DISABLE);
                                }
                            }));
                            gui.addItem((new GUIItem(itemBuilder6.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_LORE.getStringList(uuid)).build(), this.jumpWoolEnableSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItem, GUI gui2) {
                                    guiItem4.setWoolType(FireballTNTJumpingEnums.FireballTNTJumpingWoolType.ENABLE);
                                }
                            }));
                            gui.getGUIItems().forEach((guiItem) -> guiItem.addClickAction(new ClickAction() {
                                public void onClick(GUIItem guiItemx, GUI gui2x) {
                                    player.closeInventory();
                                    player.sendMessage(Language.MessagesEnum.GAME_UPDATED_YOUR_SETTINGS.getString(player.getUniqueId()));
                                    FireballTNTJumpingMode.getInstance().refresh(player, guiItem4.toData());
                                }
                            }));
                            break;
                        default:
                            return;
                    }

                    gui.open();
                }
            }
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(!WorldEngine.getInstance().getPracticeWorld().equals(e.getPlayer().getWorld())) return;
        if (e.getAction().name().contains("RIGHT") && player.getItemInHand().getType().equals(this.gameSettingsMaterial)) {
            this.open(player);
        }

    }

    private ItemBuilder createEnchantAura(ItemBuilder itemBuilder) {
        return itemBuilder.enchantment().addUnsafe(Enchantment.DURABILITY, 1).flag().add(ItemFlag.values());
    }
}
