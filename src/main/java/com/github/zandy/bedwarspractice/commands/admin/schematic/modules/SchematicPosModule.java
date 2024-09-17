package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.material.Materials;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.Position;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

public class SchematicPosModule implements Listener {
    private static SchematicPosModule instance = null;
    private final ItemStack wand;

    private SchematicPosModule() {
        this.wand = Materials.WOODEN_AXE.getItem().setDisplayName(BambooUtils.colorize("&b&lSchematic POS Wand")).setLore(BambooUtils.colorize(Arrays.asList(" ", "&7Left-Click: &fSet 1st position", "&7Right-Click: &fSet 2nd position"))).enchantment().add(Enchantment.DURABILITY, 1).flag().add(ItemFlag.values()).build();
        BambooUtils.registerEvent(this);
    }

    public static SchematicPosModule getInstance() {
        if (instance == null) {
            instance = new SchematicPosModule();
        }

        return instance;
    }

    public void execute(Player player, Position position, boolean performCommand) {
        UUID playerUUID = player.getUniqueId();
        if (!performCommand) {
            player.performCommand("/pos" + position.getPos());
        }

        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(playerUUID));
        player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_SET.getString(playerUUID).replace("[posNumber]", String.valueOf(position.getPos())));
        Sounds.PLAYER_LEVELUP.getSound().play(player, 3.0F, 3.0F);
    }

    public void executeWand(Player player) {
        player.getInventory().setItem(0, this.wand);
        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(player.getUniqueId()));
        player.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_WAND_RECEIVED.getString(player.getUniqueId()));
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().isSimilar(this.wand)) {
            Player player = event.getPlayer();
            event.setCancelled(true);
            switch (event.getAction()) {
                case LEFT_CLICK_BLOCK:
                    this.execute(player, new Position(1, 0, 0, 0), true);
                    break;
                case RIGHT_CLICK_BLOCK:
                    this.execute(player, new Position(2, 0, 0, 0), true);
            }
        }
    }
}
