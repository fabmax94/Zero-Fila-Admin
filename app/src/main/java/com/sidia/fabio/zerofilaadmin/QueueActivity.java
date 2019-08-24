package com.sidia.fabio.zerofilaadmin;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.sidia.fabio.zerofilaadmin.adapter.QueueArrayAdapter;
import com.sidia.fabio.zerofilaadmin.model.Clerk;
import com.sidia.fabio.zerofilaadmin.viewModel.QueueViewModel;

import java.util.List;
import java.util.Objects;

public class QueueActivity extends AppCompatActivity {

    private QueueViewModel queueViewModel;
    private String key;
    public static String CLERK_KEY = "CLERK_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        key = getIntent().getExtras().getString(CLERK_KEY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.queue));
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (key == null) {
            finish();
        }
        queueViewModel.getQueue(key).observe(this, new Observer<List<Clerk>>() {
            @Override
            public void onChanged(@Nullable List<Clerk> result) {
                ListView listView = findViewById(R.id.queue);
                QueueArrayAdapter adapter = new QueueArrayAdapter(getApplicationContext(), result);
                listView.setAdapter(adapter);
                findViewById(R.id.load).setVisibility(View.GONE);
            }
        });
    }

    public void onClerk(View view) {
        Intent intent = new Intent(this, ClerkActivity.class);
        intent.putExtra(ClerkActivity.CLERK_CREATE_KEY, key);
        startActivity(intent);
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
