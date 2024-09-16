package com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.item.ItemBuilder;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.VersionSupport;
import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeChangeEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeFinishEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeJoinEvent;
import com.github.zandy.bedwarspractice.api.utils.creator.FireballTNTJumpingCreator;
import com.github.zandy.bedwarspractice.api.utils.data.FireballTNTJumpingData;
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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.Callable;

public class FireballTNTJumpingMode implements Listener {
    private static FireballTNTJumpingMode instance = null;
    private final ItemBuilder settingsItem;
    private final ItemBuilder modeSelectorItem;
    private final Materials platformMaterial;
    private final Materials playerBlockMaterial;
    private final int voidRestart;
    private final int placeableAreaOffset;
    private final int platformOffset;
    private final int platformLength;

    public FireballTNTJumpingMode() {
        this.settingsItem = PracticeSettings.GameSettingsEnum.GAME_SETTINGS_MATERIAL.getMaterial().getItem();
        this.modeSelectorItem = PracticeSettings.GameSettingsEnum.MODE_SELECTOR_MATERIAL.getMaterial().getItem();
        this.platformMaterial = PracticeSettings.GameSettingsEnum.FIREBALL_TNT_JUMPING_PLATFORM_MATERIAL.getMaterial();
        this.playerBlockMaterial = PracticeSettings.GameSettingsEnum.FIREBALL_TNT_JUMPING_MATERIAL.getMaterial();
        this.voidRestart = PracticeSettings.GameSettingsEnum.FIREBALL_TNT_JUMPING_VOID_RESTART.getInt();
        this.placeableAreaOffset = PracticeSettings.GameSettingsEnum.FIREBALL_TNT_JUMPING_PLACEABLE_BLOCKS_AREA_OFFSET.getInt();
        this.platformOffset = PracticeSettings.GameSettingsEnum.FIREBALL_TNT_JUMPING_PLATFORM_OFFSET.getInt();
        this.platformLength = PracticeSettings.GameSettingsEnum.FIREBALL_TNT_JUMPING_PLATFORM_LENGTH.getInt();
        BambooUtils.registerEvent(this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> FireballTNTJumpingInfo.getFireballTntJumpingMap().keySet().forEach((var0) -> VersionSupport.getInstance().sendActionBar(Bukkit.getPlayer(var0), Language.MessagesEnum.GAME_FIREBALL_TNT_JUMPING_ACTION_BAR.getString(var0))), 0L, 40L);
    }

    public static FireballTNTJumpingMode getInstance() {
        if (instance == null) {
            instance = new FireballTNTJumpingMode();
        }

        return instance;
    }

