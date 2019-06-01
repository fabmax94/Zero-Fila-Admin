package com.sidia.fabio.zerofilaadmin.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sidia.fabio.zerofilaadmin.model.Clerk;

import java.util.List;

public interface IClerkRepository {
    MutableLiveData<List<Clerk>> findByEstablishment(String key);
    void add(Clerk clerk);

    void update(Clerk currentClerk);
}
