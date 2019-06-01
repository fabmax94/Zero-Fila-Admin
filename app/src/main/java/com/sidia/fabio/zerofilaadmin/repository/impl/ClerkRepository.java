package com.sidia.fabio.zerofilaadmin.repository.impl;

import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofilaadmin.model.Clerk;
import com.sidia.fabio.zerofilaadmin.repository.IClerkRepository;

import java.util.ArrayList;
import java.util.List;

public class ClerkRepository implements IClerkRepository {

    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_CLERK = "clerk";

    private MutableLiveData<List<Clerk>> mLiveData;

    public ClerkRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_CLERK);
        mLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<List<Clerk>> findByEstablishment(final String key) {
        reference.orderByChild("establishmentKey").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Clerk> clerks = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Clerk u = snapshot.getValue(Clerk.class);
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
    public void add(Clerk clerk) {
        this.reference.push().setValue(clerk);
    }

    @Override
    public void update(Clerk currentClerk) {
        this.reference.child(currentClerk.key).setValue(currentClerk);
    }
}
