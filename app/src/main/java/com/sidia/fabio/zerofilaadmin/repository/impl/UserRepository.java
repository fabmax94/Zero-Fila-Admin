package com.sidia.fabio.zerofilaadmin.repository.impl;

import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidia.fabio.zerofilaadmin.model.User;
import com.sidia.fabio.zerofilaadmin.repository.IUserRepository;

public class UserRepository implements IUserRepository {
    private DatabaseReference reference;
    private static final String DATABASE_NAME = "zero_fila";
    private static final String CHILD_USER = "user";

    private MutableLiveData<Boolean> mLiveData;

    public UserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(DATABASE_NAME).child(CHILD_USER);
        mLiveData = new MutableLiveData<>();
    }

    @Override
    public void add(User user) {
        this.reference.child(user.getEmail()).setValue(user);
    }

    @Override
    public MutableLiveData<Boolean> isExist(final User user) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User u = snapshot.getValue(User.class);
                    assert u != null;
                    u.email = snapshot.getKey();


                    if (u.getEmail().equals(user.getEmail())) {
                        mLiveData.setValue(u.password.equals(user.password));
                        return;
                    }
                }
                add(user);
                mLiveData.setValue(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mLiveData;
    }

    @Override
    public void delete(User session) {
        this.reference.child(session.getEmail()).removeValue();
    }
}
