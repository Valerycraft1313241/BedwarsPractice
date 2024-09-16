package com.github.zandy.bedwarspractice.support;

import org.bukkit.Location;

public class BWPVector {
    private final double x;
    private final double y;
    private final double z;

    public BWPVector(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public double[] toArray() {
        return new double[]{this.x, this.y, this.z};
    }
}
