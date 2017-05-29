package adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uninorte.classassistant.R;
import com.uninorte.classassistant.TeacherActivity;

import java.util.Collections;
import java.util.List;

import entities.Codes;
import minimum.MinSignature;

/**
 * Created by asmateus on 5/04/17.
 */

public class SignatureAdapter extends RecyclerView.Adapter<SignatureAdapter.MyHolder> {
    private LayoutInflater inflater;
    private List<MinSignature> data = Collections.emptyList();
    private TeacherActivity master;
    private Intent master_signature_intent;

    public SignatureAdapter(TeacherActivity act, List<MinSignature> data, Intent i) {
        this.master_signature_intent = i;
        this.master = act;
        inflater = LayoutInflater.from(master);
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.signature_list_element, parent, false);
        MyHolder holder = new MyHolder(view, this.data);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        MinSignature info = data.get(position);
        holder.signature_name.setText(info.getName());
        holder.signature_std_num.setText("" + data.get(position).getNumStudents());
        holder.signature_avg.setText("" + data.get(position).getScoreAverage());
        holder.signature_blw_avg.setText("" + data.get(position).getNumStudentsBelowAvg());
        holder.signature_abv_avg.setText("" + data.get(position).getNumStudentsAboveAvg());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView signature_name;
        private TextView signature_avg;
        private TextView signature_std_num;
        private TextView signature_blw_avg;
        private TextView signature_abv_avg;
        private List<MinSignature> data;

        public MyHolder(View itemView, List<MinSignature> data) {
            super(itemView);
            this.data = data;

            signature_name = (TextView) itemView.findViewById(R.id.sig_name_list);
            signature_std_num = (TextView) itemView.findViewById(R.id.num_std_list);
            signature_avg = (TextView) itemView.findViewById(R.id.prom_std_list);
            signature_blw_avg = (TextView) itemView.findViewById(R.id.below_prom_std_list);
            signature_abv_avg = (TextView) itemView.findViewById(R.id.above_prom_std_list);
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
