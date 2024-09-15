package com.github.zandy.bedwarspractice.files.language.iso;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.Arrays;
import java.util.Collections;

public class Polish extends BambooFile {
   public Polish() {
      super("Language_PL", "Languages");
      this.add(Language.MessagesEnum.PLUGIN_LANGUAGE_DISPLAY, "Polish");
      this.add(Language.MessagesEnum.PLUGIN_NO_CONSOLE, "&cNie możesz użyć tego polecenia w konsoli!");
      this.add(Language.MessagesEnum.PLUGIN_NO_PERMISSION, "&cNie masz permisji do użycia tego polecenia!");
      this.add(Language.MessagesEnum.PLUGIN_LOBBY_NOT_SET, Arrays.asList("&4&lBŁĄD! &cLobby serwerowe nie jest ustawione!", "&7&lUżyj: &f/bwpa setLobby"));
      this.add(Language.MessagesEnum.COMMAND_HEADER_DEFAULT, "        &f[&a&lBW Practice &8- &7Pomoc&f]");
      this.add(Language.MessagesEnum.COMMAND_HEADER_ADMIN, "      &f[&a&lBW Practice Admin &8- &7Pomoc&f]");
      this.add(Language.MessagesEnum.COMMAND_TAG, "&f&l[&a&lBedWars Practice&f&l]");
      this.add(Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST, "&7Kliknij, aby zasugerować to polecenie.");
      this.add(Language.MessagesEnum.COMMAND_CLICK_TO_RUN, "&7Kliknij, aby uruchomić to polecenie.");
      this.add(Language.MessagesEnum.COMMAND_WRONG_USAGE, "&7Polecenie zostało użyte nieprawidłowo.");
      this.add(Language.MessagesEnum.COMMAND_NOT_AVAILABLE_IN_PRACTICE, "&cTo polecenie nie jest dostępne w trybie treningu.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SET_LOBBY_SUCCEEDED, "&7Lobby serwerowe zostało ustawione w Twojej obecnej lokalizacji.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_HEADER, "      &f[&a&lBW Practice &8- &7Kreator schematów&f]");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_CREATE, "&a⦁ &f/bwpa schem create &8- &7&oTwórz schematy practice");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LEAVE, "&a⦁ &f/bwpa schem leave &8- &7&oOpuść kreator schematów");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LOAD, "&a⦁ &f/bwpa schem load &8- &7&oZaładuj BedWars Practice schemat");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_LIST, "&a⦁ &f/bwpa schem list &8- &7&oLista z dostępnymi schematami");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_POS, "&a⦁ &f/bwpa schem pos &8- &7&oUstaw pozycje schematu");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_DISPLAY_SAVE, "&a⦁ &f/bwpa schem save &8- &7&oZapisz schemat");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_CREATE_INFO_TELEPORTED, "&7Zostałeś teleportowany do świata kreatora schematów!");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_CREATE_INFO_TUTORIAL, Arrays.asList("&7Aby stworzyć schematy dla BedWars Practice, przeczytaj poradnik ze strony Spigot lub obejrzyj samouczek wideo na YouTube.", "&7SpigotMC Link: &f[spigotLink]", "&7YouTube Link: &f[youtubeLink]"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_USAGE, "&a⦁ &f/bwpa schem load <&aNazwa schematu&f>");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_LOADING, "&eŁadowanie '&a[schemName]&e' schematu...");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_LOADED_AND_PASTED, "&fSchemat został załadowany i wklejony &apozytywnie&f!");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_FILE_NOT_FOUND, "&c&oSchemat '[schemName]' , który próbujesz załadować nie istnieje.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_REQUIRED_SCHEMATICS, "&7Wymagane jest utworzenie następujących schematów:");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_ENUMERATION, "&7➔ &f[schemName]");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_INFO, "&c&lUWAGA! &7Wszystkie schematy są wymagane do uruchomienia tego pluginu.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LIST_ALL_CREATED, "&7Utworzyłeś wymagane schematy.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_USAGE, "&a⦁ &f/bwpa schem pos <&a1&f, &a2&f, &awand&f>");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_SET, "Schematy &epos [posNumber] &fzostały &austawione&f.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_WAND_RECEIVED, "Różdżka otrzymana na pierwszym slocie.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_USAGE, Arrays.asList("&a⦁ &f/bwpa schem save <&aSchematic Name&f>", "&c&lUWAGA!", "&7Nazwa schematu musi reprezentować wymagane schematy.", "&7Aby uzyskać szczegółową listę z wymaganymi schematami, użyj:", "&a⦁ &f/bwpa schem list"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_WRONG_SCHEMATIC_NAME, Arrays.asList("&c&lBłędna nazwa schematu!", "&7Nazwa schematu musi reprezentować wymagane schematy.", "&7Aby uzyskać szczegółową listę z wymaganymi schematami, użyj:", "&a⦁ &f/bwpa schem list"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_STARTED, "&eZapis może chwilę potrwać. Proszę czekać!");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_SAVE_SUCCESSFULLY, "&aZapisałeś &f&o'[schemName]' &aschemat.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LEAVE_SETUP_NOT_FINISHED, Arrays.asList("&cTrwa sesja konfiguracji.", "&7Anuluj to za pomocą: &f/bwpa setup quit"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LEAVE_SUCCESSFULLY, Arrays.asList("&7Wyszedłeś z kreatora schematów.", "Zostałeś teleportowany do lobby!"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_ALREADY_IN_SCHEMATIC_WORLD, "&cJesteś już w kreatorze schematów świata.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_NOT_IN_SCHEMATIC_WORLD, Arrays.asList("&cNie jesteś w kreatorze schematów świata.", "&7Aby wejść w świat schematów, użyj:", "&a⦁ &f/bwpa schem create"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_WRONG_SUBCOMMAND, "&cPodkomenda &f'[subCommand]' &cnie istnieje.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_HEADER, "      &f[&a&lBW Practice &8- &7Ustawienia Treningu&f]");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_SET, "&a⦁ &f/bwpa setup set &8- &7&oRozpocznij konfigurację treningu");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_LIST, "&a⦁ &f/bwpa setup list &8- &7&oLista z wymaganymi treningami");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_POS, "&a⦁ &f/bwpa setup pos &8- &7&oUstaw pozycje końcową treningu");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_SAVE, "&a⦁ &f/bwpa setup save &8- &7&oZapisz stan areny treningowej");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_DISPLAY_QUIT, "&a⦁ &f/bwpa setup quit &8- &7&oPrzestań ustawiać arenę");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_INVALID_SETUP_SESSION, Arrays.asList("&cNie masz uruchomionej sesji konfiguracji!", "&7Aby utworzyć sesję, użyj:", "&a⦁ &f/bwpa setup set <&aPractice Name&f>"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_ALL_CREATED, "&7Utworzyłeś wymagane konfiguracje.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_REQUIRED_SETUPS, "&7Wymagane jest ustawienie następujących treningów:");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_ENUMERATION, "&7➔ &f[practiceName]");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_INFO, "&c&lNOTE! &7Wszystkie treningi są wymagane, aby uruchomić ten plugin.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_USAGE, "&a⦁ &f/bwpa setup set <&aPractice Name&f>");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_ALREADY_IN_SETUP, Arrays.asList("&cJuż konfigurujesz arenę treningową.", "Nazwa areny: &7[Name]", "&7Jeśli chcesz zakończyć konfigurację, użyj: &f/bwpa setup quit"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_WRONG_PRACTICE_NAME, Arrays.asList("&cBłędna nazwa treningu.", "&7Nazwy treningów można znaleźć za pomocą:", "&a⦁ &f/bwpa setup list"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_SCHEMATIC_NOT_FOUND, Arrays.asList("&cSchemat '&f[schemName]&c' nie został znaleziony.", "&7Wróć i utwórz schemat za pomocą:", "&a⦁ &f/bwpa schem save"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_LOADING_SCHEMATIC, "&eŁadowanie '&a[schemName]&e' schematu...");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_SCHEMATIC_LOADED, "Schemat załadowany &apozytywnie&f!");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_INFO_TUTORIAL, Arrays.asList("&7Aby skonfigurować BedWars Practice, przeczytaj samouczek ze strony Spigot lub obejrzyj poradnik wideo na YouTube.", "&7SpigotMC Link: &f[spigotLink]", "&7YouTube Link: &f[youtubeLink]"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_USAGE, "&a⦁ &f/bwpa setup pos <&a1&f, &a2&f, &awand&f>");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_SET_SUCCESSFULLY, "&7Ustawiłeś &f[practiceName] &7pozycja &f[posNumber]");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_WAND_RECEIVED, "Różdżka otrzymana na pierwszym slocie.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SAVE_POSITION_NOT_SET, Arrays.asList("&cPozycja treningu &f[posNumber] &cnie jest ustawiona!", "&7Ustaw ją za pomocą: &f/bwpa setup pos <&aPosition Number&f>"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SAVE_SUCCESSFULLY, "&fUdało Ci się zapisać '&e[practiceName]&f' arenę treningową.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_QUIT_NOT_IN_SETUP, "&cNie można zakończyć konfiguracji, ponieważ konfiguracja nie jest uruchomiona.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_QUIT_SUCCESSFULLY, Arrays.asList("&7Opuściłeś konfigurację pomyślnie!", "Zostałeś teleportowany na spawn kreatora schematów!"));
      this.add(Language.MessagesEnum.COMMAND_ADMIN_SETUP_WRONG_SUBCOMMAND, "&cPodkomenda &f'[subCommand]' &cnie istnieje.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_BUILD_ENABLED, "&7Teraz możesz budować w lobby.");
      this.add(Language.MessagesEnum.COMMAND_ADMIN_BUILD_DISABLED, "&7Teraz nie możesz budować w lobby.");
      this.add(Language.MessagesEnum.COMMAND_PLAYER_MENU_SCHEMATICS_NOT_SET, Arrays.asList("&cBrakuje schematów, które są obowiązkowe!", "&7Aby sprawdzić jakich schematów brakuje, użyj:", "&a⦁ &f/bwpa schem list"));
      this.add(Language.MessagesEnum.COMMAND_PLAYER_MENU_CONFIGURATIONS_NOT_SET, Arrays.asList("&cBrakuje konfiguracji, które są obowiązkowe!", "&7Aby sprawdzić jakich konfiguracji brakuje, użyj:", "&a⦁ &f/bwpa setup list"));
      this.add(Language.MessagesEnum.COMMAND_PLAYER_QUIT, "&cNie jesteś na arenie treningowej!");
      this.add(Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_USAGE, Arrays.asList("&7Polecenie użyte nieprawidłowo! Użyj:", "&a⦁ &f/bwpl <&aLanguage Abbreviation&f>"));
      this.add(Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_NOT_FOUND, "&7Nie można znaleźć tego języka! Dostępne języki:");
      this.add(Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_LIST_FORMAT, "&a⦁ &7[&e[languageAbbreviation]&7] &8| &f[languageName]");
      this.add(Language.MessagesEnum.COMMAND_PLAYER_LANGUAGE_CHANGED, "&7Zmieniono język na &f[languageName] &7[&e[languageAbbreviation]&7]!");
      this.add(Language.MessagesEnum.MODE_SELECTOR_ITEM_NAME, "&a&lWybór trybu &7(Right-Click)");
      this.add(Language.MessagesEnum.MODE_SELECTOR_ITEM_LORE, Arrays.asList("&7Kliknij prawym przyciskiem myszy, aby otworzyć tryb", "&7Wybór."));
      this.add(Language.MessagesEnum.MODE_SELECTOR_TITLE, "Bed Wars Practice");
      this.add(Language.MessagesEnum.MODE_SELECTOR_BRIDGING_NAME, "&aBudowanie");
      this.add(Language.MessagesEnum.MODE_SELECTOR_BRIDGING_LORE, Arrays.asList("&7Ćwicz mosty w poprzek", "&7nad pustką z wełny.", " ", "&eKliknij, aby zagrać!"));
      this.add(Language.MessagesEnum.MODE_SELECTOR_MLG_NAME, "&aMLG");
      this.add(Language.MessagesEnum.MODE_SELECTOR_MLG_LORE, Arrays.asList("&7Ćwicz zapobieganie obrażeń", "&7z upadku za pomocą wiadra wody", "&7i drabinek", " ", "&eKliknij, aby zagrać!"));
      this.add(Language.MessagesEnum.MODE_SELECTOR_FIREBALL_TNT_JUMPING_NAME, "&aFirebballe/TNT skoki");
      this.add(Language.MessagesEnum.MODE_SELECTOR_FIREBALL_TNT_JUMPING_LORE, Arrays.asList("&7Ćwicz przeskakiwanie", "&7pustki, używając kul ognia i", "&7TNT.", " ", "&eKliknij, aby zagrać!"));
      this.add(Language.MessagesEnum.MODE_SELECTOR_GO_BACK_NAME, "&aWróć");
      this.add(Language.MessagesEnum.MODE_SELECTOR_GO_BACK_LORE, Collections.singletonList("&7Aby zagrać w Bed Wars"));
      this.add(Language.MessagesEnum.MODE_SELECTOR_CLOSE_NAME, "&cZamknij");
      this.add(Language.MessagesEnum.MODE_SELECTOR_RETURN_TO_LOBBY_NAME, "&cWróć do lobby");
      this.add(Language.MessagesEnum.MODE_SELECTOR_ALREADY_SELECTED, "&cMasz już wybrany ten tryb ćwiczeń!");
      this.add(Language.MessagesEnum.MODE_SELECTOR_COOLDOWN, "&cMożesz wybrać tryb za [seconds] sekundy!");
      this.add(Language.MessagesEnum.GAME_SETTINGS_ITEM_NAME, "&a&lUstawienia &7(prawy przycisk myszy)");
      this.add(Language.MessagesEnum.GAME_SETTINGS_ITEM_LORE, Arrays.asList("&7Kliknij prawym przyciskiem myszy, aby otworzyć", "&7aktualne ustawienia trybu.", "&7Zmiana ustawień trybu", "&7pozwala na szeroką gamę", "&7ćwiczeń praktycznych i", "&7różne poziomy", "&7trudności."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_WRONG_SLOTS_NUMBER, Arrays.asList("&cGUI ustawień gry ma nieprawidłową liczbę slotów.", "&7Liczba akceptowanych slotów: &8[&f9&7, &f18&7, &f27&7, &f36&7, &f45&7, &f54&8]"));
      this.add(Language.MessagesEnum.GAME_SETTINGS_WRONG_SLOTS_ITEM, Arrays.asList("&cElement GUI ustawień gry ma nieprawidłową liczbę slotów", "&7Maksymalna liczba slotów: &8[&f[inventorySlots]&8]"));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_TITLE, "Budowanie ustawienia");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_NAME, "&a30 Bloków");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_30_LORE, Arrays.asList("&7Ustaw końcową odległość wyspy", "&7na 30 bloków", "&7dalej."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_NAME, "&a50 Bloków");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_50_LORE, Arrays.asList("&7Ustaw końcową odległość wyspy", "&7na 50 bloków", "&7dalej."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_NAME, "&a100 Bloków");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_100_LORE, Arrays.asList("&7Ustaw końcową odległość wyspy", "&7na 100 bloków", "&7dalej."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_NAME, "&aNieskończoność!");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_BLOCKS_INFINITE_LORE, Arrays.asList("&7Nie masz wyspy kończącej", "&7zobacz, jak daleko możesz się zbudować."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_NAME, "&aBrak");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_NONE_LORE, Arrays.asList("&7Ustaw poziom wysokości", "&7zakończenia wyspy na", "&7równy."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_NAME, "&aNiewielki");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_SLIGHT_LORE, Arrays.asList("&7Ustaw poziom wysokości", "&7zakończenia wyspy na", "&7niewielki."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_NAME, "&aSchodki");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_LEVEL_STAIRCASE_LORE, Arrays.asList("&7Ustaw poziom wysokości", "&7zakończenia wyspy na", "&7schodki."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_NAME, "&aProsto");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_STRAIGHT_LORE, Arrays.asList("&7Ustaw kąt", "&7zakończenia wyspy na", "&7wsprost."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_NAME, "&aPoprzek");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_BRIDGING_ANGLE_DIAGONAL_LORE, Arrays.asList("&7Ustaw kąt", "&7zakończenia wyspy w", "&7poprzek."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TITLE, "MLG ustawienia");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_LARGE_NAME, "&aRozmiar platformy: Duża");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_LARGE_LORE, Arrays.asList("&7Platforma do lądowania będzie miała 5x5", "&7bloków."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_NAME, "&aRozmiar platformy: Średnia");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_MEDIUM_LORE, Arrays.asList("&7Platforma do lądowania będzie miała 3x3", "&7bloki."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_SMALL_NAME, "&aRozmiar platformy: Mała");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SIZE_SMALL_LORE, Arrays.asList("&7Platforma do lądowania będzie", "&7pojedyńczym blokiem."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_NAME, "&aWysokość platformy: Wysoka");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_HIGH_LORE, Arrays.asList("&7Platforma do lądowania będzie", "&7wysoko, więc upadniesz bardzo", "&7wolno."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_NAME, "&aWysokość platformy: Średnia");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_MEDIUM_LORE, Arrays.asList("&7Platforma do lądowania będzie", "&7średniej wysokości, więc upadniesz", "&7powoli."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_NAME, "&aWysokość platformy: Niska");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_HEIGHT_LOW_LORE, Arrays.asList("&7Platforma do lądowania będzie", "&7nisko, więc upadniesz bardzo", "&7szybko."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_NAME, "&aPozycja platformy: zawsze wyśrodkowana");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_CENTERED_LORE, Arrays.asList("&7Platforma do lądowania będzie zawsze", "&7umieszczona tuż przed twoją", "&7wyspą."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_NAME, "&aPozycja platformy: losowa");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_POSITION_RANDOM_LORE, Arrays.asList("&7Platforma do lądowania będzie", "&7umieszczona w losowym miejscu."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_NAME, "&aSzerokość platformy: 1 blok");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_1_BLOCKS_LORE, Arrays.asList("&7Ustaw platformę na 1 blok", "&7szerokości.", " ", "&7Przydatne przy próbie zaczepienia się o", "&7bok platformy."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_NAME, "&aSzerokość platformy: 2 bloki");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_2_BLOCKS_LORE, Arrays.asList("&7Ustaw platformę na 2 bloki", "&7szerokości.", " ", "&7Przydatne przy próbie zaczepienia się o", "&7bok platformy."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_NAME, "&aSzerokość platformy: 3 bloki");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_TALLNESS_3_BLOCKS_LORE, Arrays.asList("&7Ustaw platformę na 3 bloki", "&7szerokości.", " ", "&7Przydatne przy próbie zaczepienia się o", "&7bok platformy."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_NAME, "&aItem: Wiadro wody");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_WATER_BUCKET_LORE, Arrays.asList("&7Wylej wodę przed uderzeniem w", "&7podłożę."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_LADDER_NAME, "&aItem: Drabina");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_ITEM_LADDER_LORE, Arrays.asList("&7Umieść drabinę i wyląduj na boku", "&7jej strony."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_NONE_NAME, "&aBrak przełożenia przedmiotów.");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_NAME, "&aPrzełożenie przedmiotu po skoku.");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_MLG_SHUFFLE_SHUFFLED_LORE, Arrays.asList("&7Spowoduje to przełożenie ", "&7przedmiotu po zeskoczeniu w dół. To", "&7wymaga szybkiej reakcji", "&7i zmiany miejsca ekwipunku", "&7do wykorzystania przedmoitu."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_TITLE, "Ustawienia");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_NAME, "&a1 sztuka");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_1_LORE, Arrays.asList("&7Zacznij z 1 wybraną", "&7sztuką."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_NAME, "&a2 sztuki");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_2_LORE, Arrays.asList("&7Zacznij z 2 wybranymi", "&7sztukami."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_NAME, "&a5 sztuk");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEMS_5_LORE, Arrays.asList("&7Zacznij z 5 wybranymi", "&7sztukami."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_NAME, "&aKula Ognia");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_FIREBALL_LORE, Collections.singletonList("&7Zaczniesz z kulą ognia."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_NAME, "&aTNT");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_ITEM_TNT_LORE, Collections.singletonList("&7Zaczniesz z TNT."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_NAME, "&cWyłącz wełnę");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_DISABLE_LORE, Collections.singletonList("&7Zaczniesz bez wełny."));
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_NAME, "&aWłącz wełnę");
      this.add(Language.MessagesEnum.GAME_SETTINGS_GUI_FIREBALL_TNT_JUMPING_WOOL_ENABLE_LORE, Arrays.asList("&7Zaczniesz z 32 blokami", "&7wełny.", " ", "&7Wełna nie może być umieszczona dalej niż", "&72 bloki od krawędzi platformy."));
      this.add(Language.MessagesEnum.GAME_FAILED, "&cPorażka!");
      this.add(Language.MessagesEnum.GAME_SUCCESSFUL, "&aPomyślnie!");
      this.add(Language.MessagesEnum.GAME_BLOCK_NOT_PLACEABLE, "&cNie możesz tutaj umieścić bloku!");
      this.add(Language.MessagesEnum.GAME_UPDATED_YOUR_SETTINGS, "&aZaktualizowałeś swoje ustawienia.");
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_TITLE, "&e&lBED WARS PRACTICE");
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_BRIDGING_DEFAULT_LINES, Arrays.asList("&7[month]/[day]/[year]", " ", "Tryb: &a[game_mode]", " ", "Szybkość: &a[average_speed] m/s", " ", "Najlepszy czas: &a[personal_best]", " ", "&emc.yourserver.org"));
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_BRIDGING_INFINITE_LINES, Arrays.asList("&7[month]/[day]/[year]", " ", "Tryb: &a[game_mode]", " ", "Szybkość: &a[average_speed] m/s", " ", "Postawione bloki: &a[blocks_placed]", " ", "&emc.yourserver.org"));
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_MLG_LINES, Arrays.asList("&7[month]/[day]/[year]", " ", "Tryb: &a[game_mode]", " ", "&emc.yourserver.org"));
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_FIREBALL_TNT_JUMPING_LINES, Arrays.asList("&7[month]/[day]/[year]", " ", "Tryb: &a[game_mode]", "Longest Jump: &a[longest_jump] Blocks", " ", "&emc.yourserver.org"));
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_PERSONAL_BEST_NONE, "&cBrak!");
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_BRIDGING, "Budowanie");
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_MLG, "MLG");
      this.add(Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_FIREBALL_TNT_JUMPING, "Fireballe/TNT skoki");
      this.add(Language.MessagesEnum.GAME_BRIDGING_ACTION_BAR_TIMER_COLOR, "&6&l");
      this.add(Language.MessagesEnum.GAME_BRIDGING_ACTION_BAR_START_TIMER, "&6&lPostaw blok, aby rozpocząć!");
      this.add(Language.MessagesEnum.GAME_BRIDGING_FINISHED_MESSAGE, "&aZaliczono! Zajęło [seconds] sekund.");
      this.add(Language.MessagesEnum.GAME_BRIDGING_FINISHED_TITLE, "&a[seconds] sekundy");
      this.add(Language.MessagesEnum.GAME_MLG_ITEM_NAME_WATER, "&aUmieść wodę przed uderzeniem o ziemię.");
      this.add(Language.MessagesEnum.GAME_MLG_ITEM_NAME_LADDER, "&aUmieść drabinę i wyląduj na boku.");
      this.add(Language.MessagesEnum.GAME_MLG_ACTION_BAR, "&6&lWejdź na złote bloki bez obrażeń od upadku.");
      this.add(Language.MessagesEnum.GAME_FIREBALL_TNT_JUMPING_FINISHED_MESSAGE, "&aPomyślnie! [blocks] bloków!");
      this.add(Language.MessagesEnum.GAME_FIREBALL_TNT_JUMPING_ACTION_BAR, "&6&lUżyj przedmiotów, aby skoczyć jak najdalej!");
      this.copyDefaults();
      this.save();
   }

   private void add(Language.MessagesEnum var1, Object var2) {
      this.addDefault(var1.getPath(), var2);
   }
}