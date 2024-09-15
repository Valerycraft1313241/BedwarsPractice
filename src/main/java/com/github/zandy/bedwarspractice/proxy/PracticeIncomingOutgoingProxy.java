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

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PracticeIncomingOutgoingProxy implements Listener {
   private static final Logger LOGGER = Logger.getLogger(PracticeIncomingOutgoingProxy.class.getName());
   private static final PracticeIncomingOutgoingProxy INSTANCE = new PracticeIncomingOutgoingProxy();
   private final PluginMessageListener messageListener = this::onPluginMessageReceived;
   private final Map<String, Queue<CompletableFuture<?>>> callbackMap = new ConcurrentHashMap<>();
   private final Map<String, ForwardConsumer> forwardListeners = new ConcurrentHashMap<>();
   private final Location spawn = new Location(WorldEngine.getInstance().getPracticeWorld(), -2.999986E7D, 5000.0D, -2.999986E7D);
   private final Map<UUID, GameEngine.PracticeType> proxyMap = new ConcurrentHashMap<>();

   private PracticeIncomingOutgoingProxy() {
      // Private constructor to prevent instantiation
   }

   public static PracticeIncomingOutgoingProxy getInstance() {
      return INSTANCE;
   }

   public void init() {
      BambooUtils.registerEvent(this);
      Messenger messenger = Bukkit.getServer().getMessenger();
      messenger.registerIncomingPluginChannel(BambooLib.getPluginInstance(), "BungeeCord", this.messageListener);
      messenger.registerOutgoingPluginChannel(BambooLib.getPluginInstance(), "BungeeCord");
      this.registerForwardListener((channel, player, message) -> {
         String[] data = new String(message).split(":");
         UUID uuid = UUID.fromString(data[0]);
         GameEngine.PracticeType practiceType = GameEngine.PracticeType.valueOf(data[1]);
         LOGGER.info("----------------------");
         LOGGER.info("Incoming BedWarsPracticeProxy data:");
         LOGGER.info("UUID: " + uuid);
         LOGGER.info("Practice Mode: " + practiceType);
         LOGGER.info("----------------------");
         this.proxyMap.put(uuid, practiceType);
      });
   }

   @EventHandler
   private void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      player.teleport(this.getSpawn());
      Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
         if (this.proxyMap.containsKey(player.getUniqueId())) {
            this.executeJoin(player, this.proxyMap.get(player.getUniqueId()));
         }
      }, 2L);
   }

   private void executeJoin(Player player, GameEngine.PracticeType practiceType) {
      UUID uuid = player.getUniqueId();
      if (!GameEngine.getInstance().getPracticeTypeMap().containsKey(uuid)) {
         if (!SetupData.getInstance().isSetupDoneSchematics()) {
            sendSetupMessage(player, Language.MessagesEnum.COMMAND_PLAYER_MENU_SCHEMATICS_NOT_SET);
         } else if (!SetupData.getInstance().isSetupDoneConfigurations()) {
            sendSetupMessage(player, Language.MessagesEnum.COMMAND_PLAYER_MENU_CONFIGURATIONS_NOT_SET);
         } else {
            ModeSelectorGUI.getInstance().create(player, practiceType);
         }
      }
   }

   private void sendSetupMessage(Player player, Language.MessagesEnum messageEnum) {
      UUID uuid = player.getUniqueId();
      player.sendMessage(" ");
      player.sendMessage(" ");
      player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(uuid));
      messageEnum.getStringList(uuid).forEach(message -> {
         if (message.contains("/bwpa")) {
            BambooUtils.sendTextComponent(player, message, "/bwpa schem list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(uuid), Action.RUN_COMMAND);
         } else {
            player.sendMessage(message);
         }
      });
   }

   @EventHandler
   private void onServerCommand(ServerCommandEvent event) {
      String command = event.getCommand();
      if (command.equalsIgnoreCase("rl") || command.equalsIgnoreCase("reload")) {
         Bukkit.getOnlinePlayers().forEach(player -> getInstance().connect(player, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString()));
      }
   }

   @EventHandler
   private void onPlayerReload(PlayerCommandPreprocessEvent event) {
      if (event.getPlayer().hasPermission("bukkit.command.reload")) {
         String command = event.getMessage().replace("/", "");
         if (command.equalsIgnoreCase("rl") || command.equalsIgnoreCase("reload")) {
            Bukkit.getOnlinePlayers().forEach(player -> getInstance().connect(player, Settings.SettingsEnum.PRACTICE_PROXY_LOBBY_SERVER_ID.getString()));
         }
      }
   }

   public void connect(Player player, String server) {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF("Connect");
      out.writeUTF(server);
      player.sendPluginMessage(BambooLib.getPluginInstance(), "BungeeCord", out.toByteArray());
   }

   private void registerForwardListener(ForwardConsumer listener) {
      synchronized (this.forwardListeners) {
         this.forwardListeners.put("BedWarsPractice", listener);
      }
   }

   private void onPluginMessageReceived(String channel, Player player, byte[] message) {
      if (channel.equalsIgnoreCase("BungeeCord")) {
         ByteArrayDataInput in = ByteStreams.newDataInput(message);
         String subChannel = in.readUTF();
         synchronized (this.callbackMap) {
            Queue<CompletableFuture<?>> queue = this.callbackMap.get(subChannel);
            if (queue == null) {
               byte[] data = new byte[in.readShort()];
               in.readFully(data);
               synchronized (this.forwardListeners) {
                  ForwardConsumer listener = this.forwardListeners.get(subChannel);
                  if (listener != null) {
                     listener.accept(subChannel, player, data);
                  }
               }
            }
         }
      }
   }

   public void unregister() {
      Messenger messenger = Bukkit.getServer().getMessenger();
      messenger.unregisterIncomingPluginChannel(BambooLib.getPluginInstance(), "BungeeCord", this.messageListener);
      messenger.unregisterOutgoingPluginChannel(BambooLib.getPluginInstance());
      this.callbackMap.clear();
   }

   public Location getSpawn() {
      return this.spawn;
   }

   @FunctionalInterface
   public interface ForwardConsumer {
      void accept(String channel, Player player, byte[] message);
   }
}
