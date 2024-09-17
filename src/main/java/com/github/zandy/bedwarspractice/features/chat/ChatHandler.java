package com.github.zandy.bedwarspractice.features.chat;

import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
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
        if(!WorldEngine.getInstance().getPracticeWorld().equals(player.getWorld())) return;
        e.getRecipients().removeIf(p -> p.getWorld() != player.getWorld());
    }
}
