package com.github.zandy.bedwarspractice.features.npc;

import com.github.zandy.bamboolib.hologram.RefreshableHologram;
import com.github.zandy.bamboolib.placeholder.utils.RefreshablePlaceholder;
import com.github.zandy.bamboolib.utils.BambooUtils;
import com.github.zandy.bedwarspractice.api.events.player.PlayerLanguageChangeEvent;
import com.github.zandy.bedwarspractice.api.events.practice.PracticeQuitEvent;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingInfo;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingInfo;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGInfo;
import com.github.zandy.bedwarspractice.features.guis.ModeSelectorGUI;
import com.github.zandy.bedwarspractice.files.NPCStorage;
import com.github.zandy.bedwarspractice.files.Settings;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PracticeNPC implements Listener {
    private static PracticeNPC instance = null;
    private boolean init = false;

    public static PracticeNPC getInstance() {
        if (instance == null) {
            instance = new PracticeNPC();
        }

        return instance;
    }

    public void init() {
        BambooUtils.registerEvent(this);
        this.init = true;
    }

    @EventHandler
    private void onNPCRightClick(NPCRightClickEvent event) {
        this.clickFunction(event.getClicker(), event.getNPC().getId());
    }

    @EventHandler
    private void onNPCLeftClick(NPCLeftClickEvent event) {
        this.clickFunction(event.getClicker(), event.getNPC().getId());
    }

    @EventHandler
    private void onPracticeLeave(PracticeQuitEvent event) {
        if (event.getPlayer().isOnline()) {
            this.respawnNPCs(event.getPlayer());
        }
    }

    @EventHandler
    private void onLanguageChange(PlayerLanguageChangeEvent event) {
        PlayerDataNPC.get(event.getPlayer().getUniqueId()).removeAll();
        this.respawnNPCs(event.getPlayer());
    }

    private void clickFunction(Player player, int npcId) {
        Arrays.stream(NPCStorage.NPCType.values()).forEach((npcType) -> {
            if (NPCStorage.getInstance().contains(npcType, npcId)) {
                switch (npcType) {
                    case DEFAULT:
                        ModeSelectorGUI.getInstance().open(player, true);
                        break;
                    case BRIDGING:
                        ModeSelectorGUI.getInstance().clickFunctionality(player, GameEngine.PracticeType.BRIDGING, true);
                        break;
                    case MLG:
                        ModeSelectorGUI.getInstance().clickFunctionality(player, GameEngine.PracticeType.MLG, true);
                        break;
                    case FIREBALL_TNT_JUMPING:
                        ModeSelectorGUI.getInstance().clickFunctionality(player, GameEngine.PracticeType.FIREBALL_TNT_JUMPING, true);
                }
            }
        });
    }

    public void spawnNPC(NPC npc, Integer npcId, NPCStorage.NPCType npcType) {
        npc.setAlwaysUseNameHologram(false);
        Entity entity = npc.getEntity();
        Location location = entity.getLocation().clone().add(0.0D, 1.55D, 0.0D);
        List<RefreshablePlaceholder> placeholders = this.getRefreshablePlaceholders(npcType);
        int refreshTick = Settings.SettingsEnum.NPC_REFRESH_TICK.getInt();
        Bukkit.getOnlinePlayers().forEach((player) -> PlayerDataNPC.get(player.getUniqueId()).addNPC(npcId, new RefreshableHologram(player, "npc_" + npcId + "_" + player.getUniqueId(), location, npcType.getMessagesEnum().getStringList(player.getUniqueId()), placeholders, refreshTick)));
    }

    public void respawnNPCs(Player player) {
        int refreshTick = Settings.SettingsEnum.NPC_REFRESH_TICK.getInt();
        Arrays.stream(NPCStorage.NPCType.values()).forEach((npcType) -> NPCStorage.getInstance().getIDList(npcType).forEach((npcId) -> {
            Entity entity = CitizensAPI.getNPCRegistry().getById(npcId).getEntity();
            Location location = entity.getLocation().clone().add(0.0D, 1.55D, 0.0D);
            List<RefreshablePlaceholder> placeholders = this.getRefreshablePlaceholders(npcType);
            PlayerDataNPC.get(player.getUniqueId()).addNPC(npcId, new RefreshableHologram(player, "npc_" + npcId + "_" + player.getUniqueId(), location, npcType.getMessagesEnum().getStringList(player.getUniqueId()), placeholders, refreshTick));
        }));
    }

    public void removeNPC(Integer npcId) {
        PlayerDataNPC.getDataMap().values().forEach((playerData) -> playerData.remove(npcId));
    }

    public void despawnNPCs() {
        PlayerDataNPC.getDataMap().values().forEach(PlayerDataNPC::removeAll);
    }

    private List<RefreshablePlaceholder> getRefreshablePlaceholders(final NPCStorage.NPCType npcType) {
        ArrayList<RefreshablePlaceholder> placeholders = new ArrayList<>();
        placeholders.add(new RefreshablePlaceholder(npcType.getPlaceholder(), Settings.SettingsEnum.NPC_REFRESH_TICK.getInt()) {
            public String refresh() {
                int count = 0;
                switch (npcType) {
                    case DEFAULT:
                        count = GameEngine.getInstance().getPracticeTypeMap().size();
                        break;
                    case BRIDGING:
                        count = BridgingInfo.getBridgingInfoMap().size();
                        break;
                    case MLG:
                        count = MLGInfo.getMlgInfoMap().size();
                        break;
                    case FIREBALL_TNT_JUMPING:
                        count = FireballTNTJumpingInfo.getFireballTntJumpingMap().size();
                }

                return String.valueOf(count);
            }
        });
        return placeholders;
    }

    public boolean isInit() {
        return this.init;
    }
}
