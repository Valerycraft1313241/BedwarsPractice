package com.github.zandy.bedwarspractice.support;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SWMSupport {
    private static SWMSupport instance = null;
    private final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    private final SlimeLoader slimeLoader;

    public SWMSupport() {
        this.slimeLoader = this.slimePlugin.getLoader("file");
    }

    public static SWMSupport getInstance() {
        if (instance == null) {
            instance = new SWMSupport();
        }

        return instance;
    }

    public World generateWorld() {
        SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
        slimePropertyMap.setInt(SlimeProperties.SPAWN_X, 0);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Y, 100);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Z, 0);

        try {
            this.slimePlugin.generateWorld(this.slimePlugin.createEmptyWorld(this.slimeLoader, "bedwars_practice", true, slimePropertyMap));
        } catch (Exception ignored) {
        }

        return Bukkit.getWorld("bedwars_practice");
    }

    public void removeWorld() {
        try {
            this.slimeLoader.deleteWorld("bedwars_practice");
        } catch (Exception ignored) {
        }
    }
}
