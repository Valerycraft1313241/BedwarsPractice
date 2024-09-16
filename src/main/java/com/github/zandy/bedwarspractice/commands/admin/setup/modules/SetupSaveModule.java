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

    public void execute(Player var1) {
        if (SetupSession.get(var1.getUniqueId()).save(SetupSetModule.getSetupEditSessionMap().get(var1.getUniqueId()))) {
            var1.teleport(var1.getWorld().getSpawnLocation().clone().add(0.5D, 1.0D, 0.5D));
        }

    }
}
