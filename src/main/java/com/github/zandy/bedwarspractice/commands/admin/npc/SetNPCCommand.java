package com.github.zandy.bedwarspractice.commands.admin.npc;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.features.npc.PracticeNPC;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.NPCStorage;
import com.github.zandy.bedwarspractice.files.language.Language;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.*;

public class SetNPCCommand extends SubCommand implements Listener {
    private static SetNPCCommand instance = null;
    private final List<UUID> toClickList = new ArrayList<>();
    private final HashMap<UUID, NPCStorage.NPCType> npcTypeMap = new HashMap<>();

    private SetNPCCommand() {
        super("setNPC", Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
        BambooUtils.registerEvent(this);
    }

    public static SetNPCCommand getInstance() {
        if (instance == null) {
            instance = new SetNPCCommand();
        }

        return instance;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getString());
        } else {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            if (!Lobby.getInstance().isSet()) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                List<String> messages = Language.MessagesEnum.PLUGIN_LOBBY_NOT_SET.getStringList(playerUUID);
                Objects.requireNonNull(player);
                messages.forEach(player::sendMessage);
                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
            } else if (args.length == 0) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                player.sendMessage(Language.MessagesEnum.COMMAND_WRONG_USAGE.getString(playerUUID));
                this.sendWrongUsageMessage(player);
                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
            } else {
                String npcTypeString = args[0].toLowerCase();
                byte npcTypeByte = -1;
                switch (npcTypeString.hashCode()) {
                    case 108200:
                        if (npcTypeString.equals("mlg")) {
                            npcTypeByte = 2;
                        }
                        break;
                    case 194511366:
                        if (npcTypeString.equals("bridging")) {
                            npcTypeByte = 1;
                        }
                        break;
                    case 1544803905:
                        if (npcTypeString.equals("default")) {
                            npcTypeByte = 0;
                        }
                        break;
                    case 1647511919:
                        if (npcTypeString.equals("fireballtntjumping")) {
                            npcTypeByte = 3;
                        }
                }

                NPCStorage.NPCType npcType;
                switch (npcTypeByte) {
                    case 0:
                    case 1:
                    case 2:
                        npcType = NPCStorage.NPCType.valueOf(args[0].toUpperCase());
                        break;
                    case 3:
                        npcType = NPCStorage.NPCType.FIREBALL_TNT_JUMPING;
                        break;
                    default:
                        this.sendWrongUsageMessage(player);
                        return;
                }

                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_CLICK_TO_APPLY.getString(playerUUID));
                this.toClickList.add(playerUUID);
                this.npcTypeMap.put(playerUUID, npcType);
                Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
            }
        }
    }

    private void sendFormattedComponent(Player player, String message, String command) {
        BambooUtils.sendTextComponent(player, message, "/bwpa setNPC " + command, Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(player.getUniqueId()), Action.RUN_COMMAND);
    }

    private void sendWrongUsageMessage(Player player) {
        Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_WRONG_USAGE.getStringList(player.getUniqueId()).forEach((message) -> {
            String lowerCaseMessage = message.toLowerCase();
            if (lowerCaseMessage.contains("default")) {
                this.sendFormattedComponent(player, message, "Default");
            } else if (lowerCaseMessage.contains("bridging")) {
                this.sendFormattedComponent(player, message, "Bridging");
            } else if (lowerCaseMessage.contains("mlg")) {
                this.sendFormattedComponent(player, message, "MLG");
            } else if (lowerCaseMessage.contains("fireball/tnt jumping")) {
                this.sendFormattedComponent(player, message, "FireballTntJumping");
            } else {
                player.sendMessage(message);
            }

        });
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    private void onNPCRightClick(NPCRightClickEvent event) {
        event.setCancelled(this.clickFunction(event.getClicker(), event.getClicker().getUniqueId(), event.getNPC().getId()));
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    private void onNPCLeftClick(NPCLeftClickEvent event) {
        event.setCancelled(this.clickFunction(event.getClicker(), event.getClicker().getUniqueId(), event.getNPC().getId()));
    }

    private boolean clickFunction(Player player, UUID playerUUID, int npcID) {
        if (!this.toClickList.contains(playerUUID)) {
            return false;
        } else {
            NPCStorage.NPCType npcType = this.npcTypeMap.get(playerUUID);
            if (NPCStorage.getInstance().contains(npcType, npcID)) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_ALREADY_EXISTS.getString(playerUUID));
                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
                return false;
            } else {
                NPCStorage.getInstance().add(npcType, npcID);
                PracticeNPC.getInstance().spawnNPC(CitizensAPI.getNPCRegistry().getById(npcID), npcID, npcType);
                this.toClickList.remove(playerUUID);
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_ADDED.getString(playerUUID));
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_SET_TYPE_ADDED.getString(playerUUID).replace("[practiceType]", npcType.getType().getString(playerUUID)));
                Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
                return true;
            }
        }
    }

    public boolean canSee(CommandSender sender) {
        Lobby lobby = Lobby.getInstance();
        return sender instanceof Player && this.hasPermission(sender) && lobby.isSet() && lobby.get().getWorld().equals(((Player) sender).getWorld());
    }
}
