package com.sidia.fabio.zerofilaadmin.repository;

import android.arch.lifecycle.MutableLiveData;

import com.sidia.fabio.zerofilaadmin.model.ItemQueue;

import java.util.List;

public interface IItemQueueRepository {
    MutableLiveData<List<ItemQueue>> findAllItemQueueByKey(final String key);

    void add(ItemQueue itemQueue);

    void update(ItemQueue itemQueue);

    void delete(ItemQueue itemQueue);
}
