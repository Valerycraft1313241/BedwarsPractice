package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bamboolib.versionsupport.material.Materials;

import java.util.Arrays;

public class Settings extends BambooFile {
   private static Settings instance = null;

   private Settings() {
      super("Settings");
      Arrays.stream(
              Settings.SettingsEnum.values())
              .forEach((var1) -> this.addDefault(var1.getPath(), var1.getDefaultValue()));
      this.copyDefaults();
      this.save();
   }

   public static void init() {
      instance = new Settings();
   }

   public static Settings getInstance() {
      return instance;
   }

   public enum SettingsEnum {
      UPDATE_CHECKER("Update-Checker", true),
      PRACTICE_PROXY_ENABLED("Practice-Proxy.Enabled", false),
      PRACTICE_PROXY_LOBBY_SERVER_ID("Practice-Proxy.Lobby-Server-ID", "lobby"),
      ENABLE_VERBOSE("Enable-Verbose", false),
      LOBBY_TELEPORT_ON_JOIN("Lobby.Teleport-On-Join", true),
      LOBBY_SERVER_JOIN_LEAVE_MESSAGE("Lobby.Server-Join-Leave-Message", false),
      LOBBY_ALLOW_DAMAGE("Lobby.Allow-Damage", false),
      LOBBY_ALLOW_BLOCK_BREAK("Lobby.Allow-Block-Break", false),
      LOBBY_ALLOW_HUNGER("Lobby.Allow-Hunger", false),
      LOBBY_ALLOW_BLOCK_INTERACT("Lobby.Allow-Block-Interact", false),
      LOBBY_INTERACT_RESTRICTED_BLOCKS("Lobby.Interact-Restricted-Blocks", Arrays.asList(Materials.ENDER_CHEST.name(), Materials.CHEST.name(), Materials.NOTE_BLOCK.name(), Materials.ANVIL.name(), Materials.BEACON.name())),
      INVENTORY_CACHE("Inventory-Cache", true),
      CHAT_ENABLED("Chat.Enabled", true),
      CHAT_FORMAT("Chat.Format", "&7[playerName]: "),
      NPC_REFRESH_TICK("NPC.Refresh.Tick", 5),
      DEFAULT_LANGUAGE("Default-Language", "EN"),
      SCOREBOARD_REFRESH_TICK("Scoreboard.Refresh-Tick", 20);

      final String path;
      final Object defaultValue;

      SettingsEnum(String path, Object defaultValue) {
         this.path = path;
         this.defaultValue = defaultValue;
      }

      public boolean getBoolean() {
         return Settings.getInstance().getBoolean(this.path);
      }

      public int getInt() {
         return Settings.getInstance().getInt(this.path);
      }

      public String getString() {
         return Settings.getInstance().getString(this.path);
      }

      public String getPath() {
         return this.path;
      }

      public Object getDefaultValue() {
         return this.defaultValue;
      }

   }
}
