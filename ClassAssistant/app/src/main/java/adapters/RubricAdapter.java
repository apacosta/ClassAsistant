package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uninorte.classassistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Codes;
import minimum.MinSignature;

/**
 * Created by asmateus on 19/04/17.
 */

public class RubricAdapter extends RecyclerView.Adapter<RubricAdapter.MyHolder>{
    private LayoutInflater inflater;
    private ArrayList<String> data;
    private Intent master_signature_intent;

    public RubricAdapter(Context master, ArrayList<String> data, Intent i) {
        this.master_signature_intent = i;
        inflater = LayoutInflater.from(master);
        this.data = data;
    }

    @Override
    public RubricAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        RubricAdapter.MyHolder holder = new RubricAdapter.MyHolder(view, this.data);
        return holder;
    }

    @Override
    public void onBindViewHolder(RubricAdapter.MyHolder holder, int position) {
        holder.signature_name.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView signature_name;
        private ArrayList<String> data;

        public MyHolder(View itemView, ArrayList<String> data) {
            super(itemView);
            this.data = data;

            signature_name = (TextView) itemView.findViewById(R.id.sig_item);
            signature_name.setOnClickListener(ViewOnClickListener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


        private View.OnClickListener ViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }
}
