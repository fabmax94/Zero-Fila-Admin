package com.sidia.fabio.zerofilaadmin.model;

import com.google.firebase.database.Exclude;

public class Establishment {
    public String name;
    public String local;
    public double latitude;
    public double longitude;
    public String type;

    @Exclude
    public String key;

    public Establishment(String name, String local, String type, double latitude, double longitude) {
        this.name = name;
        this.local = local;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Establishment() {
    }

    public boolean isValid() {
        return (!name.isEmpty() && !local.isEmpty() && !type.isEmpty());
    }
}
