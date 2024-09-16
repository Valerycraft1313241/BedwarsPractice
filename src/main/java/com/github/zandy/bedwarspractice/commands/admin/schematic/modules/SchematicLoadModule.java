package com.github.zandy.bedwarspractice.commands.admin.schematic.modules;

import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.support.BWPVector;
import com.github.zandy.bedwarspractice.support.WESupport;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.github.zandy.bedwarspractice.utils.BWPUtils;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

public class SchematicLoadModule {
    private static SchematicLoadModule instance = null;

    private SchematicLoadModule() {
    }

    public static SchematicLoadModule getInstance() {
        if (instance == null) {
            instance = new SchematicLoadModule();
        }

        return instance;
    }

    public void execute(Player var1, String var2) {
        File var3 = new File("plugins/BedWarsPractice/Schematics/", var2 + BWPUtils.getExtension());
        var1.sendMessage(" ");
        var1.sendMessage(" ");
        if (!var3.exists()) {
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_FILE_NOT_FOUND.getString(var1.getUniqueId()).replace("[schemName]", var2));
            Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
        } else {
            Location var4 = var1.getLocation();
            var1.sendMessage(Language.MessagesEnum.COMMAND_TAG.getString(var1.getUniqueId()));
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_LOADING.getString(var1.getUniqueId()).replace("[schemName]", var2));
            World var5 = WESupport.getWEWorld(var1.getWorld());
            double[] var6 = (new BWPVector(var4)).toArray();
            if (BWPUtils.isLegacy()) {
                BWPLegacyAdapter.getInstance().pasteSchematic(var2, var5, var6);
            }
            Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SCHEMATIC_LOAD_LOADED_AND_PASTED.getString(var1.getUniqueId()));
        }
    }
}
