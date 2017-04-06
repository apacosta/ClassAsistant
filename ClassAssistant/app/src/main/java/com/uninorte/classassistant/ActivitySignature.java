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
import io.Connector;
import io.SQLCommandGenerator;
import minimum.MinExam;
import minimum.MinSignature;
import minimum.MinStudent;

public class ActivitySignature extends AppCompatActivity {

    private MinSignature sig_info;

    private ExamAdapter exam_adapter;
    private StudentHideableAdapter student_adapter;

    private RecyclerView exam_recycler_view;
    private TextView signature_information_view;

    private Intent exam_intent;
    private Intent student_intent;

    ExpandableListView student_list_view;
    HashMap<String, List<String>> expandableListDetail;

    List<MinStudent> student_list;
    List<MinExam> exam_list;

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
        String sig_name = this.getIntent().getStringExtra("SIG_NAME");
        this.sig_info = new MinSignature(sig_name);
        this.dbHarvest();

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

    private void dbHarvest() {
        Connector cc = new Connector();

        // Get student info
        String cmd = SQLCommandGenerator.studentsFromSignature(sig_info.getName());
        this.student_list = MinStudent.dbParse(cc.getContent(cmd));

        // Get evaluation info
        cmd = SQLCommandGenerator.evaluationFromSignature(sig_info.getName());
        this.exam_list = MinExam.dbParse(cc.getContent(cmd));

    }

    private void list_demo() {
        this.exam_adapter = new ExamAdapter(this, exam_list, exam_intent);

        this.exam_recycler_view = (RecyclerView) this.findViewById(R.id.recycle);
        this.exam_recycler_view.setAdapter(this.exam_adapter);
        this.exam_recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void expand_demo() {
        this.student_list_view = (ExpandableListView) findViewById(R.id.signatureStudents);
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
