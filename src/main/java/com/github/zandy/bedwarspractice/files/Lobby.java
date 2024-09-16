package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bamboolib.utils.BambooFile;
import org.bukkit.Location;

public class Lobby extends BambooFile {
    private static final String lobby = "Lobby";
    private static Lobby instance = null;
    private Location lobbyLocation = null;
    private boolean isSet = false;

    private Lobby() {
        super(lobby, "Map");
    }

    public static Lobby getInstance() {
        if (instance == null) {
            instance = new Lobby();
        }

        return instance;
    }

    public boolean isSet() {
        if (!this.isSet) {
            this.isSet = this.contains(lobby);
        }

        return this.isSet;
    }

    public void set(Location location) {
        this.setLocation(lobby, location);
        this.lobbyLocation = location;
        this.isSet = true;
    }

    public Location get() {
        if (this.lobbyLocation == null) {
            this.lobbyLocation = this.getLocation(lobby);
        }

        return this.lobbyLocation;
    }
}
