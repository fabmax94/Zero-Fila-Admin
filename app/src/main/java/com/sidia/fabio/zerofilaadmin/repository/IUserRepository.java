package com.sidia.fabio.zerofilaadmin.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sidia.fabio.zerofilaadmin.model.User;

public interface IUserRepository {
    void add(User user);
    MutableLiveData<Boolean> isExist(User user);
    void delete(User session);
}
