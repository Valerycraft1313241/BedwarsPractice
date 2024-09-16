package com.github.zandy.bedwarspractice.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Position {
    private final int position;
    private final int x;
    private final int y;
    private final int z;

    public Position(int position, Location location) {
        this.position = position;
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public Position(int position, int x, int y, int z) {
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getPos() {
        return this.position;
    }

    public Location getLocation(World world) {
        return new Location(world, this.x, this.y, this.z);
    }
}
