package com.sidia.fabio.zerofilaadmin.repository.impl;

import android.arch.lifecycle.MutableLiveData;
import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofilaadmin.model.ItemQueue;
import com.sidia.fabio.zerofilaadmin.model.RequestQueue;
import com.sidia.fabio.zerofilaadmin.repository.IItemQueueRepository;

import java.util.ArrayList;
import java.util.List;

public class ItemQueueRepository implements IItemQueueRepository {
    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_ITEM = "item_queue";
    private MutableLiveData<List<ItemQueue>> mLiveData;

    public ItemQueueRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_ITEM);
        mLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<List<ItemQueue>> findAllItemQueueByKey(String key) {
        reference.orderByChild("clerkKey").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ItemQueue> requestQueueList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemQueue itemQueue = snapshot.getValue(ItemQueue.class);
                    itemQueue.key = snapshot.getKey();
                    requestQueueList.add(itemQueue);
                }
                mLiveData.setValue(requestQueueList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveData;
    }

    @Override
    public void add(ItemQueue itemQueue) {
        reference.push().setValue(itemQueue);
    }

    @Override
    public void delete(ItemQueue itemQueue) {
        reference.child(itemQueue.key).removeValue();
    }

    @Override
    public void update(ItemQueue itemQueue) {
        reference.child(itemQueue.key).setValue(itemQueue);
    }
}
