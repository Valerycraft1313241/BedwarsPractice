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
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> BridgingInfo.getBridgingInfoMap().keySet().forEach((var0) -> {
            BridgingInfo var1 = BridgingInfo.get(var0);
            if (var1.getBlocksPlaced() != null) {
                var1.addPracticeTime();
                VersionSupport.getInstance().sendActionBar(Bukkit.getPlayer(var0), Language.MessagesEnum.GAME_BRIDGING_ACTION_BAR_TIMER_COLOR.getString(var0) + String.format("%,.2f", var1.getPracticeTime()));
            }

        }), 1L, 1L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> BridgingInfo.getBridgingInfoMap().keySet().forEach((var0) -> {
            if (BridgingInfo.get(var0).getBlocksPlaced() == null) {
                VersionSupport.getInstance().sendActionBar(Bukkit.getPlayer(var0), Language.MessagesEnum.GAME_BRIDGING_ACTION_BAR_START_TIMER.getString(var0));
            }

        }), 0L, 40L);
    }

    public static BridgingMode getInstance() {
        if (instance == null) {
            instance = new BridgingMode();
        }

        return instance;
    }

    public BridgingCreator create(Player var1) {
        UUID var2 = var1.getUniqueId();
        BridgingInfo var3 = new BridgingInfo(var2);
        var3.setCuboidRegion(GameEngine.getInstance().initMode(var1, var2, var3.formatArena(), GameEngine.PracticeType.BRIDGING));
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.giveItems(var1);
            Bukkit.getPluginManager().callEvent(new PracticeJoinEvent(var1, GameEngine.PracticeType.BRIDGING));
        }, 5L);
        ScoreboardEngine.getInstance().sendSidebar(var1, GameEngine.PracticeType.BRIDGING);
        return new BridgingCreator(var3);
    }

    public void refresh(Player var1, BridgingData var2) {
        UUID var3 = var1.getUniqueId();
        BridgingInfo var4 = BridgingInfo.get(var3);
        var4.removeBlocksPlaced(true);
        var4.removePracticeTime();
        var4.removePlacedBlocks();
        GameEngine var5 = GameEngine.getInstance();
        var1.getInventory().clear();
        Location var6 = var5.getPracticeLocationMap().get(var3);
        if (var4.isInfinite()) {
            if (!var4.isInfiniteBefore()) {
                var6 = this.loadSetting(var4, var3, var5);
                var4.setInfiniteBefore(true);
                ScoreboardEngine.getInstance().sendSidebar(var1, GameEngine.PracticeType.BRIDGING);
            }
        } else {
            if (var4.isInfiniteBefore()) {
                var4.setInfiniteBefore(false);
                ScoreboardEngine.getInstance().sendSidebar(var1, GameEngine.PracticeType.BRIDGING);
            }

            var6 = this.loadSetting(var4, var3, var5);
        }

        var1.teleport(var6);
        this.giveItems(var1);
        if (var2 != null) {
            Bukkit.getPluginManager().callEvent(new PracticeChangeEvent(var1, var2, var4.toData()));
        }

    }

    private Location loadSetting(BridgingInfo var1, UUID var2, GameEngine var3) {
        var3.clear(var2, false, true);
        Location var4 = var3.getPracticeLocation(var2);
        double[] var5 = (new BWPVector(var4)).toArray();
        World var6 = WorldEngine.getInstance().getPracticeWEWorld();
        var3.getPlayerEditSessionMap().put(var2, BWPLegacyAdapter.getInstance().pasteSchematic(var1.formatArena(), var6, var5));
        if (!var1.isInfinite()) {
            var1.setCuboidRegion(var4);
        }

        Location var7 = var4.clone().add(0.5D, 0.0D, 0.5D);
        var3.getPracticeLocationMap().put(var2, var7);
        return var7;
    }

    public void quit(Player var1) {
        UUID var2 = var1.getUniqueId();
        BridgingInfo var3 = BridgingInfo.get(var2);
        var3.removeBlocksPlaced(true);
        var3.removePlacedBlocks();
        GameEngine var4 = GameEngine.getInstance();
        var4.clear(var2, true, true);
        var4.getLastOffset().remove(var2);
        var1.getInventory().clear();
        if (!Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean()) {
            var1.teleport(Lobby.getInstance().get());
        }

        BridgingInfo.remove(var2);
        if (InventoryCache.isInstantiated()) {
            InventoryCache.getInstance().restore(var1);
        }

    }

    public void switchClear(Player var1) {
        UUID var2 = var1.getUniqueId();
        if (BridgingInfo.contains(var2)) {
            BridgingInfo var3 = BridgingInfo.get(var2);
            var3.removeBlocksPlaced(true);
            var3.removePlacedBlocks();
            GameEngine.getInstance().clear(var2, true, true);
            BridgingInfo.remove(var2);
            var1.getInventory().clear();
        }
    }

    private void giveItems(Player var1) {
        PlayerInventory var2 = var1.getInventory();
        var2.clear();

        for (int var3 = 0; var3 <= 3; ++var3) {
            var2.setItem(var3, this.bridgingBlock);
        }

        UUID var4 = var1.getUniqueId();
        var2.setItem(7, this.settingsItem.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_ITEM_NAME.getString(var4)).setLore(Language.MessagesEnum.GAME_SETTINGS_ITEM_LORE.getStringList(var4)).build());
        var2.setItem(8, this.modeSelectorItem.setDisplayName(Language.MessagesEnum.MODE_SELECTOR_ITEM_NAME.getString(var4)).setLore(Language.MessagesEnum.MODE_SELECTOR_ITEM_LORE.getStringList(var4)).build());
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent var1) {
        if (BridgingInfo.contains(var1.getPlayer().getUniqueId())) {
            Player var2 = var1.getPlayer();
            if (var2.getItemInHand().getType().equals(this.settingsItem.getMaterial())) {
                var1.setCancelled(true);
            } else {
                UUID var3 = var2.getUniqueId();
                Block var4 = var1.getBlock();
                if (var4.getZ() < 1) {
                    var1.setCancelled(true);
                    var2.sendMessage(Language.MessagesEnum.GAME_BLOCK_NOT_PLACEABLE.getString(var3));
                } else {
                    BridgingInfo var5 = BridgingInfo.get(var3);
                    var5.addBlockPlaced(var4);
                    var5.addPlacedBlocks();
                    if (var5.isInfinite()) {
                        ItemStack var6 = var1.getItemInHand();
                        var6.setAmount(64);
                        var1.getPlayer().setItemInHand(var6);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onPlayerMoveAverageSpeed(PlayerMoveEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (BridgingInfo.contains(var2)) {
            double var3 = var1.getFrom().getX() - var1.getTo().getX();
            double var5 = 0.0D;
            double var7 = var1.getFrom().getZ() - var1.getTo().getZ();
            BridgingInfo.get(var2).setAverageSpeed(String.format("%,.2f", Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7) * 20.0D));
        }
    }

    @EventHandler
    private void onPlayerMoveFinish(PlayerMoveEvent var1) {
        if (BridgingInfo.contains(var1.getPlayer().getUniqueId())) {
            Player var2 = var1.getPlayer();
            double[] var3 = (new BWPVector(var2.getLocation())).toArray();
            CuboidRegion var4 = BridgingInfo.get(var2.getUniqueId()).getCuboidRegion();
            if (BWPUtils.isLegacy()) {
                if (!BWPLegacyAdapter.getInstance().cuboidRegionContains(var4, var3)) {
                    return;
                }
            }

            this.restart(var2, true);
        }
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageEvent var1) {
        if (var1.getEntityType().equals(EntityType.PLAYER)) {
            if (BridgingInfo.contains(var1.getEntity().getUniqueId())) {
                var1.setCancelled(true);
            }

        }
    }

    @EventHandler
    private void onPlayerMoveReset(PlayerMoveEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (!GameEngine.getInstance().getPending().contains(var2) && BridgingInfo.contains(var2)) {
            Location var3 = GameEngine.getInstance().getPracticeLocationMap().get(var2);
            Location var4 = var1.getTo();
            if (var4.getBlockY() <= this.voidRestart || var4.getBlockX() < var3.getBlockX() - 100 || var4.getBlockX() > var3.getBlockX() + 100) {
                this.restart(var1.getPlayer(), false);
            }

        }
    }

    private void restart(Player var1, boolean var2) {
        UUID var3 = var1.getUniqueId();
        var1.teleport(GameEngine.getInstance().getPracticeLocationMap().get(var3));
        this.giveItems(var1);
        BridgingInfo var4 = BridgingInfo.get(var3);
        int var8;
        if (var2) {
            double var5 = var4.getPracticeTime();
            if (var5 != 0.0D) {
                String var7 = String.format("%,.2f", var5);
                var1.sendMessage(Language.MessagesEnum.GAME_BRIDGING_FINISHED_MESSAGE.getString(var3).replace("[seconds]", var7));
                VersionSupport.getInstance().sendTitle(var1, Language.MessagesEnum.GAME_BRIDGING_FINISHED_TITLE.getString(var3).replace("[seconds]", var7), "");
                if (var4.getStatistic() > var5 || var4.getStatistic() == 0.0D) {
                    var4.setStatistic(var5);
                }
            }

            Bukkit.getPluginManager().callEvent(new PracticeFinishEvent(var1, GameEngine.PracticeType.BRIDGING, new BridgingData(var4)));
        } else if (var4.isInfinite()) {
            var8 = var4.getPlacedBlocks();
            String var6 = String.format("%,.2f", var4.getPracticeTime());
            if (var8 != 0 && (var4.getStatistic() < (double) var8 || var4.getStatistic() == 0.0D)) {
                var4.setStatistic(var8);
            }

            var1.sendMessage(Language.MessagesEnum.GAME_BRIDGING_FINISHED_INFINITE_MESSAGE.getString(var3).replace("[seconds]", var6).replace("[blocks_placed]", String.valueOf(var8)));
        } else {
            var1.sendMessage(Language.MessagesEnum.GAME_FAILED.getString(var3));
        }

        (var2 ? Sounds.PLAYER_LEVELUP : Sounds.ENDERMAN_TELEPORT).getSound().play(var1, 1.0F, 1.0F);
        var8 = 0;
        if (var4.getBlocksPlaced() != null) {
            for (Iterator<Block> var9 = var4.getBlocksPlaced().iterator(); var9.hasNext(); ++var8) {
                Block var10 = var9.next();
                Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> var10.setType(Material.AIR), var8 + 2);
            }
        }

        var4.removeBlocksPlaced(false);
        var4.removePracticeTime();
        var4.removePlacedBlocks();
    }

    public List<String> getScoreboardLines(UUID var1) {
        return (!BridgingInfo.get(var1).isInfinite() ? Language.MessagesEnum.GAME_SCOREBOARD_BRIDGING_DEFAULT_LINES : Language.MessagesEnum.GAME_SCOREBOARD_BRIDGING_INFINITE_LINES).getStringList(var1);
    }

    public HashMap<String, Callable<String>> getPlaceholders(UUID var1) {
        HashMap<String, Callable<String>> var2 = new HashMap<>();
        GameEngine var3 = GameEngine.getInstance();
        BridgingInfo var4 = BridgingInfo.get(var1);
        var2.put("[month]", () -> String.valueOf(var3.getCalendar().get(Calendar.MONTH) + 1));
        var2.put("[day]", () -> String.valueOf(var3.getCalendar().get(Calendar.DATE)));
        var2.put("[year]", () -> String.valueOf(var3.getCalendar().get(Calendar.YEAR)));
        var2.put("[game_mode]", () -> Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_MLG.getString(var1));
        var2.put("[average_speed]", var4::getAverageSpeed);
        var2.put("[personal_best]", var4::getPersonalBest);
        if (BridgingInfo.get(var1).isInfinite()) {
            var2.put("[blocks_placed]", () -> String.valueOf(var4.getPlacedBlocks()));
        }
        return var2;
    }
}
