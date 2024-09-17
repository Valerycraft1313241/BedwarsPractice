package com.github.zandy.bedwarspractice.engine.practice.mlg;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.item.ItemBuilder;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.VersionSupport;
import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeChangeEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeFinishEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeJoinEvent;
import com.github.zandy.bedwarspractice.api.utils.creator.MLGCreator;
import com.github.zandy.bedwarspractice.api.utils.data.MLGData;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.ScoreboardEngine;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.engine.practice.utils.PlatformGenerator;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.InventoryCache;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

public class MLGMode implements Listener {
    private static MLGMode instance = null;
    private final ItemBuilder settingsItem;
    private final ItemBuilder modeSelectorItem;

    public MLGMode() {
        this.settingsItem = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_MATERIAL.getMaterial().getItem();
        this.modeSelectorItem = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_MATERIAL.getMaterial().getItem();
        BambooUtils.registerEvent(this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> MLGInfo.getMlgInfoMap().keySet().forEach((playerUUID) -> VersionSupport.getInstance().sendActionBar(Bukkit.getPlayer(playerUUID), Language.MessagesEnum.GAME_MLG_ACTION_BAR.getString(playerUUID))), 0L, 40L);
    }

    public static MLGMode getInstance() {
        if (instance == null) {
            instance = new MLGMode();
        }

        return instance;
    }

