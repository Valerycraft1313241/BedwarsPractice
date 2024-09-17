package com.github.zandy.bedwarspractice.commands.admin.setup.modules;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.utils.RelativeLocation;
import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.commands.admin.setup.SetupSession;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.Position;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SetupPosModule implements Listener {
    private static SetupPosModule instance = null;
    private final ItemStack wand;

    private SetupPosModule() {
        this.wand = Materials.GOLDEN_AXE.getItem().setDisplayName(BambooUtils.colorize("&b&lSetup POS Wand")).setLore(BambooUtils.colorize(Arrays.asList(" ", "&7Left-Click: &fSet 1st position", "&7Right-Click: &fSet 2nd position"))).enchantment().add(Enchantment.DURABILITY, 1).flag().add(ItemFlag.values()).build();
        BambooUtils.registerEvent(this);
    }

    public static SetupPosModule getInstance() {
        if (instance == null) {
            instance = new SetupPosModule();
        }

        return instance;
    }

    public void execute(Player player, Position position) {
        SetupSession setupSession = SetupSession.get(player.getUniqueId());
        setupSession.setRelativePosition(position.getPos(), new RelativeLocation(setupSession.getSpawnLocation(), position.getLocation(player.getWorld())));
        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
        player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_SET_SUCCESSFULLY.getString(player.getUniqueId()).replace("[practiceName]", setupSession.getName()).replace("[posNumber]", String.valueOf(position.getPos())));
        Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
    }

    public void executeWand(Player player) {
        player.getInventory().setItem(0, this.wand);
        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
        player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_WAND_RECEIVED.getString(player.getUniqueId()));
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().equals(this.wand)) {
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            event.setCancelled(true);
            switch (event.getAction()) {
                case LEFT_CLICK_BLOCK:
                    this.execute(player, new Position(1, clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ()));
                    break;
                case RIGHT_CLICK_BLOCK:
                    this.execute(player, new Position(2, clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ()));
            }
        }
    }
}
