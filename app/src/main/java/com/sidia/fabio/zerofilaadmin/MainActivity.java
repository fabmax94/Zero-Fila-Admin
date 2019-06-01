package com.sidia.fabio.zerofilaadmin;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sidia.fabio.zerofilaadmin.adapter.ItemQueueArrayAdapter;
import com.sidia.fabio.zerofilaadmin.model.Clerk;
import com.sidia.fabio.zerofilaadmin.model.Establishment;
import com.sidia.fabio.zerofilaadmin.model.ItemQueue;
import com.sidia.fabio.zerofilaadmin.model.User;
import com.sidia.fabio.zerofilaadmin.service.RequestService;
import com.sidia.fabio.zerofilaadmin.viewModel.QueueViewModel;
import com.sidia.fabio.zerofilaadmin.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity {

    private QueueViewModel queueViewModel;
    private UserViewModel userViewModel;
    private User user;
    private Spinner spinner;
    private List<Clerk> clerkList;
    private ListView listView;
    private Clerk currentClerk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModels();
        bindViews();
        initListener();
        observers();
    }

    void initViewModels() {
        queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    void bindViews() {
        spinner = findViewById(R.id.clerks);
        listView = findViewById(R.id.queue);
    }

    void initListener() {
        findViewById(R.id.approve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemQueue itemQueue = (ItemQueue) listView.getAdapter().getItem(0);
                if (itemQueue == null) {
                    return;
                }
                if (!itemQueue.isAttendance) {
                    itemQueue.isAttendance = true;
                    queueViewModel.updateItemQueue(itemQueue);
                    Toast.makeText(getApplicationContext(), itemQueue.name + getString(R.string.queue_message), Toast.LENGTH_SHORT).show();
                } else {
                    queueViewModel.deleteItemQueue(itemQueue);
                    if (currentClerk.lastAttendance != 0) {
                        long diff = new Date(System.currentTimeMillis()).getTime() - currentClerk.lastAttendance;
                        if (currentClerk.average == 0) {
                            currentClerk.average = diff;
                        } else {
                            currentClerk.average = (currentClerk.average + diff) / 2;
                        }
                    }
                    if (listView.getAdapter().getCount() > 1) {
                        itemQueue = (ItemQueue) listView.getAdapter().getItem(1);
                        itemQueue.isAttendance = true;
                        queueViewModel.updateItemQueue(itemQueue);
                        Toast.makeText(getApplicationContext(), itemQueue.name + getString(R.string.queue_message), Toast.LENGTH_SHORT).show();
                    }
                }
                currentClerk.lastAttendance = new Date(System.currentTimeMillis()).getTime();
                queueViewModel.updateClerk(currentClerk);
            }
        });

        findViewById(R.id.reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemQueue itemQueue = (ItemQueue) listView.getAdapter().getItem(0);
                if (itemQueue == null) {
                    return;
                }
                if (!itemQueue.isAttendance) {
                    itemQueue.index += 3;
                    queueViewModel.updateItemQueue(itemQueue);
                    Toast.makeText(getApplicationContext(), itemQueue.name + getString(R.string.late), Toast.LENGTH_SHORT).show();
                } else {
                    queueViewModel.deleteItemQueue(itemQueue);
                    if (listView.getAdapter().getCount() > 1) {
                        itemQueue = (ItemQueue) listView.getAdapter().getItem(1);
                        itemQueue.index += 3;
                        queueViewModel.updateItemQueue(itemQueue);
                        Toast.makeText(getApplicationContext(), itemQueue.name + " está atrasado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                findViewById(R.id.load).setVisibility(View.VISIBLE);
                currentClerk = clerkList.get(i);
                queueViewModel.getListItemQueue(clerkList.get(i).key).observe(MainActivity.this, new Observer<List<ItemQueue>>() {
                    @Override
                    public void onChanged(@Nullable List<ItemQueue> itemQueues) {
                        listView = findViewById(R.id.queue);

                        Collections.sort(itemQueues, new Comparator<ItemQueue>() {
                            @Override
                            public int compare(final ItemQueue object1, final ItemQueue object2) {
                                return object1.index < object2.index ? -1 : 1;
                            }
                        });

                        int index = 1;
                        for (ItemQueue itemQueue : itemQueues) {
                            itemQueue.indexShow = index++;
                        }

                        ItemQueueArrayAdapter adapter = new ItemQueueArrayAdapter(getApplicationContext(), itemQueues);
                        listView.setAdapter(adapter);
                        findViewById(R.id.load).setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void observers() {
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    checkConfig(user);
                    loadSpinner(user);
                    MainActivity.this.user = user;
                    Intent startIntent = new Intent(getApplicationContext(), RequestService.class);
                    startIntent.putExtra(RequestService.REQUEST_ID, user.getEmail());
                    startService(startIntent);
                }
            }
        });
    }

    private void loadSpinner(User user) {
        queueViewModel.getQueue(user.getEmail()).observe(this, new Observer<List<Clerk>>() {
            @Override
            public void onChanged(@Nullable List<Clerk> clerks) {
                clerkList = clerks;
                ArrayList<String> names = new ArrayList();
                for (Clerk c : clerks) {
                    names.add(c.name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner = (MaterialSpinner) findViewById(R.id.clerks);
                spinner.setAdapter(adapter);
            }
        });
    }

    private void checkConfig(final User user) {
        queueViewModel.getEstablishment(user.getEmail()).observe(this, new Observer<Establishment>() {
            @Override
            public void onChanged(@Nullable Establishment establishment) {
                if (establishment == null) {
                    Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                    intent.putExtra(ConfigActivity.USER_KEY, user.getEmail());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                new LogoutTask().execute();
                break;
            case R.id.settings: {
                Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                intent.putExtra(ConfigActivity.USER_KEY, user.getEmail());
                startActivity(intent);
                break;
            }
            case R.id.queue: {
                Intent intent = new Intent(this, QueueActivity.class);
                intent.putExtra(QueueActivity.CLERK_KEY, user.getEmail());
                startActivity(intent);
                break;
            }
            case R.id.checkin:
                Intent intent = new Intent(this, RequestQueueActivity.class);
                intent.putExtra(RequestQueueActivity.REQUEST_KEY, user.getEmail());
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    class LogoutTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                userViewModel.deleteUser();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }
}
