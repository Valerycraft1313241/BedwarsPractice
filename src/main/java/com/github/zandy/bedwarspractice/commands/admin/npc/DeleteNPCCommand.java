package com.github.zandy.bedwarspractice.commands.admin.npc;

import com.github.zandy.bamboolib.command.SubCommand;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.BedWarsPracticeAdminCommand;
import com.github.zandy.bedwarspractice.features.npc.PracticeNPC;
import com.github.zandy.bedwarspractice.files.Lobby;
import com.github.zandy.bedwarspractice.files.NPCStorage;
import com.github.zandy.bedwarspractice.files.language.Language;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class DeleteNPCCommand extends SubCommand implements Listener {
    private static DeleteNPCCommand instance = null;
    private final List<UUID> toClickList = new ArrayList<>();
    private final HashMap<UUID, NPCStorage.NPCType> npcTypeMap = new HashMap<>();

    private DeleteNPCCommand() {
        super("deleteNPC", Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_DESCRIPTION.getString(), BedWarsPracticeAdminCommand.getPermissions());
        BambooUtils.registerEvent(this);
    }

    public static DeleteNPCCommand getInstance() {
        if (instance == null) {
            instance = new DeleteNPCCommand();
        }

        return instance;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.MessagesEnum.PLUGIN_NO_CONSOLE.getPath());
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
            } else {
                boolean noNPCs = true;
                NPCStorage.NPCType[] npcTypes = NPCStorage.NPCType.values();

                for (NPCStorage.NPCType npcType : npcTypes) {
                    if (!NPCStorage.getInstance().isEmpty(npcType)) {
                        noNPCs = false;
                    }
                }

                if (noNPCs) {
                    player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                    player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_NO_NPCS_SET.getString());
                } else {
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                    player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_CLICK_TO_DELETE.getString(playerUUID));
                    this.toClickList.add(playerUUID);
                    Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
                }
            }
        }
    }

    @EventHandler
    private void onNPCRightClick(NPCRightClickEvent event) {
        this.clickFunction(event.getClicker(), event.getClicker().getUniqueId(), event.getNPC().getId());
    }

    @EventHandler
    private void onNPCLeftClick(NPCLeftClickEvent event) {
        this.clickFunction(event.getClicker(), event.getClicker().getUniqueId(), event.getNPC().getId());
    }

    private void clickFunction(Player player, UUID playerUUID, int npcID) {
        if (this.toClickList.contains(playerUUID)) {
            boolean isPracticeNPC = false;
            NPCStorage.NPCType npcType = null;
            NPCStorage.NPCType[] npcTypes = NPCStorage.NPCType.values();

            for (NPCStorage.NPCType type : npcTypes) {
                if (NPCStorage.getInstance().getIDList(type).contains(npcID)) {
                    isPracticeNPC = true;
                    npcType = type;
                    break;
                }
            }

            if (!isPracticeNPC) {
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_NOT_PRACTICE.getString(playerUUID));
                Sounds.VILLAGER_NO.getSound().play(player, 3.0F, 1.0F);
            } else {
                NPCStorage.getInstance().remove(npcType, npcID);
                PracticeNPC.getInstance().removeNPC(npcID);
                this.toClickList.remove(playerUUID);
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
                player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_NPC_DELETE_DELETED.getString(playerUUID));
                Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
            }
        }
    }

    public boolean canSee(CommandSender sender) {
        Lobby lobby = Lobby.getInstance();
        return sender instanceof Player && this.hasPermission(sender) && lobby.isSet() && lobby.get().getWorld().equals(((Player) sender).getWorld());
    }
}
