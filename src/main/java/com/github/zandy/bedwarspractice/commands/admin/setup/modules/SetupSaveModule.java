package com.github.zandy.bedwarspractice.commands.admin.setup.modules;

import com.github.zandy.bedwarspractice.commands.admin.setup.SetupSession;
import org.bukkit.entity.Player;

public class SetupSaveModule {
    private static SetupSaveModule instance = null;

    private SetupSaveModule() {
    }

    public static SetupSaveModule getInstance() {
        if (instance == null) {
            instance = new SetupSaveModule();
        }

        return instance;
    }

    public void execute(Player player) {
        if (SetupSession.get(player.getUniqueId()).save(SetupSetModule.getSetupEditSessionMap().get(player.getUniqueId()))) {
            player.teleport(player.getWorld().getSpawnLocation().clone().add(0.5D, 1.0D, 0.5D));
        }

    }
}
