package com.github.zandy.bedwarspractice.engine.practice.bridging;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.item.ItemBuilder;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.VersionSupport;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeChangeEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeFinishEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeJoinEvent;
import com.github.zandy.bedwarspractice.api.utils.creator.BridgingCreator;
import com.github.zandy.bedwarspractice.api.utils.data.BridgingData;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.ScoreboardEngine;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.support.BWPVector;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.InventoryCache;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.concurrent.Callable;

public class BridgingMode implements Listener {
    private static BridgingMode instance = null;
    private final ItemStack bridgingBlock;
    private final int voidRestart;
    private final ItemBuilder settingsItem;
    private final ItemBuilder modeSelectorItem;

    public BridgingMode() {
        this.bridgingBlock = PracticeSettings.GameSettingsEnum.BRIDGING_MATERIAL.getMaterial().getItem().setAmount(64).build();
        this.voidRestart = PracticeSettings.GameSettingsEnum.BRIDGING_VOID_RESTART.getInt();
        this.settingsItem = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_MATERIAL.getMaterial().getItem();
        this.modeSelectorItem = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_MATERIAL.getMaterial().getItem();
        BambooUtils.registerEvent(this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> BridgingInfo.getBridgingInfoMap().keySet().forEach((playerUUID) -> {
            BridgingInfo bridgingInfo = BridgingInfo.get(playerUUID);
            if (bridgingInfo.getBlocksPlaced() != null) {
                bridgingInfo.addPracticeTime();
                VersionSupport.getInstance().sendActionBar(Bukkit.getPlayer(playerUUID), Language.MessagesEnum.GAME_BRIDGING_ACTION_BAR_TIMER_COLOR.getString(playerUUID) + String.format("%,.2f", bridgingInfo.getPracticeTime()));
            }

        }), 1L, 1L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> BridgingInfo.getBridgingInfoMap().keySet().forEach((playerUUID) -> {
            if (BridgingInfo.get(playerUUID).getBlocksPlaced() == null) {
                VersionSupport.getInstance().sendActionBar(Bukkit.getPlayer(playerUUID), Language.MessagesEnum.GAME_BRIDGING_ACTION_BAR_START_TIMER.getString(playerUUID));
            }

        }), 0L, 40L);
    }

    public static BridgingMode getInstance() {
        if (instance == null) {
            instance = new BridgingMode();
        }

        return instance;
    }

