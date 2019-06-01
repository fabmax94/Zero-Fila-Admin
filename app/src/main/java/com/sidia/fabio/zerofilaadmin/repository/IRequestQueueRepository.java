package com.sidia.fabio.zerofilaadmin.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sidia.fabio.zerofilaadmin.model.RequestQueue;

import java.util.List;

public interface IRequestQueueRepository {
    MutableLiveData<List<RequestQueue>> findAllByEstablishment(String key);

    void delete(RequestQueue requestQueue);
}
