package com.github.zandy.bedwarspractice.features.chat;

import com.github.zandy.bamboolib.placeholder.PlaceholderManager;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.files.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {
    public ChatHandler() {
        BambooUtils.registerEvent(this);
    }

    @EventHandler
    private void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        e.setFormat(PlaceholderManager.getInstance().setPlaceholders(player, Settings.SettingsEnum.CHAT_FORMAT.getString().replace("[playerName]", player.getName())) + e.getMessage().replace("%", "%%"));
    }
}