    public BridgingCreator create(Player player) {
        UUID playerUUID = player.getUniqueId();
        BridgingInfo bridgingInfo = new BridgingInfo(playerUUID);
        bridgingInfo.setCuboidRegion(GameEngine.getInstance().initMode(player, playerUUID, bridgingInfo.formatArena(), GameEngine.PracticeType.BRIDGING));
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.giveItems(player);
            Bukkit.getPluginManager().callEvent(new PracticeJoinEvent(player, GameEngine.PracticeType.BRIDGING));
        }, 5L);
        ScoreboardEngine.getInstance().sendSidebar(player, GameEngine.PracticeType.BRIDGING);
        return new BridgingCreator(bridgingInfo);
    }

    public void refresh(Player player, BridgingData bridgingData) {
        UUID playerUUID = player.getUniqueId();
        BridgingInfo bridgingInfo = BridgingInfo.get(playerUUID);
        bridgingInfo.removeBlocksPlaced(true);
        bridgingInfo.removePracticeTime();
        bridgingInfo.removePlacedBlocks();
        GameEngine gameEngine = GameEngine.getInstance();
        player.getInventory().clear();
        Location practiceLocation = gameEngine.getPracticeLocationMap().get(playerUUID);
        if (bridgingInfo.isInfinite()) {
            if (!bridgingInfo.isInfiniteBefore()) {
                practiceLocation = this.loadSetting(bridgingInfo, playerUUID, gameEngine);
                bridgingInfo.setInfiniteBefore(true);
                ScoreboardEngine.getInstance().sendSidebar(player, GameEngine.PracticeType.BRIDGING);
            }
        } else {
            if (bridgingInfo.isInfiniteBefore()) {
                bridgingInfo.setInfiniteBefore(false);
                ScoreboardEngine.getInstance().sendSidebar(player, GameEngine.PracticeType.BRIDGING);
            }

            practiceLocation = this.loadSetting(bridgingInfo, playerUUID, gameEngine);
        }

        player.teleport(practiceLocation);
        this.giveItems(player);
        if (bridgingData != null) {
            Bukkit.getPluginManager().callEvent(new PracticeChangeEvent(player, bridgingData, bridgingInfo.toData()));
        }

    }

    private Location loadSetting(BridgingInfo bridgingInfo, UUID playerUUID, GameEngine gameEngine) {
        gameEngine.clear(playerUUID, false, true);
        Location practiceLocation = gameEngine.getPracticeLocation(playerUUID);
        double[] coordinates = (new BWPVector(practiceLocation)).toArray();
        World world = WorldEngine.getInstance().getPracticeWEWorld();
        gameEngine.getPlayerEditSessionMap().put(playerUUID, BWPLegacyAdapter.getInstance().pasteSchematic(bridgingInfo.formatArena(), world, coordinates));
        if (!bridgingInfo.isInfinite()) {
            bridgingInfo.setCuboidRegion(practiceLocation);
        }

        Location teleportLocation = practiceLocation.clone().add(0.5D, 0.0D, 0.5D);
        gameEngine.getPracticeLocationMap().put(playerUUID, teleportLocation);
        return teleportLocation;
    }

    public void quit(Player player) {
        UUID playerUUID = player.getUniqueId();
        BridgingInfo bridgingInfo = BridgingInfo.get(playerUUID);
        bridgingInfo.removeBlocksPlaced(true);
        bridgingInfo.removePlacedBlocks();
        GameEngine gameEngine = GameEngine.getInstance();
        gameEngine.clear(playerUUID, true, true);
        gameEngine.getLastOffset().remove(playerUUID);
        player.getInventory().clear();
        if (!Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
            player.teleport(Lobby.getInstance().get());
        }

        BridgingInfo.remove(playerUUID);
        if (InventoryCache.isInstantiated()) {
            InventoryCache.getInstance().restore(player);
        }

    }

    public void switchClear(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (BridgingInfo.contains(playerUUID)) {
            BridgingInfo bridgingInfo = BridgingInfo.get(playerUUID);
            bridgingInfo.removeBlocksPlaced(true);
            bridgingInfo.removePlacedBlocks();
            GameEngine.getInstance().clear(playerUUID, true, true);
            BridgingInfo.remove(playerUUID);
            player.getInventory().clear();
        }
    }

    private void giveItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        for (int i = 0; i <= 3; ++i) {
            inventory.setItem(i, this.bridgingBlock);
        }

        UUID playerUUID = player.getUniqueId();
        inventory.setItem(7, this.settingsItem.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_ITEM_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.GAME_SETTINGS_ITEM_LORE.getStringList(playerUUID)).build());
        inventory.setItem(8, this.modeSelectorItem.setDisplayName(Language.MessagesEnum.MODE_SELECTOR_ITEM_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.MODE_SELECTOR_ITEM_LORE.getStringList(playerUUID)).build());
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (BridgingInfo.contains(event.getPlayer().getUniqueId())) {
            Player player = event.getPlayer();
            if (player.getItemInHand().getType().equals(this.settingsItem.getMaterial())) {
                event.setCancelled(true);
            } else {
                UUID playerUUID = player.getUniqueId();
                Block block = event.getBlock();
                if (block.getZ() < 1) {
                    event.setCancelled(true);
                    player.sendMessage(Language.MessagesEnum.GAME_BLOCK_NOT_PLACEABLE.getString(playerUUID));
                } else {
                    BridgingInfo bridgingInfo = BridgingInfo.get(playerUUID);
                    bridgingInfo.addBlockPlaced(block);
                    bridgingInfo.addPlacedBlocks();
                    if (bridgingInfo.isInfinite()) {
                        ItemStack itemInHand = event.getItemInHand();
                        itemInHand.setAmount(64);
                        event.getPlayer().setItemInHand(itemInHand);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onPlayerMoveAverageSpeed(PlayerMoveEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (BridgingInfo.contains(playerUUID)) {
            double deltaX = event.getFrom().getX() - event.getTo().getX();
            double deltaY = 0.0D;
            double deltaZ = event.getFrom().getZ() - event.getTo().getZ();
            BridgingInfo.get(playerUUID).setAverageSpeed(String.format("%,.2f", Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 20.0D));
        }
    }

    @EventHandler
    private void onPlayerMoveFinish(PlayerMoveEvent event) {
        if (BridgingInfo.contains(event.getPlayer().getUniqueId())) {
            Player player = event.getPlayer();
            double[] coordinates = (new BWPVector(player.getLocation())).toArray();
            CuboidRegion cuboidRegion = BridgingInfo.get(player.getUniqueId()).getCuboidRegion();
            if (BWPUtils.isLegacy()) {
                if (!BWPLegacyAdapter.getInstance().cuboidRegionContains(cuboidRegion, coordinates)) {
                    return;
                }
            }

            this.restart(player, true);
        }
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) {
            if (BridgingInfo.contains(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onPlayerMoveReset(PlayerMoveEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (!GameEngine.getInstance().getPending().contains(playerUUID) && BridgingInfo.contains(playerUUID)) {
            Location practiceLocation = GameEngine.getInstance().getPracticeLocationMap().get(playerUUID);
            Location toLocation = event.getTo();
            if (toLocation.getBlockY() <= this.voidRestart || toLocation.getBlockX() < practiceLocation.getBlockX() - 100 || toLocation.getBlockX() > practiceLocation.getBlockX() + 100) {
                this.restart(event.getPlayer(), false);
            }
        }
    }

    private void restart(Player player, boolean finished) {
        UUID playerUUID = player.getUniqueId();
        player.teleport(GameEngine.getInstance().getPracticeLocationMap().get(playerUUID));
        this.giveItems(player);
        BridgingInfo bridgingInfo = BridgingInfo.get(playerUUID);
        int delay;
        if (finished) {
            double practiceTime = bridgingInfo.getPracticeTime();
            if (practiceTime != 0.0D) {
                String formattedTime = String.format("%,.2f", practiceTime);
                player.sendMessage(Language.MessagesEnum.GAME_BRIDGING_FINISHED_MESSAGE.getString(playerUUID).replace("[seconds]", formattedTime));
                VersionSupport.getInstance().sendTitle(player, Language.MessagesEnum.GAME_BRIDGING_FINISHED_TITLE.getString(playerUUID).replace("[seconds]", formattedTime), "");
                if (bridgingInfo.getStatistic() > practiceTime || bridgingInfo.getStatistic() == 0.0D) {
                    bridgingInfo.setStatistic(practiceTime);
                }
            }

            Bukkit.getPluginManager().callEvent(new PracticeFinishEvent(player, GameEngine.PracticeType.BRIDGING, new BridgingData(bridgingInfo)));
        } else if (bridgingInfo.isInfinite()) {
            int placedBlocks = bridgingInfo.getPlacedBlocks();
            String formattedTime = String.format("%,.2f", bridgingInfo.getPracticeTime());
            if (placedBlocks != 0 && (bridgingInfo.getStatistic() < (double) placedBlocks || bridgingInfo.getStatistic() == 0.0D)) {
                bridgingInfo.setStatistic(placedBlocks);
            }

            player.sendMessage(Language.MessagesEnum.GAME_BRIDGING_FINISHED_INFINITE_MESSAGE.getString(playerUUID).replace("[seconds]", formattedTime).replace("[blocks_placed]", String.valueOf(placedBlocks)));
        } else {
            player.sendMessage(Language.MessagesEnum.GAME_FAILED.getString(playerUUID));
        }

        (finished ? Sounds.PLAYER_LEVELUP : Sounds.ENDERMAN_TELEPORT).getSound().play(player, 1.0F, 1.0F);
        delay = 0;
        if (bridgingInfo.getBlocksPlaced() != null) {
            for (Iterator<Block> iterator = bridgingInfo.getBlocksPlaced().iterator(); iterator.hasNext(); ++delay) {
                Block block = iterator.next();
                Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> block.setType(Material.AIR), delay + 2);
            }
        }

        bridgingInfo.removeBlocksPlaced(false);
        bridgingInfo.removePracticeTime();
        bridgingInfo.removePlacedBlocks();
    }

    public List<String> getScoreboardLines(UUID playerUUID) {
        return (!BridgingInfo.get(playerUUID).isInfinite() ? Language.MessagesEnum.GAME_SCOREBOARD_BRIDGING_DEFAULT_LINES : Language.MessagesEnum.GAME_SCOREBOARD_BRIDGING_INFINITE_LINES).getStringList(playerUUID);
    }

    public HashMap<String, Callable<String>> getPlaceholders(UUID playerUUID) {
        HashMap<String, Callable<String>> placeholders = new HashMap<>();
        GameEngine gameEngine = GameEngine.getInstance();
        BridgingInfo bridgingInfo = BridgingInfo.get(playerUUID);
        placeholders.put("[month]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.MONTH) + 1));
        placeholders.put("[day]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.DATE)));
        placeholders.put("[year]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.YEAR)));
        placeholders.put("[game_mode]", () -> Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_BRIDGING.getString(playerUUID));
        placeholders.put("[average_speed]", bridgingInfo::getAverageSpeed);
        placeholders.put("[personal_best]", bridgingInfo::getPersonalBest);
        if (BridgingInfo.get(playerUUID).isInfinite()) {
            placeholders.put("[blocks_placed]", () -> String.valueOf(bridgingInfo.getPlacedBlocks()));
        }
        return placeholders;
    }

}
