package com.sidia.fabio.zerofilaadmin.model;

import com.google.firebase.database.Exclude;

public class Clerk {
    public String name;
    public String description;
    public String type;
    public String establishmentKey;
    public long lastAttendance;
    public long average;
    @Exclude
    public String key;

    public Clerk(String name, String type, String description, String key) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.establishmentKey = key;
    }

    public Clerk() {

    }
}
