package com.github.zandy.bedwarspractice.features.bedwars1058;

import com.andrei1058.bedwars.api.events.player.PlayerLangChangeEvent;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.api.events.player.PlayerLanguageChangeEvent;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChangeLanguageListener implements Listener {
    public PlayerChangeLanguageListener() {
        BambooUtils.registerEvent(this);
    }

    @EventHandler
    private void onPlayerChangeLanguage(PlayerLangChangeEvent event) {
        String oldLang = event.getOldLang().toUpperCase();
        String newLang = event.getNewLang().toUpperCase();
        Language.getInstance().getPlayerLocale().put(event.getPlayer().getUniqueId(), newLang);
        Bukkit.getPluginManager().callEvent(new PlayerLanguageChangeEvent(event.getPlayer(), oldLang, newLang));
    }
}
