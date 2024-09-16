package com.github.zandy.bedwarspractice.engine;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.exceptions.BambooErrorException;
import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeChangeEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeJoinEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeQuitEvent;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;
import com.github.zandy.bedwarspractice.features.PracticeSpectator;
import com.github.zandy.bedwarspractice.features.npc.PlayerDataNPC;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.PracticeSettings;
import com.github.zandy.bedwarspractice.files.Settings;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.support.BWPVector;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.github.zandy.bedwarspractice.utils.InventoryCache;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.*;

public class GameEngine implements Listener {
    private static GameEngine instance = null;
    private final HashMap<String, BambooFile> practiceFile = new HashMap<>();
    private final HashMap<UUID, Integer> practicePlayerOffset = new HashMap<>();
    private final HashMap<UUID, EditSession> playerEditSessionMap = new HashMap<>();
    private final HashMap<UUID, GameEngine.PracticeType> practiceTypeMap = new HashMap<>();
    private final HashMap<UUID, Location> practiceLocationMap = new HashMap<>();
    private final HashMap<UUID, Integer> lastOffset = new HashMap<>();
    private final HashMap<UUID, List<UUID>> spectators = new HashMap<>();
    private final List<Integer> availableOffsets = new ArrayList<>();
    private final Location baseLocation = new Location(WorldEngine.getInstance().getPracticeWorld(), -2.999986E7D, 100.0D, 0.0D);
    private final Calendar calendar = Calendar.getInstance();
    private final List<UUID> pending = new ArrayList<>();
    private final List<Material> restrictedInteract;
    private int nextOffset = 0;

