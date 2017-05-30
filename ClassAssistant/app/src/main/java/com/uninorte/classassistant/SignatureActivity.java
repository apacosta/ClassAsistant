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
    private Intent evaluation_intent;

    // Overall controller
    private Signature signature;
    private MinSignature parent_sig;

    // Navigation Drawer lists
    private Menu navdrawer_menu;
    private long join_request_group_id;
    private long current_std_group_id;
    private ArrayList<MinStudent> current_students_ids = new ArrayList<>();
    private ArrayList<MinStudent> join_request_ids = new ArrayList<>();

    // Firebase variables
    private FirebaseDatabase database;
    private ArrayList<InformationTracker> trackers = new ArrayList<>();
    private ArrayList<InformationTracker> temp_trackers = new ArrayList<>();

    private Intent add_evaluation;

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
                    Toast.makeText(SignatureActivity.this, "This signature is FULL", Toast.LENGTH_SHORT).show();
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

    private void createIntents() {
        this.evaluation_intent = new Intent(this, TeacherActivity.class);
        this.add_evaluation = new Intent(this, DialogNewEvaluation.class);
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
        // Get all students representation
        for(String s: sig.getStudents().split(";")) {
            InformationTracker tracker = new InformationTracker(InformationTracker.STUDENTS_TRACKER_DEEP, database, s);
            tracker.addListener(this);
            trackers.add(tracker);
        }

        // Get all students representation
        for(String s: sig.getPetitions().split(";")) {
            InformationTracker tracker = new InformationTracker(InformationTracker.PETITIONS_TRACKER, database, s);
            tracker.addListener(this);
            trackers.add(tracker);
        }

        // Get all evaluations representations
        for(String s: sig.getEvaluations().split(";")) {
            InformationTracker tracker = new InformationTracker(InformationTracker.EVALUATION_TRACKER_DEEP, database, s);
            tracker.addListener(this);
            trackers.add(tracker);
        }
    }

    private void attachInformationToSignature(StandardTransactionOutput out) {
        Log.d("EvalTracker", "" + out.getContent().get("id"));
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

        // Check if trackers finished
        if(this.signature.getEvaluations().size() == this.parent_sig.getEvaluations().split(";").length &&
                this.signature.getStudents().size() == this.parent_sig.getStudents().split(";").length &&
                this.signature.getPetitions().size() == this.parent_sig.getPetitions().split(";").length) {
            drawUI();
            addSignatureStudents();
        }
    }

    private void addSignatureStudents() {
        for(MinStudent s: this.signature.getStudents()) {
            this.navdrawer_menu.getItem(1).getSubMenu().add(0, 2000 + this.current_students_ids.size(), 0, s.getName());
            this.current_students_ids.add(s);
        }

        for(MinStudent s: this.signature.getPetitions()) {
            this.navdrawer_menu.getItem(0).getSubMenu().add(0, 1000 + this.join_request_ids.size(), 0, s.getName());
            this.join_request_ids.add(s);
        }
    }

    private void drawUI() {
        view_adapter = new EvaluationAdapter(this, signature.getEvaluations(), this.evaluation_intent);

        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resCod, Intent data) {
        if(reqCode == Codes.EVALUATION_CREATE_REQUEST) {
            if(resCod == Codes.RESULT_OK) {
                String new_sig_name = data.getStringExtra("name");
                Integer new_sig_weight = Integer.parseInt(data.getStringExtra("weight"));
                createNewEvaluation(new_sig_name, new_sig_weight);
            }
        }
    }
}
