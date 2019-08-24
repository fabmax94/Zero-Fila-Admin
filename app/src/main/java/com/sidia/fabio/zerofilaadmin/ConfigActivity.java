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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sidia.fabio.zerofilaadmin.model.Establishment;
import com.sidia.fabio.zerofilaadmin.model.User;
import com.sidia.fabio.zerofilaadmin.viewModel.QueueViewModel;
import com.sidia.fabio.zerofilaadmin.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ConfigActivity extends AppCompatActivity {
    public static final int GET_LOCATION = 1;
    private EditText etName;
    private EditText etLocal;
    private MaterialSpinner spType;
    private QueueViewModel queueViewModel;
    private UserViewModel userViewModel;
    private String userEmail;
    public static String USER_KEY = "USER_KEY";
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        userEmail = getIntent().getExtras().getString(USER_KEY);
        if (userEmail == null) {
            finish();
        }
        bindView();
        queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        setUser();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.setting));
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUser() {
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                checkConfig(user);
            }
        });
    }

    private void checkConfig(final User user) {
        queueViewModel.getEstablishment(user.getEmail()).observe(this, new Observer<Establishment>() {
            @Override
            public void onChanged(@Nullable Establishment establishment) {
                if (establishment != null) {
                    etName.setText(establishment.name);
                    etLocal.setText(establishment.local);
                    int index = 0;
                    for (String item : getResources().getStringArray(R.array.establishment_type))
                        if (item.equals(establishment.type))
                            break;
                        else
                            index++;

                    spType.setSelection(index);
                }
            }
        });
    }

    void bindView() {
        etName = findViewById(R.id.et_name);
        etLocal = findViewById(R.id.et_local);
        spType = findViewById(R.id.sp_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.establishment_type));
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spType.setAdapter(adapter);
    }

    public void onSave(View view) {
        Establishment establishment = new Establishment(etName.getText().toString(), etLocal.getText().toString(), getResources().getStringArray(R.array.establishment_type)[spType.getSelectedItemPosition()], latitude, longitude);
        if (establishment.isValid()) {
            queueViewModel.addEstablishment(establishment, userEmail);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.establishment_error_message, Toast.LENGTH_SHORT).show();
        }
    }

    public void onMap(View view) {
        Intent intent = new Intent(ConfigActivity.this, MapsActivity.class);
        startActivityForResult(intent, GET_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GET_LOCATION) {
            assert data != null;
            etLocal.setText(data.getExtras().getString(MapsActivity.LOCATION));
            latitude = data.getExtras().getDouble(MapsActivity.LATITUDE);
            longitude = data.getExtras().getDouble(MapsActivity.LONGITUDE);
        }
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