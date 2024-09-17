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
        Bukkit.getScheduler().runTaskTimerAsynchronously(BambooLib.getPluginInstance(), () -> FireballTNTJumpingInfo.getFireballTntJumpingMap().keySet().forEach((uuid) -> VersionSupport.getInstance().sendActionBar(Bukkit.getPlayer(uuid), Language.MessagesEnum.GAME_FIREBALL_TNT_JUMPING_ACTION_BAR.getString(uuid))), 0L, 40L);
    }

    public static FireballTNTJumpingMode getInstance() {
        if (instance == null) {
            instance = new FireballTNTJumpingMode();
        }

        return instance;
    }

    public FireballTNTJumpingCreator create(Player player) {
        UUID playerUUID = player.getUniqueId();
        FireballTNTJumpingInfo jumpingInfo = new FireballTNTJumpingInfo(playerUUID);
        jumpingInfo.setPlatformGenerator((new PlatformGenerator()).create(GameEngine.getInstance().initMode(player, playerUUID, "FIREBALL-TNT-JUMPING", GameEngine.PracticeType.FIREBALL_TNT_JUMPING).clone().add(0.0D, -2.0D, this.platformOffset), this.platformLength, this.platformMaterial));
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.giveItems(player);
            Bukkit.getPluginManager().callEvent(new PracticeJoinEvent(player, GameEngine.PracticeType.FIREBALL_TNT_JUMPING));
        }, 5L);
        ScoreboardEngine.getInstance().sendSidebar(player, GameEngine.PracticeType.FIREBALL_TNT_JUMPING);
        return new FireballTNTJumpingCreator(jumpingInfo);
    }

    private void restart(Player player, boolean finished, int blocks) {
        UUID playerUUID = player.getUniqueId();
        player.teleport(GameEngine.getInstance().getPracticeLocationMap().get(playerUUID));
        player.sendMessage(finished ? Language.MessagesEnum.GAME_FIREBALL_TNT_JUMPING_FINISHED_MESSAGE.getString(playerUUID).replace("[blocks]", String.valueOf(blocks)) : Language.MessagesEnum.GAME_FAILED.getString(playerUUID));
        (finished ? Sounds.PLAYER_LEVELUP : Sounds.ENDERMAN_TELEPORT).getSound().play(player, 1.0F, 1.0F);
        FireballTNTJumpingInfo jumpingInfo = FireballTNTJumpingInfo.get(playerUUID);
        if (finished) {
            Bukkit.getPluginManager().callEvent(new PracticeFinishEvent(player, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, new FireballTNTJumpingData(jumpingInfo)));
            if (jumpingInfo.getStatistic() < blocks) {
                jumpingInfo.setStatistic(blocks);
            }
        }

        this.giveItems(player);
        int delay = 0;

        for (Iterator<Block> iterator = jumpingInfo.getBlocksPlaced().iterator(); iterator.hasNext(); ++delay) {
            Block block = iterator.next();
            Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> block.setType(Material.AIR), delay + 4);
        }

        jumpingInfo.removeBlocksPlaced();
    }

    public void refresh(Player player, FireballTNTJumpingData data) {
        UUID playerUUID = player.getUniqueId();
        int delay = 0;
        FireballTNTJumpingInfo jumpingInfo = FireballTNTJumpingInfo.get(playerUUID);

        for (Iterator<Block> iterator = jumpingInfo.getBlocksPlaced().iterator(); iterator.hasNext(); ++delay) {
            Block block = iterator.next();
            Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> block.setType(Material.AIR), delay + 4);
        }

        this.giveItems(player);
        player.teleport(GameEngine.getInstance().getPracticeLocationMap().get(playerUUID));
        if (data != null) {
            Bukkit.getPluginManager().callEvent(new PracticeChangeEvent(player, data, jumpingInfo.toData()));
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

        FireballTNTJumpingInfo jumpingInfo = FireballTNTJumpingInfo.get(playerUUID);
        jumpingInfo.removeBlocksPlaced();
        jumpingInfo.getPlatformGenerator().destroy();
        FireballTNTJumpingInfo.remove(playerUUID);
        if (InventoryCache.isInstantiated()) {
            InventoryCache.getInstance().restore(player);
        }
    }

    public void switchClear(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (FireballTNTJumpingInfo.contains(playerUUID)) {
            GameEngine.getInstance().clear(playerUUID, true, true);
            FireballTNTJumpingInfo jumpingInfo = FireballTNTJumpingInfo.get(playerUUID);
            jumpingInfo.removeBlocksPlaced();
            jumpingInfo.getPlatformGenerator().destroy();
            FireballTNTJumpingInfo.remove(playerUUID);
            player.getInventory().clear();
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (FireballTNTJumpingInfo.contains(playerUUID)) {
            if (player.getItemInHand().getType().equals(this.settingsItem.getMaterial())) {
                event.setCancelled(true);
            } else {
                Block block = event.getBlock();
                if (block.getZ() >= 1 && block.getZ() <= this.placeableAreaOffset) {
                    FireballTNTJumpingInfo.get(playerUUID).addBlocksPlaced(block);
                } else {
                    event.setCancelled(true);
                    player.sendMessage(Language.MessagesEnum.GAME_BLOCK_NOT_PLACEABLE.getString(playerUUID));
                }
            }
        }
    }

    @EventHandler
    private void onBlockPlacePlatform(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (FireballTNTJumpingInfo.contains(playerUUID)) {
            FireballTNTJumpingInfo jumpingInfo = FireballTNTJumpingInfo.get(playerUUID);
            if (player.getLocation().getBlockZ() > jumpingInfo.getPlatformGenerator().getStartingZ()) {
                if (!jumpingInfo.isBlockPlaced()) {
                    jumpingInfo.setBlockPlaced(true);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerMovePlaceReset(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (FireballTNTJumpingInfo.contains(playerUUID)) {
            FireballTNTJumpingInfo jumpingInfo = FireballTNTJumpingInfo.get(playerUUID);
            if (jumpingInfo.isBlockPlaced()) {
                event.setCancelled(true);
                jumpingInfo.setBlockPlaced(false);
            }
        }
    }

    @EventHandler
    private void onPlayerMoveReset(PlayerMoveEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (!GameEngine.getInstance().getPending().contains(playerUUID) && FireballTNTJumpingInfo.contains(playerUUID)) {
            Location practiceLocation = GameEngine.getInstance().getPracticeLocationMap().get(playerUUID);
            Location toLocation = event.getTo();
            if (toLocation.getBlockY() <= this.voidRestart || toLocation.getBlockX() < practiceLocation.getBlockX() - 100 || toLocation.getBlockX() > practiceLocation.getBlockX() + 100) {
                this.restart(event.getPlayer(), false, 0);
            }
        }
    }

    @EventHandler
    private void onPlayerMoveFinish(PlayerMoveEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (FireballTNTJumpingInfo.contains(playerUUID)) {
            Block standingBlock = GameEngine.getInstance().getStandingBlock(event.getPlayer().getLocation());
            if (standingBlock.getType().equals(this.platformMaterial.getItem().getMaterial())) {
                if (!BWPUtils.isLegacy() || standingBlock.getData() == (byte) this.platformMaterial.getData()) {
                    this.restart(event.getPlayer(), true, event.getTo().getBlockZ() - this.placeableAreaOffset + 1);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerInteractFireball(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT")) {
            if (FireballTNTJumpingInfo.contains(event.getPlayer().getUniqueId())) {
                Player player = event.getPlayer();
                ItemStack itemInHand = player.getItemInHand();
                if (itemInHand.getType() == Materials.FIRE_CHARGE.getItem().getMaterial()) {
                    event.setCancelled(true);
                    if (itemInHand.getAmount() != 1) {
                        itemInHand.setAmount(itemInHand.getAmount() - 1);
                    } else {
                        player.getInventory().remove(itemInHand);
                    }

                    Fireball fireball = player.launchProjectile(Fireball.class);
                    Vector direction = player.getEyeLocation().getDirection();
                    fireball = VersionSupport.getInstance().setFireballDirection(fireball, direction);
                    fireball.setVelocity(fireball.getDirection().multiply(2.5D));
                    fireball.setIsIncendiary(false);
                }
            }
        }
    }

    void pushAway(Player player, Location location, double heightForce, double radiusForce) {
        Location playerLocation = player.getLocation();
        double adjustedHeightForce = Math.max(-4.0, Math.min(4.0, heightForce));
        double adjustedRadiusForce = Math.max(-4.0, Math.min(4.0, -1.0 * radiusForce));
        player.setVelocity(location.toVector().subtract(playerLocation.toVector()).normalize().multiply(adjustedRadiusForce).setY(adjustedHeightForce));
    }

    @EventHandler
    private void onFireballExplosion(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType().equals(EntityType.FIREBALL) || entity.getType().equals(EntityType.PRIMED_TNT)) {
            if (WorldEngine.getInstance().getPracticeWorld().equals(entity.getWorld())) {
                event.setCancelled(true);
                Location explosionLocation = event.getLocation();
                explosionLocation.getWorld()
                        .getNearbyEntities(explosionLocation, 3.0D, 3.0D, 3.0D)
                        .stream()
                        .filter((nearbyEntity) -> nearbyEntity.getType().equals(EntityType.PLAYER))
                        .forEach(
                                (nearbyPlayer) -> {
                                    double heightForce = 1.25;
                                    double radiusForce = 1.6;
                                    this.pushAway((Player) nearbyPlayer, explosionLocation, heightForce, radiusForce);
                                }
                        );
            }
        }
    }

    @EventHandler
    private void onPlayerInteractTNT(PlayerInteractEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && FireballTNTJumpingInfo.contains(playerUUID)) {
            if (FireballTNTJumpingInfo.get(playerUUID).getItemType().equals(FireballTNTJumpingEnums.FireballTNTJumpingItemType.TNT)) {
                Player player = event.getPlayer();
                ItemStack itemInHand = player.getItemInHand();
                if (itemInHand.getType() == Materials.TNT.getItem().getMaterial()) {
                    event.setCancelled(true);
                    if (itemInHand.getAmount() != 1) {
                        itemInHand.setAmount(itemInHand.getAmount() - 1);
                    } else {
                        player.getInventory().remove(itemInHand);
                    }

                    Location clickedBlockLocation = event.getClickedBlock().getLocation().clone().add(0.0D, 1.0D, 0.0D);
                    TNTPrimed tnt = clickedBlockLocation.getWorld().spawn(clickedBlockLocation.clone().add(0.5D, 1.0D, 0.5D), TNTPrimed.class);
                    Sounds.DIG_GRASS.getSound().play(player, 1.0F, 1.0F);
                    tnt.setFuseTicks(40);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) {
            if (FireballTNTJumpingInfo.contains(event.getEntity().getUniqueId())) {
                event.setDamage(0.0D);
            }
        }
    }

    public List<String> getScoreboardLines(UUID playerUUID) {
        return Language.MessagesEnum.GAME_SCOREBOARD_FIREBALL_TNT_JUMPING_LINES.getStringList(playerUUID);
    }

    public HashMap<String, Callable<String>> getPlaceholders(UUID playerUUID) {
        HashMap<String, Callable<String>> placeholders = new HashMap<>();
        GameEngine gameEngine = GameEngine.getInstance();
        placeholders.put("[month]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.MONTH) + 1));
        placeholders.put("[day]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.DATE)));
        placeholders.put("[year]", () -> String.valueOf(gameEngine.getCalendar().get(Calendar.YEAR)));
        placeholders.put("[game_mode]", () -> Language.MessagesEnum.GAME_SCOREBOARD_GAMEMODE_FIREBALL_TNT_JUMPING.getString(playerUUID));
        placeholders.put("[longest_jump]", () -> String.valueOf(FireballTNTJumpingInfo.get(playerUUID).getStatistic()));
        return placeholders;
    }

    private void giveItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        FireballTNTJumpingInfo jumpingInfo = FireballTNTJumpingInfo.get(player.getUniqueId());
        inventory.clear();
        inventory.setItem(0, (jumpingInfo.getItemType().equals(FireballTNTJumpingEnums.FireballTNTJumpingItemType.FIREBALL) ? Materials.FIRE_CHARGE : Materials.TNT).getItem().setAmount(jumpingInfo.getAmountType().getValue()).build());
        UUID playerUUID = player.getUniqueId();
        if (jumpingInfo.getWoolType().equals(FireballTNTJumpingEnums.FireballTNTJumpingWoolType.ENABLE)) {
            inventory.setItem(1, this.playerBlockMaterial.getItem().setAmount(32).build());
        }

        inventory.setItem(7, this.settingsItem.setDisplayName(Language.MessagesEnum.GAME_SETTINGS_ITEM_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.GAME_SETTINGS_ITEM_LORE.getStringList(playerUUID)).build());
        inventory.setItem(8, this.modeSelectorItem.setDisplayName(Language.MessagesEnum.MODE_SELECTOR_ITEM_NAME.getString(playerUUID)).setLore(Language.MessagesEnum.MODE_SELECTOR_ITEM_LORE.getStringList(playerUUID)).build());
    }

}
