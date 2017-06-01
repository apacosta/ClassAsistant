package com.uninorte.classassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import adapters.StudentEvaluationAdapter;
import entities.Signature;
import entities.StandardTransactionOutput;
import io.InformationTracker;
import io.TransactionListeners;
import minimum.MinEvaluation;
import minimum.MinSignature;
import minimum.MinStudent;

public class EvaluationActivity extends AppCompatActivity implements TransactionListeners {

    TextView activity_name;
    MinEvaluation evaluation;
    MinSignature min_signature;
    Signature signature;
    String default_rubric;

    // Rubrics variable
    ArrayList<String> rubrics_names;
    ArrayList<String> rubrics_indices;

    private HashMap<String, String> current_students_score = new HashMap<>();

    // Student list variables
    StudentEvaluationAdapter view_adapter;
    RecyclerView recycler_view;

    // Intents
    Intent view_evaluate_student;

    // Firebase variables
    FirebaseDatabase database;
    ArrayList<InformationTracker> trackers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // No snack bar needed
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAvaiableRubrics();
            }
        });

        // INIT CODE
        database = FirebaseDatabase.getInstance();

        this.activity_name = (TextView) findViewById(R.id.activity_name);
        this.recycler_view = (RecyclerView) findViewById(R.id.recycle_student_list_eval_act);

        this.evaluation = (MinEvaluation) getIntent().getSerializableExtra("evaluation");
        this.min_signature = (MinSignature) getIntent().getSerializableExtra("min_signature");
        this.default_rubric = min_signature.getDefaultRubric();

        this.activity_name.setText(this.evaluation.getName());

        this.signature = new Signature(min_signature.getID());
        this.signature.setName(min_signature.getName());
        this.signature.setOwner(min_signature.getOwner());

        createIntents();
        loadStudentsFromSignature();
    }

    private void getAvaiableRubrics() {
        InformationTracker tracker = new InformationTracker(InformationTracker
                .RUBRIC_TRACKER, database, signature.getOwner().split("@")[0]);
        tracker.addListener(this);
        this.trackers.add(tracker);
    }

    private void createIntents() {
        this.view_evaluate_student = new Intent(this, TeacherActivity.class);
    }

    private void loadStudentsFromSignature() {
        // Track students scores
        for(String s: min_signature.getStudents().split(";")) {
            if(!s.isEmpty()) {
                InformationTracker tracker =
                        new InformationTracker(InformationTracker
                                .STUDENTS_SCORE_TRACKER, database, min_signature.getID() + ";" + s);
                tracker.addListener(this);
                this.trackers.add(tracker);
            }
        }

        // Track students deep info
        for(String s: min_signature.getStudents().split(";")) {
            if(!s.isEmpty()) {
                InformationTracker tracker = new InformationTracker(InformationTracker.STUDENTS_TRACKER_DEEP, database, s);
                tracker.addListener(this);
                this.trackers.add(tracker);
            }
        }
    }

    private void processInformationOutput(StandardTransactionOutput out) {
        if(out.getResultType() == InformationTracker.STUDENTS_TRACKER_DEEP) {
            MinStudent std = new MinStudent();
            std.setID(out.getContent().get("email").split("@")[0]);
            std.setSignatures(out.getContent().get("courses"));
            std.setName(out.getContent().get("name"));

            this.signature.replaceStudent(std);
        }
        else if(out.getResultType() == InformationTracker.STUDENTS_SCORE_TRACKER) {
            if(out.getContent().keySet().size() > 1) {
                for(String k: out.getContent().keySet()) {
                    if(!k.equals("sig_target")) {
                        this.current_students_score.put(k, out.getContent().get(k));
                    }
                }
            }
        }

        boolean check1 = this.signature.getStudents().size() == this.min_signature.getStudents().split(";").length ||
                this.min_signature.getStudents().split(";")[0].isEmpty();

        if(check1) {
            drawUI();
        }
    }

    private void drawUI() {
        view_adapter = new StudentEvaluationAdapter(this, signature.getStudents(), this.view_evaluate_student);

        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayRubricsInformation(HashMap<String, String> content) {
        this.rubrics_indices = new ArrayList<>();
        this.rubrics_names = new ArrayList<>();

        for(String k: content.keySet()) {
            this.rubrics_indices.add(k);
            this.rubrics_names.add(content.get(k));
        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Results");

        // Create selection dialog
        b.setSingleChoiceItems(rubrics_names.toArray(new String[rubrics_names.size()]),
                rubrics_indices.indexOf(default_rubric), new  DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateEvaluationRubric(which);
                        dialog.dismiss();
                    }
                });
        b.show();
    }

    private void updateEvaluationRubric(int which) {
        if(this.rubrics_indices.get(which).equals(default_rubric))
            return;

        DatabaseReference ref = database.getReference();

        // Update rubric in signature
        ref.child("evaluation").child(evaluation.getID()).child("rubric").setValue(this.rubrics_indices.get(which));

        // For each student that has the evaluation remove their results
        for(String k: current_students_score.keySet()) {
            String[] evaluations = current_students_score.get(k).split("@")[1].split("-");
            boolean check = false;
            int i;
            for(i = 0; i < evaluations.length; ++i) {
                if(evaluations[i].equals(this.evaluation.getID())) {
                    check = true;
                    break;
                }
            }
            if(check == false)
                break;

            ref.child("eval-results").child(k).child(evaluations[i]).removeValue();
        }

    }

    public double calculateStudentScore(String id) {
        double result = 0.0;

        if(!current_students_score.containsKey(id))
            return result;

        Log.d("Scores", current_students_score.get(id));
        String[] evaluations = current_students_score.get(id).split("@")[1].split("-");

        boolean check = false;
        int i;
        for(i = 0; i < evaluations.length; ++i) {
            if(evaluations[i].equals(this.evaluation.getID())) {
                check = true;
                break;
            }
        }
        if(check == false)
            return result;

        String student_score = current_students_score.get(id).split("@")[0].split("-")[i];

        String[] scores = student_score.split(":");
        double temp;
        for(int j = 1; j < scores.length; ++j) {
            String[] cat = scores[j].split("_");
            String[] ele = cat[1].split(";");
            temp = 0.0;
            for(int h = 0; h < ele.length; ++h) {
                temp += Double.parseDouble(ele[h].split(",")[0])*Double.parseDouble(ele[h].split(",")[1])/100;
            }
            temp = temp*Integer.parseInt(cat[0])/100;
            result += temp;
        }

        return Math.floor(result * 100) / 100;
    }

    @Override
    public void manageTransactionResult(StandardTransactionOutput output) {
        if(!output.isNull()) {
            switch (output.getResultType()) {
                case InformationTracker.STUDENTS_TRACKER_DEEP:
                    processInformationOutput(output);
                    break;
                case InformationTracker.STUDENTS_SCORE_TRACKER:
                    processInformationOutput(output);
                    break;
                case InformationTracker.RUBRIC_TRACKER:
                    displayRubricsInformation(output.getContent());
                    break;
            }
        }
    }
}
