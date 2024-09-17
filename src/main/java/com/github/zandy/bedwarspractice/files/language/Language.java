package com.github.zandy.bedwarspractice.files.language;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bedwarspractice.Main;
import com.github.zandy.bedwarspractice.files.language.iso.*;

import java.io.File;
import java.util.*;

public class Language {
    private static Language instance = null;
    private final HashMap<String, BambooFile> localeFiles = new HashMap<>();
    private final List<String> languageAbbreviations = new ArrayList<>();
    private final HashMap<UUID, String> playerLocale = new HashMap<>();

    private Language() {
    }

    public static Language getInstance() {
        if (instance == null) {
            instance = new Language();
        }

        return instance;
    }

    public void init() {
        new Chinese();
        new Deutsch();
        new English();
        new Italian();
        new Polish();
        new Romanian();
        new Vietnamese();


        ArrayList<String> languageList = new ArrayList<>();
        File languageDir = new File("plugins/BedWarsPractice/Languages");
        languageDir.mkdir();
        Arrays.stream(languageDir.listFiles()).forEach((file) -> {
            if (file.isFile() && !file.getName().contains("DS_Store")) {
                languageList.add(file.getName().replace("Language_", "").replace(".yml", ""));
            }

        });
        if (Main.getBedWarsAPI() != null) {
            com.andrei1058.bedwars.api.language.Language.getLanguages().forEach((language) -> {
                if (!languageList.contains(language.getIso().toUpperCase())) {
                    languageList.add(language.getIso().toUpperCase());
                }

            });
        }

        languageList.forEach((language) -> {
            BambooFile languageFile = new BambooFile("Language_" + language, "Languages");
            Arrays.stream(Language.MessagesEnum.values()).forEach((messageEnum) -> {
                if (!messageEnum.hasNoLanguageSupport()) {
                    languageFile.addDefault(messageEnum.getPath(), messageEnum.getDefaultValue());
                }

            });
            languageFile.copyDefaults();
            languageFile.save();
            this.localeFiles.put(language, languageFile);
            this.languageAbbreviations.add(language);
        });
    }

    public HashMap<String, BambooFile> getLocaleFiles() {
        return this.localeFiles;
    }

    public List<String> getLanguageAbbreviations() {
        return this.languageAbbreviations;
    }

    public HashMap<UUID, String> getPlayerLocale() {
        return this.playerLocale;
    }

