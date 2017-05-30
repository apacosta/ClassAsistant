package adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uninorte.classassistant.R;
import com.uninorte.classassistant.SignatureActivity;

import java.util.Collections;
import java.util.List;

import entities.Codes;
import minimum.MinEvaluation;

/**
 * Created by asmateus on 30/05/17.
 */

public class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.MyHolder> {
    private LayoutInflater inflater;
    private List<MinEvaluation> data = Collections.emptyList();
    private SignatureActivity master;
    private Intent master_signature_intent;

    public EvaluationAdapter(SignatureActivity act, List<MinEvaluation> data, Intent i) {
        this.master_signature_intent = i;
        this.master = act;
        inflater = LayoutInflater.from(master);
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.evaluation_list_element, parent, false);
        MyHolder holder = new MyHolder(view, this.data);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        MinEvaluation info = data.get(position);
        holder.signature_name.setText(info.getName());
        holder.weight.setText("" + data.get(position).getWeight());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView signature_name;
        private TextView weight;
        private List<MinEvaluation> data;

        public MyHolder(View itemView, List<MinEvaluation> data) {
            super(itemView);
            this.data = data;

            signature_name = (TextView) itemView.findViewById(R.id.eval_name_list);
            weight = (TextView) itemView.findViewById(R.id.weight_eval_list);
            signature_name.setOnClickListener(ViewOnClickListener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


        private View.OnClickListener ViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MinEvaluation selected = data.get(getAdapterPosition());

                //master_signature_intent.putExtra("selected_signature", selected);
                //master.startActivityForResult(master_signature_intent, Codes.REQ_EVALUATION);
            }
        };
    }
}
