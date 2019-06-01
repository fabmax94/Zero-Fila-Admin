package com.sidia.fabio.zerofilaadmin.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sidia.fabio.zerofilaadmin.model.User;

@Dao
public interface IUserDAO {
    @Insert
    void save(User user);

    @Query("DELETE FROM User")
    void deleteAll();

    @Query("SELECT * FROM User LIMIT 1")
    LiveData<User> getUser();
}