package adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uninorte.classassistant.EvaluationActivity;
import com.uninorte.classassistant.R;

import java.util.Collections;
import java.util.List;

import minimum.MinEvaluation;
import minimum.MinStudent;

/**
 * Created by asmateus on 1/06/17.
 */

public class StudentEvaluationAdapter extends RecyclerView.Adapter<StudentEvaluationAdapter.MyHolder> {
    private LayoutInflater inflater;
    private List<MinStudent> data = Collections.emptyList();
    private EvaluationActivity master;
    private Intent master_signature_intent;

    public StudentEvaluationAdapter(EvaluationActivity act, List<MinStudent> data, Intent i) {
        this.master_signature_intent = i;
        this.master = act;
        inflater = LayoutInflater.from(master);
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.student_element_eval_list, parent, false);
        MyHolder holder = new MyHolder(view, this.data);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        MinStudent info = data.get(position);
        holder.student_name.setText(info.getName());
        holder.student_score.setText("" + master.calculateStudentScore(data.get(position).getID()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView student_name;
        private TextView student_score;
        private List<MinStudent> data;

        public MyHolder(View itemView, List<MinStudent> data) {
            super(itemView);
            this.data = data;

            student_name = (TextView) itemView.findViewById(R.id.student_name_eval_list);
            student_score = (TextView) itemView.findViewById(R.id.student_score_eval);
            student_name.setOnClickListener(ViewOnClickListener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


        private View.OnClickListener ViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                master.loadGradingDialog(getAdapterPosition());
            }
        };

    }
}