    public enum MessagesEnum {
        PLUGIN_LANGUAGE_DISPLAY("Plugin.Language-Display", "English"),
        PLUGIN_NO_CONSOLE("Plugin.No.Console", "&cYou can't use this command in console!"),
        PLUGIN_NO_PERMISSION("Plugin.No.Permission", "&cYou don't have permission to use this command!"),
        PLUGIN_LOBBY_NOT_SET("Plugin.Lobby-Not-Set", Arrays.asList("&4&lERROR! &cServer lobby is not set!", "&7&lUse: &f/bwpa setLobby")),
        COMMAND_HEADER_DEFAULT("Command.Header.Default", "        &f[&a&lBW Practice &8- &7Help&f]"),
        COMMAND_HEADER_ADMIN("Command.Header.Admin", "      &f[&a&lBW Practice Admin &8- &7Help&f]"),
        COMMAND_TAG("Command.Tag", "&f&l[&a&lBedWars Practice&f&l]"),
        COMMAND_CLICK_TO_SUGGEST("Command.Click-To-Suggest", "&7Click to suggest this command."),
        COMMAND_CLICK_TO_RUN("Command.Click-To-Run", "&7Click to run this command."),
        COMMAND_WRONG_USAGE("Command.Wrong-Usage", "&7The command was used wrong."),
        COMMAND_NOT_AVAILABLE_IN_PRACTICE("Command.Not-Available-In-Practice", "&cThis command is not available while you're in practice mode."),
        COMMAND_ADMIN_SET_LOBBY_DESCRIPTION("Command.Admin.Set-Spawn.Description", "&7&oSet server spawn.", true),
        COMMAND_ADMIN_SET_LOBBY_SUCCEEDED("Command.Admin.Set-Lobby.Succeeded", "&7Server lobby was set at your current location."),
        COMMAND_ADMIN_SCHEMATIC_DESCRIPTION("Command.Admin.Schematic.Description", "&7&oCreate practice schematics.", true),
        COMMAND_ADMIN_SCHEMATIC_DISPLAY_HEADER("Command.Admin.Schematic.Display.Header", "      &f[&a&lBW Practice &8- &7Schematic Creator&f]"),
        COMMAND_ADMIN_SCHEMATIC_DISPLAY_CREATE("Command.Admin.Schematic.Display.Create", "&a⦁ &f/bwpa schem create &8- &7&oCreate practice schematics"),
        COMMAND_ADMIN_SCHEMATIC_DISPLAY_LEAVE("Command.Admin.Schematic.Display.Leave", "&a⦁ &f/bwpa schem leave &8- &7&oLeave Schematic Creator"),
        COMMAND_ADMIN_SCHEMATIC_DISPLAY_LOAD("Command.Admin.Schematic.Display.Load", "&a⦁ &f/bwpa schem load &8- &7&oLoad BedWars Practice schematic"),
        COMMAND_ADMIN_SCHEMATIC_DISPLAY_LIST("Command.Admin.Schematic.Display.List", "&a⦁ &f/bwpa schem list &8- &7&oList with required schematics"),
        COMMAND_ADMIN_SCHEMATIC_DISPLAY_POS("Command.Admin.Schematic.Display.Pos", "&a⦁ &f/bwpa schem pos &8- &7&oSet schematic positions"),
        COMMAND_ADMIN_SCHEMATIC_DISPLAY_SAVE("Command.Admin.Schematic.Display.Save", "&a⦁ &f/bwpa schem save &8- &7&oSave the schematic"),
        COMMAND_ADMIN_SCHEMATIC_CREATE_INFO_TELEPORTED("Command.Admin.Schematic.Create.Info.Teleported", "&7You have been teleported to the schematic creator world!"),
        COMMAND_ADMIN_SCHEMATIC_CREATE_INFO_TUTORIAL("Command.Admin.Schematic.Create.Info.Tutorial", Arrays.asList("&7To create schematics for BedWars Practice, read the tutorial from the plugin's Spigot page or watch the Video Tutorial on YouTube.", "&7SpigotMC Link: &f[spigotLink]", "&7YouTube Link: &f[youtubeLink]")),
        COMMAND_ADMIN_SCHEMATIC_LOAD_USAGE("Command.Admin.Schematic.Load.Usage", "&a⦁ &f/bwpa schem load <&aSchematic Name&f>"),
        COMMAND_ADMIN_SCHEMATIC_LOAD_LOADING("Command.Admin.Schematic.Load.Loading", "&eLoading '&a[schemName]&e' schematic..."),
        COMMAND_ADMIN_SCHEMATIC_LOAD_LOADED_AND_PASTED("Command.Admin.Schematic.Load.Loaded-And-Pasted", "&fThe schematic was loaded and pasted &asuccessfully&f!"),
        COMMAND_ADMIN_SCHEMATIC_LOAD_FILE_NOT_FOUND("Command.Admin.Schematic.Load.File-Not-Found", "&c&oThe schematic '[schemName]' you're trying to load doesn't exist."),
        COMMAND_ADMIN_SCHEMATIC_LIST_REQUIRED_SCHEMATICS("Command.Admin.Schematic.List.Required-Schematics", "&7The following schematics are required to be created:"),
        COMMAND_ADMIN_SCHEMATIC_LIST_ENUMERATION("Command.Admin.Schematic.List.Enumeration", "&7➔ &f[schemName]"),
        COMMAND_ADMIN_SCHEMATIC_LIST_INFO("Command.Admin.Schematic.List.Info", "&c&lNOTE! &7All schematics are required in order to run this plugin."),
        COMMAND_ADMIN_SCHEMATIC_LIST_ALL_CREATED("Command.Admin.Schematic.List.All-Created", "&7You've created the required schematics."),
        COMMAND_ADMIN_SCHEMATIC_POS_USAGE("Command.Admin.Schematic.Pos.Usage", "&a⦁ &f/bwpa schem pos <&a1&f, &a2&f, &awand&f>"),
        COMMAND_ADMIN_SCHEMATIC_POS_SET("Command.Admin.Schematic.Pos.Set", "Schematic's &epos [posNumber] &fwas &aset&f."),
        COMMAND_ADMIN_SCHEMATIC_POS_WAND_RECEIVED("Command.Admin.Schematic.Pos.Wand-Received", "Wand received on the first slot."),
        COMMAND_ADMIN_SCHEMATIC_SAVE_USAGE("Command.Admin.Schematic.Save.Usage", Arrays.asList("&a⦁ &f/bwpa schem save <&aSchematic Name&f>", "&c&lNOTE!", "&7The schematic name has to represent the required schematics.", "&7For a detailed list with the schematics required, use:", "&a⦁ &f/bwpa schem list")),
        COMMAND_ADMIN_SCHEMATIC_SAVE_WRONG_SCHEMATIC_NAME("Command.Admin.Schematic.Save.Wrong-Schematic-Name", Arrays.asList("&c&lWrong schematic name!", "&7The schematic name has to represent the required schematics.", "&7For a detailed list with the schematics required, use:", "&a⦁ &f/bwpa schem list")),
        COMMAND_ADMIN_SCHEMATIC_SAVE_STARTED("Command.Admin.Schematic.Save.Started", "&eThe save may take a while. Please wait!"),
        COMMAND_ADMIN_SCHEMATIC_SAVE_SUCCESSFULLY("Command.Admin.Schematic.Save.Successfully", "&aYou've saved &f&o'[schemName]' &aschematic."),
        COMMAND_ADMIN_SCHEMATIC_LEAVE_SETUP_NOT_FINISHED("Command.Admin.Schematic.Leave.Setup-Not-Finished", Arrays.asList("&cThere is a running setup session.", "&7Cancel it by using: &f/bwpa setup quit")),
        COMMAND_ADMIN_SCHEMATIC_LEAVE_SUCCESSFULLY("Command.Admin.Schematic.Leave.Successfully", Arrays.asList("&7You've left schematic creator.", "You have been teleported to the Lobby!")),
        COMMAND_ADMIN_SCHEMATIC_ALREADY_IN_SCHEMATIC_WORLD("Command.Admin.Schematic.Already-In-Schematic-World", "&cYou're already in the schematic world creator."),
        COMMAND_ADMIN_SCHEMATIC_NOT_IN_SCHEMATIC_WORLD("Command.Admin.Schematic.Not-In-Schematic-World", Arrays.asList("&cYou're not in the schematic world creator.", "&7To go in schematic world, use:", "&a⦁ &f/bwpa schem create")),
        COMMAND_ADMIN_SCHEMATIC_WRONG_SUBCOMMAND("Command.Admin.Schematic.Wrong-Subcommand", "&cThe subcommand &f'[subCommand]' &cdoesn't exist."),
        COMMAND_ADMIN_SETUP_DESCRIPTION("Command.Admin.Setup.Description", "&7&oSetup practice modes.", true),
        COMMAND_ADMIN_SETUP_DISPLAY_HEADER("Command.Admin.Setup.Display.Header", "      &f[&a&lBW Practice &8- &7Setup Practice&f]"),
        COMMAND_ADMIN_SETUP_DISPLAY_SET("Command.Admin.Setup.Display.Set", "&a⦁ &f/bwpa setup set &8- &7&oStart setup Practice game"),
        COMMAND_ADMIN_SETUP_DISPLAY_LIST("Command.Admin.Setup.Display.List", "&a⦁ &f/bwpa setup list &8- &7&oList with required practices"),
        COMMAND_ADMIN_SETUP_DISPLAY_POS("Command.Admin.Setup.Display.Pos", "&a⦁ &f/bwpa setup pos &8- &7&oSet Practice ending positions"),
        COMMAND_ADMIN_SETUP_DISPLAY_SAVE("Command.Admin.Setup.Display.Save", "&a⦁ &f/bwpa setup save &8- &7&oSave practice arena state"),
        COMMAND_ADMIN_SETUP_DISPLAY_QUIT("Command.Admin.Setup.Display.Quit", "&a⦁ &f/bwpa setup quit &8- &7&oQuit setting up the arena"),
        COMMAND_ADMIN_SETUP_INVALID_SETUP_SESSION("Command.Admin.Setup.Invalid-Setup-Session", Arrays.asList("&cYou don't have a running setup session!", "&7In order to create a session, use:", "&a⦁ &f/bwpa setup set <&aPractice Name&f>")),
        COMMAND_ADMIN_SETUP_LIST_ALL_CREATED("Command.Admin.Setup.List.All-Created", "&7You've created the required setups."),
        COMMAND_ADMIN_SETUP_LIST_REQUIRED_SETUPS("Command.Admin.Setup.List.Required-Setups", "&7The following practices are required to be set:"),
        COMMAND_ADMIN_SETUP_LIST_ENUMERATION("Command.Admin.Setup.List.Enumeration", "&7➔ &f[practiceName]"),
        COMMAND_ADMIN_SETUP_LIST_INFO("Command.Admin.Setup.List.Info", "&c&lNOTE! &7All practices are required in order to run this plugin."),
        COMMAND_ADMIN_SETUP_SET_USAGE("Command.Admin.Setup.Set.Usage", "&a⦁ &f/bwpa setup set <&aPractice Name&f>"),
        COMMAND_ADMIN_SETUP_SET_ALREADY_IN_SETUP("Command.Admin.Setup.Set.Already-In-Setup", Arrays.asList("&cYou're already setting up a practice arena.", "Arena name: &7[Name]", "&7If you'd like to quit the setup, use: &f/bwpa setup quit")),
        COMMAND_ADMIN_SETUP_SET_WRONG_PRACTICE_NAME("Command.Admin.Setup.Set.Wrong-Practice-Name", Arrays.asList("&cWrong practice name.", "&7Practice names are found by using:", "&a⦁ &f/bwpa setup list")),
        COMMAND_ADMIN_SETUP_SET_SCHEMATIC_NOT_FOUND("Command.Admin.Setup.Set.Schematic-Not-Found", Arrays.asList("&cSchematic '&f[schemName]&c' was not found.", "&7Go back and create the schematic with:", "&a⦁ &f/bwpa schem save")),
        COMMAND_ADMIN_SETUP_SET_LOADING_SCHEMATIC("Command.Admin.Setup.Set.Loading-Schematic", "&eLoading '&a[schemName]&e' schematic..."),
        COMMAND_ADMIN_SETUP_SET_SCHEMATIC_LOADED("Command.Admin.Setup.Set.Schematic-Loaded", "Schematic loaded &asuccessfully&f!"),
        COMMAND_ADMIN_SETUP_SET_INFO_TUTORIAL("Command.Admin.Setup.Set.Tutorial", Arrays.asList("&7To setup BedWars Practice, read the tutorial from the plugin's Spigot page or watch the Video Tutorial on YouTube.", "&7SpigotMC Link: &f[spigotLink]", "&7YouTube Link: &f[youtubeLink]")),
        COMMAND_ADMIN_SETUP_POS_USAGE("Command.Admin.Setup.Pos.Usage", "&a⦁ &f/bwpa setup pos <&a1&f, &a2&f, &awand&f>"),
        COMMAND_ADMIN_SETUP_POS_SET_SUCCESSFULLY("Command.Admin.Setup.Pos.Set.Successfully", "&7You've set &f[practiceName]'s &7pos &f[posNumber]"),
        COMMAND_ADMIN_SETUP_POS_WAND_RECEIVED("Command.Admin.Setup.Pos.Wand-Received", "Wand received on the first slot."),
        COMMAND_ADMIN_SETUP_SAVE_POSITION_NOT_SET("Command.Admin.Setup.Save.Position-Not-Set", Arrays.asList("&cThe practice's position &f[posNumber] &cis not set!", "&7Set it with: &f/bwpa setup pos <&aPosition Number&f>")),
        COMMAND_ADMIN_SETUP_SAVE_SUCCESSFULLY("Command.Admin.Setup.Save.Successfully", "&fYou've successfully saved '&e[practiceName]&f' practice arena."),
        COMMAND_ADMIN_SETUP_QUIT_NOT_IN_SETUP("Command.Admin.Setup.Quit.Not-In-Setup", "&cCan't quit setup since there's no running setup."),
        COMMAND_ADMIN_SETUP_QUIT_SUCCESSFULLY("Command.Admin.Setup.Quit.Successfully", Arrays.asList("&7You have left the setup successfully!", "You have been teleported to the schematic creator spawn!")),
        COMMAND_ADMIN_SETUP_WRONG_SUBCOMMAND("Command.Admin.Setup.Wrong-Subcommand", "&cThe subcommand &f'[subCommand]' &cdoesn't exist."),
        COMMAND_ADMIN_BUILD_DESCRIPTION("Command.Admin.Build.Description", "&7&oAllow build in lobby.", true),
        COMMAND_ADMIN_BUILD_ENABLED("Command.Admin.Build.Enabled", "&7Now you are allowed to build in lobby."),
        COMMAND_ADMIN_BUILD_DISABLED("Command.Admin.Build.Disabled", "&7Now you are not allowed to build in lobby."),
        COMMAND_ADMIN_NPC_SET_DESCRIPTION("Command.Admin.NPC.Set.Description", "&7&oSet Practice NPC.", true),
        COMMAND_ADMIN_NPC_SET_WRONG_USAGE("Command.Admin.NPC.Set.Wrong-Usage", Arrays.asList("&7Usage: &f/bwpa setNPC <&aNPC Type&f>", "&7Available npc types:", "&c➥ &fDefault &c&l[&e&lCLICK HERE&c&l]", "&c➥ &fBridging &c&l[&e&lCLICK HERE&c&l]", "&c➥ &fMLG &c&l[&e&lCLICK HERE&c&l]", "&c➥ &fFireball/TNT Jumping &c&l[&e&lCLICK HERE&c&l]")),
        COMMAND_ADMIN_NPC_SET_CLICK_TO_APPLY("Command.Admin.NPC.Set.Click-To-Apply", "&7Click a &lCitizens NPC &7that you would like to be a Practice NPC."),
        COMMAND_ADMIN_NPC_SET_ALREADY_EXISTS("Command.Admin.NPC.Set.Already-Exists", "&cThe clicked NPC is already set as a Practice NPC."),
        COMMAND_ADMIN_NPC_SET_ADDED("Command.Admin.NPC.Set.Added", "&aThe selected NPC has beed marked as a Practice NPC."),
        COMMAND_ADMIN_NPC_SET_TYPE_ADDED("Command.Admin.NPC.Set.Type-Added", "&7Practice NPC Type: &a&l[practiceType]"),
        COMMAND_ADMIN_NPC_DELETE_DESCRIPTION("Command.Admin.NPC.Delete.Description", "&7&oDetele Practice NPC.", true),
        COMMAND_ADMIN_NPC_DELETE_NO_NPCS_SET("Command.Admin.NPC.Delete.No-NPCs-set", "&7There are no Practice NPCs set."),
        COMMAND_ADMIN_NPC_DELETE_CLICK_TO_DELETE("Command.Admin.NPC.Delete.Click-To-Delete", "&7Click a &lCitizens NPC &7that has already been set as a Practice NPC in order to delete."),
        COMMAND_ADMIN_NPC_DELETE_NOT_PRACTICE("Command.Admin.NPC.Delete.Not-Practice", "&cThe clicked NPC is not a Practice NPC."),
        COMMAND_ADMIN_NPC_DELETE_DELETED("Command.Admin.NPC.Delete.Deleted", "&aThe selected NPC has been turned to a regular &lCitizens NPC&a."),
        COMMAND_PLAYER_PRACTICE_USAGE("Command.Player.Practice.Usage", Arrays.asList("&7Available usages for &a/bwp&7:", "&a⦁ &f/bwp &8[&7Fires up the GUI&8]", "&a⦁ &f/bwp bridging &8[&7Joins bridging&8]", "&a⦁ &f/bwp mlg &8[&7Joins MLG&8]", "&a⦁ &f/bwp fireballtntjumping &8[&7Joins Fireball/TNT Jumping&8]", "&a⦁ &f/bwp spectate &8[&7Spectate a player&8]", "&a⦁ &f/bwp quit &7or &fleave &8[&7Leaves Practice&8]")),
        COMMAND_PLAYER_MENU_SCHEMATICS_NOT_SET("Command.Player.Menu.Schematics-Not-Set", Arrays.asList("&cThere are missing schematics that are mandatory!", "&7In order to check what schematics are missing, use:", "&a⦁ &f/bwpa schem list")),
        COMMAND_PLAYER_MENU_CONFIGURATIONS_NOT_SET("Command.Player.Menu.Configurations-Not-Set", Arrays.asList("&cThere are missing configurations that are mandatory!", "&7In order to check what confirations are missing, use:", "&a⦁ &f/bwpa setup list")),
        COMMAND_PLAYER_QUIT("Command.Player.Quit", "&cYou're not in a practice arena!"),
        COMMAND_PLAYER_LANGUAGE_USAGE("Command.Player.Language.Usage", Arrays.asList("&7Command used incorrectly! Usage:", "&a⦁ &f/bwpl <&aLanguage Abbreviation&f>")),
        COMMAND_PLAYER_LANGUAGE_NOT_FOUND("Command.Player.Language.Not-Found", "&7This language cannot be found! Available languages:"),
        COMMAND_PLAYER_LANGUAGE_LIST_FORMAT("Command.Player.Language.List-Format", "&a⦁ &7[&e[languageAbbreviation]&7] &8| &f[languageName]"),
        COMMAND_PLAYER_LANGUAGE_CHANGED("Command.Player.Language.Changed", "&7Language changed to &f[languageName] &7[&e[languageAbbreviation]&7]!"),
        COMMAND_PLAYER_SPECTATE_WRONG_USAGE("Command.Player.Spectate.Wrong-Usage", Arrays.asList("&7Command used incorrectly! Usage:", "&a⦁ &f/bwp spectate <&aPlayer&f>")),
        COMMAND_PLAYER_SPECTATE_TARGET_NULL("Command.Player.Spectate.Target.Null", "&cTarget player &f[player_name] &cis not online!"),
        COMMAND_PLAYER_SPECTATE_TARGET_NOT_IN_PRACTICE("Command.Player.Spectate.Target.Not-In-Practice", "&cTarget player &f[player_name] &cis not practicing."),
        COMMAND_PLAYER_SPECTATE_ALREADY_SPECTATING("Command.Player.Spectate.Already-Spectating", "&cYou're already spectating &f[player_name]&c!"),
        COMMAND_PLAYER_SPECTATE_IN_COOLDOWN("Command.Player.Spectate.In-Cooldown", "&cYou can change the spectated player once [seconds] seconds!"),
        MODE_SELECTOR_ITEM_NAME("Mode-Selector.Item.Name", "&a&lMode Selector &7(Right-Click)"),
        MODE_SELECTOR_ITEM_LORE("Mode-Selector.Item.Lore", Arrays.asList("&7Right-click to open the mode", "&7selector.")),
        MODE_SELECTOR_TITLE("Mode-Selector.Title", "Bed Wars Practice"),
        MODE_SELECTOR_BRIDGING_NAME("Mode-Selector.Bridging.Name", "&aBridging"),
        MODE_SELECTOR_BRIDGING_LORE("Mode-Selector.Bridging.Lore", Arrays.asList("&7Practice bridging across", "&7the void with wool.", " ", "&eClick to play!")),
        MODE_SELECTOR_MLG_NAME("Mode-Selector.MLG.Name", "&aMLG"),
        MODE_SELECTOR_MLG_LORE("Mode-Selector.MLG.Lore", Arrays.asList("&7Practice preventing fall", "&7damage with water buckets", "&7and ladders", " ", "&eClick to play!")),
        MODE_SELECTOR_FIREBALL_TNT_JUMPING_NAME("Mode-Selector.Fireball-TNT-Jumping.Name", "&aFireball/TNT Jumping"),
        MODE_SELECTOR_FIREBALL_TNT_JUMPING_LORE("Mode-Selector.Fireball-TNT-Jumping.Lore", Arrays.asList("&7Practice jumping over the", "&7void using Fireballs and", "&7TNT.", " ", "&eClick to play!")),
        MODE_SELECTOR_GO_BACK_NAME("Mode-Selector.Go-Back.Name", "&aGo Back"),
        MODE_SELECTOR_GO_BACK_LORE("Mode-Selector.Go-Back.Lore", Collections.singletonList("&7To Play Bed Wars")),
        MODE_SELECTOR_CLOSE_NAME("Mode-Selector.Close.Name", "&cClose"),
        MODE_SELECTOR_RETURN_TO_LOBBY_NAME("Mode-Selector.Return-To-Lobby.Name", "&cReturn to Lobby"),
        MODE_SELECTOR_ALREADY_SELECTED("Mode-Selector.Already-Selected", "&cYou've already have this practice mode selected!"),
        MODE_SELECTOR_COOLDOWN("Mode-Selector.Cooldown", "&cYou can only select a mode every [seconds] seconds!"),
        GAME_SETTINGS_ITEM_NAME("Game-Settings.Name", "&a&lSettings &7(Right Click)"),
        GAME_SETTINGS_ITEM_LORE("Game-Settings.Lore", Arrays.asList("&7Right-click to open the", "&7current mode settings.", "&7Changing mode settings", "&7allows for a wider variety", "&7of practice drills and", "&7different levels of", "&7difficulty.")),
        GAME_SETTINGS_WRONG_SLOTS_NUMBER("Game-Settings.Wrong-Slots.Inventory", Arrays.asList("&cGame Settings GUI has an invalid number of slots.", "&7Accepted slots number: &8[&f9&7, &f18&7, &f27&7, &f36&7, &f45&7, &f54&8]")),
        GAME_SETTINGS_WRONG_SLOTS_ITEM("Game-Settings.Wrong-Slots.Item", Arrays.asList("&cGame Settings GUI Item has an invalid number of slots", "&7Maximum slot number: &8[&f[inventorySlots]&8]")),
        GAME_SETTINGS_GUI_BRIDGING_TITLE("Game-Settings.Gui.Bridging.Title", "Bridging Settings"),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_NAME("Game-Settings.Gui.Bridging.Blocks.30.Name", "&a30 Blocks"),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_LORE("Game-Settings.Gui.Bridging.Blocks.30.Lore", Arrays.asList("&7Set the finishing island", "&7distance to be 30 blocks", "&7away.")),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_NAME("Game-Settings.Gui.Bridging.Blocks.50.Name", "&a50 Blocks"),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_LORE("Game-Settings.Gui.Bridging.Blocks.50.Lore", Arrays.asList("&7Set the finishing island", "&7distance to be 50 blocks", "&7away.")),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_NAME("Game-Settings.Gui.Bridging.Blocks.100.Name", "&a100 Blocks"),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_LORE("Game-Settings.Gui.Bridging.Blocks.100.Lore", Arrays.asList("&7Set the finishing island", "&7distance to be 100 blocks", "&7away.")),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_NAME("Game-Settings.Gui.Bridging.Blocks.Infinite.Name", "&aInfinite!"),
        GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_LORE("Game-Settings.Gui.Bridging.Blocks.Infinite.Lore", Arrays.asList("&7Have no finishing island", "&7and see how far you can go.")),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_NAME("Game-Settings.Gui.Bridging.Level.None.Name", "&aNone"),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_LORE("Game-Settings.Gui.Bridging.Level.None.Lore", Arrays.asList("&7Set the elevation level of", "&7the finishing island to", "&7None.")),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_NAME("Game-Settings.Gui.Bridging.Level.Slight.Name", "&aSlight"),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_LORE("Game-Settings.Gui.Bridging.Level.Slight.Lore", Arrays.asList("&7Set the elevation level of", "&7the finishing island to", "&7Slight.")),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_NAME("Game-Settings.Gui.Bridging.Level.Staircase.Name", "&aStaircase"),
        GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_LORE("Game-Settings.Gui.Bridging.Level.Staircase.Lore", Arrays.asList("&7Set the elevation level of", "&7the finishing island to", "&7Staircase.")),
        GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_NAME("Game-Settings.Gui.Bridging.Angle.Straight.Name", "&aStraight"),
        GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_LORE("Game-Settings.Gui.Bridging.Angle.Straight.Lore", Arrays.asList("&7Set the angle of the", "&7finishing island to", "&7Straight.")),
        GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_NAME("Game-Settings.Gui.Bridging.Angle.Diagonal.Name", "&aDiagonal"),
        GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_LORE("Game-Settings.Gui.Bridging.Angle.Diagonal.Lore", Arrays.asList("&7Set the angle of the", "&7finishing island to", "&7Diagonal.")),
        GAME_SETTINGS_GUI_MLG_TITLE("Game-Settings.Gui.MLG.Title", "MLG Settings"),
        GAME_SETTINGS_GUI_MLG_SIZE_LARGE_NAME("Game-Settings.Gui.MLG.Size.Large.Name", "&aPlatform Size: Large"),
        GAME_SETTINGS_GUI_MLG_SIZE_LARGE_LORE("Game-Settings.Gui.MLG.Size.Large.Lore", Arrays.asList("&7The landing platform will be 5x5", "&7blocks.")),
        GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_NAME("Game-Settings.Gui.MLG.Size.Medium.Name", "&aPlatform Size: Medium"),
        GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_LORE("Game-Settings.Gui.MLG.Size.Medium.Lore", Arrays.asList("&7The landing platform will be 3x3", "&7blocks.")),
        GAME_SETTINGS_GUI_MLG_SIZE_SMALL_NAME("Game-Settings.Gui.MLG.Size.Small.Name", "&aPlatform Size: Small"),
        GAME_SETTINGS_GUI_MLG_SIZE_SMALL_LORE("Game-Settings.Gui.MLG.Size.Small.Lore", Arrays.asList("&7The landing platform will be a", "&7single block.")),
        GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_NAME("Game-Settings.Gui.MLG.Height.High.Name", "&aPlatform Height: High"),
        GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_LORE("Game-Settings.Gui.MLG.Height.High.Lore", Arrays.asList("&7The landing platform will be", "&7high up, so you fall very", "&7little.")),
        GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_NAME("Game-Settings.Gui.MLG.Height.Medium.Name", "&aPlatform Height: Medium"),
        GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_LORE("Game-Settings.Gui.MLG.Height.Medium.Lore", Arrays.asList("&7The landing platform will be", "&7further down, so you fall", "&7longer.")),
        GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_NAME("Game-Settings.Gui.MLG.Height.Low.Name", "&aPlatform Height: Low"),
        GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_LORE("Game-Settings.Gui.MLG.Height.Low.Lore", Arrays.asList("&7The landing platform will be", "&7very far down, so you fall very", "&7long.")),
        GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_NAME("Game-Settings.Gui.MLG.Position.Centered.Name", "&aPlatform Position: Always centered"),
        GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_LORE("Game-Settings.Gui.MLG.Position.Centered.Lore", Arrays.asList("&7The landing platform will always", "&7be placed right in front of your", "&7island.")),
        GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_NAME("Game-Settings.Gui.MLG.Position.Random.Name", "&aPlatform Position: Random"),
        GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_LORE("Game-Settings.Gui.MLG.Position.Random.Lore", Arrays.asList("&7The landing platform will be", "&7placed random.")),
        GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_NAME("Game-Settings.Gui.MLG.Tallness.1.Blocks.Name", "&aPlatform Tallness: 1 Blocks"),
        GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_LORE("Game-Settings.Gui.MLG.Tallness.1.Blocks.Lore", Arrays.asList("&7Set the platform to be 1 blocks", "&7tall.", " ", "&7Useful for trying to clutch onto", "&7the side of the platform.")),
        GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_NAME("Game-Settings.Gui.MLG.Tallness.2.Blocks.Name", "&aPlatform Tallness: 2 Blocks"),
        GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_LORE("Game-Settings.Gui.MLG.Tallness.2.Blocks.Lore", Arrays.asList("&7Set the platform to be 2 blocks", "&7tall.", " ", "&7Useful for trying to clutch onto", "&7the side of the platform.")),
        GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_NAME("Game-Settings.Gui.MLG.Tallness.3.Blocks.Name", "&aPlatform Tallness: 3 Blocks"),
        GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_LORE("Game-Settings.Gui.MLG.Tallness.3.Blocks.Lore", Arrays.asList("&7Set the platform to be 3 blocks", "&7tall.", " ", "&7Useful for trying to clutch onto", "&7the side of the platform.")),
        GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_NAME("Game.Settings.Gui.MLG.Item.Water-Bucket.Name", "&aItem: Water Bucket"),
        GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_LORE("Game.Settings.Gui.MLG.Item.Water-Bucket.Lore", Arrays.asList("&7Place water before hitting the", "&7ground.")),
        GAME_SETTINGS_GUI_MLG_ITEM_LADDER_NAME("Game-Settings.Gui.MLG.Item.Ladder.Name", "&aItem: Ladder"),
        GAME_SETTINGS_GUI_MLG_ITEM_LADDER_LORE("Game-Settings.Gui.MLG.Item.Ladder.Lore", Arrays.asList("&7Place the ladder and land on its side.", "&7side.")),
        GAME_SETTINGS_GUI_MLG_SHUFFLE_NONE_NAME("Game-Settings.Gui.MLG.Shuffle.None.Name", "&aNo item shuffle."),
        GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_NAME("Game-Settings.Gui.MLG.Shuffle.Shuffled.Name", "&aShuffle item after jumping."),
        GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_LORE("Game-Settings.Gui.MLG.Shuffle.Shuffled.Lore", Arrays.asList("&7This shuffles the slot of the", "&7item after jumping down. This", "&7requires you to react quickly", "&7and change your slot before", "&7making use of the item.")),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_TITLE("Game-Settings.Gui.Fireball-TNT-Jumping.Title", "Settings"),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_NAME("Game-Settings.Gui.Fireball-TNT-Jumping.Items.1.Name", "&a1 Items"),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_LORE("Game-Settings.Gui.Fireball-TNT-Jumping.Items.1.Lore", Arrays.asList("&7Start with 1 of the selected", "&7item.")),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_NAME("Game-Settings.Gui.Fireball-TNT-Jumping.Items.2.Name", "&a2 Items"),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_LORE("Game-Settings.Gui.Fireball-TNT-Jumping.Items.2.Lore", Arrays.asList("&7Start with 2 of the selected", "&7item.")),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_NAME("Game-Settings.Gui.Fireball-TNT-Jumping.Items.5.Name", "&a5 Items"),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_LORE("Game-Settings.Gui.Fireball-TNT-Jumping.Items.5.Lore", Arrays.asList("&7Start with 5 of the selected", "&7item.")),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_NAME("Game-Settings.Gui.Fireball-TNT-Jumping.Item.Fireball.Name", "&aFireball"),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_LORE("Game-Settings.Gui.Fireball-TNT-Jumping.Item.Fireball.Lore", Collections.singletonList("&7You will start with Fireball.")),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_NAME("Game-Settings.Gui.Fireball-TNT-Jumping.Item.TNT.Name", "&aTNT"),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_LORE("Game-Settings.Gui.Fireball-TNT-Jumping.Item.TNT.Lore", Collections.singletonList("&7You will start with TNT.")),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_NAME("Game-Settings.Gui.Fireball-TNT-Jumping.Wool.Disable.Name", "&cDisable Wool"),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_LORE("Game-Settings.Gui.Fireball-TNT-Jumping.Wool.Disable.TNT.Lore", Collections.singletonList("&7You will start with no wool.")),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_NAME("Game-Settings.Gui.Fireball-TNT-Jumping.Wool.Enable.Name", "&aEnable Wool"),
        GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_LORE("Game-Settings.Gui.Fireball-TNT-Jumping.Wool.Enable.TNT.Lore", Arrays.asList("&7You will start with 32 blocks of", "&7wool.", " ", "&7Wool cannot be placed more than", "&72 blocks from the platform edge.")),
        GAME_FAILED("Game.Failed", "&cFailed!"),
        GAME_SUCCESSFUL("Game.Successfull", "&aSuccessful!"),
        GAME_BLOCK_NOT_PLACEABLE("Game.Block-Not-Placeable", "&cYou can't place a block here!"),
        GAME_UPDATED_YOUR_SETTINGS("Game.Updated-Your-Settings", "&aUpdated your settings."),
        GAME_SCOREBOARD_TITLE("Game.Scoreboard.Title", "&e&lBED WARS PRACTICE"),
        GAME_SCOREBOARD_BRIDGING_DEFAULT_LINES("Game.Scoreboard.Bridging.Default.Lines", Arrays.asList("&7[month]/[day]/[year]", " ", "Mode: &a[game_mode]", " ", "Speed: &a[average_speed] m/s", " ", "Personal Best: &a[personal_best]", " ", "&emc.yourserver.org")),
        GAME_SCOREBOARD_BRIDGING_INFINITE_LINES("Game.Scoreboard.Bridging.Infinite.Lines", Arrays.asList("&7[month]/[day]/[year]", " ", "Mode: &a[game_mode]", " ", "Speed: &a[average_speed] m/s", " ", "Blocks placed: &a[blocks_placed]", " ", "&emc.yourserver.org")),
        GAME_SCOREBOARD_MLG_LINES("Game.Scoreboard.MLG.Lines", Arrays.asList("&7[month]/[day]/[year]", " ", "Mode: &a[game_mode]", " ", "&emc.yourserver.org")),
        GAME_SCOREBOARD_FIREBALL_TNT_JUMPING_LINES("Game.Scoreboard.Fireball-TNT-Jumping.Lines", Arrays.asList("&7[month]/[day]/[year]", " ", "Mode: &a[game_mode]", "Longest Jump: &a[longest_jump] Blocks", " ", "&emc.yourserver.org")),
        GAME_SCOREBOARD_PERSONAL_BEST_NONE("Game.Scoreboard.Personal-Best.None", "&cNone!"),
        GAME_SCOREBOARD_GAMEMODE_BRIDGING("Game.Scoreboard.Bridging.Name", "Bridging"),
        GAME_SCOREBOARD_GAMEMODE_MLG("Game.Scoreboard.MLG.Name", "MLG"),
        GAME_SCOREBOARD_GAMEMODE_FIREBALL_TNT_JUMPING("Game.Scoreboard.Fireball-TNT-Jumping.Name", "Fireball/TNT Jumping"),
        GAME_BRIDGING_ACTION_BAR_TIMER_COLOR("Game.Bridging.Action-Bar.Timer-Color", "&6&l"),
        GAME_BRIDGING_ACTION_BAR_START_TIMER("Game.Bridging.Start-Timer", "&6&lPlace a block to start!"),
        GAME_BRIDGING_FINISHED_MESSAGE("Game.Bridging.Finished.Message", "&aCompleted! Took [seconds] seconds."),
        GAME_BRIDGING_FINISHED_TITLE("Game.Bridging.Finished.Title", "&a[seconds] seconds"),
        GAME_BRIDGING_FINISHED_INFINITE_MESSAGE("Game.Bridging.Finished-Infinite.Message", "&aTook [seconds] seconds, with [blocks_placed] blocks placed!"),
        GAME_MLG_ITEM_NAME_WATER("Game.MLG.Item-Name.Water", "&aPlace water before hitting the ground."),
        GAME_MLG_ITEM_NAME_LADDER("Game.MLG.Item-Name.Ladder", "&aPlace the ladder and land on its side."),
        GAME_MLG_ACTION_BAR("Game.MLG.Action-Bar", "&6&lGet on the gold blocks without taking fall damage."),
        GAME_FIREBALL_TNT_JUMPING_FINISHED_MESSAGE("Game.Fireball-TNT-Jumping.Finished", "&aSuccessful! [blocks] blocks!"),
        GAME_FIREBALL_TNT_JUMPING_ACTION_BAR("Game.Fireball-TNT-Jumping.Action-Bar", "&6&lUse the items to jump as far as you can!"),
        TABLIST_HEADER("Tablist.Header", List.of("&bYou are playing on &e&lMC.YOURSERVER.NET")),
        TABLIST_FOOTER("Tablist.Footer", List.of("&aRanks, Boosters & MORE! &c&lSTORE.YOURSERVER.NET")),
        SPECTATE_SPECTATING("Spectate.Spectating", Arrays.asList("&7Spectating &f[player_name]&7:", "&7Current mode: &f[practice_mode]", "&7Settings: &f[current_settings]")),
        SPECTATE_SETTINGS_CHANGED("Spectate.Settings-Changed", Arrays.asList("&f[player_name] &7changed the practice settings:", "&7Current mode: &f[practice_mode]", "&7Settings: &f[current_settings]")),
        SPECTATE_PRACTICE_MODE_CHANGED("Spectate.Practice-Mode-Changed", Arrays.asList("&f[player_name] &7changed the practice mode to &f[practice_mode]", "&7Settings: &f[current_settings]")),
        SPECTATE_TITLE("Spectate.Title", "&aSpectating"),
        SPECTATE_SUBTITLE("Spectate.Subtitle", "&f[player_name]"),
        SPECTATE_PLAYER_QUIT("Spectate.Player-Quit", "&7The player you were spectating quit the practice arena."),
        SPECTATE_QUIT("Spectate.Quit", "&7You've quit spectating &f[player_name]&7."),
        SPECTATE_GAMEMODE_NOT_CHANGEABLE("Spectate.GameMode.Not-Changeable", "&cYou cannot change your game mode while you spectate a practice player."),
        SPECTATE_NOT_SPECTATING("Spectate.Not-Spectating", "&cYou're not spectating a player."),
        SPECTATE_SETTINGS_BRIDGING_BLOCKS("Spectate.Settings.Bridging.Blocks", "&f[block_number] Blocks"),
        SPECTATE_SETTINGS_BRIDGING_ANGLE("Spectate.Settings.Bridging.Angle", "&f[angle_type] Angle"),
        SPECTATE_SETTINGS_BRIDGING_ELEVATION("Spectate.Settings.Bridging.Elevation", "&f[elevation_type] Elevation"),
        SPECTATE_SETTINGS_MLG_SIZE("Spectate.Settings.MLG.Size", "&f[size_type] Size"),
        SPECTATE_SETTINGS_MLG_HEIGHT("Spectate.Settings.MLG.Height", "&f[height_type] Height"),
        SPECTATE_SETTINGS_MLG_POSITION("Spectate.Settings.MLG.Position", "&f[position_type] Position"),
        SPECTATE_SETTINGS_MLG_TALLNESS("Spectate.Settings.MLG.Tallness", "&f[tallness_type] Block Tallness"),
        SPECTATE_SETTINGS_FIREBALL_TNT_JUMPING_ITEM_TYPE_AMOUNT("Spectate.Settings.Fireball-TNT-Jumping.Item-Type-Amount", "&f[item_amount] [item_type]"),
        SPECTATE_SETTINGS_FIREBALL_TNT_JUMPING_WOOL("Spectate.Settings.Fireball-TNT-Jumping.Wool", "&fWool [wool_type]"),
        SPECTATE_SETTINGS_DISPLAY_COMMA("Spectate.Settings.Display.Comma", "&7, "),
        SPECTATE_SETTINGS_DISPLAY_BRIDGING_BLOCKS_INFINITE("Spectate.Settings.Display.Bridging.Blocks.Infinite", "Infinite"),
        SPECTATE_SETTINGS_DISPLAY_BRIDGING_ANGLE_STRAIGHT("Spectate.Settings.Display.Bridging.Angle.Straight", "Straight"),
        SPECTATE_SETTINGS_DISPLAY_BRIDGING_ANGLE_DIAGONAL("Spectate.Settings.Display.Bridging.Angle.Diagonal", "Diagonal"),
        SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_NONE("Spectate.Settings.Display.Bridging.Elevation.None", "No"),
        SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_SLIGHT("Spectate.Settings.Display.Bridging.Elevation.Slight", "Slight"),
        SPECTATE_SETTINGS_DISPLAY_BRIDGING_ELEVATION_STAIRCASE("Spectate.Settings.Display.Bridging.Elevation.Staircase", "Staircase"),
        SPECTATE_SETTINGS_DISPLAY_MLG_SIZE_LARGE("Spectate.Settings.Display.MLG.Size.Large", "Large"),
        SPECTATE_SETTINGS_DISPLAY_MLG_SIZE_MEDIUM("Spectate.Settings.Display.MLG.Size.Medium", "Medium"),
        SPECTATE_SETTINGS_DISPLAY_MLG_SIZE_SMALL("Spectate.Settings.Display.MLG.Size.Small", "Small"),
        SPECTATE_SETTINGS_DISPLAY_MLG_HEIGHT_HIGH("Spectate.Settings.Display.MLG.Height.High", "High"),
        SPECTATE_SETTINGS_DISPLAY_MLG_HEIGHT_MEDIUM("Spectate.Settings.Display.MLG.Height.Medium", "Medium"),
        SPECTATE_SETTINGS_DISPLAY_MLG_HEIGHT_LOW("Spectate.Settings.Display.MLG.Height.Low", "Low"),
        SPECTATE_SETTINGS_DISPLAY_MLG_POSITION_CENTER("Spectate.Settings.Display.MLG.Position.Center", "Center"),
        SPECTATE_SETTINGS_DISPLAY_MLG_POSITION_RANDOM("Spectate.Settings.Display.MLG.Position.Random", "Random"),
        SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_SINGULAR("Spectate.Settings.Display.Fireball-TNT-Jumping.Item.Fireball.Singular", "Fireball"),
        SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_PLURAL("Spectate.Settings.Display.Fireball-TNT-Jumping.Item.Fireball.Plural", "Fireballs"),
        SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_ITEM_TNT_SINGULAR("Spectate.Settings.Display.Fireball-TNT-Jumping.Item.TNT.Singular", "TNT"),
        SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_ITEM_TNT_PLURAL("Spectate.Settings.Display.Fireball-TNT-Jumping.Item.TNT.Plural", "TNTs"),
        SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_WOOL_ENABLED("Spectate.Settings.Display.Fireball-TNT-Jumping.Wool.Enabled", "Enabled"),
        SPECTATE_SETTINGS_DISPLAY_FIREBALL_TNT_JUMPING_WOOL_DISABLED("Spectate.Settings.Display.Fireball-TNT-Jumping.Wool.Disabled", "Disabled"),
        SPECTATE_SPECTATING_AREA_NOT_LEAVEABLE("Spectate.Spectating-Area-Not-Leaveable", "&cYou cannot leave the spectating area."),
        PRACTICE_NAME_BRIDGING("Practice.Name.Bridging", "Bridging"),
        PRACTICE_NAME_MLG("Practice.Name.MLG", "MLG"),
        PRACTICE_NAME_FIREBALL_TNT_JUMPING("Practice.Name.Fireball-TNT-Jumping", "Fireball/TNT Jumping"),
        NPC_HOLOGRAM_DEFAULT("NPC.Hologram.Default", Arrays.asList("&e&lCLICK TO PLAY", "&bPractice &7[0.1]", "&e&l[practice_player] Players")),
        NPC_HOLOGRAM_BRIDGING("NPC.Hologram.Bridging", Arrays.asList("&aBRIDGING", "&e&lCLICK TO PLAY", "&bPractice &7[0.1]", "&e&l[bridging_player] Players")),
        NPC_HOLOGRAM_MLG("NPC.Hologram.MLG", Arrays.asList("&aMLG", "&e&lCLICK TO PLAY", "&bPractice &7[0.1]", "&e&l[mlg_player] Players")),
        NPC_HOLOGRAM_FIREBALL_TNT_JUMPING("NPC.Hologram.Fireball-TNT-Jumping", Arrays.asList("&aFIREBALL/TNT JUMPING", "&e&lCLICK TO PLAY", "&bPractice &7[0.1]", "&e&l[fireball_tnt_jumping_player] Players")),
        NPC_TYPE_DEFAULT("NPC.Type.Default", "DEFAULT"),
        NPC_TYPE_BRIDGING("NPC.Type.Bridging", "BRIDGING"),
        NPC_TYPE_MLG("NPC.Type.MLG", "MLG"),
        NPC_TYPE_FIREBALL_TNT_JUMPING("NPC.Type.Fireball-TNT-Jumping", "FIREBALL/TNT JUMPING");

        final String path;
        final Object defaultValue;
        boolean noLanguageSupport = false;

        MessagesEnum(String path, Object defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        MessagesEnum(String path, Object defaultValue, boolean noLanguageSupport) {
            this.path = path;
            this.defaultValue = defaultValue;
            this.noLanguageSupport = noLanguageSupport;
        }

        public boolean hasNoLanguageSupport() {
            return this.noLanguageSupport;
        }

        public String getString(UUID uuid) {
            return Language.getInstance().getLocaleFiles().get(Language.getInstance().getPlayerLocale().getOrDefault(uuid, "EN")).getString(this.path);
        }

        public String getString() {
            return Language.getInstance().getLocaleFiles().get("EN").getString(this.path);
        }

        public List<String> getStringList(UUID uuid) {
            return Language.getInstance().getLocaleFiles().get(Language.getInstance().getPlayerLocale().getOrDefault(uuid, "EN")).getStringList(this.path);
        }

        public List<String> getStringList() {
            return Language.getInstance().getLocaleFiles().get("EN").getStringList(this.path);
        }

        public String getPath() {
            return this.path;
        }

        public Object getDefaultValue() {
            return this.defaultValue;
        }

    }
}
