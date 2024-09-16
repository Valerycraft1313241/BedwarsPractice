package com.github.zandy.bedwarspractice.commands.admin.setup.modules;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bamboolib.versionsupport.sound.Sounds;
import com.github.zandy.bedwarspractice.files.language.Language;
import com.github.zandy.bedwarspractice.utils.SchematicChecker;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SetupListModule {
    private static SetupListModule instance = null;
    private final List<String> requiredSchematics = new ArrayList<>(SchematicChecker.getRequiredSchematics(false));

    private SetupListModule() {
    }

    public static SetupListModule getInstance() {
        if (instance == null) {
            instance = new SetupListModule();
        }

        return instance;
    }

    public void execute(Player var1) {
        ArrayList<String> var2 = new ArrayList<>(this.requiredSchematics);
        var2.remove("FIREBALL-TNT-JUMPING");
        File var3 = new File("plugins/BedWarsPractice/Data");
        if (var3.exists()) {
            Arrays.stream(var3.listFiles()).forEach((var1x) -> var2.remove(var1x.getName().replace(".yml", "")));
        }

        var1.sendMessage(" ");
        var1.sendMessage(" ");
        UUID var4 = var1.getUniqueId();
        if (var2.isEmpty()) {
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_ALL_CREATED.getString(var4));
            Sounds.VILLAGER_NO.getSound().play(var1, 3.0F, 1.0F);
        } else {
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_REQUIRED_SETUPS.getString(var4));
            String var5 = Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_ENUMERATION.getString(var4);
            var2.forEach((var3x) -> BambooUtils.sendTextComponent(var1, var5.replace("[practiceName]", var3x), "/bwpa setup set " + var3x, Language.MessagesEnum.COMMAND_CLICK_TO_RUN.getString(var4), Action.RUN_COMMAND));
            var1.sendMessage(Language.MessagesEnum.COMMAND_ADMIN_SETUP_LIST_INFO.getString(var4));
            Sounds.PLAYER_LEVELUP.getSound().play(var1, 3.0F, 3.0F);
        }
    }
}
