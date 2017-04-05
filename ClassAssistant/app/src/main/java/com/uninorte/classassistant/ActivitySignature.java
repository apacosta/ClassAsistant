package com.uninorte.classassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.ExamAdapter;
import adapters.StudentHideableAdapter;
import minimum.MinSignature;

public class ActivitySignature extends AppCompatActivity {

    private List<MinSignature> data = new ArrayList<>();

    private ExamAdapter exam_adapter;
    private StudentHideableAdapter student_adapter;

    private RecyclerView exam_recycler_view;
    private TextView signature_information;

    private Intent exam_intent;
    private Intent student_intent;

    ExpandableListView student_list_view;
    List<String> student_list_title;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_signature);

        // Set signature title
        this.setTitle("Single Signature");

        // Set intents
        exam_intent = new Intent(this, SingleExam.class);
        student_intent = new Intent(this, StudentInfo.class);

        // Gather information from database of students and exams

        // Fill list with gathered information
        list_demo();
        expand_demo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_single_signature, menu);
        return true;
    }

    private void list_demo() {
        String[] names = {
                "EXAMEN 1", "EXAMEN 2", "EXAMEN 3", "EXAMEN 4"
        };

        for(String e: names) {
            MinSignature f = new MinSignature(e);
            data.add(f);
        }

        view_adapter = new ViewAdapter(this, data, exam_intent);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle);
        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        this.info = (TextView) this.findViewById(R.id.signatureInformation);
        this.info.setEnabled(false);
        this.info.setText("This is an information field.\n"
                + "Number of students: 16\n"
                + "Number of exams evaluated: 2\n"
                + "Failed: 7\n"
                + "Passed: 9\n"
        );
    }

    private void expand_demo() {
        expandableListView = (ExpandableListView) findViewById(R.id.signatureStudents);
        expandableListDetail = StudentList.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new StudentsAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                startActivity(student_intent);
                return false;
            }
        });
    }
}
