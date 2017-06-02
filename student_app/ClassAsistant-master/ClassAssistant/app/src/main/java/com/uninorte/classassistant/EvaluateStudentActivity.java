package com.uninorte.classassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import adapters.GradingAdapter;
import entities.Codes;
import minimum.MinRubric;
import minimum.MinStudent;

public class EvaluateStudentActivity extends AppCompatActivity {

    GradingAdapter view_adapter;
    RecyclerView recycler_view;

    MinRubric rubric;
    MinStudent student;

    TextView disp_std_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_student);
        setTitle("");

        disp_std_name = (TextView) findViewById(R.id.student_grade_name);
        recycler_view = (RecyclerView) findViewById(R.id.list_element_eval);

        rubric = (MinRubric) getIntent().getSerializableExtra("rubric");
        student = (MinStudent) getIntent().getSerializableExtra("student");

        disp_std_name.setText(student.getName());

        loadList();
    }

    private void loadList() {
        view_adapter = new GradingAdapter(this, rubric);
        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateValue(int index, double val) {
        this.rubric.getScoreFields().set(index, val);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();

        i.putExtra("rubric", rubric);
        i.putExtra("student", student);

        setResult(Codes.RESULT_OK, i);

        super.onBackPressed();
    }
}
