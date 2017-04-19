package com.uninorte.classassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.ExamAdapter;
import adapters.StudentHideableAdapter;
import entities.Codes;
import entities.Signature;
import io.Connector;
import io.DBManagerSignature;
import io.DBRepresentation;
import io.SQLCommandGenerator;
import io.SQLPacket;
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
    private Intent add_student;
    private Intent add_evaluation;

    private Intent rename_intent;
    private Intent report_intent;


    ExpandableListView student_list_view;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        // Information field is not user enabled
        this.signature_information_view = (TextView) findViewById(R.id.signatureInformation);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Set intents
        exam_intent = new Intent(this, ActivityExam.class);
        student_intent = new Intent(this, ActivityStudent.class);
        add_student = new Intent(this,ActivityAddStudent.class);
        add_evaluation = new Intent(this,ActivityAddEvaluation.class);

        rename_intent = new Intent(this, ActivityAddAsignature.class);
        rename_intent.putExtra("method", "update");
        report_intent = new Intent(this, ActivityAddReport.class);

        // Receive signature information
        Serializable master_info = getIntent().getSerializableExtra(getString(R.string.sig_token));
        this.signature = Signature.expandIntoSignature((MinSignature) master_info);
        rename_intent.putExtra("materia", master_info);

        // Set signature title
        this.setTitle(this.signature.getName());

        // The MinSignature received was trimmed down to name and id, so Signature needs to be
        // refilled
        this.dbHarvest();

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
        Connector cc = new Connector(this, DBRepresentation.TYPE_STUDENT);
        this.signature.destroyEvaluations();
        this.signature.destroyStudents();
        // Get student info
        SQLPacket pkg = SQLCommandGenerator.getStudentsFromSignature(signature.getID());
        for(MinStudent s: MinStudent.dbParse(cc.getContent(pkg))) {
            this.signature.addStudent(s);
        }

        cc = new Connector(this, DBRepresentation.TYPE_EVALUATION);

        // Get evaluation info
        pkg = SQLCommandGenerator.getEvaluationFromSignature(signature.getID());
        for(MinExam e: MinExam.dbParse(cc.getContent(pkg))) {
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
        expandableListDetail.put("Estudiantes", stud_list);

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

    public void createStudent(MenuItem item) {

        add_student.putExtra("asignatura",MinSignature.reduceIntoMinSignature(this.signature));
        startActivityForResult(add_student, Codes.REQ_ADD_STUDENT);
    }

    public void createEvaluation(MenuItem item) {

        add_evaluation.putExtra("asignatura",MinSignature.reduceIntoMinSignature(this.signature));
        startActivityForResult(add_evaluation, Codes.REQ_ADD_EVALUATION);

    }

    @Override
    public void onActivityResult(int req_code, int res_code, Intent data){
        if(req_code == Codes.REQ_EVALUATION) {
            setResult(1, new Intent());
            finish();
        }
        else if(req_code == Codes.EDT_REPORT) {}
        else {
            this.dbHarvest();

            // Fill list with gathered information
            loadExamInformation();
            loadStudentInformation();
        }
    }

    public void renameSignature(MenuItem item) {
        startActivityForResult(this.rename_intent, Codes.REQ_EVALUATION);
    }

    public void deleteSignature(MenuItem item) {
        DBManagerSignature manager = new DBManagerSignature(this);
        manager.open();
        manager.delete(this.signature.getID());
        manager.close();
        onActivityResult(Codes.REQ_EVALUATION, 1, new Intent());
    }

    public void reportSignature(MenuItem item) {
        report_intent.putExtra("method", "edit");
        report_intent.putExtra("materia", MinSignature.reduceIntoMinSignature(this.signature));
        startActivityForResult(report_intent, Codes.EDT_REPORT);
    }

    public void seeReports(View view) {
        report_intent.putExtra("method", "view");
        report_intent.putExtra("materia", MinSignature.reduceIntoMinSignature(this.signature));
        startActivityForResult(report_intent, Codes.EDT_REPORT);
    }
}