    public FireballTNTJumpingCreator create(Player var1) {
        UUID var2 = var1.getUniqueId();
        FireballTNTJumpingInfo var3 = new FireballTNTJumpingInfo(var2);
        var3.setPlatformGenerator((new PlatformGenerator()).create(GameEngine.getInstance().initMode(var1, var2, "FIREBALL-TNT-JUMPING", GameEngine.PracticeType.FIREBALL_TNT_JUMPING).clone().add(0.0D, -2.0D, this.platformOffset), this.platformLength, this.platformMaterial));
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.giveItems(var1);
            Bukkit.getPluginManager().callEvent(new PracticeJoinEvent(var1, GameEngine.PracticeType.FIREBALL_TNT_JUMPING));
        }, 5L);
        ScoreboardEngine.getInstance().sendSidebar(var1, GameEngine.PracticeType.FIREBALL_TNT_JUMPING);
        return new FireballTNTJumpingCreator(var3);
    }

    private void restart(Player var1, boolean var2, int var3) {
        UUID var4 = var1.getUniqueId();
        var1.teleport(GameEngine.getInstance().getPracticeLocationMap().get(var4));
        var1.sendMessage(var2 ? Language.MessagesEnum.GAME_FIREBALL_TNT_JUMPING_FINISHED_MESSAGE.getString(var4).replace("[blocks]", String.valueOf(var3)) : Language.MessagesEnum.GAME_FAILED.getString(var4));
        (var2 ? Sounds.PLAYER_LEVELUP : Sounds.ENDERMAN_TELEPORT).getSound().play(var1, 1.0F, 1.0F);
        FireballTNTJumpingInfo var5 = FireballTNTJumpingInfo.get(var4);
        if (var2) {
            Bukkit.getPluginManager().callEvent(new PracticeFinishEvent(var1, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, new FireballTNTJumpingData(var5)));
            if (var5.getStatistic() < var3) {
                var5.setStatistic(var3);
            }
        }

        this.giveItems(var1);
        int var6 = 0;

        for (Iterator<Block> var7 = var5.getBlocksPlaced().iterator(); var7.hasNext(); ++var6) {
            Block var8 = var7.next();
            Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> var8.setType(Material.AIR), var6 + 4);
        }

        var5.removeBlocksPlaced();
    }

    public void refresh(Player var1, FireballTNTJumpingData var2) {
        UUID var3 = var1.getUniqueId();
        int var4 = 0;
        FireballTNTJumpingInfo var5 = FireballTNTJumpingInfo.get(var3);

        for (Iterator<Block> var6 = var5.getBlocksPlaced().iterator(); var6.hasNext(); ++var4) {
            Block var7 = var6.next();
            Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> var7.setType(Material.AIR), var4 + 4);
        }

        this.giveItems(var1);
        var1.teleport(GameEngine.getInstance().getPracticeLocationMap().get(var3));
        if (var2 != null) {
            Bukkit.getPluginManager().callEvent(new PracticeChangeEvent(var1, var2, var5.toData()));
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

        FireballTNTJumpingInfo var4 = FireballTNTJumpingInfo.get(var2);
        var4.removeBlocksPlaced();
        var4.getPlatformGenerator().destroy();
        FireballTNTJumpingInfo.remove(var2);
        if (InventoryCache.isInstantiated()) {
            InventoryCache.getInstance().restore(var1);
        }

    }

    public void switchClear(Player var1) {
        UUID var2 = var1.getUniqueId();
        if (FireballTNTJumpingInfo.contains(var2)) {
            GameEngine.getInstance().clear(var2, true, true);
            FireballTNTJumpingInfo var3 = FireballTNTJumpingInfo.get(var2);
            var3.removeBlocksPlaced();
            var3.getPlatformGenerator().destroy();
            FireballTNTJumpingInfo.remove(var2);
            var1.getInventory().clear();
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent var1) {
        Player var2 = var1.getPlayer();
        UUID var3 = var2.getUniqueId();
        if (FireballTNTJumpingInfo.contains(var3)) {
            if (var2.getItemInHand().getType().equals(this.settingsItem.getMaterial())) {
                var1.setCancelled(true);
            } else {
                Block var4 = var1.getBlock();
                if (var4.getZ() >= 1 && var4.getZ() <= this.placeableAreaOffset) {
                    FireballTNTJumpingInfo.get(var3).addBlocksPlaced(var4);
                } else {
                    var1.setCancelled(true);
                    var2.sendMessage(Language.MessagesEnum.GAME_BLOCK_NOT_PLACEABLE.getString(var3));
                }
            }
        }
    }

    @EventHandler
    private void onBlockPlacePlatform(BlockPlaceEvent var1) {
        Player var2 = var1.getPlayer();
        UUID var3 = var2.getUniqueId();
        if (FireballTNTJumpingInfo.contains(var3)) {
            FireballTNTJumpingInfo var4 = FireballTNTJumpingInfo.get(var3);
            if (var2.getLocation().getBlockZ() > var4.getPlatformGenerator().getStartingZ()) {
                if (!var4.isBlockPlaced()) {
                    var4.setBlockPlaced(true);
                }

            }
        }
    }

    @EventHandler
    private void onPlayerMovePlaceReset(PlayerMoveEvent var1) {
        Player var2 = var1.getPlayer();
        UUID var3 = var2.getUniqueId();
        if (FireballTNTJumpingInfo.contains(var3)) {
            FireballTNTJumpingInfo var4 = FireballTNTJumpingInfo.get(var3);
            if (var4.isBlockPlaced()) {
                var1.setCancelled(true);
                var4.setBlockPlaced(false);
            }

        }
    }

    @EventHandler
    private void onPlayerMoveReset(PlayerMoveEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (!GameEngine.getInstance().getPending().contains(var2) && FireballTNTJumpingInfo.contains(var2)) {
            Location var3 = GameEngine.getInstance().getPracticeLocationMap().get(var2);
            Location var4 = var1.getTo();
            if (var4.getBlockY() <= this.voidRestart || var4.getBlockX() < var3.getBlockX() - 100 || var4.getBlockX() > var3.getBlockX() + 100) {
                this.restart(var1.getPlayer(), false, 0);
            }

        }
    }

    @EventHandler
    private void onPlayerMoveFinish(PlayerMoveEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (FireballTNTJumpingInfo.contains(var2)) {
            Block var3 = GameEngine.getInstance().getStandingBlock(var1.getPlayer().getLocation());
            if (var3.getType().equals(this.platformMaterial.getItem().getMaterial())) {
                if (!BWPUtils.isLegacy() || var3.getData() == (byte) this.platformMaterial.getData()) {
                    this.restart(var1.getPlayer(), true, var1.getTo().getBlockZ() - this.placeableAreaOffset + 1);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerInteractFireball(PlayerInteractEvent var1) {
        if (var1.getAction().name().contains("RIGHT")) {
            if (FireballTNTJumpingInfo.contains(var1.getPlayer().getUniqueId())) {
                Player var2 = var1.getPlayer();
                ItemStack var3 = var2.getItemInHand();
                if (var3.getType() == Materials.FIRE_CHARGE.getItem().getMaterial()) {
                    var1.setCancelled(true);
                    if (var3.getAmount() != 1) {
                        var3.setAmount(var3.getAmount() - 1);
                    } else {
                        var2.getInventory().remove(var3);
                    }

                    Fireball var4 = var2.launchProjectile(Fireball.class);
                    Vector var5 = var2.getEyeLocation().getDirection();
                    var4 = VersionSupport.getInstance().setFireballDirection(var4, var5);
                    var4.setVelocity(var4.getDirection().multiply(2.5D));
                    var4.setIsIncendiary(false);
                }
            }
        }
    }

    @EventHandler
    private void onFireballExplosion(EntityExplodeEvent var1) {
        Entity var2 = var1.getEntity();
        if (var2.getType().equals(EntityType.FIREBALL) || var2.getType().equals(EntityType.PRIMED_TNT)) {
            if (WorldEngine.getInstance().getPracticeWorld().equals(var2.getWorld())) {
                var1.setCancelled(true);
                Location var3 = var1.getLocation();
                var3.getWorld()
                        .getNearbyEntities(var3, 3.0D, 3.0D, 3.0D)
                        .stream()
                        .filter((var0) -> var0.getType().equals(EntityType.PLAYER))
                        .forEach(
                                (var1x) -> {
                                    // Get the vector from the knocked entity to the source entity
                                    Vector knockbackDirection = var3.toVector().subtract(var1x.getLocation().toVector()).normalize();

                                    // Get the player's current velocity
                                    Vector playerMomentum = var1x.getVelocity();

                                    // Modify the knockback to incorporate player's momentum
                                    Vector finalKnockback = knockbackDirection.multiply(-1.1D).setY(1.4D).add(playerMomentum.multiply(0.5D)); // Adjust multiplier as needed

                                    // Apply the knockback to the player
                                    var1x.setVelocity(finalKnockback);
                                }
                        );

            }
        }
    }

    @EventHandler
    private void onPlayerInteractTNT(PlayerInteractEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (var1.getAction().equals(Action.RIGHT_CLICK_BLOCK) && FireballTNTJumpingInfo.contains(var2)) {
            if (FireballTNTJumpingInfo.get(var2).getItemType().equals(FireballTNTJumpingEnums.FireballTNTJumpingItemType.TNT)) {
                Player var3 = var1.getPlayer();
                ItemStack var4 = var3.getItemInHand();
                if (var4.getType() == Materials.TNT.getItem().getMaterial()) {
                    var1.setCancelled(true);
                    if (var4.getAmount() != 1) {
                        var4.setAmount(var4.getAmount() - 1);
                    } else {
                        var3.getInventory().remove(var4);
                    }

                    Location var5 = var1.getClickedBlock().getLocation().clone().add(0.0D, 1.0D, 0.0D);
                    TNTPrimed var6 = var5.getWorld().spawn(var5.clone().add(0.5D, 1.0D, 0.5D), TNTPrimed.class);
                    Sounds.DIG_GRASS.getSound().play(var3, 1.0F, 1.0F);
                    var6.setFuseTicks(40);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageEvent var1) {
        if (var1.getEntityType().equals(EntityType.PLAYER)) {
            if (FireballTNTJumpingInfo.contains(var1.getEntity().getUniqueId())) {
                var1.setDamage(0.0D);
            }

        }
    }

    public List<String> getScoreboardLines(UUID var1) {
        return Language.MessagesEnum.GAME_SCOREBOARD_FIREBALL_TNT_JUMPING_LINES.getStringList(var1);
    }

    public HashMap<String, Callable<String>> getPlaceholders(UUID var1) {
        HashMap<String, Callable<String>> var2 = new HashMap<>();
        GameEngine var3 = GameEngine.getInstance();
        var2.put("[month]", () -> String.valueOf(var3.getCalendar().get(Calendar.MONTH) + 1));
        var2.put("[day]", () -> String.valueOf(var3.getCalendar().get(Calendar.DATE)));
        var2.put("[year]", () -> String.valueOf(var3.getCalendar().get(Calendar.YEAR)));
        var2.put("[game_mode]", () -> Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_FIREBALL_TNT_JUMPING.getString(var1));
        var2.put("[longest_jump]", () -> String.valueOf(FireballTNTJumpingInfo.get(var1).getStatistic()));
        return var2;
    }

    private void giveItems(Player var1) {
        PlayerInventory var2 = var1.getInventory();
        FireballTNTJumpingInfo var3 = FireballTNTJumpingInfo.get(var1.getUniqueId());
        var2.clear();
        var2.setItem(0, (var3.getItemType().equals(FireballTNTJumpingEnums.FireballTNTJumpingItemType.FIREBALL) ? Materials.FIRE_CHARGE : Materials.TNT).getItem().setAmount(var3.getAmountType().getValue()).build());
        UUID var4 = var1.getUniqueId();
        if (var3.getWoolType().equals(FireballTNTJumpingEnums.FireballTNTJumpingWoolType.ENABLE)) {
            var2.setItem(1, this.playerBlockMaterial.getItem().setAmount(32).build());
        }

        var2.setItem(7, this.settingsItem.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_ITEM_NAME.getString(var4)).setLore(Language.MessagesEnum.GAME_SETTINGS_ITEM_LORE.getStringList(var4)).build());
        var2.setItem(8, this.modeSelectorItem.setDisplayName(Language.MessagesEnum.MODE_SELECTOR_ITEM_NAME.getString(var4)).setLore(Language.MessagesEnum.MODE_SELECTOR_ITEM_LORE.getStringList(var4)).build());
    }
}
