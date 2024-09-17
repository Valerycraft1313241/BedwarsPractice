package com.github.zandy.bedwarspractice.api;

import com.github.zandy.bedwarspractice.api.utils.creator.PracticeCreator;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.practice.bridging.BridgingMode;
import com.github.zandy.bedwarspractice.engine.practice.fireballtntjumping.FireballTNTJumpingMode;
import com.github.zandy.bedwarspractice.engine.practice.mlg.MLGMode;
import com.github.zandy.bedwarspractice.features.stats.PlayerStats;
import com.github.zandy.bedwarspractice.features.stats.Stats;
import com.github.zandy.bedwarspractice.files.language.Language;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BedWarsPracticeAPI {
    private static BedWarsPracticeAPI instance = null;

    private BedWarsPracticeAPI() {
    }

    public static BedWarsPracticeAPI getInstance() {
        if (instance == null) {
            instance = new BedWarsPracticeAPI();
        }

        return instance;
    }

    public double getStatistic(UUID playerUUID, Stats.StatsType statsType) {
        return PlayerStats.get(playerUUID).get(statsType);
    }

    public String getISO(UUID playerUUID) {
        return Language.getInstance().getPlayerLocale().get(playerUUID);
    }

    public double getAPIVersion() {
        return 1.3D;
    }

    public PracticeCreator joinPractice(@NotNull Player player, @NotNull GameEngine.PracticeType practiceType) {

        switch (practiceType) {
            case BRIDGING:
                return BridgingMode.getInstance().create(player);
            case MLG:
                return MLGMode.getInstance().create(player);
            case FIREBALL_TNT_JUMPING:
                return FireballTNTJumpingMode.getInstance().create(player);
            default:
                return null;
        }
    }

}
