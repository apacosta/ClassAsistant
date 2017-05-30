package com.uninorte.classassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import adapters.EvaluationAdapter;
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
    Signature signature;
    MinSignature parent_sig;

    // Firebase variables
    FirebaseDatabase database;
    ArrayList<InformationTracker> trackers = new ArrayList<>();
    ArrayList<InformationTracker> temp_trackers = new ArrayList<>();

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
    }

    private void buildFullSignatureInformation(MinSignature sig) {
        // Get all students representation
        for(String s: sig.getStudents().split(";")) {
            InformationTracker tracker = new InformationTracker(InformationTracker.STUDENTS_TRACKER_DEEP, database, s);
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

        // Check if trackers finished
        if(this.signature.getEvaluations().size() == this.parent_sig.getEvaluations().split(";").length &&
                this.signature.getStudents().size() == this.parent_sig.getStudents().split(";").length) {
            drawUI();
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
            }
        }
    }
}
