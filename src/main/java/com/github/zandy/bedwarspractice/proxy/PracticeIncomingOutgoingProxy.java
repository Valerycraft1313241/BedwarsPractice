package com.github.zandy.bedwarspractice.proxy;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.features.guis.ModeSelectorGUI;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PracticeIncomingOutgoingProxy implements Listener {
   private static PracticeIncomingOutgoingProxy instance = null;
   private final PluginMessageListener messageListener = this::onPluginMessageReceived;
   private final Map<String, Queue<CompletableFuture<?>>> callbackMap = new HashMap<>();
   private Map<String, PracticeIncomingOutgoingProxy.ForwardConsumer> forwardListeners;
   private final Location spawn = new Location(WorldEngine.getInstance().getPracticeWorld(), -2.999986E7D, 5000.0D, -2.999986E7D);
   private final HashMap<UUID, GameEngine.PracticeType> proxyMap = new HashMap<>();

   private PracticeIncomingOutgoingProxy() {
   }

   public void init() {
      BambooUtils.registerEvent(this);
      Messenger var1 = Bukkit.getServer().getMessenger();
      var1.registerIncomingPluginChannel(BambooLib.getPluginInstance(), "BungeeCord", this.messageListener);
      var1.registerOutgoingPluginChannel(BambooLib.getPluginInstance(), "BungeeCord");
      this.registerForwardListener((var1x, var2, var3) -> {
         String[] var4 = (new String(var3)).split(":");
         UUID var5 = UUID.fromString(var4[0]);
         GameEngine.PracticeType var6 = GameEngine.PracticeType.valueOf(var4[1]);
         BambooUtils.consolePrint("&m----------------------");
         BambooUtils.consolePrint("&eIncomming BedWarsPracticeProxy data:");
         BambooUtils.consolePrint("&aUUID: &f" + var5);
         BambooUtils.consolePrint("&aPractice Mode: &f" + var6);
         BambooUtils.consolePrint("&m----------------------");
         this.proxyMap.put(var5, var6);
      });
   }

   @EventHandler
   private void onPlayerJoin(PlayerJoinEvent var1) {
      Player var2 = var1.getPlayer();
      var2.teleport(this.getSpawn());
      Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
         if (this.proxyMap.containsKey(var2.getUniqueId())) {
            this.executeJoin(var2, this.proxyMap.get(var2.getUniqueId()));
         }

      }, 2L);
   }

   private void executeJoin(Player var1, GameEngine.PracticeType var2) {
      UUID var3 = var1.getUniqueId();
      if (!GameEngine.getInstance().getPracticeTypeMap().containsKey(var3)) {
         if (!SetupData.getInstance().isSetupDoneSchematics()) {
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
            Language.MessagesEnum.COMMAND_PLAYER_MENU_SCHEMATICS_NOT_SET.getStringList(var3).forEach((var2x) -> {
               if (var2x.contains("/bwpa")) {
                  BambooUtils.sendTextComponent(var1, var2x, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var3), Action.RUN_COMMAND);
               } else {
                  var1.sendMessage(var2x);
               }

            });
         } else if (!SetupData.getInstance().isSetupDoneConfigurations()) {
            var1.sendMessage(" ");
            var1.sendMessage(" ");
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
            Language.MessagesEnum.COMMAND_PLAYER_MENU_CONFIGURATIONS_NOT_SET.getStringList(var3).forEach((var2x) -> {
               if (var2x.contains("/bwpa")) {
                  BambooUtils.sendTextComponent(var1, var2x, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var3), Action.RUN_COMMAND);
               } else {
                  var1.sendMessage(var2x);
               }

            });
         } else {
            ModeSelectorGUI.getInstance().create(var1, var2);
         }
      }
   }

   @EventHandler
   private void onServerCommand(ServerCommandEvent var1) {
      String var2 = var1.getCommand();
      if (var2.equalsIgnoreCase("rl") || var2.equalsIgnoreCase("reload")) {
         Bukkit.getOnlinePlayers().forEach((var0) -> getInstance().connect(var0, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString()));
      }

   }

   @EventHandler
   private void onPlayerReload(PlayerCommandPreprocessEvent var1) {
      if (var1.getPlayer().hasPermission("bukkit.command.reload")) {
         String var2 = var1.getMessage().replace("/", "");
         if (var2.equalsIgnoreCase("rl") || var2.equalsIgnoreCase("reload")) {
            Bukkit.getOnlinePlayers().forEach((var0) -> getInstance().connect(var0, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString()));
         }

      }
   }

   public void connect(Player var1, String var2) {
      ByteArrayDataOutput var3 = ByteStreams.newDataOutput();
      var3.writeUTF("Connect");
      var3.writeUTF(var2);
      var1.sendPluginMessage(BambooLib.getPluginInstance(), "BungeeCord", var3.toByteArray());
   }

   private void registerForwardListener(ForwardConsumer var2) {
      if (this.forwardListeners == null) {
         this.forwardListeners = new HashMap<>();
      }

      synchronized(this.forwardListeners) {
         this.forwardListeners.put("BedWarsPractice", var2);
      }
   }

   private void onPluginMessageReceived(String var1, Player var2, byte[] var3) {
      if (var1.equalsIgnoreCase("BungeeCord")) {
         ByteArrayDataInput var4 = ByteStreams.newDataInput(var3);
         String var5 = var4.readUTF();
         synchronized(this.callbackMap) {
            Queue<CompletableFuture<?>> var7 = this.callbackMap.get(var5);
            if (var7 == null) {
               byte[] var8 = new byte[var4.readShort()];
               var4.readFully(var8);
               if (this.forwardListeners != null) {
                  synchronized(this.forwardListeners) {
                     PracticeIncomingOutgoingProxy.ForwardConsumer var10 = this.forwardListeners.get(var5);
                     if (var10 != null) {
                        var10.accept(var5, var2, var8);
                     }
                  }

               }
            }
         }
      }
   }

   public void unregister() {
      Messenger var1 = Bukkit.getServer().getMessenger();
      var1.unregisterIncomingPluginChannel(BambooLib.getPluginInstance(), "BungeeCord", this.messageListener);
      var1.unregisterOutgoingPluginChannel(BambooLib.getPluginInstance());
      this.callbackMap.clear();
   }

   public static PracticeIncomingOutgoingProxy getInstance() {
      if (instance == null) {
         instance = new PracticeIncomingOutgoingProxy();
      }

      return instance;
   }

   public Location getSpawn() {
      return this.spawn;
   }

   @FunctionalInterface
   public interface ForwardConsumer {
      void accept(String var1, Player var2, byte[] var3);
   }
}
