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
        File dataFile = new File("plugins/BedWarsPractice/Data/");
        if (dataFile.exists()) {
            Arrays.stream(dataFile.listFiles()).forEach((file) -> {
                String fileName = file.getName().replace(".yml", "");
                this.practiceFile.put(fileName, new BambooFile(fileName, "Data"));
            });
        }

        this.rebuildSchematicCache();
        BambooUtils.registerEvent(this);
    }

    public void rebuildSchematicCache() {
        File schematicsFile = new File("plugins/BedWarsPractice/Schematics/");
        if (schematicsFile.exists()) {
            Arrays.stream(schematicsFile.listFiles()).forEach((schematic) -> {
                String schematicName = schematic.getName();
                if (schematicName.contains("BRIDGING") || schematicName.contains("MLG") || schematicName.contains("FIREBALL-TNT-JUMPING")) {
                    try {
                        BWPLegacyAdapter.getInstance().addSchematicToCache(schematicName, schematic);
                    } catch (Exception e) {
                        throw new BambooErrorException(e, this.getClass(), Arrays.asList("Schematic Cache rebuild failed!", "One ore more files are unreadable or corrupt"));
                    }
                }

            });
        }
    }

    public void serverReload() {
        if (Lobby.getInstance().isSet()) {
            Location lobbyLocation = Lobby.getInstance().get();
            this.getPracticeTypeMap().keySet().forEach((uuid) -> {
                Bukkit.getPlayer(uuid).getInventory().clear();
                ScoreboardEngine.getInstance().unload(uuid);
                Bukkit.getPlayer(uuid).teleport(lobbyLocation);
            });
            PracticeSpectator.getPracticeSpectators().keySet().forEach((uuid) -> {
                Bukkit.getPlayer(uuid).getInventory().clear();
                Bukkit.getPlayer(uuid).teleport(lobbyLocation);
                Bukkit.getPlayer(uuid).setGameMode(GameMode.SURVIVAL);
            });
        }
    }

    public Location getPracticeLocation(UUID uuid) {
        Integer offset = this.getNextOffset();
        boolean bool = true;
        if (!this.availableOffsets.isEmpty() && this.availableOffsets.get(0) != null) {
            if (this.getLastOffset().containsKey(uuid)) {
                Integer lastOffset = this.getLastOffset().get(uuid);
                if (this.availableOffsets.get(0).equals(lastOffset)) {
                    ArrayList<Integer> availableOffsets = new ArrayList<>(this.availableOffsets);
                    availableOffsets.remove(lastOffset);
                    if (!availableOffsets.isEmpty() && availableOffsets.get(0) != null) {
                        offset = availableOffsets.get(0);
                        this.availableOffsets.remove(offset);
                        bool = false;
                    }
                } else {
                    offset = this.availableOffsets.get(0);
                    this.availableOffsets.remove(offset);
                    bool = false;
                }
            } else {
                offset = this.availableOffsets.get(0);
                this.availableOffsets.remove(offset);
                bool = false;
            }
        }

        if (bool) {
            offset = this.nextOffset += 350;
        }

        this.practicePlayerOffset.put(uuid, offset);
        return this.getBaseLocation().clone().add((double) offset, 0.0D, 0.0D);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getWorld().equals(WorldEngine.getInstance().getPracticeWorld())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getWorld().equals(WorldEngine.getInstance().getPracticeWorld())) {
            if (this.restrictedInteract.contains(block.getType()) || block.getType().name().contains("BED")) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().equals(WorldEngine.getInstance().getPracticeWorld())) {
            Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(player));
        }

        if (InventoryCache.isInstantiated()) {
            InventoryCache.getInstance().remove(player);
        }

        if (PracticeSpectator.isSpectating(player.getUniqueId())) {
            PracticeSpectator.remove(player.getUniqueId());
            player.setGameMode(GameMode.SURVIVAL);
        }

    }

    @EventHandler
    private void onPracticeQuit(PracticeQuitEvent event) {
        Player player = event.getPlayer();
        if (this.getPracticeTypeMap().containsKey(player.getUniqueId())) {
            switch (this.getPracticeTypeMap().get(player.getUniqueId())) {
                case BRIDGING:
                    BridgingMode.getInstance().quit(player);
                    break;
                case MLG:
                    MLGMode.getInstance().quit(player);
                    break;
                case FIREBALL_TNT_JUMPING:
                    FireballTNTJumpingMode.getInstance().quit(player);
            }

            Location lobbyLocation = Settings.SettingsEnum.PRACTICE_PROXY_ENABLED.getBoolean() ? Lobby.getInstance().get() : null;
            this.getSpectators().get(player.getUniqueId()).forEach((spectator) -> {
                Player player1 = Bukkit.getPlayer(spectator);
                player1.getInventory().clear();
                if (lobbyLocation != null) {
                    player1.teleport(lobbyLocation);
                }

                PracticeSpectator.remove(spectator);
                player1.setGameMode(GameMode.SURVIVAL);
                player1.sendMessage(Language.MessagesEnum.SPECTATE_PLAYER_QUIT.getString(spectator));
            });
            this.getSpectators().remove(player.getUniqueId());
        }
    }

    @EventHandler
    private void onPracticeSwitch(PracticeJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (this.getSpectators().containsKey(uuid)) {
            GameEngine gameEngine = getInstance();
            this.getSpectators().get(uuid).forEach((spectator) -> {
                Player player1 = Bukkit.getPlayer(spectator);
                player1.teleport(this.getPracticeLocationMap().get(uuid).clone().add(0.0D, 1.5D, 0.0D));
                player1.sendMessage(" ");
                player1.sendMessage(" ");
                Language.MessagesEnum.SPECTATE_PRACTICE_MODE_CHANGED.getStringList(spectator).forEach((s) -> {
                    if (s.contains("[player_name]") && s.contains("[practice_mode]")) {
                        player1.sendMessage(s.replace("[player_name]", event.getPlayer().getName()).replace("[practice_mode]", gameEngine.getPracticeTypeMap().get(uuid).getDisplayName(spectator)));
                    } else if (s.contains("[player_name]")) {
                        player1.sendMessage(s.replace("[player_name]", event.getPlayer().getName()));
                    } else if (s.contains("[practice_mode]")) {
                        player1.sendMessage(s.replace("[practice_mode]", gameEngine.getPracticeTypeMap().get(uuid).getDisplayName(spectator)));
                    } else if (s.contains("[current_settings]")) {
                        player1.sendMessage(s.replace("[current_settings]", gameEngine.getCurrentGameSettings(uuid, spectator)));
                    }

                });
            });
        }
    }

    @EventHandler
    private void onPracticeChangeSettings(PracticeChangeEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (this.getSpectators().containsKey(uuid)) {
            GameEngine gameEngine = getInstance();
            this.getSpectators().get(uuid).forEach((spectator) -> {
                Player player1 = Bukkit.getPlayer(spectator);
                player1.teleport(this.getPracticeLocationMap().get(uuid).clone().add(0.0D, 1.5D, 0.0D));
                player1.sendMessage(" ");
                player1.sendMessage(" ");
                Language.MessagesEnum.SPECTATE_SETTINGS_CHANGED.getStringList(spectator).forEach((s) -> {
                    if (s.contains("[player_name]")) {
                        player1.sendMessage(s.replace("[player_name]", event.getPlayer().getName()));
                    }

                    if (s.contains("[practice_mode]")) {
                        player1.sendMessage(s.replace("[practice_mode]", gameEngine.getPracticeTypeMap().get(uuid).getDisplayName(spectator)));
                    }

                    if (s.contains("[current_settings]")) {
                        player1.sendMessage(s.replace("[current_settings]", gameEngine.getCurrentGameSettings(uuid, spectator)));
                    }

                });
            });
        }
    }

    @EventHandler
    private void onGameModeChange(PlayerGameModeChangeEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (PracticeSpectator.isSpectating(playerUUID)) {
            event.getPlayer().sendMessage(Language.MessagesEnum.SPECTATE_GAMEMODE_NOT_CHANGEABLE.getString(playerUUID));
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onWorldChange(PlayerChangedWorldEvent event) {
        if (this.getPracticeTypeMap().containsKey(event.getPlayer().getUniqueId()) && event.getFrom().getName().equals("bedwars_practice")) {
            Bukkit.getPluginManager().callEvent(new PracticeQuitEvent(event.getPlayer()));
        }
    }

    public String getCurrentGameSettings(UUID playerUUID, UUID spectatorUUID) {
        String settings = "";
        String separator = Language.MessagesEnum.SPECTATE_SETTINGS_DISPLAY_COMMA.getString(spectatorUUID);
        switch (this.getPracticeTypeMap().get(playerUUID)) {
            case BRIDGING:
                BridgingInfo bridgingInfo = BridgingInfo.get(playerUUID);
                settings = Language.MessagesEnum.SPECTATE_SETTINGS_BRIDGING_BLOCKS.getString(spectatorUUID).replace("[block_number]", bridgingInfo.getBlocksType().getDisplayName(spectatorUUID)) + separator + Language.MessagesEnum.SPECTATE_SETTINGS_BRIDGING_ANGLE.getString(spectatorUUID).replace("[angle_type]", bridgingInfo.getAngleType().getDisplayName(spectatorUUID)) + separator + Language.MessagesEnum.SPECTATE_SETTINGS_BRIDGING_ELEVATION.getString(spectatorUUID).replace("[elevation_type]", bridgingInfo.getLevelType().getDisplayName(spectatorUUID));
                break;
            case MLG:
                MLGInfo mlgInfo = MLGInfo.get(playerUUID);
                settings = Language.MessagesEnum.SPECTATE_SETTINGS_MLG_SIZE.getString(spectatorUUID).replace("[size_type]", mlgInfo.getSizeType().getDisplayName(spectatorUUID)) + separator + Language.MessagesEnum.SPECTATE_SETTINGS_MLG_HEIGHT.getString(spectatorUUID).replace("[height_type]", mlgInfo.getHeightType().getDisplayName(spectatorUUID)) + separator + Language.MessagesEnum.SPECTATE_SETTINGS_MLG_POSITION.getString(spectatorUUID).replace("[position_type]", mlgInfo.getPositionType().getDisplayName(spectatorUUID)) + separator + Language.MessagesEnum.SPECTATE_SETTINGS_MLG_TALLNESS.getString(spectatorUUID).replace("[tallness_type]", String.valueOf(mlgInfo.getTallnessType().getValue()));
                break;
            case FIREBALL_TNT_JUMPING:
                FireballTNTJumpingInfo fireballTNTJumpingInfo = FireballTNTJumpingInfo.get(playerUUID);
                int itemAmount = fireballTNTJumpingInfo.getAmountType().getValue();
                settings = Language.MessagesEnum.SPECTATE_SETTINGS_FIREBALL_TNT_JUMPING_ITEM_TYPE_AMOUNT.getString(spectatorUUID).replace("[item_amount]", String.valueOf(itemAmount)).replace("[item_type]", Language.MessagesEnum.valueOf("Spectate.Settings.Display.Fireball-TNT-Jumping.Item." + fireballTNTJumpingInfo.getItemType().getValueName() + "." + (itemAmount == 1 ? "Singular" : "Plural")).getString(spectatorUUID)) + separator + Language.MessagesEnum.SPECTATE_SETTINGS_FIREBALL_TNT_JUMPING_WOOL.getString(spectatorUUID).replace("[wool_type]", fireballTNTJumpingInfo.getWoolType().getDisplayName(spectatorUUID));
        }

        return settings;
    }

    public Location initMode(Player player, UUID playerUUID, String schematicName, GameEngine.PracticeType practiceType) {
        this.getPending().add(playerUUID);
        Location practiceLocation = this.getPracticeLocation(playerUUID);
        Location teleportLocation = practiceLocation.clone().add(0.5D, 0.0D, 0.5D);
        double[] coordinates = (new BWPVector(practiceLocation)).toArray();
        World world = WorldEngine.getInstance().getPracticeWEWorld();
        this.getPlayerEditSessionMap().put(playerUUID, BWPLegacyAdapter.getInstance().pasteSchematic(schematicName, world, coordinates));
        player.getInventory().clear();
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.getPending().remove(playerUUID);
            player.teleport(teleportLocation);
            if (!this.spectators.containsKey(playerUUID)) {
                this.spectators.put(playerUUID, new ArrayList<>());
            }

        }, 5L);
        this.getPracticeTypeMap().put(playerUUID, practiceType);
        this.getPracticeLocationMap().put(playerUUID, teleportLocation);
        return practiceLocation;
    }


    public void clear(UUID uuid, boolean removePracticeMap, boolean removeOffset) {
        if (removePracticeMap) {
            this.getPracticeTypeMap().remove(uuid);
        }

        if (this.getPracticePlayerOffset().containsKey(uuid) && removeOffset) {
            int practiceOffset = this.getPracticePlayerOffset().get(uuid);
            this.getLastOffset().put(uuid, practiceOffset);
            this.getAvailableOffsets().add(practiceOffset);
            this.getPracticePlayerOffset().remove(uuid);
        }

        EditSession editSession = this.getPlayerEditSessionMap().get(uuid);
        editSession.undo(editSession);
        this.getPlayerEditSessionMap().remove(uuid);
        this.getPracticeLocationMap().remove(uuid);
    }

    public Block getStandingBlock(Location location) {
        Block block = location.clone().add(0.3D, -1.0D, -0.3D).getBlock();
        if (!block.getType().equals(Material.AIR)) {
            return block;
        } else {
            Block block1 = location.clone().add(-0.3D, -1.0D, -0.3D).getBlock();
            if (!block1.getType().equals(Material.AIR)) {
                return block1;
            } else {
                Block block2 = location.clone().add(0.3D, -1.0D, 0.3D).getBlock();
                if (!block2.getType().equals(Material.AIR)) {
                    return block2;
                } else {
                    Block block3 = location.clone().add(-0.3D, -1.0D, 0.3D).getBlock();
                    return !block3.getType().equals(Material.AIR) ? block3 : location.getBlock();
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

        PracticeType(Language.MessagesEnum messagesEnum) {
            this.displayName = messagesEnum;
        }

        public String getDisplayName(UUID uuid) {
            return this.displayName.getString(uuid);
        }

    }
}
