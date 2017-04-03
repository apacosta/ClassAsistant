package com.uninorte.classassistant;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by asmateus on 3/04/17.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyHolder> {

    private LayoutInflater inflater;
    private List<MinSignature> data = Collections.emptyList();
    private Context master;
    private Intent i;

    public ViewAdapter(Context context, List<MinSignature> data, Intent i) {
        this.master = context;
        this.i = i;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        MinSignature info = data.get(position);
        holder.tv.setText(info.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv;

        public MyHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.sig_item);
            tv.setOnClickListener(ViewOnClickListener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


        private View.OnClickListener ViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                master.startActivity(i);
            }
        };
    }
}
