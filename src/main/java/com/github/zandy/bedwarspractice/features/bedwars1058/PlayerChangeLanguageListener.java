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
   private void onPlayerChangeLanguage(PlayerLangChangeEvent e) {
      String oldLang = e.getOldLang().toUpperCase();
      String newLang = e.getNewLang().toUpperCase();
      Language.getInstance().getPlayerLocale().put(e.getPlayer().getUniqueId(), newLang);
      Bukkit.getPluginManager().callEvent(new PlayerLanguageChangeEvent(e.getPlayer(), oldLang, newLang));
   }
}
