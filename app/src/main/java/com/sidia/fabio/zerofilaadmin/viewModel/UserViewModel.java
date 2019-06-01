package com.sidia.fabio.zerofilaadmin.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.sidia.fabio.zerofilaadmin.model.User;
import com.sidia.fabio.zerofilaadmin.persistence.AppDatabase;
import com.sidia.fabio.zerofilaadmin.repository.IUserRepository;
import com.sidia.fabio.zerofilaadmin.repository.impl.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private IUserRepository mUserRepository;
    private AppDatabase app;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository();
        this.app = Room.databaseBuilder(application.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_ZERO).build();

    }

    public MutableLiveData<Boolean> getAuthenticatedLogin(User user) {
        return mUserRepository.isExist(user);
    }

    public void setSession(User session) {
        this.app.userDAO().deleteAll();
        this.app.userDAO().save(session);
    }

    public LiveData<User> getUser() {
        return this.app.userDAO().getUser();
    }

    public void logout() {
        this.app.userDAO().deleteAll();
    }

    public void deleteUser() {
        this.app.userDAO().deleteAll();
    }
}
