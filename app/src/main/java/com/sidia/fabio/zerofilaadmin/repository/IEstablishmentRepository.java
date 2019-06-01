package com.sidia.fabio.zerofilaadmin.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sidia.fabio.zerofilaadmin.model.Establishment;

public interface IEstablishmentRepository {
    MutableLiveData<Establishment> findByUser(String key);
    void add(Establishment establishment, String user);
    void edit(Establishment establishment);
}
