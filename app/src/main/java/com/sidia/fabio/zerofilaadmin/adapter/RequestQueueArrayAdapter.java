package com.sidia.fabio.zerofilaadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sidia.fabio.zerofilaadmin.R;
import com.sidia.fabio.zerofilaadmin.model.RequestQueue;

import java.util.List;



public class RequestQueueArrayAdapter extends BaseAdapter {

    Context ctx;
    List<RequestQueue> requestQueueList;

    public RequestQueueArrayAdapter(Context ctx, List<RequestQueue> requestQueueList) {
        this.ctx = ctx;
        this.requestQueueList = requestQueueList;

    }

    @Override
    public int getCount() {
        return requestQueueList.size();
    }

    @Override
    public Object getItem(int i) {
        return requestQueueList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        RequestQueue requestQueue = requestQueueList.get(position);
        View viewContext = LayoutInflater.from(ctx).inflate(R.layout.item_array_request_queue_adapter, viewGroup, false);

        TextView cpfDoctor = viewContext.findViewById(R.id.tv_name);
        cpfDoctor.setText(requestQueue.clerkName);

        TextView cpfClient = viewContext.findViewById(R.id.tv_cpf);
        cpfClient.setText(requestQueue.cpf);

        TextView client = viewContext.findViewById(R.id.tv_client);
        client.setText(requestQueue.name);

        return viewContext;
    }
}

