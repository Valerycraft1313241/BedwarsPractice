package com.github.zandy.bedwarspractice.commands.admin.setup.modules;

import com.github.zandy.bamboolib.BambooLib;
import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.utils.RelativeLocation;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.admin.setup.SetupSession;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.support.BWPVector;
import com.github.zandy.bedwarspractice.support.WESupport;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.world.World;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SetupSetModule {
    private static final HashMap<UUID, String> setupNameMap = new HashMap<>();
    private static final HashMap<UUID, EditSession> setupEditSessionMap = new HashMap<>();
    private static SetupSetModule instance = null;
    private final HashMap<UUID, Integer> setupOffsetMap = new HashMap<>();
    private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));
    private final List<Integer> availableOffsets = new ArrayList<>();
    private final Location baseLocation = new Location(Bukkit.getWorld("bedwars_practice_schematic_creator"), 0.0D, 25.0D, 0.0D);
    private int nextOffset = 0;

    private SetupSetModule() {
    }

    public static SetupSetModule getInstance() {
        if (instance == null) {
            instance = new SetupSetModule();
        }

        return instance;
    }

    public static HashMap<UUID, String> getSetupNameMap() {
        return setupNameMap;
    }

    public static HashMap<UUID, EditSession> getSetupEditSessionMap() {
        return setupEditSessionMap;
    }

    public void execute(Player player, String practiceName) {
        UUID playerUUID = player.getUniqueId();
        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
        if (setupNameMap.containsKey(playerUUID)) {
            Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_ALREADY_IN_SETUP.getStringList(playerUUID).forEach((message) -> {
                if (message.contains("/bwpa")) {
                    BambooUtils.sendTextComponent(player, message.replace("[Name]", practiceName), "/bwpa setup quit", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(playerUUID), Action.RUN_COMMAND);
                } else {
                    player.sendMessage(message.replace("[Name]", practiceName));
                }
            });
        } else if (!this.requiredSchematics.contains(practiceName)) {
            Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_WRONG_PRACTICE_NAME.getStringList(playerUUID).forEach((message) -> {
                if (message.contains("/bwpa")) {
                    BambooUtils.sendTextComponent(player, message, "/bwpa setup list", Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(playerUUID), Action.RUN_COMMAND);
                } else {
                    player.sendMessage(message);
                }
            });
            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
        } else {
            label41:
            {
                if (BWPUtils.isLegacy()) {
                    if (!BWPLegacyAdapter.getInstance().getClipboardCache().containsKey(practiceName)) {
                        break label41;
                    }
                }

                Location setupLocation = this.getSetupLocation(playerUUID);
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_LOADING_SCHEMATIC.getString(playerUUID).replace("[schemName]", practiceName));
                double[] coordinates = (new BWPVector(setupLocation)).toArray();
                setupLocation.getWorld().getEntities().forEach((entity) -> {
                    if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
                        entity.remove();
                    }
                });
                World world = WESupport.getWEWorld(player.getWorld());
                getSetupEditSessionMap().put(player.getUniqueId(), BWPLegacyAdapter.getInstance().pasteSchematic(practiceName, world, coordinates));
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_SCHEMATIC_LOADED.getString(playerUUID));
                Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
                GameEngine.PracticeType practiceType = practiceName.contains("FIREBALL") ? GameEngine.PracticeType.FIREBALL_TNT_JUMPING : GameEngine.PracticeType.valueOf(practiceName.split("-")[0]);
                setupLocation.add(0.5D, 0.0D, 0.5D);
                if (GameEngine.getInstance().getPracticeFile().containsKey(practiceName)) {
                    BambooFile practiceFile = GameEngine.getInstance().getPracticeFile().get(practiceName);
                    new SetupSession(player, practiceType, practiceName, setupLocation, new RelativeLocation((float) practiceFile.getInt("Position-1.X"), (float) practiceFile.getInt("Position-1.Y"), (float) practiceFile.getInt("Position-1.Z")), new RelativeLocation((float) practiceFile.getInt("Position-2.X"), (float) practiceFile.getInt("Position-2.Y"), (float) practiceFile.getInt("Position-2.Z")));
                } else {
                    new SetupSession(player, practiceType, practiceName, setupLocation);
                }

                player.teleport(setupLocation);
                Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_INFO_TUTORIAL.getStringList(playerUUID).forEach((message) -> player.sendMessage(message.replace("[spigotLink]", "https://bit.ly/3f4lyvF").replace("[youtubeLink]", "https://bit.ly/3BSNJXn")));
                setupNameMap.put(player.getUniqueId(), practiceName);
                return;
            }

            Language.MessagesEnum.COMMAND_ADMIN_SETUP_SET_SCHEMATIC_NOT_FOUND.getStringList(playerUUID).forEach((message) -> {
                if (message.contains("/bwpa")) {
                    BambooUtils.sendTextComponent(player, message, "/bwpa schem save", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(playerUUID), Action.SUGGEST_COMMAND);
                } else {
                    player.sendMessage(message.replace("[schemName]", practiceName));
                }
            });
            Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
        }
    }

    public Location getSetupLocation(UUID playerUUID) {
        int offset = this.getNextOffset();
        if (!this.availableOffsets.isEmpty() && this.availableOffsets.get(0) != null) {
            offset = this.availableOffsets.get(0);
            this.availableOffsets.remove(offset);
            this.getSetupOffsetMap().put(playerUUID, offset);
        } else {
            short increment = 400;
            this.nextOffset += increment;
        }

        return this.getBaseLocation().clone().add(offset, 0.0D, 0.0D);
    }

    public void removeOffset(UUID playerUUID) {
        Bukkit.getScheduler().runTaskLater(BambooLib.getPluginInstance(), () -> {
            this.availableOffsets.add(this.getSetupOffsetMap().get(playerUUID));
            this.getSetupOffsetMap().remove(playerUUID);
        }, 500L);
    }

    public HashMap<UUID, Integer> getSetupOffsetMap() {
        return this.setupOffsetMap;
    }

    public Location getBaseLocation() {
        return this.baseLocation;
    }

    public int getNextOffset() {
        return this.nextOffset;
    }
}
