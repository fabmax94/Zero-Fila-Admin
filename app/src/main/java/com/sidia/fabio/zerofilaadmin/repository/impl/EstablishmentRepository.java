package com.sidia.fabio.zerofilaadmin.repository.impl;

import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofilaadmin.model.Establishment;
import com.sidia.fabio.zerofilaadmin.repository.IEstablishmentRepository;

public class EstablishmentRepository implements IEstablishmentRepository {
    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_ESTABLISHMENT = "establishment";

    private MutableLiveData<Establishment> mLiveData;

    public EstablishmentRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_ESTABLISHMENT);
        mLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Establishment> findByUser(String key) {
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Establishment u = dataSnapshot.getValue(Establishment.class);
                    u.key = dataSnapshot.getKey();
                    mLiveData.setValue(u);
                } else {
                    mLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveData;
    }

    @Override
    public void add(Establishment establishment, String user) {
        this.reference.child(user).setValue(establishment);
    }

    @Override
    public void edit(Establishment establishment) {

    }
}
