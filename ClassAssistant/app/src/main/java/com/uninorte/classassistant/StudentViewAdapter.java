package com.uninorte.classassistant;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import minimum.MinExam;

/**
 * Created by asmateus on 3/04/17.
 */

public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.MyHolder> {

    private LayoutInflater inflater;
    private List<MinExam> data = Collections.emptyList();
    private Context master;
    private Intent i;

    public StudentViewAdapter(Context context, List<MinExam> data) {
        this.master = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_student, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        MinExam info = data.get(position);
        holder.tv.setText(info.getName());
        //holder.tv2.setText(info.result);
        //holder.tv3.setText(info.perc);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv;
        private TextView tv2;
        private  TextView tv3;

        public MyHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.student_name);
            tv3 = (TextView) itemView.findViewById(R.id.eval_perc);
            tv2 = (TextView) itemView.findViewById(R.id.eval_value);
            tv.setOnClickListener(ViewOnClickListener);
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
