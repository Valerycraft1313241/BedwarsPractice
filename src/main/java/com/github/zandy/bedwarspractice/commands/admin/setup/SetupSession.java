package com.github.zandy.bedwarspractice.commands.admin.setup;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.utils.RelativeLocation;
import com.github.zandy.bedwarspractice.commands.admin.setup.modules.SetupSetModule;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.files.SetupData;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import com.sk89q.worldedit.EditSession;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class SetupSession {
    private static final HashMap<UUID, SetupSession> setupSessionMap = new HashMap<>();
    private final GameEngine.PracticeType practiceType;
    private final String name;
    private final Location spawnLocation;
    private final UUID uuid;
    private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));
    private RelativeLocation relativePosition1;
    private RelativeLocation relativePosition2;

    public SetupSession(Player player, GameEngine.PracticeType practiceType, String name, Location spawnLocation) {
        this.practiceType = practiceType;
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.uuid = player.getUniqueId();
        setupSessionMap.put(this.uuid, this);
    }

    public SetupSession(Player player, GameEngine.PracticeType practiceType, String name, Location spawnLocation, RelativeLocation relativePosition1, RelativeLocation relativePosition2) {
        this.practiceType = practiceType;
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.uuid = player.getUniqueId();
        this.relativePosition1 = relativePosition1;
        this.relativePosition2 = relativePosition2;
        setupSessionMap.put(this.uuid, this);
    }

    public static boolean exists(UUID uuid) {
        return setupSessionMap.containsKey(uuid);
    }

    public static SetupSession get(UUID uuid) {
        return setupSessionMap.get(uuid);
    }

    public static void remove(UUID uuid) {
        setupSessionMap.remove(uuid);
    }

    public void setRelativePosition(int positionNumber, RelativeLocation relativePosition) {
        if (positionNumber == 1) {
            this.relativePosition1 = relativePosition;
        } else {
            this.relativePosition2 = relativePosition;
        }
    }

    public boolean containsData() {
        Player player = Bukkit.getPlayer(this.uuid);
        if (!this.practiceType.equals(GameEngine.PracticeType.BRIDGING) && !this.practiceType.equals(GameEngine.PracticeType.MLG)) {
            return true;
        } else {
            byte positionNumber;
            if (this.relativePosition1 == null) {
                positionNumber = 1;
            } else if (this.relativePosition2 == null) {
                positionNumber = 2;
            } else {
                positionNumber = 0;
            }

            if (positionNumber == 0) {
                return true;
            } else {
                Language.MessagesEnum.COMMAND_ADMIN_SETUP_SAVE_POSITION_NOT_SET.getStringList(this.uuid).forEach((message) -> {
                    String formattedMessage = message.replace("[posNumber]", String.valueOf(positionNumber));
                    if (message.contains("/bwpa setup pos")) {
                        BambooUtils.sendTextComponent(player, formattedMessage, "/bwpa setup pos", Language.MessagesEnum.COMMAND_CLICK_TO_SUGGEST.getString(this.uuid), Action.SUGGEST_COMMAND);
                    } else {
                        player.sendMessage(formattedMessage);
                    }
                });
                return false;
            }
        }
    }

    public boolean save(EditSession editSession) {
        if (!this.containsData()) {
            return false;
        } else {
            BambooFile dataFile = new BambooFile(this.getName(), "Data");
            dataFile.set("Position-1.X", this.relativePosition1.getRelativeX());
            dataFile.set("Position-1.Y", this.relativePosition1.getRelativeY());
            dataFile.set("Position-1.Z", this.relativePosition1.getRelativeZ());
            dataFile.set("Position-2.X", this.relativePosition2.getRelativeX());
            dataFile.set("Position-2.Y", this.relativePosition2.getRelativeY());
            dataFile.set("Position-2.Z", this.relativePosition2.getRelativeZ());
            Player player = Bukkit.getPlayer(this.uuid);
            player.sendMessage(" ");
            player.sendMessage(" ");
            player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(this.uuid));
            player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_SAVE_SUCCESSFULLY.getString(this.uuid).replace("[practiceName]", this.name));
            editSession.undo(editSession);
            setupSessionMap.remove(this.uuid);
            ArrayList<String> remainingSchematics = new ArrayList<>(this.requiredSchematics);
            remainingSchematics.remove("FIREBALL-TNT-JUMPING");
            File dataFolder = new File("plugins/BedWarsPractice/Data");
            if (dataFolder.exists()) {
                Arrays.stream(dataFolder.listFiles()).forEach((file) -> remainingSchematics.remove(file.getName().replace(".yml", "")));
            }

            if (remainingSchematics.isEmpty()) {
                SetupData.getInstance().setSetupDoneConfigurations(true);
            }

            SetupSetModule.getSetupNameMap().remove(this.uuid);
            SetupSetModule.getInstance().removeOffset(this.uuid);
            player.getWorld().getSpawnLocation().clone().add(0.5D, 1.0D, 0.5D);
            return true;
        }
    }

    public String getName() {
        return this.name;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }
}