    public MLGCreator create(Player player) {
        UUID playerUUID = player.getUniqueId();
        MLGInfo mlgInfo = new MLGInfo(playerUUID);
        mlgInfo.setCuboidRegion(GameEngine.getInstance().initMode(player, playerUUID, "MLG", GameEngine.PracticeType.MLG));
        mlgInfo.setPlatformGenerator((new PlatformGenerator()).create(playerUUID));
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.giveItems(player);
            Bukkit.getPluginManager().callEvent(new PracticeJoinEvent(player, GameEngine.PracticeType.MLG));
        }, 5L);
        ScoreboardEngine.getInstance().sendSidebar(player, GameEngine.PracticeType.MLG);
        return new MLGCreator(mlgInfo);
    }

    private void restart(Player player, boolean successful) {
        UUID playerUUID = player.getUniqueId();
        player.teleport(GameEngine.getInstance().getPracticeLocationMap().get(playerUUID));
        player.sendMessage((successful ? Language.MessagesEnum.GAME_SUCCESSFUL : Language.MessagesEnum.GAME_FAILED).getString(playerUUID));
        (successful ? Sounds.PLAYER_LEVELUP : Sounds.ENDERMAN_TELEPORT).getSound().play(player, 1.0F, 1.0F);
        MLGInfo mlgInfo = MLGInfo.get(playerUUID);
        if (successful) {
            Bukkit.getPluginManager().callEvent(new PracticeFinishEvent(player, GameEngine.PracticeType.MLG, new MLGData(mlgInfo)));
        }

        PlatformGenerator platformGenerator = mlgInfo.getPlatformGenerator();
        platformGenerator.destroy();
        platformGenerator.create(playerUUID);
        this.giveItems(player);
        player.teleport(GameEngine.getInstance().getPracticeLocationMap().get(playerUUID));
        mlgInfo.removeJumpTask();
        mlgInfo.removeWaterPlaced();
        mlgInfo.removeShuffle();
    }

    public void refresh(Player player, MLGData data) {
        UUID playerUUID = player.getUniqueId();
        MLGInfo mlgInfo = MLGInfo.get(playerUUID);
        PlatformGenerator platformGenerator = mlgInfo.getPlatformGenerator();
        platformGenerator.destroy();
        platformGenerator.create(playerUUID);
        this.giveItems(player);
        player.teleport(GameEngine.getInstance().getPracticeLocationMap().get(playerUUID));
        mlgInfo.removeJumpTask();
        mlgInfo.removeWaterPlaced();
        mlgInfo.removeShuffle();
        if (data != null) {
            Bukkit.getPluginManager().callEvent(new PracticeChangeEvent(player, data, mlgInfo.toData()));
        }
    }

    public void quit(Player player) {
        UUID playerUUID = player.getUniqueId();
        GameEngine gameEngine = GameEngine.getInstance();
        gameEngine.clear(playerUUID, true, true);
        gameEngine.getLastOffset().remove(playerUUID);
        player.getInventory().clear();
        if (!Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
            player.teleport(Lobby.getInstance().get());
        }

        MLGInfo mlgInfo = MLGInfo.get(playerUUID);
        mlgInfo.removeShuffle();
        mlgInfo.removeJumpTask();
        mlgInfo.removeWaterPlaced();
        MLGInfo.get(playerUUID).getPlatformGenerator().destroy();
        MLGInfo.remove(playerUUID);
        if (InventoryCache.isInstantiated()) {
            InventoryCache.getInstance().restore(player);
        }
    }

    public void switchClear(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (MLGInfo.contains(playerUUID)) {
            GameEngine.getInstance().clear(playerUUID, true, true);
            MLGInfo mlgInfo = MLGInfo.get(playerUUID);
            mlgInfo.removeShuffle();
            mlgInfo.removeJumpTask();
            mlgInfo.removeWaterPlaced();
            MLGInfo.get(playerUUID).getPlatformGenerator().destroy();
            MLGInfo.remove(playerUUID);
            player.getInventory().clear();
        }
    }

    @EventHandler
    private void onPlayerMoveShuffle(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (MLGInfo.contains(playerUUID)) {
            MLGInfo mlgInfo = MLGInfo.get(playerUUID);
            if (mlgInfo.getShuffleType().equals(MLGEnums.MLGShuffleType.SHUFFLED) && !mlgInfo.isShuffleItem()) {
                if (event.getTo().getY() <= (double) (GameEngine.getInstance().getPracticeLocationMap().get(playerUUID).getBlockY() - 1)) {
                    mlgInfo.setShuffleItem(true);
                    Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
                        player.getInventory().clear();
                        Sounds.CLICK.getSound().play(player, 1.0F, 3.0F);
                        ItemBuilder itemBuilder = mlgInfo.getItemType().equals(MLGEnums.MLGItemType.WATER) ? Materials.WATER_BUCKET.getItem().setDisplayName(Language.MessagesEnum.GAME_MLG_ITEM_NAME_WATER.getString(playerUUID)) : Materials.LADDER.getItem().setDisplayName(Language.MessagesEnum.GAME_MLG_ITEM_NAME_LADDER.getString(playerUUID));
                        player.getInventory().setItem(BWPUtils.genRandomNumber(8), itemBuilder.build());
                    }, 8L);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerMoveRestart(PlayerMoveEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (!GameEngine.getInstance().getPending().contains(playerUUID) && MLGInfo.contains(playerUUID)) {
            if (event.getTo().getY() <= (double) (GameEngine.getInstance().getPracticeLocationMap().get(playerUUID).getBlockY() - MLGInfo.get(playerUUID).getHeightType().getValue() - 5)) {
                this.restart(event.getPlayer(), false);
            }
        }
    }

    @EventHandler
    private void onWaterPlace(BlockFromToEvent event) {
        if (event.getBlock().getWorld().equals(WorldEngine.getInstance().getPracticeWorld())) {
            if (event.getBlock().isLiquid()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onBucketPlace(PlayerBucketEmptyEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (MLGInfo.contains(playerUUID)) {
            Location practiceLocation = GameEngine.getInstance().getPracticeLocationMap().get(playerUUID);
            if (event.getBlockClicked().getLocation().getBlockY() > practiceLocation.getBlockY() - 1) {
                event.setCancelled(true);
            } else {
                MLGInfo mlgInfo = MLGInfo.get(playerUUID);
                Materials materials = mlgInfo.getTallnessType().getMaterials();
                Block blockClicked = event.getBlockClicked();
                if (!blockClicked.getType().equals(materials.getItem().getMaterial())) {
                    event.setCancelled(true);
                } else if (!BWPUtils.isLegacy() || blockClicked.getData() == (byte) materials.getData()) {
                    mlgInfo.setWaterPlaced(blockClicked.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock());
                }
            }
        }
    }


    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (MLGInfo.contains(player.getUniqueId())) {
            if (player.getItemInHand().getType().equals(this.settingsItem.getMaterial())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onBucketFill(PlayerBucketFillEvent event) {
        Block blockClicked = event.getBlockClicked();
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (MLGInfo.contains(playerUUID)) {
            MLGInfo mlgInfo = MLGInfo.get(playerUUID);
            Materials materials = mlgInfo.getTallnessType().getMaterials();
            if (blockClicked.getType().equals(materials.getItem().getMaterial())) {
                if (!BWPUtils.isLegacy() || blockClicked.getData() == (byte) materials.getData()) {
                    if (blockClicked.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid()) {
                        mlgInfo.removeWaterPlaced();
                    }
                }
            }
        }
    }

    @EventHandler
    private void onPlayerFallDamage(EntityDamageEvent event) {
        UUID playerUUID = event.getEntity().getUniqueId();
        if (event.getEntityType().equals(EntityType.PLAYER) && MLGInfo.contains(playerUUID)) {
            Player player = (Player) event.getEntity();
            MLGInfo mlgInfo = MLGInfo.get(playerUUID);
            Materials materials = mlgInfo.getTallnessType().getMaterials();
            Block standingBlock = GameEngine.getInstance().getStandingBlock(player.getLocation());
            if (!standingBlock.getType().equals(materials.getItem().getMaterial())) {
                boolean isLegacy = BWPUtils.isLegacy();

                if (isLegacy) {
                    event.setCancelled(true);
                    return;
                }
            }

            this.restart(player, false);
            event.setCancelled(true);
            mlgInfo.removeJumpTask();
        }
    }

    @EventHandler
    private void onPlayerMoveFinish(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (MLGInfo.contains(playerUUID)) {
            Block blockBelow = player.getLocation().clone().subtract(0.0D, 1.0D, 0.0D).getBlock();
            Materials materials = MLGInfo.get(playerUUID).getTallnessType().getMaterials();
            if (blockBelow.getType().equals(materials.getItem().getMaterial())) {
                if (!BWPUtils.isLegacy() || blockBelow.getData() == (byte) materials.getData()) {
                    MLGInfo mlgInfo = MLGInfo.get(playerUUID);
                    if (mlgInfo.getJumpTask() == null) {
                        mlgInfo.setJumpTask(Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> this.restart(player, true), 5L));
                    }
                }
            }
        }
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        if (GameEngine.getInstance().getPracticeTypeMap().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    public List<String> getScoreboardLines(UUID playerUUID) {
        return Language.MessagesEnum.GAME_SCOREBOARD_MLG_LINES.getStringList(playerUUID);
    }

    public HashMap<String, Callable<String>> getPlaceholders(UUID playerUUID) {
        HashMap<String, Callable<String>> placeholders = new HashMap<>();
        GameEngine gameEngine = GameEngine.getInstance();
        placeholders.put("[month]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.MONTH) + 1));
        placeholders.put("[day]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.DATE)));
        placeholders.put("[year]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.YEAR)));
        placeholders.put("[game_mode]", () -> Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_MLG.getString(playerUUID));
        return placeholders;
    }

    private void giveItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        UUID playerUUID = player.getUniqueId();
        ItemBuilder itemBuilder = MLGInfo.get(playerUUID).getItemType().equals(MLGEnums.MLGItemType.WATER) ? Materials.WATER_BUCKET.getItem().setDisplayName(Language.MessagesEnum.GAME_MLG_ITEM_NAME_WATER.getString(playerUUID)) : Materials.LADDER.getItem().setDisplayName(Language.MessagesEnum.GAME_MLG_ITEM_NAME_LADDER.getString(playerUUID));
        inventory.clear();
        inventory.setItem(0, itemBuilder.build());
        inventory.setItem(7, this.settingsItem.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_ITEM_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.GAME_SETTINGS_ITEM_LORE.getStringList(playerUUID)).build());
        inventory.setItem(8, this.modeSelectorItem.setDisplayName(Language.MessagesEnum.MODE_SELECTOR_ITEM_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.MODE_SELECTOR_ITEM_LORE.getStringList(playerUUID)).build());
    }

}
