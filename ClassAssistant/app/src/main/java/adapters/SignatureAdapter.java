package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uninorte.classassistant.ActivitySignatures;
import com.uninorte.classassistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Codes;
import entities.Signature;
import minimum.MinSignature;

/**
 * Created by asmateus on 5/04/17.
 */

public class SignatureAdapter extends RecyclerView.Adapter<SignatureAdapter.MyHolder> {
    private LayoutInflater inflater;
    private List<MinSignature> data = Collections.emptyList();
    private ActivitySignatures master;
    private Intent master_signature_intent;

    public SignatureAdapter(ActivitySignatures act, List<MinSignature> data, Intent i) {
        this.master_signature_intent = i;
        this.master = act;
        inflater = LayoutInflater.from(master);
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        MyHolder holder = new MyHolder(view, this.data);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        MinSignature info = data.get(position);
        holder.signature_name.setText(info.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView signature_name;
        private List<MinSignature> data;

        public MyHolder(View itemView, List<MinSignature> data) {
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
                MinSignature selected = data.get(getAdapterPosition());

                master_signature_intent.putExtra(master.getString(R.string.sig_token), selected);
                master.startActivityForResult(master_signature_intent, Codes.REQ_EVALUATION);
            }
        };
    }
}
