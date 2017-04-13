package com.uninorte.classassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.ExamAdapter;
import adapters.StudentHideableAdapter;
import entities.Signature;
import io.Connector;
import io.SQLCommandGenerator;
import minimum.MinExam;
import minimum.MinSignature;
import minimum.MinStudent;

public class ActivitySignature extends AppCompatActivity {

    private Signature signature;

    private ExamAdapter exam_adapter;
    private StudentHideableAdapter student_adapter;

    private RecyclerView exam_recycler_view;
    private TextView signature_information_view;

    private Intent exam_intent;
    private Intent student_intent;

    ExpandableListView student_list_view;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        // Information field is not user enabled
        this.signature_information_view = (TextView) findViewById(R.id.signatureInformation);
        this.signature_information_view.setEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Set intents
        exam_intent = new Intent(this, ActivityExam.class);
        student_intent = new Intent(this, ActivityStudent.class);

        // Receive signature information
        Serializable master_info = getIntent().getSerializableExtra(getString(R.string.sig_token));
        this.signature = Signature.expandIntoSignature((MinSignature) master_info);

        // Set signature title
        this.setTitle(this.signature.getName());

        // The MinSignature received was trimmed down to name and id, so Signature needs to be
        // refilled
        this.dbHarvest();

        // Try something
        MinExam e;
        for(int i = 0; i < 4; ++i) {
            e = new MinExam();
            e.setName("Evaluation " + Integer.toString(i+1));
            this.signature.addEvaluation(e);
        }

        MinStudent s;
        for(int i = 0; i < 30; ++i) {
            s = new MinStudent();
            s.setName("Student " + Integer.toString(i+1));
            this.signature.addStudent(s);
        }

        // Fill list with gathered information
        loadExamInformation();
        loadStudentInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_signature, menu);

        return true;
    }

    private void dbHarvest() {
        Connector cc = new Connector();

        // Get student info
        String cmd = SQLCommandGenerator.getStudentsFromSignature(signature.getID());
        for(MinStudent s: MinStudent.dbParse(cc.getContent(cmd))) {
            this.signature.addStudent(s);
        }

        // Get evaluation info
        cmd = SQLCommandGenerator.getEvaluationFromSignature(signature.getID());
        for(MinExam e: MinExam.dbParse(cc.getContent(cmd))) {
            this.signature.addEvaluation(e);
        }

    }

    private void loadExamInformation() {
        this.exam_adapter = new ExamAdapter(this, this.signature.getEvaluations(), exam_intent);

        this.exam_recycler_view = (RecyclerView) this.findViewById(R.id.recycle);
        this.exam_recycler_view.setAdapter(this.exam_adapter);
        this.exam_recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadStudentInformation() {
        this.student_list_view = (ExpandableListView) findViewById(R.id.signatureStudents);
        expandableListDetail = new HashMap<String, List<String>>();
        ArrayList<String> stud_list = new ArrayList<>();
        for(MinStudent e: this.signature.getStudents()) {
            stud_list.add(e.getName());
        }
        expandableListDetail.put("Students", stud_list);

        ArrayList<String> expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        this.student_adapter = new StudentHideableAdapter(this, expandableListTitle, expandableListDetail);
        this.student_list_view.setAdapter(this.student_adapter);
        this.student_list_view.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        this.student_list_view.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        this.student_list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                MinStudent s = signature.getStudents().get(childPosition);
                student_intent.putExtra("Selected_student", s);
                startActivity(student_intent);
                return false;
            }
        });
    }
}
