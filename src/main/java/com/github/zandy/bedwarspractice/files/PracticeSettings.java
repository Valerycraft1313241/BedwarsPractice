package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bamboolib.versionsupport.material.Materials;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PracticeSettings extends BambooFile {
    private static PracticeSettings instance = null;

    private PracticeSettings() {
        super("PracticeSettings");
        Arrays.stream(PracticeSettings.GameSettingsEnum.values()).forEach((setting) -> this.addDefault(setting.path, setting.defaultValue));
        this.copyDefaults();
        this.save();
    }

    public static PracticeSettings getInstance() {
        if (instance == null) {
            instance = new PracticeSettings();
        }

        return instance;
    }

    public enum GameSettingsEnum {
        BRIDGING_MATERIAL("Bridging.Material", Materials.LIME_WOOL.name()),
        BRIDGING_VOID_RESTART("Bridging.Void-Restart", 90),
        MLG_HEIGHT_HIGH("MLG.Height.High", 19),
        MLG_HEIGHT_MEDIUM("MLG.Height.Medium", 39),
        MLG_HEIGHT_LOW("MLG.Height.Low", 59),
        FIREBALL_TNT_JUMPING_MATERIAL("Fireball-TNT-Jumping.Material", Materials.LIME_WOOL.name()),
        FIREBALL_TNT_JUMPING_VOID_RESTART("Fireball-TNT-Jumping.Void-Restart", 90),
        FIREBALL_TNT_JUMPING_PLACEABLE_BLOCKS_AREA_OFFSET("Fireball-TNT-Jumping.Placeable-Blocks-Area-Offset", 4),
        FIREBALL_TNT_JUMPING_PLATFORM_MATERIAL("Fireball-TNT-Jumping.Platform.Material", Materials.GOLD_BLOCK.name()),
        FIREBALL_TNT_JUMPING_PLATFORM_OFFSET("Fireball-TNT-Jumping.Platform.Offset", 7),
        FIREBALL_TNT_JUMPING_PLATFORM_LENGTH("Fireball-TNT-Jumping.Platform.Length", 64),
        SETTINGS_TIME_STATIC("Settings.Time.Static", true),
        SETTINGS_TIME_TYPE("Settings.Time.Type", "DAY"),
        SETTINGS_TIME_PRECISE("Settings.Time.Precise", 10000),
        MODE_SELECTOR_MATERIAL("Mode-Selector.Material", Materials.NETHER_STAR.name()),
        MODE_SELECTOR_COOLDOWN_SECONDS("Mode-Selector.Cooldown-Seconds", 5),
        MODE_SELECTOR_GUI_SLOTS("Mode-Selector.Gui.Slots", 36),
        MODE_SELECTOR_GUI_BRIDGING_MATERIAL("Mode-Selector.Gui.Bridging.Material", Materials.CYAN_WOOL.name()),
        MODE_SELECTOR_GUI_BRIDGING_SLOT("Mode-Selector.Gui.Bridging.Slot", 12),
        MODE_SELECTOR_GUI_MLG_MATERIAL("Mode-Selector.Gui.MLG.Material", Materials.WATER_BUCKET.name()),
        MODE_SELECTOR_GUI_MLG_SLOT("Mode-Selector.Gui.MLG.Slot", 14),
        MODE_SELECTOR_GUI_FIREBALL_TNT_JUMPING_MATERIAL("Mode-Selector.Gui.Fireball-TNT-Jumping.Material", Materials.TNT.name()),
        MODE_SELECTOR_GUI_FIREBALL_TNT_JUMPING_SLOT("Mode-Selector.Gui.Fireball-TNT-Jumping.Slot", 16),
        MODE_SELECTOR_GUI_CLOSE_MATERIAL("Mode-Selector.Gui.Close.Material", Materials.BARRIER.name()),
        MODE_SELECTOR_GUI_CLOSE_SLOT("Mode-Selector.Gui.Close.Slot", 32),
        MODE_SELECTOR_GUI_RETURN_TO_LOBBY_MATERIAL("Mode-Selector.Gui.Return-To-Lobby.Material", Materials.RED_BED.name()),
        MODE_SELECTOR_GUI_RETURN_TO_LOBBY_SLOT("Mode-Selector.Gui.Return-To-Lobby.Slot", 36),
        MODE_SELECTOR_GUI_GO_BACK_ENABLED("Mode-Selector.Gui.Go-Back.Enabled", true),
        MODE_SELECTOR_GUI_GO_BACK_MATERIAL("Mode-Selector.Gui.Go-Back.Material", Materials.ARROW.name()),
        MODE_SELECTOR_GUI_GO_BACK_SLOT("Mode-Selector.Gui.Go-Back.Slot", 32),
        MODE_SELECTOR_GUI_GO_BACK_COMMANDS("Mode-Selector.Gui.Go-Back.Commands", Arrays.asList("/dummy_command_1", "/dummy_command_2")),
        GAME_SETTINGS_MATERIAL("Game-Settings.Material", Materials.COMPARATOR.name()),
        GAME_SETTINGS_GUI_SLOTS("Game-Settings.Gui.Slots", 54),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_MATERIAL("Game-Settings.Gui.Bridging.Blocks.30.Material", Materials.WHITE_WOOL.name()),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_SLOT("Game-Settings.Gui.Bridging.Blocks.30.Slot", 12),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_MATERIAL("Game-Settings.Gui.Bridging.Blocks.50.Material", Materials.WHITE_WOOL.name()),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_SLOT("Game-Settings.Gui.Bridging.Blocks.50.Slot", 21),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_MATERIAL("Game-Settings.Gui.Bridging.Blocks.100.Material", Materials.WHITE_WOOL.name()),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_SLOT("Game-Settings.Gui.Bridging.Blocks.100.Slot", 30),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_MATERIAL("Game-Settings.Gui.Bridging.Blocks.Infinite.Material", Materials.WHITE_WOOL.name()),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_SLOT("Game-Settings.Gui.Bridging.Blocks.Infinite.Slot", 39),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_MATERIAL("Game-Settings.Gui.Bridging.Level.None.Material", Materials.OAK_STAIRS.name()),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_SLOT("Game-Settings.Gui.Bridging.Level.None.Slot", 14),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_MATERIAL("Game-Settings.Gui.Bridging.Level.Slight.Material", Materials.OAK_STAIRS.name()),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_SLOT("Game-Settings.Gui.Bridging.Level.Slight.Slot", 23),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_MATERIAL("Game-Settings.Gui.Bridging.Level.Staircase.Material", Materials.OAK_STAIRS.name()),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_SLOT("Game-Settings.Gui.Bridging.Level.Staircase.Slot", 32),
        GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_MATERIAL("Game-Settings.Gui.Bridging.Angle.Straight.Material", Materials.ARROW.name()),
        GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_SLOT("Game-Settings.Gui.Bridging.Angle.Straight.Slot", 16),
        GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_MATERIAL("Game-Settings.Gui.Bridging.Angle.Diagonal.Material", Materials.ARROW.name()),
        GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_SLOT("Game-Settings.Gui.Bridging.Angle.Diagonal.Slot", 25),
        GAME_SETTINGS_GUI_MLG_SIZE_LARGE_MATERIAL("Game-Settings.Gui.MLG.Size.Large.Material", Materials.OAK_PLANKS.name()),
        GAME_SETTINGS_GUI_MLG_SIZE_LARGE_SLOT("Game-Settings.Gui.MLG.Size.Large.Slot", 11),
        GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_MATERIAL("Game-Settings.Gui.MLG.Size.Medium.Material", Materials.OAK_PRESSURE_PLATE.name()),
        GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_SLOT("Game-Settings.Gui.MLG.Size.Medium.Slot", 20),
        GAME_SETTINGS_GUI_MLG_SIZE_SMALL_MATERIAL("Game-Settings.Gui.MLG.Size.Small.Material", Materials.OAK_BUTTON.name()),
        GAME_SETTINGS_GUI_MLG_SIZE_SMALL_SLOT("Game-Settings.Gui.MLG.Size.Small.Slot", 29),
        GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_MATERIAL("Game-Settings.Gui.MLG.Height.High.Material", Materials.QUARTZ_BLOCK.name()),
        GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_SLOT("Game-Settings.Gui.MLG.Height.High.Slot", 13),
        GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_MATERIAL("Game-Settings.Gui.MLG.Height.Medium.Material", Materials.QUARTZ_STAIRS.name()),
        GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_SLOT("Game-Settings.Gui.MLG.Height.Medium.Slot", 22),
        GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_MATERIAL("Game-Settings.Gui.MLG.Height.Low.Material", Materials.QUARTZ_SLAB.name()),
        GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_SLOT("Game-Settings.Gui.MLG.Height.Low.Slot", 31),
        GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_MATERIAL("Game-Settings.Gui.MLG.Position.Centered.Material", Materials.PUMPKIN.name()),
        GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_SLOT("Game-Settings.Gui.MLG.Position.Centered.Slot", 15),
        GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_MATERIAL("Game-Settings.Gui.MLG.Position.Random.Material", Materials.JACK_O_LANTERN.name()),
        GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_SLOT("Game-Settings.Gui.MLG.Position.Random.Slot", 24),
        GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_MATERIAL("Game-Settings.Gui.MLG.Tallness.1.Blocks.Material", Materials.GOLD_BLOCK.name()),
        GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_SLOT("Game-Settings.Gui.MLG.Tallness.1.Blocks.Slot", 17),
        GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_MATERIAL("Game-Settings.Gui.MLG.Tallness.2.Blocks.Material", Materials.GOLD_BLOCK.name()),
        GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_SLOT("Game-Settings.Gui.MLG.Tallness.2.Blocks.Slot", 26),
        GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_MATERIAL("Game-Settings.Gui.MLG.Tallness.3.Blocks.Material", Materials.GOLD_BLOCK.name()),
        GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_SLOT("Game-Settings.Gui.MLG.Tallness.3.Blocks.Slot", 35),
        GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_SLOT("Game-Settings.Gui.MLG.Item.Water-Bucket.Slot", 46),
        GAME_SETTINGS_GUI_MLG_ITEM_LADDER_SLOT("Game-Settings.Gui.MLG.Item.Ladder.Slot", 47),
        GAME_SETTINGS_GUI_MLG_SHUFFLE_NONE_MATERIAL("Game-Settings.Gui.MLG.Shuffle.None.Material", Materials.ENDER_EYE.name()),
        GAME_SETTINGS_GUI_MLG_SHUFFLE_NONE_SLOT("Game-Settings.Gui.MLG.Shuffle.None.Slot", 53),
        GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_MATERIAL("Game-Settings.Gui.MLG.Shuffle.Shuffled.Material", Materials.ENDER_PEARL.name()),
        GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_SLOT("Game-Settings.Gui.MLG.Shuffle.Shuffled.Slot", 54),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_MATERIAL("Game-Settings.Gui.Fireball-TNT-Jumping.Items.1.Material", Materials.STICK.name()),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_SLOT("Game-Settings.Gui.Fireball-TNT-Jumping.Items.1.Slot", 11),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_MATERIAL("Game-Settings.Gui.Fireball-TNT-Jumping.Items.2.Material", Materials.STICK.name()),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_SLOT("Game-Settings.Gui.Fireball-TNT-Jumping.Items.2.Slot", 20),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_MATERIAL("Game-Settings.Gui.Fireball-TNT-Jumping.Items.5.Material", Materials.STICK.name()),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_SLOT("Game-Settings.Gui.Fireball-TNT-Jumping.Items.5.Slot", 29),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_SLOT("Game-Settings.Gui.Fireball-TNT-Jumping.Item.Fireball.Slot", 13),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_SLOT("Game-Settings.Gui.Fireball-TNT-Jumping.Item.TNT.Slot", 22),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_MATERIAL("Game-Settings.Gui.Fireball-TNT-Jumping.Wool.Disable.Material", Materials.WHITE_WOOL.name()),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_SLOT("Game-Settings.Gui.Fireball-TNT-Jumping.Wool.Disable.Slot", 15),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_MATERIAL("Game-Settings.Gui.Fireball-TNT-Jumping.Wool.Enable.Material", Materials.WHITE_WOOL.name()),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_SLOT("Game-Settings.Gui.Fireball-TNT-Jumping.Wool.Enable.Slot", 24),
        COMMAND_SPECTATE_COOLDOWN("Command.Spectate.Cooldown", 5),
        INTERACT_RESTRICTED_BLOCKS("Interact.Restricted-Blocks", Arrays.asList(Materials.ENDER_CHEST.name(), Materials.CHEST.name()));

        final String path;
        final Object defaultValue;

        GameSettingsEnum(String path, Object defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        public Materials getMaterial() {
            return Materials.valueOf(PracticeSettings.getInstance().getString(this.path));
        }

        public List<Material> getMaterials() {
            ArrayList<Material> materials = new ArrayList<>();

            for (String materialName : PracticeSettings.getInstance().getStringList(this.path)) {
                materials.add(Materials.valueOf(materialName).getItem().getMaterial());
            }

            return materials;
        }

        public boolean getBoolean() {
            return PracticeSettings.getInstance().getBoolean(this.path);
        }

        public String getString() {
            return PracticeSettings.getInstance().getString(this.path);
        }

        public List<String> getStringList() {
            return PracticeSettings.getInstance().getStringList(this.path);
        }

        public int getInt() {
            return PracticeSettings.getInstance().getInt(this.path);
        }

    }
}
