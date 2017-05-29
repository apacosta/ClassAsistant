package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.uninorte.classassistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Codes;
import minimum.MinCategory;
import minimum.MinSignature;

/**
 * Created by asmateus on 19/04/17.
 */

public class RubricAdapter extends RecyclerView.Adapter<RubricAdapter.MyHolder>{
    private LayoutInflater inflater;
    private ArrayList<MinCategory> data;
    private Intent master_signature_intent;

    public RubricAdapter(Context master, ArrayList<MinCategory> data, Intent i) {
        this.master_signature_intent = i;
        inflater = LayoutInflater.from(master);
        this.data = data;
    }

    @Override
    public RubricAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_list_element, parent, false);
        RubricAdapter.MyHolder holder = new RubricAdapter.MyHolder(view, this.data);
        return holder;
    }

    @Override
    public void onBindViewHolder(RubricAdapter.MyHolder holder, int position) {
        MinCategory c = data.get(position);

        holder.category_name.setText(c.getName());
        holder.num_elements.setText(Integer.toString(c.getItemsWeights().size()));
        holder.weight.setText(Integer.toString(c.getWeight()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView category_name;
        private TextView num_elements;
        private TextView weight;
        private ImageView delete;


        private ArrayList<MinCategory> data;

        public MyHolder(View itemView, ArrayList<MinCategory> data) {
            super(itemView);
            this.data = data;

            category_name = (TextView) itemView.findViewById(R.id.category_list_item);
            category_name.setOnClickListener(ViewOnClickListener);

            num_elements = (TextView) itemView.findViewById(R.id.num_elem_cat);
            weight = (TextView) itemView.findViewById(R.id.weight_cat);

            delete = (ImageView) itemView.findViewById(R.id.delete_category);
            delete.setOnClickListener(deleteOnClickListener);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        private View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("deleting", "" + getAdapterPosition());
            }
        };

        private View.OnClickListener ViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }
}
