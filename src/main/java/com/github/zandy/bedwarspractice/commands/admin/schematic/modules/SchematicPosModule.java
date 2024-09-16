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

    public void execute(Player var1, Position var2, boolean var3) {
        UUID var4 = var1.getUniqueId();
        if (!var3) {
            var1.performCommand("/pos" + var2.getPos());
        }

        var1.sendMessage(" ");
        var1.sendMessage(" ");
        var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var4));
        var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_SET.getString(var4).replace("[posNumber]", String.valueOf(var2.getPos())));
        Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
    }

    public void executeWand(Player var1) {
        var1.getInventory().setItem(0, this.wand);
        var1.sendMessage(" ");
        var1.sendMessage(" ");
        var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
        var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_POS_WAND_RECEIVED.getString(var1.getUniqueId()));
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent var1) {
        if (var1.getItem() != null && var1.getItem().isSimilar(this.wand)) {
            Player var2 = var1.getPlayer();
            var1.setCancelled(true);
            switch (var1.getAction()) {
                case LEFT_CLICK_BLOCK:
                    this.execute(var2, new Position(1, 0, 0, 0), true);
                    break;
                case RIGHT_CLICK_BLOCK:
                    this.execute(var2, new Position(2, 0, 0, 0), true);
            }

        }
    }
}
