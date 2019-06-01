
package com.sidia.fabio.zerofilaadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sidia.fabio.zerofilaadmin.R;
import com.sidia.fabio.zerofilaadmin.model.ItemQueue;

import java.util.List;


public class ItemQueueArrayAdapter extends BaseAdapter {

    Context ctx;
    List<ItemQueue> doctorList;

    public ItemQueueArrayAdapter(Context ctx, List<ItemQueue> doctorList) {
        this.ctx = ctx;
        this.doctorList = doctorList;

    }

    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int i) {
        try {
            return doctorList.get(i);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemQueue itemQueue = doctorList.get(position);
        View viewContext = LayoutInflater.from(ctx).inflate(R.layout.item_array_queue_adapter, viewGroup, false);

        TextView cpf = viewContext.findViewById(R.id.cpf);
        cpf.setText(itemQueue.cpf);

        TextView name = viewContext.findViewById(R.id.tv_name);
        name.setText(itemQueue.name);

        TextView index = viewContext.findViewById(R.id.index);
        index.setText(String.valueOf(itemQueue.indexShow));
        if (itemQueue.isAttendance) {
            index.setTextColor(viewContext.getContext().getResources().getColor(android.R.color.holo_green_light));
        }

        return viewContext;
    }
}
