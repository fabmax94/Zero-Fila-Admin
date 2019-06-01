package com.sidia.fabio.zerofilaadmin.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sidia.fabio.zerofilaadmin.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_ZERO = "database-zero";
    public abstract IUserDAO userDAO();
}
