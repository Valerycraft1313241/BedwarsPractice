package com.github.zandy.bedwarspractice.utils;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bedwarspractice.engine.GameEngine;
import com.github.zandy.bedwarspractice.engine.WorldEngine;
import com.github.zandy.bedwarspractice.support.BWPVector;
import com.github.zandy.bedwarspractice.support.legacy.BWPLegacyAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;

public class FinishPositions {
    private final BWPVector position1;
    private final BWPVector position2;

    public FinishPositions(String fileName, Location baseLocation) {
        BambooFile practiceFile = GameEngine.getInstance().getPracticeFile().get(fileName);
        this.position1 = this.getPosition(1, baseLocation, practiceFile);
        this.position2 = this.getPosition(2, baseLocation, practiceFile);
    }

    private BWPVector getPosition(int positionNumber, Location baseLocation, BambooFile practiceFile) {
        return new BWPVector(new Location(baseLocation.getWorld(), practiceFile.getInt("Position-" + positionNumber + ".X") + baseLocation.getBlockX(), practiceFile.getInt("Position-" + positionNumber + ".Y") + baseLocation.getBlockY(), practiceFile.getInt("Position-" + positionNumber + ".Z") + baseLocation.getBlockZ()));
    }

    public CuboidRegion getRegion() {
        World practiceWorld = WorldEngine.getInstance().getPracticeWEWorld();
        return BWPLegacyAdapter.getInstance().getCuboidRegion(practiceWorld, this.position1.toArray(), this.position2.toArray());
    }
}
