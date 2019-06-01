package com.sidia.fabio.zerofilaadmin;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.sidia.fabio.zerofilaadmin.model.Clerk;
import com.sidia.fabio.zerofilaadmin.viewModel.QueueViewModel;

import java.util.Objects;

public class ClerkActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etType;
    private EditText etDescription;
    private String key;
    public static String CLERK_CREATE_KEY = "CLERK_CREATE_KEY";
    private QueueViewModel queueViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk);
        queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        key = getIntent().getExtras().getString(CLERK_CREATE_KEY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.new_queue));
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (key == null) {
            finish();
        }
        bindViews();
    }

    void bindViews() {
        etName = findViewById(R.id.et_name);
        etType = findViewById(R.id.et_type);
        etDescription = findViewById(R.id.et_description);
    }

    void onSave(View view) {
        Clerk clerk = new Clerk(etName.getText().toString(), etType.getText().toString(), etDescription.getText().toString(), key);
        queueViewModel.addClerk(clerk);
        finish();
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
