package com.sidia.fabio.zerofilaadmin.model;

import com.google.firebase.database.Exclude;


public class ItemQueue {
    public int index;
    @Exclude
    public int indexShow;
    public String cpf;
    public String name;
    public String clerkKey;
    public boolean isAttendance;
    @Exclude
    public String key;

    public ItemQueue(String cpf, String name, String clerkKey, int index) {
        this.index = index;
        this.cpf = cpf;
        this.name = name;
        this.clerkKey = clerkKey;
        isAttendance = false;
    }

    public ItemQueue() {

    }
}
