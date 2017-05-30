package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uninorte.classassistant.R;

import java.util.Collections;
import java.util.List;

import minimum.MinEvaluation;

/**
 * Created by asmateus on 5/04/17.
 */

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.MyHolder> {
    private LayoutInflater inflater;
    private List<MinEvaluation> data = Collections.emptyList();
    private Context master;
    private Intent master_exam_intent;

    public ExamAdapter(Context context, List<MinEvaluation> data, Intent i) {
        this.master = context;
        this.master_exam_intent = i;
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
        MinEvaluation info = data.get(position);
        holder.exam_name.setText(info.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView exam_name;

        public MyHolder(View itemView) {
            super(itemView);

            exam_name = (TextView) itemView.findViewById(R.id.sig_item);
            exam_name.setOnClickListener(ViewOnClickListener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


        private View.OnClickListener ViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinEvaluation e = data.get(getAdapterPosition());
                master_exam_intent.putExtra("Selected_exam", e);
                master.startActivity(master_exam_intent);
            }
        };
    }
}
