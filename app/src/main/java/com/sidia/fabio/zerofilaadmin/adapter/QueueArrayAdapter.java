package com.sidia.fabio.zerofilaadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sidia.fabio.zerofilaadmin.R;
import com.sidia.fabio.zerofilaadmin.model.Clerk;

import java.util.List;

public class QueueArrayAdapter extends BaseAdapter {

    Context ctx;
    List<Clerk> clerks;

    public QueueArrayAdapter(Context ctx, List<Clerk> clerks) {
        this.ctx = ctx;
        this.clerks = clerks;

    }

    @Override
    public int getCount() {
        return clerks.size();
    }

    @Override
    public Object getItem(int i) {
        return clerks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Clerk clerk = clerks.get(position);
        View viewContext = LayoutInflater.from(ctx).inflate(R.layout.item_array_clerk_adapter, viewGroup, false);

        TextView name = viewContext.findViewById(R.id.tv_name);
        name.setText(clerk.name);

        TextView specialty = viewContext.findViewById(R.id.tv_type);
        specialty.setText(clerk.type);

        return viewContext;
    }
}
