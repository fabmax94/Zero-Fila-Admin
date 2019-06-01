package com.sidia.fabio.zerofilaadmin.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.Exclude;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @Exclude
    public String email;
    public String password;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email.replace(".", ",");
    }
}

