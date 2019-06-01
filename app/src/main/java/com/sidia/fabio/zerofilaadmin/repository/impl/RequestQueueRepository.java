package com.sidia.fabio.zerofilaadmin.repository.impl;

import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofilaadmin.model.RequestQueue;
import com.sidia.fabio.zerofilaadmin.repository.IRequestQueueRepository;

import java.util.ArrayList;
import java.util.List;

public class RequestQueueRepository implements IRequestQueueRepository {
    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_REQUEST = "request_queue";
    private MutableLiveData<List<RequestQueue>> mLiveData;

    public RequestQueueRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_REQUEST);
        mLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<List<RequestQueue>> findAllByEstablishment(String key) {
        reference.orderByChild("establishmentKey").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<RequestQueue> clerks = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestQueue u = snapshot.getValue(RequestQueue.class);
                    u.key = snapshot.getKey();
                    assert u != null;
                    clerks.add(u);
                }
                mLiveData.setValue(clerks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveData;
    }

    @Override
    public void delete(RequestQueue requestQueue) {
        reference.child(requestQueue.key).removeValue();
    }
}
