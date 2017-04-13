package com.uninorte.classassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

import entities.Exam;
import minimum.MinExam;

public class ActivityExam extends AppCompatActivity {

    private Exam exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_exam);

        // Get Serializable from master
        Serializable data = getIntent().getSerializableExtra("Selected_exam");
        exam = Exam.expandIntoExam((MinExam) data);

        // Rename activity
        this.setTitle(exam.getName());
    }
}