    private GameEngine() {
        this.restrictedInteract = PracticeSettings.GameSettingsEnum.INTERACT_RESTRICTED_BLOCKS.getMaterials();
    }

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }

        return instance;
    }

    public void init() {
        File var1 = new File("plugins/BedWarsPractice/Data/");
        if (var1.exists()) {
            Arrays.stream(var1.listFiles()).forEach((var1x) -> {
                String var2 = var1x.getName().replace(".yml", "");
                this.practiceFile.put(var2, new BambooFile(var2, "Data"));
            });
        }

        this.rebuildSchematicCache();
        BambooUtils.registerEvent(this);
    }

    public void rebuildSchematicCache() {
        File var1 = new File("plugins/BedWarsPractice/Schematics/");
        if (var1.exists()) {
            Arrays.stream(var1.listFiles()).forEach((var1x) -> {
                String var2 = var1x.getName();
                if (var2.contains("BRIDGING") || var2.contains("MLG") || var2.contains("FIREBALL-TNT-JUMPING")) {
                    try {
                        BWPLegacyAdapter.getInstance().addSchematicToCache(var2, var1x);
                    } catch (Exception var4) {
                        throw new BambooErrorException(var4, this.getClass(), Arrays.asList("Schematic Cache rebuild failed!", "One ore more files are unreadable or corrupt"));
                    }
                }

            });
        }
    }

    public void serverReload() {
        if (Lobby.getInstance().isSet()) {
            Location var1 = Lobby.getInstance().get();
            this.getPracticeTypeMap().keySet().forEach((var1x) -> {
                Bukkit.getPlayer(var1x).getInventory().clear();
                ScoreboardEngine.getInstance().unload(var1x);
                Bukkit.getPlayer(var1x).teleport(var1);
            });
            PracticeSpectator.getPracticeSpectators().keySet().forEach((var1x) -> {
                Bukkit.getPlayer(var1x).getInventory().clear();
                Bukkit.getPlayer(var1x).teleport(var1);
                Bukkit.getPlayer(var1x).setGameMode(GameMode.SURVIVAL);
            });
        }
    }

    public Location getPracticeLocation(UUID var1) {
        Integer var2 = this.getNextOffset();
        boolean var3 = true;
        if (!this.availableOffsets.isEmpty() && this.availableOffsets.get(0) != null) {
            if (this.getLastOffset().containsKey(var1)) {
                Integer var4 = this.getLastOffset().get(var1);
                if (this.availableOffsets.get(0).equals(var4)) {
                    ArrayList<Integer> var5 = new ArrayList<>(this.availableOffsets);
                    var5.remove(var4);
                    if (!var5.isEmpty() && var5.get(0) != null) {
                        var2 = var5.get(0);
                        this.availableOffsets.remove(var2);
                        var3 = false;
                    }
                } else {
                    var2 = this.availableOffsets.get(0);
                    this.availableOffsets.remove(var2);
                    var3 = false;
                }
            } else {
                var2 = this.availableOffsets.get(0);
                this.availableOffsets.remove(var2);
                var3 = false;
            }
        }

        if (var3) {
            var2 = this.nextOffset += 350;
        }

        this.practicePlayerOffset.put(var1, var2);
        return this.getBaseLocation().clone().add((double) var2, 0.0D, 0.0D);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent var1) {
        if (var1.getBlock().getWorld().equals(WorldEngine.getInstance().getPracticeWorld())) {
            var1.setCancelled(true);
        }

    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent var1) {
        Block var2 = var1.getClickedBlock();
        if (var2 != null && var2.getWorld().equals(WorldEngine.getInstance().getPracticeWorld())) {
            if (this.restrictedInteract.contains(var2.getType()) || var2.getType().name().contains("BED")) {
                var1.setCancelled(true);
            }

        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent var1) {
        Player var2 = var1.getPlayer();
        if (var2.getWorld().equals(WorldEngine.getInstance().getPracticeWorld())) {
            Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(var2));
        }

        if (InventoryCache.isInstantiated()) {
            InventoryCache.getInstance().remove(var2);
        }

        if (PracticeSpectator.isSpectating(var2.getUniqueId())) {
            PracticeSpectator.remove(var2.getUniqueId());
            var2.setGameMode(GameMode.SURVIVAL);
        }

    }

    @EventHandler
    private void onPracticeQuit(PracticeQuitEvent var1) {
        Player var2 = var1.getPlayer();
        if (this.getPracticeTypeMap().containsKey(var2.getUniqueId())) {
            switch (this.getPracticeTypeMap().get(var2.getUniqueId())) {
                case BRIDGING:
                    BridgingMode.getInstance().quit(var2);
                    break;
                case MLG:
                    MLGMode.getInstance().quit(var2);
                    break;
                case FIREBALL_TNT_JUMPING:
                    FireballTNTJumpingMode.getInstance().quit(var2);
            }

            Location var3 = Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean() ? Lobby.getInstance().get() : null;
            this.getSpectators().get(var2.getUniqueId()).forEach((var1x) -> {
                Player var2s = Bukkit.getPlayer(var1x);
                var2s.getInventory().clear();
                if (var3 != null) {
                    var2s.teleport(var3);
                }

                PracticeSpectator.remove(var1x);
                var2s.setGameMode(GameMode.SURVIVAL);
                var2s.sendMessage(Language.MessagesEnum.SPECTATE_PLAYER_QUIT.getString(var1x));
            });
            this.getSpectators().remove(var2.getUniqueId());
        }
    }

    @EventHandler
    private void onPracticeSwitch(PracticeJoinEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (this.getSpectators().containsKey(var2)) {
            GameEngine var3 = getInstance();
            this.getSpectators().get(var2).forEach((var4) -> {
                Player var5 = Bukkit.getPlayer(var4);
                var5.teleport(this.getPracticeLocationMap().get(var2).clone().add(0.0D, 1.5D, 0.0D));
                var5.sendMessage(" ");
                var5.sendMessage(" ");
                Language.MessagesEnum.SPECTATE_PRACTICE_MODE_CHANGED.getStringList(var4).forEach((var5x) -> {
                    if (var5x.contains("[player_name]") && var5x.contains("[practice_mode]")) {
                        var5.sendMessage(var5x.replace("[player_name]", var1.getPlayer().getName()).replace("[practice_mode]", var3.getPracticeTypeMap().get(var2).getDisplayName(var4)));
                    } else if (var5x.contains("[player_name]")) {
                        var5.sendMessage(var5x.replace("[player_name]", var1.getPlayer().getName()));
                    } else if (var5x.contains("[practice_mode]")) {
                        var5.sendMessage(var5x.replace("[practice_mode]", var3.getPracticeTypeMap().get(var2).getDisplayName(var4)));
                    } else if (var5x.contains("[current_settings]")) {
                        var5.sendMessage(var5x.replace("[current_settings]", var3.getCurrentGameSettings(var2, var4)));
                    }

                });
            });
        }
    }

    @EventHandler
    private void onPracticeChangeSettings(PracticeChangeEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (this.getSpectators().containsKey(var2)) {
            GameEngine var3 = getInstance();
            this.getSpectators().get(var2).forEach((var4) -> {
                Player var5 = Bukkit.getPlayer(var4);
                var5.teleport(this.getPracticeLocationMap().get(var2).clone().add(0.0D, 1.5D, 0.0D));
                var5.sendMessage(" ");
                var5.sendMessage(" ");
                Language.MessagesEnum.SPECTATE_SETTINGS_CHANGED.getStringList(var4).forEach((var5x) -> {
                    if (var5x.contains("[player_name]")) {
                        var5.sendMessage(var5x.replace("[player_name]", var1.getPlayer().getName()));
                    }

                    if (var5x.contains("[practice_mode]")) {
                        var5.sendMessage(var5x.replace("[practice_mode]", var3.getPracticeTypeMap().get(var2).getDisplayName(var4)));
                    }

                    if (var5x.contains("[current_settings]")) {
                        var5.sendMessage(var5x.replace("[current_settings]", var3.getCurrentGameSettings(var2, var4)));
                    }

                });
            });
        }
    }

    @EventHandler
    private void onGameModeChange(PlayerGameModeChangeEvent var1) {
        UUID var2 = var1.getPlayer().getUniqueId();
        if (PracticeSpectator.isSpectating(var2)) {
            var1.getPlayer().sendMessage(Language.MessagesEnum.SPECTATE_GAMEMODE_NOT_CHANGEABLE.getString(var2));
            var1.setCancelled(true);
        }
    }

    @EventHandler
    private void onWorldChange(PlayerChangedWorldEvent var1) {
        if (this.getPracticeTypeMap().containsKey(var1.getPlayer().getUniqueId()) && var1.getFrom().getName().equals("bedwars_practice")) {
            Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(var1.getPlayer()));
        }

    }

    public String getCurrentGameSettings(UUID var1, UUID var2) {
        String var3 = "";
        String var4 = Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_COMMA.getString(var2);
        switch (this.getPracticeTypeMap().get(var1)) {
            case BRIDGING:
                BridgingInfo var8 = BridgingInfo.get(var1);
                var3 = Language.MessagesEnum.SPECTATE_SETTINGS_BRIDGING_BLOCKS.getString(var2).replace("[block_number]", var8.getBlocksType().getDisplayName(var2)) + var4 + Language.MessagesEnum.SPECTATE_SETTINGS_BRIDGING_ANGLE.getString(var2).replace("[angle_type]", var8.getAngleType().getDisplayName(var2)) + var4 + Language.MessagesEnum.SPECTATE_SETTINGS_BRIDGING_ELEVATION.getString(var2).replace("[elevation_type]", var8.getLevelType().getDisplayName(var2));
                break;
            case MLG:
                MLGInfo var7 = MLGInfo.get(var1);
                var3 = Language.MessagesEnum.SPECTATE_SETTINGS_MLG_SIZE.getString(var2).replace("[size_type]", var7.getSizeType().getDisplayName(var2)) + var4 + Language.MessagesEnum.SPECTATE_SETTINGS_MLG_HEIGHT.getString(var2).replace("[height_type]", var7.getHeightType().getDisplayName(var2)) + var4 + Language.MessagesEnum.SPECTATE_SETTINGS_MLG_POSITION.getString(var2).replace("[position_type]", var7.getPositionType().getDisplayName(var2)) + var4 + Language.MessagesEnum.SPECTATE_SETTINGS_MLG_TALLNESS.getString(var2).replace("[tallness_type]", String.valueOf(var7.getTallnessType().getValue()));
                break;
            case FIREBALL_TNT_JUMPING:
                FireballTNTJumpingInfo var5 = FireballTNTJumpingInfo.get(var1);
                int var6 = var5.getAmountType().getValue();
                var3 = Language.MessagesEnum.SPECTATE_SETTINGS_FIREBALL_TNT_JUMPING_ITEM_TYPE_AMOUNT.getString(var2).replace("[item_amount]", String.valueOf(var6)).replace("[item_type]", Language.MessagesEnum.valueOf("Spectate.Settings.Display.Fireball-TNT-Jumping.Item." + var5.getItemType().getValueName() + "." + (var6 == 1 ? "Singular" : "Plural")).getString(var2)) + var4 + Language.MessagesEnum.SPECTATE_SETTINGS_FIREBALL_TNT_JUMPING_WOOL.getString(var2).replace("[wool_type]", var5.getWoolType().getDisplayName(var2));
        }

        return var3;
    }

    public Location initMode(Player var1, UUID var2, String var3, GameEngine.PracticeType var4) {
        this.getPending().add(var2);
        Location var5 = this.getPracticeLocation(var2);
        Location var6 = var5.clone().add(0.5D, 0.0D, 0.5D);
        double[] var7 = (new BWPVector(var5)).toArray();
        World var8 = WorldEngine.getInstance().getPracticeWEWorld();
        this.getPlayerEditSessionMap().put(var2, BWPLegacyAdapter.getInstance().pasteSchematic(var3, var8, var7));
        var1.getInventory().clear();
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.getPending().remove(var2);
            var1.teleport(var6);
            if (!this.spectators.containsKey(var2)) {
                this.spectators.put(var2, new ArrayList<>());
            }

        }, 5L);
        this.getPracticeTypeMap().put(var2, var4);
        this.getPracticeLocationMap().put(var2, var6);
        PlayerDataNPC.get(var2).removeAll();
        return var5;
    }

    public void clear(UUID var1, boolean var2, boolean var3) {
        if (var2) {
            this.getPracticeTypeMap().remove(var1);
        }

        if (this.getPracticePlayerOffset().containsKey(var1) && var3) {
            int var4 = this.getPracticePlayerOffset().get(var1);
            this.getLastOffset().put(var1, var4);
            this.getAvailableOffsets().add(var4);
            this.getPracticePlayerOffset().remove(var1);
        }

        EditSession var5 = this.getPlayerEditSessionMap().get(var1);
        var5.undo(var5);
        this.getPlayerEditSessionMap().remove(var1);
        this.getPracticeLocationMap().remove(var1);
    }

    public Block getStandingBlock(Location var1) {
        Block var2 = var1.clone().add(0.3D, -1.0D, -0.3D).getBlock();
        if (!var2.getType().equals(Material.AIR)) {
            return var2;
        } else {
            Block var3 = var1.clone().add(-0.3D, -1.0D, -0.3D).getBlock();
            if (!var3.getType().equals(Material.AIR)) {
                return var3;
            } else {
                Block var4 = var1.clone().add(0.3D, -1.0D, 0.3D).getBlock();
                if (!var4.getType().equals(Material.AIR)) {
                    return var4;
                } else {
                    Block var5 = var1.clone().add(-0.3D, -1.0D, 0.3D).getBlock();
                    return !var5.getType().equals(Material.AIR) ? var5 : var1.getBlock();
                }
            }
        }
    }

    public HashMap<String, BambooFile> getPracticeFile() {
        return this.practiceFile;
    }

    public HashMap<UUID, Integer> getPracticePlayerOffset() {
        return this.practicePlayerOffset;
    }

    public HashMap<UUID, EditSession> getPlayerEditSessionMap() {
        return this.playerEditSessionMap;
    }

    public HashMap<UUID, GameEngine.PracticeType> getPracticeTypeMap() {
        return this.practiceTypeMap;
    }

    public HashMap<UUID, Location> getPracticeLocationMap() {
        return this.practiceLocationMap;
    }

    public HashMap<UUID, Integer> getLastOffset() {
        return this.lastOffset;
    }

    public HashMap<UUID, List<UUID>> getSpectators() {
        return this.spectators;
    }

    public List<Integer> getAvailableOffsets() {
        return this.availableOffsets;
    }

    public Location getBaseLocation() {
        return this.baseLocation;
    }

    public int getNextOffset() {
        return this.nextOffset;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public List<UUID> getPending() {
        return this.pending;
    }

    public enum PracticeType {
        BRIDGING(Language.MessagesEnum.PRACTICE_NAME_BRIDGING),
        MLG(Language.MessagesEnum.PRACTICE_NAME_MLG),
        FIREBALL_TNT_JUMPING(Language.MessagesEnum.PRACTICE_NAME_FIREBALL_TNT_JUMPING);

        private final Language.MessagesEnum displayName;

        PracticeType(Language.MessagesEnum var3) {
            this.displayName = var3;
        }

        public String getDisplayName(UUID var1) {
            return this.displayName.getString(var1);
        }

    }
}
