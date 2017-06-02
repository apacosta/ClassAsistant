package com.uninorte.classassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import adapters.EvaluationAdapter;
import entities.Codes;
import entities.Signature;
import entities.StandardTransactionOutput;
import io.InformationTracker;
import io.TransactionListeners;
import minimum.MinEvaluation;
import minimum.MinRubric;
import minimum.MinSignature;
import minimum.MinStudent;

public class SignatureActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TransactionListeners {

    private TextView appbar_title;

    // Evaluation list variables
    private EvaluationAdapter view_adapter;
    private RecyclerView recycler_view;

    // Intents
    private Intent student_request_intent;
    private Intent add_evaluation;
    private Intent view_edit_evaluation;

    // Overall controller
    private Signature signature;
    private MinSignature parent_sig;

    // Navigation Drawer lists
    private Menu navdrawer_menu;
    private ArrayList<MinStudent> current_students_ids = new ArrayList<>();
    private ArrayList<MinStudent> join_request_ids = new ArrayList<>();
    private HashMap<String, String> current_students_score = new HashMap<>();

    // Firebase variables
    private FirebaseDatabase database;
    private ArrayList<InformationTracker> trackers = new ArrayList<>();
    private ArrayList<InformationTracker> temp_trackers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signature.getSumEvaluationWeights() < 100)
                    createNewEvalDialog();
                else
                    Toast.makeText(SignatureActivity.this, "This min_signature is FULL", Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header_view = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        this.navdrawer_menu = navigationView.getMenu();
        //this.current_std_group_id = R.id.join_request_group_id;
        //this.join_request_group_id = R.id.join_request_group_id;

        // INITIALIZATION CODE
        this.appbar_title = (TextView) findViewById(R.id.activity_name);
        this.recycler_view = (RecyclerView) findViewById(R.id.recycle_evaluation_list);

        // Intents
        createIntents();

        MinSignature sig = (MinSignature) getIntent().getSerializableExtra("selected_signature");
        parent_sig = sig;
        this.appbar_title.setText(sig.getName());
        this.signature = new Signature(sig.getID());
        this.signature.setName(sig.getName());
        this.signature.setOwner(sig.getOwner());

        // Retreive information from Firebase
        database = FirebaseDatabase.getInstance();
        buildFullSignatureInformation(sig);
    }

    public void deleteEvaluation(int index) {
        String eval_id = this.signature.getEvaluations().get(index).getID();
        this.signature.getEvaluations().remove(index);

        String a = "";
        for(int i = 0; i < parent_sig.getEvaluations().split(";").length; ++i) {
            if(i != index) {
                a += parent_sig.getEvaluations().split(";")[i] + ";";
            }
        }

        this.parent_sig.setEvaluations(a);

        // Removing from firebase
        database.getReference().child("evaluations").child(eval_id).removeValue();
        database.getReference().child("signatures").child(signature.getID()).child("evaluations").setValue(a);

        // Remove evaluation results for each student
        for(MinStudent s: signature.getStudents()) {
            database.getReference().child("eval-results").child(s.getID()).child(eval_id).removeValue();
        }
        removeAllInformationTrackers();
        drawUI();
    }

    public void removeAllInformationTrackers() {
        for(InformationTracker tr: this.trackers) {
            tr.unSubscribeFromSource();
            tr.removeListener(-1);
        }

        this.trackers = new ArrayList<>();
    }

    private void createIntents() {
        this.add_evaluation = new Intent(this, DialogNewEvaluation.class);
        this.student_request_intent = new Intent(this, DialogJoinSignature.class);
        this.view_edit_evaluation = new Intent(this, EvaluationActivity.class);
    }

    private void createNewEvalDialog() {
        this.startActivityForResult(add_evaluation, Codes.EVALUATION_CREATE_REQUEST);
    }

    private void createNewEvaluation(String n, int w) {
        // Get new evaluation id
        int max = 0;
        for(MinEvaluation e: this.signature.getEvaluations()) {
            if(max < Integer.parseInt(e.getID().split("_")[1])) {
                max = Integer.parseInt(e.getID().split("_")[1]);
            }
        }
        ++max;
        String tail = "";
        if(max < 10)
            tail += "0";
        tail += max;

        String new_id = this.signature.getID() + "_" + tail;

        // Upload changes to database (EVALUATIONS)
        database.getReference().child("evaluations").child(new_id).child("annotations").setValue("");
        database.getReference().child("evaluations").child(new_id).child("id").setValue(new_id);
        database.getReference().child("evaluations").child(new_id).child("rubric").setValue("");
        database.getReference().child("evaluations").child(new_id).child("state").setValue("");
        database.getReference().child("evaluations").child(new_id).child("name").setValue(n);
        database.getReference().child("evaluations").child(new_id).child("weight").setValue("" + w);

        // Upload changes to database (SIGNATURE)
        database.getReference().child("signatures")
                .child(this.signature.getID()).child("evaluations").setValue(parent_sig.getEvaluations() + new_id + ";");

        parent_sig.setEvaluations(parent_sig.getEvaluations() + new_id + ";");

        InformationTracker tracker = new InformationTracker(InformationTracker.EVALUATION_TRACKER_DEEP, database, new_id);
        tracker.addListener(this);
        trackers.add(tracker);
    }

    private void buildFullSignatureInformation(MinSignature sig) {
        if(!sig.getStudents().equals("")) {
            // Get all students representation
            for (String s : sig.getStudents().split(";")) {
                InformationTracker tracker = new InformationTracker(InformationTracker.STUDENTS_TRACKER_DEEP, database, s);
                tracker.addListener(this);
                trackers.add(tracker);
            }
        }

        if(!sig.getStudents().equals("") && !sig.getEvaluations().equals("")) {
            // Get all evaluations results per student
            for (String s: sig.getStudents().split(";")) {
                InformationTracker tracker =
                        new InformationTracker(InformationTracker.STUDENTS_SCORE_TRACKER, database, sig.getID()+";"+s);
                tracker.addListener(this);
                trackers.add(tracker);
            }
        }

        if(!sig.getPetitions().equals("")) {
            // Get all petitions representation
            for (String s : sig.getPetitions().split(";")) {
                InformationTracker tracker = new InformationTracker(InformationTracker.PETITIONS_TRACKER, database, s);
                tracker.addListener(this);
                trackers.add(tracker);
            }
        }

        if(!sig.getEvaluations().equals("")) {
            // Get all evaluations representations
            for (String s : sig.getEvaluations().split(";")) {
                InformationTracker tracker = new InformationTracker(InformationTracker.EVALUATION_TRACKER_DEEP, database, s);
                tracker.addListener(this);
                trackers.add(tracker);
            }
        }
    }

    private void attachInformationToSignature(StandardTransactionOutput out) {
        if(out.getResultType() == InformationTracker.EVALUATION_TRACKER_DEEP) {
            MinEvaluation ev = new MinEvaluation();
            ev.setID(out.getContent().get("id"));
            ev.setName(out.getContent().get("name"));
            ev.setWeight(Integer.parseInt(out.getContent().get("weight")));

            MinRubric m = new MinRubric(out.getContent().get("rubric"));
            ev.setRubric(m);

            this.signature.replaceEvaluation(ev);
        }
        else if(out.getResultType() == InformationTracker.STUDENTS_TRACKER_DEEP) {
            MinStudent std = new MinStudent();
            std.setID(out.getContent().get("email").split("@")[0]);
            std.setSignatures(out.getContent().get("courses"));
            std.setName(out.getContent().get("name"));

            this.signature.replaceStudent(std);
        }
        else if(out.getResultType() == InformationTracker.PETITIONS_TRACKER) {
            MinStudent std = new MinStudent();
            std.setID(out.getContent().get("email").split("@")[0]);
            std.setSignatures(out.getContent().get("courses"));
            std.setName(out.getContent().get("name"));

            this.signature.replacePetitionStudent(std);
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

        boolean check1 = signature.getEvaluations().size() == parent_sig.getEvaluations().split(";").length ||
                         parent_sig.getEvaluations().split(";")[0].equals("");

        boolean check2 = signature.getStudents().size() == parent_sig.getStudents().split(";").length ||
                parent_sig.getStudents().split(";")[0].equals("");


        boolean check3 = signature.getPetitions().size() == parent_sig.getPetitions().split(";").length ||
                parent_sig.getPetitions().split(";")[0].equals("");

        // Check if trackers finished
        if(check1 && check2 && check3) {
            drawUI();
            addSignatureStudents();
        }
    }

    private double calculateScore(String id) {
        String[] res = this.current_students_score.get(id).split("@")[0].split("-");
        double total_sig_per_std = 0.0;
        double exam_weight;
        double exam_result;
        double temp;

        String[] r;
        for(int i = 0; i < res.length; ++i) {
            exam_weight = Integer.parseInt(res[i].split(":")[0]);
            exam_result = 0.0;
            String[] rubric_vals = res[i].split(":");
            String[] rub_elements;
            String[] element;

            // "40   (50  4.6  50  4.8)       60_40,4.7;60,5"

            for(int j = 1; j < rubric_vals.length; ++j) {
                temp = 0.0;
                rub_elements = rubric_vals[j].split("_");
                r = rub_elements[1].split(";");
                for(int h = 0; h < r.length; ++h) {
                    element = r[h].split(",");
                    temp += Integer.parseInt(element[0])*Double.parseDouble(element[1]);
                }
                temp = temp*Integer.parseInt(rub_elements[0]);
                exam_result += temp;
            }
            exam_result = exam_weight*exam_result/(100*100*100);

            total_sig_per_std += exam_result;
        }
        return Math.floor(total_sig_per_std * 100) / 100;
    }

    private void addSignatureStudents() {
        Log.d("OnResult", "GOT HERE WITH ");
        if(current_students_ids.size() == signature.getStudents().size()) {
            for(int i = 0; i < current_students_ids.size(); ++i) {
                this.navdrawer_menu.getItem(1).getSubMenu().removeItem(2000 + i);
            }
            this.current_students_ids = new ArrayList<>();
        }

        Log.d("students", current_students_score.keySet().toString());
        for (MinStudent s : this.signature.getStudents()) {
            Log.d("students", s.getID());
            if(!this.current_students_score.containsKey(s.getID()))
                this.navdrawer_menu.getItem(1).getSubMenu()
                        .add(0, 2000 + this.current_students_ids.size(), 0, "(0.0)    " + s.getName());
            else
                this.navdrawer_menu.getItem(1).getSubMenu().add(0, 2000 + this.current_students_ids.size(),
                        0, "(" + calculateScore(s.getID()) + ")   " + s.getName());

            this.current_students_ids.add(s);
        }

        if(join_request_ids.size() == signature.getStudents().size()) {
            for(int i = 0; i < join_request_ids.size(); ++i) {
                this.navdrawer_menu.getItem(0).getSubMenu().removeItem(1000 + i);
            }
            this.current_students_ids = new ArrayList<>();
        }

        boolean ck;
        for(MinStudent s: this.signature.getPetitions()) {
            Log.d("OnResult", "joins sizes: " + this.join_request_ids.size());
            ck = false;
            for(MinStudent ss: this.signature.getStudents()) {
                if(ss.getID().equals(s.getID())) {
                    ck = true;
                    break;
                }
            }
            if(ck == false) {
                this.navdrawer_menu.getItem(0).getSubMenu().add(0, 1000 + this.join_request_ids.size(), 0, s.getName());
                this.join_request_ids.add(s);
            }
        }

    }

    public void loadEvaluationActivity(int index) {
        this.view_edit_evaluation.putExtra("evaluation", this.signature.getEvaluations().get(index));
        this.view_edit_evaluation.putExtra("default_rubric", this.signature.getDefaultRubricId());
        this.view_edit_evaluation.putExtra("min_signature", this.parent_sig);

        this.startActivityForResult(this.view_edit_evaluation, Codes.EVALUATION_VIEW_REQUEST_CODE);
    }


    private void drawUI() {
        Log.d("evaluations", parent_sig.getEvaluations());
        view_adapter = new EvaluationAdapter(this, signature.getEvaluations(), this.view_edit_evaluation);

        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void joinStudentToSignature(int index) {
        MinStudent ss = this.join_request_ids.get(index);

        // Remove and add to respective lists
        this.navdrawer_menu.getItem(0).getSubMenu().removeItem(1000 + index);
        this.join_request_ids.remove(index);

        Log.d("OnResult", "joins after remove: " + this.join_request_ids.toString());
        this.navdrawer_menu.getItem(1).getSubMenu().add(0, 2000 + this.current_students_ids.size(), 0, ss.getName());
        this.current_students_ids.add(ss);

        // First add student to current students to avoid an unneeded database call
        this.signature.replaceStudent(ss);

        // Remove from signatures
        int i;
        for(i = 0; i < signature.getPetitions().size(); ++i) {
            if(signature.getPetitions().get(i).getID().equals(ss.getID()))
                break;
        }
        signature.getPetitions().remove(i);

        String a = "";
        for(String s: this.parent_sig.getPetitions().split(";")) {
            if(!ss.getID().equals(s)) {
                a += s + ";";
            }
        }

        this.parent_sig.setPetitions(a);
        this.parent_sig.setStudents(this.parent_sig.getStudents() + ss.getID() + ";");

        // Update database
        this.database.getReference().child("signatures").child(this.signature.getID()).child("petitions").setValue(a);
        this.database.getReference().child("signatures")
                .child(this.signature.getID()).child("users").setValue(this.parent_sig.getStudents());
        this.database.getReference().child("students")
                .child(ss.getID()).child("courses").setValue(ss.getSignatures()+this.signature.getID()+";");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent();
            i.putExtra("id", parent_sig.getID());
            setResult(Codes.RESULT_OK, i);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signature, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(item.getItemId() - 1000 < 1000) {
            // This is a class join request, so display the join dialog
            id -= 1000;
            this.student_request_intent.putExtra("index", id);
            this.student_request_intent.putExtra("name", this.signature.getPetitions().get(id).getName());
            this.startActivityForResult(this.student_request_intent, Codes.JOIN_SIGNATURE_REQUEST);
        }
        else {
            // This is a current, so display the student activity
        }

        Log.d("ItemSelected", "" + id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void manageTransactionResult(StandardTransactionOutput output) {
        if(!output.isNull()) {
            switch (output.getResultType()) {
                case InformationTracker.EVALUATION_TRACKER_DEEP:
                    attachInformationToSignature(output);
                    break;
                case InformationTracker.STUDENTS_TRACKER_DEEP:
                    attachInformationToSignature(output);
                    break;
                case InformationTracker.PETITIONS_TRACKER:
                    attachInformationToSignature(output);
                    break;
                case InformationTracker.STUDENTS_SCORE_TRACKER:
                    attachInformationToSignature(output);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resCod, Intent data) {
        if(reqCode == Codes.EVALUATION_CREATE_REQUEST) {
            if(resCod == Codes.RESULT_OK) {
                String new_sig_name = data.getStringExtra("name");
                Integer new_sig_weight = Integer.parseInt(data.getStringExtra("weight"));
                if(this.signature.getSumEvaluationWeights() + new_sig_weight <= 100)
                    createNewEvaluation(new_sig_name, new_sig_weight);
                else
                    Toast.makeText(this, "Signature evaluation OVERFLOW", Toast.LENGTH_SHORT).show();
            }
        }
        else if(reqCode == Codes.JOIN_SIGNATURE_REQUEST) {
            if(resCod == Codes.RESULT_OK) {
                joinStudentToSignature(data.getIntExtra("index", 0));
            }
        }
        else if(reqCode == Codes.EVALUATION_VIEW_REQUEST_CODE) {
            if(resCod == Codes.RESULT_OK) {
                buildFullSignatureInformation(parent_sig);
            }
        }
    }
}
