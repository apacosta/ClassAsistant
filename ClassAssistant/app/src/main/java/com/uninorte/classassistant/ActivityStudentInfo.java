package com.uninorte.classassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapters.StudentAdapter;
import minimum.MinExam;

public class ActivityStudentInfo extends AppCompatActivity {

    private List<MinExam> data = new ArrayList<>();
    private StudentAdapter view_adapter;
    private RecyclerView recycler_view;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        list_demo();
    }

    public void list_demo() {
        String[] names = {
                "EXAM 1", "EXAM 2", "EXAM 3", "EXAM 4"
        };

        for(String e: names) {
            MinExam f = new MinExam();
            f.setName(e);
            //f.perc = "25%";
            //f.result = "5.0";
            data.add(f);
        }

        view_adapter = new StudentAdapter(this, data);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle);
        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

    }
}
