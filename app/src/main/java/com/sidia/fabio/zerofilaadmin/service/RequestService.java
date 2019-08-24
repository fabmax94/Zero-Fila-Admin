package com.sidia.fabio.zerofilaadmin.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.sidia.fabio.zerofilaadmin.R;
import com.sidia.fabio.zerofilaadmin.RequestQueueActivity;
import com.sidia.fabio.zerofilaadmin.model.RequestQueue;
import com.sidia.fabio.zerofilaadmin.viewModel.QueueViewModel;

import java.util.List;

public class RequestService extends LifecycleService {
    private QueueViewModel queueViewModel;
    public static String REQUEST_ID = "REQUEST_ID";
    private String key;

    @Override
    public void onCreate() {
        super.onCreate();
        queueViewModel = new QueueViewModel(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        key = intent.getExtras().getString(REQUEST_ID);
        queueViewModel.getRequests(key).observe(this, new Observer<List<RequestQueue>>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onChanged(@Nullable List<RequestQueue> requestQueues) {
                if (requestQueues != null && requestQueues.size() > 0) {
                    createNotificationChannel();
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        Intent intent = new Intent(this, RequestQueueActivity.class);
        intent.putExtra(RequestQueueActivity.REQUEST_KEY, key);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder;
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3000";
            CharSequence name = "New Request";
            String description = "Request Queue";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(mChannel);
            mBuilder = new NotificationCompat.Builder(this, chanel_id);
        } else {
            mBuilder = new NotificationCompat.Builder(this);
        }
        mBuilder.setSmallIcon(R.drawable.ic_person_black_24dp)
                .setContentTitle("Nova requisição")
                .setContentText("Um cliente pediu para entrar na fila.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Notification n = mBuilder.build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.setAutoCancel(true);
        notificationManager.notify(544, n);
    }
}
