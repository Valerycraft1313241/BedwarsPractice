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
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> MLGInfo.getMlgInfoMap().keySet().forEach((var0) -> VersionSupport.getInstance().sendActionBar(Bukkit.getPlayer(var0), Language.MessagesEnum.GAME_MLG_ACTION_BAR.getString(var0))), 0L, 40L);
    }

    public static MLGMode getInstance() {
        if (instance == null) {
            instance = new MLGMode();
        }

        return instance;
    }

    public MLGCreator create(Player var1) {
        UUID var2 = var1.getUniqueId();
        MLGInfo var3 = new MLGInfo(var2);
        var3.setCuboidRegion(GameEngine.getInstance().initMode(var1, var2, "MLG", GameEngine.PracticeType.MLG));
        var3.setPlatformGenerator((new PlatformGenerator()).create(var2));
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.giveItems(var1);
            Bukkit.getPluginManager().callEvent(new PracticeJoinEvent(var1, GameEngine.PracticeType.MLG));
        }, 5L);
        ScoreboardEngine.getInstance().sendSidebar(var1, GameEngine.PracticeType.MLG);
        return new MLGCreator(var3);
    }

    private void restart(Player var1, boolean var2) {
        UUID var3 = var1.getUniqueId();
        var1.teleport(GameEngine.getInstance().getPracticeLocationMap().get(var3));
        var1.sendMessage((var2 ? Language.MessagesEnum.GAME_SUCCESSFUL : Language.MessagesEnum.GAME_FAILED).getString(var3));
        (var2 ? Sounds.PLAYER_LEVELUP : Sounds.ENDERMAN_TELEPORT).getSound().play(var1, 1.0F, 1.0F);
        MLGInfo var4 = MLGInfo.get(var3);
        if (var2) {
            Bukkit.getPluginManager().callEvent(new PracticeFinishEvent(var1, GameEngine.PracticeType.MLG, new MLGData(var4)));
        }

        PlatformGenerator var5 = var4.getPlatformGenerator();
        var5.destroy();
        var5.create(var3);
        this.giveItems(var1);
        var1.teleport(GameEngine.getInstance().getPracticeLocationMap().get(var3));
        var4.removeJumpTask();
        var4.removeWaterPlaced();
        var4.removeShuffle();
    }

    public void refresh(Player var1, MLGData var2) {
        UUID var3 = var1.getUniqueId();
        MLGInfo var4 = MLGInfo.get(var3);
        PlatformGenerator var5 = var4.getPlatformGenerator();
        var5.destroy();
        var5.create(var3);
        this.giveItems(var1);
        var1.teleport(GameEngine.getInstance().getPracticeLocationMap().get(var3));
        var4.removeJumpTask();
        var4.removeWaterPlaced();
        var4.removeShuffle();
        if (var2 != null) {
            Bukkit.getPluginManager().callEvent(new PracticeChangeEvent(var1, var2, var4.toData()));
        }

    }

    public void quit(Player var1) {
        UUID var2 = var1.getUniqueId();
        GameEngine var3 = GameEngine.getInstance();
        var3.clear(var2, true, true);
        var3.getLastOffset().remove(var2);
        var1.getInventory().clear();
        if (!Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
            var1.teleport(Lobby.getInstance().get());
        }

        MLGInfo var4 = MLGInfo.get(var2);
        var4.removeShuffle();
        var4.removeJumpTask();
        var4.removeWaterPlaced();
        MLGInfo.get(var2).getPlatformGenerator().destroy();
        MLGInfo.remove(var2);
        if (InventoryCache.isInstantiated()) {
            InventoryCache.getInstance().restore(var1);
        }

    }

    public void switchClear(Player var1) {
        UUID var2 = var1.getUniqueId();
        if (MLGInfo.contains(var2)) {
            GameEngine.getInstance().clear(var2, true, true);
            MLGInfo var3 = MLGInfo.get(var2);
            var3.removeShuffle();
            var3.removeJumpTask();
            var3.removeWaterPlaced();
            MLGInfo.get(var2).getPlatformGenerator().destroy();
            MLGInfo.remove(var2);
            var1.getInventory().clear();
        }
    }

    @EventHandler
    private void onPlayerMoveShuffle(PlayerMoveEvent var1) {
        Player var2 = var1.getPlayer();
        UUID var3 = var2.getUniqueId();
        if (MLGInfo.contains(var3)) {
            MLGInfo var4 = MLGInfo.get(var3);
            if (var4.getShuffleType().equals(MLGEnums.MLGShuffleType.SHUFFLED) && !var4.isShuffleItem()) {
                if (var1.getTo().getY() <= (double) (GameEngine.getInstance().getPracticeLocationMap().get(var3).getBlockY() - 1)) {
                    var4.setShuffleItem(true);
                    Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
                        var2.getInventory().clear();
                        Sounds.CLICK.getSound().play(var2, 1.0F, 3.0F);
                        ItemBuilder var3x = var4.getItemType().equals(MLGEnums.MLGItemType.WATER) ? Materials.WATER_BUCKET.getItem().setDisplayName(Language.MessagesEnum.GAME_MLG_ITEM_NAME_WATER.getString(var3)) : Materials.LADDER.getItem().setDisplayName(Language.MessagesEnum.GAME_MLG_ITEM_NAME_LADDER.getString(var3));
                        var2.getInventory().setItem(BWPUtils.genRandomNumber(8), var3x.build());
                    }, 8L);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerMoveRestart(PlayerMoveEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (!GameEngine.getInstance().getPending().contains(var2) && MLGInfo.contains(var2)) {
            if (var1.getTo().getY() <= (double) (GameEngine.getInstance().getPracticeLocationMap().get(var2).getBlockY() - MLGInfo.get(var2).getHeightType().getValue() - 5)) {
                this.restart(var1.getPlayer(), false);
            }

        }
    }

    @EventHandler
    private void onWaterPlace(BlockFromToEvent var1) {
        if (var1.getBlock().getWorld().equals(WorldEngine.getInstance().getPracticeWorld())) {
            if (var1.getBlock().isLiquid()) {
                var1.setCancelled(true);
            }

        }
    }

    @EventHandler
    private void onBucketPlace(PlayerBucketEmptyEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (MLGInfo.contains(var2)) {
            Location var3 = GameEngine.getInstance().getPracticeLocationMap().get(var2);
            if (var1.getBlockClicked().getLocation().getBlockY() > var3.getBlockY() - 1) {
                var1.setCancelled(true);
            } else {
                MLGInfo var4 = MLGInfo.get(var2);
                Materials var5 = var4.getTallnessType().getMaterials();
                Block var6 = var1.getBlockClicked();
                if (!var6.getType().equals(var5.getItem().getMaterial())) {
                    var1.setCancelled(true);
                } else if (!BWPUtils.isLegacy() || var6.getData() == (byte) var5.getData()) {
                    var4.setWaterPlaced(var6.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock());
                }
            }
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent var1) {
        Player var2 = var1.getPlayer();
        if (MLGInfo.contains(var2.getUniqueId())) {
            if (var2.getItemInHand().getType().equals(this.settingsItem.getMaterial())) {
                var1.setCancelled(true);
            }

        }
    }

    @EventHandler
    private void onBucketFill(PlayerBucketFillEvent var1) {
        Block var2 = var1.getBlockClicked();
        UUID var3 = var1.getPlayer().getUniqueId();
        if (MLGInfo.contains(var3)) {
            MLGInfo var4 = MLGInfo.get(var3);
            Materials var5 = var4.getTallnessType().getMaterials();
            if (var2.getType().equals(var5.getItem().getMaterial())) {
                if (!BWPUtils.isLegacy() || var2.getData() == (byte) var5.getData()) {
                    if (var2.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid()) {
                        var4.removeWaterPlaced();
                    }

                }
            }
        }
    }

    @EventHandler
    private void onPlayerFallDamage(EntityDamageEvent var1) {
        UUID var2 = var1.getEntity().getUniqueId();
        if (var1.getEntityType().equals(EntityType.PLAYER) && MLGInfo.contains(var2)) {
            Player var3 = (Player) var1.getEntity();
            MLGInfo var4 = MLGInfo.get(var2);
            Materials var5 = var4.getTallnessType().getMaterials();
            Block var6 = GameEngine.getInstance().getStandingBlock(var3.getLocation());
            if (!var6.getType().equals(var5.getItem().getMaterial())) {
                boolean var7 = BWPUtils.isLegacy();

                if (var7) {
                    var1.setCancelled(true);
                    return;
                }
            }

            this.restart(var3, false);
            var1.setCancelled(true);
            var4.removeJumpTask();
        }
    }

    @EventHandler
    private void onPlayerMoveFinish(PlayerMoveEvent var1) {
        Player var2 = var1.getPlayer();
        UUID var3 = var2.getUniqueId();
        if (MLGInfo.contains(var3)) {
            Block var4 = var2.getLocation().clone().subtract(0.0D, 1.0D, 0.0D).getBlock();
            Materials var5 = MLGInfo.get(var3).getTallnessType().getMaterials();
            if (var4.getType().equals(var5.getItem().getMaterial())) {
                if (!BWPUtils.isLegacy() || var4.getData() == (byte) var5.getData()) {
                    MLGInfo var6 = MLGInfo.get(var3);
                    if (var6.getJumpTask() == null) {
                        var6.setJumpTask(Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> this.restart(var2, true), 5L));
                    }

                }
            }
        }
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent var1) {
        if (GameEngine.getInstance().getPracticeTypeMap().containsKey(var1.getPlayer().getUniqueId())) {
            var1.setCancelled(true);
        }

    }

    public List<String> getScoreboardLines(UUID var1) {
        return Language.MessagesEnum.GAME_SCOREBOARD_MLG_LINES.getStringList(var1);
    }

    public HashMap<String, Callable<String>> getPlaceholders(UUID var1) {
        HashMap<String, Callable<String>> var2 = new HashMap<>();
        GameEngine var3 = GameEngine.getInstance();
        var2.put("[month]", () -> String.valueOf(var3.getCalendar().get(Calendar.MONTH) + 1));
        var2.put("[day]", () -> String.valueOf(var3.getCalendar().get(Calendar.DATE)));
        var2.put("[year]", () -> String.valueOf(var3.getCalendar().get(Calendar.YEAR)));
        var2.put("[game_mode]", () -> Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_MLG.getString(var1));
        return var2;
    }

    private void giveItems(Player var1) {
        PlayerInventory var2 = var1.getInventory();
        UUID var3 = var1.getUniqueId();
        ItemBuilder var4 = MLGInfo.get(var3).getItemType().equals(MLGEnums.MLGItemType.WATER) ? Materials.WATER_BUCKET.getItem().setDisplayName(Language.MessagesEnum.GAME_MLG_ITEM_NAME_WATER.getString(var3)) : Materials.LADDER.getItem().setDisplayName(Language.MessagesEnum.GAME_MLG_ITEM_NAME_LADDER.getString(var3));
        var2.clear();
        var2.setItem(0, var4.build());
        var2.setItem(7, this.settingsItem.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_ITEM_NAME.getString(var3)).setLore(Language.MessagesEnum.GAME_SETTINGS_ITEM_LORE.getStringList(var3)).build());
        var2.setItem(8, this.modeSelectorItem.setDisplayName(Language.MessagesEnum.MODE_SELECTOR_ITEM_NAME.getString(var3)).setLore(Language.MessagesEnum.MODE_SELECTOR_ITEM_LORE.getStringList(var3)).build());
    }
}
