package com.sidia.fabio.zerofilaadmin;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sidia.fabio.zerofilaadmin.adapter.RequestQueueArrayAdapter;
import com.sidia.fabio.zerofilaadmin.model.ItemQueue;
import com.sidia.fabio.zerofilaadmin.model.RequestQueue;
import com.sidia.fabio.zerofilaadmin.viewModel.QueueViewModel;

import java.util.List;
import java.util.Objects;

public class RequestQueueActivity extends AppCompatActivity {

    public static String REQUEST_KEY = "REQUEST_KEY";
    private String key;
    private QueueViewModel queueViewModel;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_queue);
        key = getIntent().getExtras().getString(REQUEST_KEY);
        queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        listView = findViewById(R.id.requests);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Check-ins");
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestQueue requestQueue = (RequestQueue) listView.getAdapter().getItem(position);
                showCheckinDialog(requestQueue);
            }
        });
        queueViewModel.getRequests(key).observe(this, new Observer<List<RequestQueue>>() {
            @Override
            public void onChanged(@Nullable List<RequestQueue> requestQueues) {
                RequestQueueArrayAdapter adapter = new RequestQueueArrayAdapter(getApplicationContext(), requestQueues);
                listView.setAdapter(adapter);
                findViewById(R.id.load).setVisibility(View.GONE);
                if (requestQueues.size() == 0) {
                    findViewById(R.id.request_empty).setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void showCheckinDialog(final RequestQueue requestQueue) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RequestQueueActivity.this, android.R.style.Theme_DeviceDefault_Dialog));
        builder.setTitle("Check-in");
        builder.setMessage(getString(R.string.the_client) + requestQueue.cpf + getString(R.string.is_approved) + requestQueue.clerkName + "?");
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                queueViewModel.getListItemQueue(key).observe(RequestQueueActivity.this, new Observer<List<ItemQueue>>() {
                    @Override
                    public void onChanged(@Nullable List<ItemQueue> itemQueues) {
                        int maxIndex = -1;
                        for (ItemQueue itemQueue : itemQueues) {
                            if (itemQueue.index > maxIndex) {
                                maxIndex = itemQueue.index;
                            }
                        }
                        queueViewModel.approveRequestQueue(requestQueue, ++maxIndex);
                        queueViewModel.getListItemQueue(key).removeObserver(this);
                    }
                });
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                queueViewModel.disapproveRequestQueue(requestQueue);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
