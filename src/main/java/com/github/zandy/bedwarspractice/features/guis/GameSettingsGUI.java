package com.github.zandy.bedwarspractice.features.guis;

import com.github.zandy.bamboolib.gui.ClickAction;
import com.github.zandy.bamboolib.gui.GUI;
import com.github.zandy.bamboolib.gui.GUIItem;
import com.github.zandy.bamboolib.item.ItemBuilder;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bedwarspractice.engine.GameEngine;
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
            boolean isSlot = false;

            for (int i = 1; i <= 6; ++i) {
                if ((double) this.inventorySlots / 9.0D == (double) i) {
                    isSlot = true;
                    break;
                }
            }

            if (!isSlot) {

                for (String s : Language.MessagesEnum.GAME_SETTINGS_WRONG_SLOTS_NUMBER.getStringList(uuid)) {
                    player.sendMessage(s);
                }

            } else {
                boolean isSlot2 = false;

                for (Integer var7 : this.slotsList) {
                    if (var7 > this.inventorySlots - 1) {
                        isSlot2 = true;
                        break;
                    }
                }

                if (isSlot2) {
                    Language.MessagesEnum.GAME_SETTINGS_WRONG_SLOTS_ITEM.getStringList(uuid).forEach((var2x) -> player.sendMessage(var2x.replace("[inventorySlots]", String.valueOf(this.inventorySlots))));
                } else {
                    ItemBuilder var8;
                    ItemBuilder var9;
                    ItemBuilder var10;
                    ItemBuilder var11;
                    ItemBuilder var12;
                    ItemBuilder var13;
                    ItemBuilder var15;
                    GUI var23;
                    ItemBuilder var25;
                    ItemBuilder var28;
                    switch (gameEngine.getPracticeTypeMap().get(uuid)) {
                        case BRIDGING:
                            var23 = new GUI(player, this.inventorySlots, Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_TITLE.getString(uuid));
                            var25 = this.bridging30Material.getItem();
                            var8 = this.bridging50Material.getItem();
                            var9 = this.bridging100Material.getItem();
                            var10 = this.bridgingInfiniteMaterial.getItem();
                            var11 = this.bridgingNoneMaterial.getItem();
                            var12 = this.bridgingSlightMaterial.getItem();
                            var13 = this.bridgingStaircaseMaterial.getItem();
                            var28 = this.bridgingStraightMaterial.getItem();
                            var15 = this.bridgingDiagonalMaterial.getItem();
                            final BridgingInfo var29 = BridgingInfo.get(uuid);
                            switch (var29.getBlocksType()) {
                                case BLOCKS_30:
                                    var25 = this.createEnchantAura(var25);
                                    break;
                                case BLOCKS_50:
                                    var8 = this.createEnchantAura(var8);
                                    break;
                                case BLOCKS_100:
                                    var9 = this.createEnchantAura(var9);
                                    break;
                                case BLOCKS_INFINITE:
                                    var10 = this.createEnchantAura(var10);
                            }

                            switch (var29.getLevelType()) {
                                case NONE:
                                    var11 = this.createEnchantAura(var11);
                                    break;
                                case SLIGHT:
                                    var12 = this.createEnchantAura(var12);
                                    break;
                                case STAIRCASE:
                                    var13 = this.createEnchantAura(var13);
                            }

                            switch (var29.getAngleType()) {
                                case STRAIGHT:
                                    var28 = this.createEnchantAura(var28);
                                    break;
                                case DIAGONAL:
                                    var15 = this.createEnchantAura(var15);
                            }

                            var23.addItem((new GUIItem(var25.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_LORE.getStringList(uuid)).setAmount(30).build(), this.bridging30Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setBlocksType(BridgingEnums.BridgingBlocksType.BLOCKS_30);
                                }
                            }));
                            var23.addItem((new GUIItem(var8.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_LORE.getStringList(uuid)).setAmount(50).build(), this.bridging50Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setBlocksType(BridgingEnums.BridgingBlocksType.BLOCKS_50);
                                }
                            }));
                            var23.addItem((new GUIItem(var9.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_LORE.getStringList(uuid)).setAmount(64).build(), this.bridging100Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setBlocksType(BridgingEnums.BridgingBlocksType.BLOCKS_100);
                                }
                            }));
                            var23.addItem((new GUIItem(var10.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_LORE.getStringList(uuid)).build(), this.bridgingInfiniteSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setBlocksType(BridgingEnums.BridgingBlocksType.BLOCKS_INFINITE);
                                }
                            }));
                            var23.addItem((new GUIItem(var11.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_LORE.getStringList(uuid)).build(), this.bridgingNoneSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setLevelType(BridgingEnums.BridgingLevelType.NONE);
                                }
                            }));
                            var23.addItem((new GUIItem(var12.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_LORE.getStringList(uuid)).build(), this.bridgingSlightSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setLevelType(BridgingEnums.BridgingLevelType.SLIGHT);
                                }
                            }));
                            var23.addItem((new GUIItem(var13.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_LORE.getStringList(uuid)).build(), this.bridgingStaircaseSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setLevelType(BridgingEnums.BridgingLevelType.STAIRCASE);
                                }
                            }));
                            var23.addItem((new GUIItem(var28.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_LORE.getStringList(uuid)).build(), this.bridgingStraightSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setAngleType(BridgingEnums.BridgingAngleType.STRAIGHT);
                                }
                            }));
                            var23.addItem((new GUIItem(var15.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_LORE.getStringList(uuid)).build(), this.bridgingDiagonalSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var29.setAngleType(BridgingEnums.BridgingAngleType.DIAGONAL);
                                }
                            }));
                            var23.getGUIItems().forEach((var3x) -> var3x.addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1x, GUI var2x) {
                                    player.closeInventory();
                                    BridgingMode.getInstance().refresh(player, var29.toData());
                                }
                            }));
                            break;
                        case MLG:
                            final MLGInfo var27 = MLGInfo.get(uuid);
                            var23 = new GUI(player, this.inventorySlots, Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TITLE.getString(uuid));
                            var9 = this.mlgSizeLargeMaterial.getItem();
                            var10 = this.mlgSizeMediumMaterial.getItem();
                            var11 = this.mlgSizeSmallMaterial.getItem();
                            var12 = this.mlgHeightHighMaterial.getItem();
                            var13 = this.mlgHeightMediumMaterial.getItem();
                            var28 = this.mlgHeightLowMaterial.getItem();
                            var15 = this.mlgPositionCenteredMaterial.getItem();
                            ItemBuilder var16 = this.mlgPositionRandomMaterial.getItem();
                            ItemBuilder var17 = this.mlgTallness1Material.getItem();
                            ItemBuilder var18 = this.mlgTallness2Material.getItem();
                            ItemBuilder var19 = this.mlgTallness3Material.getItem();
                            ItemBuilder var20 = this.mlgShuffleNoneMaterial.getItem();
                            ItemBuilder var21 = this.mlgShuffleShuffledMaterial.getItem();
                            switch (var27.getSizeType()) {
                                case LARGE:
                                    var9 = this.createEnchantAura(var9);
                                    break;
                                case MEDIUM:
                                    var10 = this.createEnchantAura(var10);
                                    break;
                                case SMALL:
                                    var11 = this.createEnchantAura(var11);
                            }

                            switch (var27.getHeightType()) {
                                case HIGH:
                                    var12 = this.createEnchantAura(var12);
                                    break;
                                case MEDIUM:
                                    var13 = this.createEnchantAura(var13);
                                    break;
                                case LOW:
                                    var28 = this.createEnchantAura(var28);
                            }

                            switch (var27.getPositionType()) {
                                case CENTER:
                                    var15 = this.createEnchantAura(var15);
                                    break;
                                case RANDOM:
                                    var16 = this.createEnchantAura(var16);
                            }

                            switch (var27.getTallnessType()) {
                                case BLOCKS_1:
                                    var17 = this.createEnchantAura(var17);
                                    break;
                                case BLOCKS_2:
                                    var18 = this.createEnchantAura(var18);
                                    break;
                                case BLOCKS_3:
                                    var19 = this.createEnchantAura(var19);
                            }

                            switch (var27.getShuffleType()) {
                                case NONE:
                                    var20 = this.createEnchantAura(var20);
                                    break;
                                case SHUFFLED:
                                    var21 = this.createEnchantAura(var21);
                            }

                            var23.addItem((new GUIItem(var9.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_LARGE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_LARGE_LORE.getStringList(uuid)).build(), this.mlgSizeLargeSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setSizeType(MLGEnums.MLGSizeType.LARGE);
                                }
                            }));
                            var23.addItem((new GUIItem(var10.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_LORE.getStringList(uuid)).build(), this.mlgSizeMediumSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setSizeType(MLGEnums.MLGSizeType.MEDIUM);
                                }
                            }));
                            var23.addItem((new GUIItem(var11.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_SMALL_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_SMALL_LORE.getStringList(uuid)).build(), this.mlgSizeSmallSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setSizeType(MLGEnums.MLGSizeType.SMALL);
                                }
                            }));
                            var23.addItem((new GUIItem(var12.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_LORE.getStringList(uuid)).build(), this.mlgHeightHighSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setHeightType(MLGEnums.MLGHeightType.HIGH);
                                }
                            }));
                            var23.addItem((new GUIItem(var13.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_LORE.getStringList(uuid)).build(), this.mlgHeightMediumSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setHeightType(MLGEnums.MLGHeightType.MEDIUM);
                                }
                            }));
                            var23.addItem((new GUIItem(var28.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_LORE.getStringList(uuid)).build(), this.mlgHeightLowSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setHeightType(MLGEnums.MLGHeightType.LOW);
                                }
                            }));
                            var23.addItem((new GUIItem(var15.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_LORE.getStringList(uuid)).build(), this.mlgPositionCenteredSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setPositionType(MLGEnums.MLGPositionType.CENTER);
                                }
                            }));
                            var23.addItem((new GUIItem(var16.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_LORE.getStringList(uuid)).build(), this.mlgPositionRandomSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setPositionType(MLGEnums.MLGPositionType.RANDOM);
                                }
                            }));
                            var23.addItem((new GUIItem(var17.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_LORE.getStringList(uuid)).build(), this.mlgTallness1Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setTallnessType(MLGEnums.MLGTallnessType.BLOCKS_1);
                                }
                            }));
                            var23.addItem((new GUIItem(var18.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_LORE.getStringList(uuid)).build(), this.mlgTallness2Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setTallnessType(MLGEnums.MLGTallnessType.BLOCKS_2);
                                }
                            }));
                            var23.addItem((new GUIItem(var19.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_LORE.getStringList(uuid)).build(), this.mlgTallness3Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setTallnessType(MLGEnums.MLGTallnessType.BLOCKS_3);
                                }
                            }));
                            var23.addItem((new GUIItem(var20.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_NONE_NAME.getString(uuid)).build(), this.mlgShuffleNoneSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setShuffleType(MLGEnums.MLGShuffleType.NONE);
                                }
                            }));
                            var23.addItem((new GUIItem(var21.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_LORE.getStringList(uuid)).build(), this.mlgShuffleShuffledSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setShuffleType(MLGEnums.MLGShuffleType.SHUFFLED);
                                }
                            }));
                            var23.addItem((new GUIItem(Materials.WATER_BUCKET.getItem().setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_LORE.getStringList(uuid)).build(), this.mlgItemWaterBucketSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setItemType(MLGEnums.MLGItemType.WATER);
                                }
                            }));
                            var23.addItem((new GUIItem(Materials.LADDER.getItem().setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_LADDER_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_LADDER_LORE.getStringList(uuid)).build(), this.mlgItemLadderSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var27.setItemType(MLGEnums.MLGItemType.LADDER);
                                }
                            }));
                            var23.getGUIItems().forEach((var3x) -> var3x.addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1x, GUI var2x) {
                                    player.closeInventory();
                                    player.sendMessage(Language.MessagesEnum.GAME_UPDATED_YOUR_SETTINGS.getString(player.getUniqueId()));
                                    MLGMode.getInstance().refresh(player, var27.toData());
                                }
                            }));
                            break;
                        case FIREBALL_TNT_JUMPING:
                            var23 = new GUI(player, this.inventorySlots, Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_TITLE.getString(uuid));
                            var25 = this.jumpItems1Material.getItem();
                            var8 = this.jumpItems2Material.getItem();
                            var9 = this.jumpItems5Material.getItem();
                            var10 = Materials.FIRE_CHARGE.getItem();
                            var11 = Materials.TNT.getItem();
                            var12 = this.jumpWoolDisableMaterial.getItem();
                            var13 = this.jumpWoolEnableMaterial.getItem();
                            final FireballTNTJumpingInfo var14 = FireballTNTJumpingInfo.get(uuid);
                            switch (var14.getAmountType()) {
                                case AMOUNT_1:
                                    var25 = this.createEnchantAura(var25);
                                    break;
                                case AMOUNT_2:
                                    var8 = this.createEnchantAura(var8);
                                    break;
                                case AMOUNT_5:
                                    var9 = this.createEnchantAura(var9);
                            }

                            switch (var14.getItemType()) {
                                case FIREBALL:
                                    var10 = this.createEnchantAura(var10);
                                    break;
                                case TNT:
                                    var11 = this.createEnchantAura(var11);
                            }

                            switch (var14.getWoolType()) {
                                case DISABLE:
                                    var12 = this.createEnchantAura(var12);
                                    break;
                                case ENABLE:
                                    var13 = this.createEnchantAura(var13);
                            }

                            var23.addItem((new GUIItem(var25.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_LORE.getStringList(uuid)).build(), this.jumpItems1Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var14.setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType.AMOUNT_1);
                                }
                            }));
                            var23.addItem((new GUIItem(var8.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_LORE.getStringList(uuid)).setAmount(2).build(), this.jumpItems2Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var14.setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType.AMOUNT_2);
                                }
                            }));
                            var23.addItem((new GUIItem(var9.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_LORE.getStringList(uuid)).setAmount(5).build(), this.jumpItems5Slot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var14.setAmountType(FireballTNTJumpingEnums.FireballTNTJumpingAmountType.AMOUNT_5);
                                }
                            }));
                            var23.addItem((new GUIItem(var10.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_LORE.getStringList(uuid)).build(), this.jumpItemFireballSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var14.setItemType(FireballTNTJumpingEnums.FireballTNTJumpingItemType.FIREBALL);
                                }
                            }));
                            var23.addItem((new GUIItem(var11.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_LORE.getStringList(uuid)).build(), this.jumpItemTNTSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var14.setItemType(FireballTNTJumpingEnums.FireballTNTJumpingItemType.TNT);
                                }
                            }));
                            var23.addItem((new GUIItem(var12.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_LORE.getStringList(uuid)).build(), this.jumpWoolDisableSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var14.setWoolType(FireballTNTJumpingEnums.FireballTNTJumpingWoolType.DISABLE);
                                }
                            }));
                            var23.addItem((new GUIItem(var13.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_NAME.getString(uuid)).setLore(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_LORE.getStringList(uuid)).build(), this.jumpWoolEnableSlot)).addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1, GUI var2) {
                                    var14.setWoolType(FireballTNTJumpingEnums.FireballTNTJumpingWoolType.ENABLE);
                                }
                            }));
                            var23.getGUIItems().forEach((var3x) -> var3x.addClickAction(new ClickAction() {
                                public void onClick(GUIItem var1x, GUI var2x) {
                                    player.closeInventory();
                                    player.sendMessage(Language.MessagesEnum.GAME_UPDATED_YOUR_SETTINGS.getString(player.getUniqueId()));
                                    FireballTNTJumpingMode.getInstance().refresh(player, var14.toData());
                                }
                            }));
                            break;
                        default:
                            return;
                    }

                    var23.open();
                }
            }
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().name().contains("RIGHT") && player.getItemInHand().getType().equals(this.gameSettingsMaterial)) {
            this.open(player);
        }

    }

    private ItemBuilder createEnchantAura(ItemBuilder itemBuilder) {
        return itemBuilder.enchantment().addUnsafe(Enchantment.DURABILITY, 1).flag().add(ItemFlag.values());
    }
}
