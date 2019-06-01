package com.sidia.fabio.zerofilaadmin.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.sidia.fabio.zerofilaadmin.model.Clerk;
import com.sidia.fabio.zerofilaadmin.model.Establishment;
import com.sidia.fabio.zerofilaadmin.model.ItemQueue;
import com.sidia.fabio.zerofilaadmin.model.RequestQueue;
import com.sidia.fabio.zerofilaadmin.repository.IClerkRepository;
import com.sidia.fabio.zerofilaadmin.repository.IEstablishmentRepository;
import com.sidia.fabio.zerofilaadmin.repository.IItemQueueRepository;
import com.sidia.fabio.zerofilaadmin.repository.IRequestQueueRepository;
import com.sidia.fabio.zerofilaadmin.repository.impl.ClerkRepository;
import com.sidia.fabio.zerofilaadmin.repository.impl.EstablishmentRepository;
import com.sidia.fabio.zerofilaadmin.repository.impl.ItemQueueRepository;
import com.sidia.fabio.zerofilaadmin.repository.impl.RequestQueueRepository;

import java.util.List;

public class QueueViewModel extends AndroidViewModel {
    private IEstablishmentRepository establishmentRepository;
    private IClerkRepository clerkRepository;
    private IRequestQueueRepository requestQueueRepository;
    private IItemQueueRepository itemQueueRepository;

    public QueueViewModel(@NonNull Application application) {
        super(application);
        establishmentRepository = new EstablishmentRepository();
        clerkRepository = new ClerkRepository();
        requestQueueRepository = new RequestQueueRepository();
        itemQueueRepository = new ItemQueueRepository();
    }

    public MutableLiveData<Establishment> getEstablishment(String user) {
        return establishmentRepository.findByUser(user);
    }

    public void addEstablishment(Establishment establishment, String user) {
        establishmentRepository.add(establishment, user);
    }

    public LiveData<List<Clerk>> getQueue(String key) {
        return clerkRepository.findByEstablishment(key);
    }

    public LiveData<List<RequestQueue>> getRequests(String key) {
        return requestQueueRepository.findAllByEstablishment(key);
    }

    public void approveRequestQueue(RequestQueue requestQueue, int index) {
        ItemQueue itemQueue = new ItemQueue(requestQueue.cpf, requestQueue.name, requestQueue.clerkKey, index);
        requestQueueRepository.delete(requestQueue);
        itemQueueRepository.add(itemQueue);

    }

    public void addClerk(Clerk clerk) {
        clerkRepository.add(clerk);
    }

    public LiveData<List<ItemQueue>> getListItemQueue(String key) {
        return itemQueueRepository.findAllItemQueueByKey(key);
    }

    public void disapproveRequestQueue(RequestQueue requestQueue) {
        requestQueueRepository.delete(requestQueue);
    }

    public void updateItemQueue(ItemQueue itemQueue) {
        itemQueueRepository.update(itemQueue);
    }

    public void deleteItemQueue(ItemQueue itemQueue) {
        itemQueueRepository.delete(itemQueue);
    }

    public void updateClerk(Clerk currentClerk) {
        clerkRepository.update(currentClerk);
    }
}
