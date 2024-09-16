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

    public void execute(Player var1, Position var2) {
        SetupSession var3 = SetupSession.get(var1.getUniqueId());
        var3.setRelativePosition(var2.getPos(), new RelativeLocation(var3.getSpawnLocation(), var2.getLocation(var1.getWorld())));
        var1.sendMessage(" ");
        var1.sendMessage(" ");
        var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
        var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_SET_SUCCESSFULLY.getString(var1.getUniqueId()).replace("[practiceName]", var3.getName()).replace("[posNumber]", String.valueOf(var2.getPos())));
        Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
    }

    public void executeWand(Player var1) {
        var1.getInventory().setItem(0, this.wand);
        var1.sendMessage(" ");
        var1.sendMessage(" ");
        var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
        var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_POS_WAND_RECEIVED.getString(var1.getUniqueId()));
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent var1) {
        if (var1.getItem() != null && var1.getItem().equals(this.wand)) {
            Player var2 = var1.getPlayer();
            Block var3 = var1.getClickedBlock();
            var1.setCancelled(true);
            switch (var1.getAction()) {
                case LEFT_CLICK_BLOCK:
                    this.execute(var2, new Position(1, var3.getX(), var3.getY(), var3.getZ()));
                    break;
                case RIGHT_CLICK_BLOCK:
                    this.execute(var2, new Position(2, var3.getX(), var3.getY(), var3.getZ()));
            }

        }
    }
}
