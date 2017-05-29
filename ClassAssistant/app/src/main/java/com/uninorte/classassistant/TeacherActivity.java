package com.uninorte.classassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import adapters.SignatureAdapter;
import entities.Codes;
import entities.StandardTransactionOutput;
import io.InformationTracker;
import io.TransactionListeners;
import minimum.MinSignature;

public class TeacherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TransactionListeners {

    // Signature list variables
    private List<MinSignature> signatures_data = new ArrayList<>();
    private SignatureAdapter view_adapter;
    private RecyclerView recycler_view;
    private String courses_id_stamps = "";

    // User information variables
    private TextView header_user_name;
    private TextView navbar_user_name;
    private TextView navbar_email;

    // Activity trackers
    private final ArrayList<InformationTracker> trackers = new ArrayList<>();

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = null;
    private FirebaseDatabase database;
    private String username;

    // intents
    private Intent signature_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);
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
        View header_view = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        // Bind elements
        this.header_user_name = (TextView) findViewById(R.id.teacher_appbar_user_name);
        this.navbar_user_name = (TextView) header_view.findViewById(R.id.teacher_navbar_user_name);
        this.navbar_email = (TextView) header_view.findViewById(R.id.teacher_navbar_email);

        // INITIALIZATION CODE
        // Initialize login activity if no session is active
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    username = user.getEmail().split("@")[0];

                    database = FirebaseDatabase.getInstance();
                    loadActivityView();
                }
                else {
                    proceedToLogin();
                }
            }
        };

    }

    private void proceedToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivityForResult(intent, Codes.LOGIN_REQUEST_CODE);
    }

    private void loadActivityView() {
        /*
        * Obtain user information, that is: name, courses, last online.
        * Compile user statistics.
        * Load the course list.
        */

        InformationTracker tracker = new InformationTracker(InformationTracker.TEACHERS_TRACKER_DEEP, database, username);
        tracker.addListener(this);
        this.trackers.add(tracker);

        // Create intents
        createIntents();

    }

    private void createIntents() {
        this.signature_intent = new Intent(this, SignatureActivity.class);
    }

    private void fillSignatureList() {
        view_adapter = new SignatureAdapter(this, signatures_data, signature_intent);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle_teacher);
        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void requestSignaturesInfo(String[] sig_ids) {
        for(String s: sig_ids) {
            InformationTracker tracker = new InformationTracker(InformationTracker.SIGNATURE_TRACKER_DEEP, database, s);
            tracker.addListener(this);
            this.trackers.add(tracker);
        }
    }

    private void requestGeneralScoreData(String id, String students) {
        InformationTracker tracker = new InformationTracker(InformationTracker.STUDENTS_SCORE_TRACKER, database, id+";"+students);
        tracker.addListener(this);
        this.trackers.add(tracker);
    }

    private void updateSignatureInfo(HashMap<String, String> hs) {
        for(MinSignature s: this.signatures_data) {
            if(s.getIdStr().equals(hs.get("sig_target"))) {
                int exam_weight = 0;
                ArrayList<Double> results = new ArrayList<>();
                double exam_result;
                double total_sig_per_std;
                String[] r;

                Set<String> k = hs.keySet();
                k.remove("null");
                k.remove("sig_target");
                k.remove("result_type");
                for(String v: k) {
                    String[] res = hs.get(v).split("-");
                    total_sig_per_std = 0.0;
                    for(int i = 0; i < res.length; ++i) {
                        exam_weight = Integer.parseInt(res[i].split(";")[0]);
                        exam_result = 0.0;
                        String[] rubric_vals = res[i].split(";");
                        for(int j = 1; j < rubric_vals.length; ++j) {
                            r = rubric_vals[j].split(",");
                            exam_result += Integer.parseInt(r[0])*Double.parseDouble(r[1]);
                        }
                        exam_result = exam_weight*exam_result/(100*100);
                        total_sig_per_std += exam_result;
                    }
                    results.add(total_sig_per_std);
                }

                // Get average
                exam_result = 0.0;
                for(Double e: results) {
                    exam_result += e;
                }

                exam_result = Math.floor((exam_result/results.size()) * 100) / 100;
                s.setScoreAverage(exam_result);

                // Count above average
                int i = 0;
                for(Double e: results) {
                    if(e >= s.getScoreAverage()) {
                        ++i;
                    }
                }

                s.setNumStudentsAboveAvg(i);
                s.setNumStudentsBelowAvg(s.getNumStudents()-i);
                break;
            }
        }
    }


    @Override
    public void manageTransactionResult(StandardTransactionOutput output) {
        for(String s: output.getContent().keySet()) {
            Log.d("TransactionOutput", output.getContent().get(s));
        }

        if(!output.isNull()) {
            int result_type = Integer.parseInt(output.getContent().get("result_type"));
            switch (result_type) {
                case InformationTracker.TEACHERS_TRACKER_DEEP:
                    if(!this.header_user_name.getText().equals(output.getContent().get("name"))) {
                        this.header_user_name.setText(output.getContent().get("name"));
                        this.navbar_user_name.setText(output.getContent().get("name"));
                    }

                    if(!this.navbar_email.getText().equals(output.getContent().get("email"))) {
                        this.navbar_email.setText(output.getContent().get("email"));
                    }

                    if(!this.courses_id_stamps.equals(output.getContent().get("courses"))) {
                        this.courses_id_stamps = output.getContent().get("courses");

                        String[] courses_ids = this.courses_id_stamps.split(";");
                        requestSignaturesInfo(courses_ids);
                    }
                    break;

                case InformationTracker.SIGNATURE_TRACKER_DEEP:
                    MinSignature ms = new MinSignature(1);
                    ms.setName(output.getContent().get("name"));
                    ms.setIdStr(output.getContent().get("id"));
                    ms.setNumStudents(output.getContent().get("users").split(";").length);
                    this.signatures_data.add(ms);

                    requestGeneralScoreData(output.getContent().get("id"), output.getContent().get("users"));
                    break;

                case InformationTracker.STUDENTS_SCORE_TRACKER:
                    updateSignatureInfo(output.getContent());
                    fillSignatureList();
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
        getMenuInflater().inflate(R.menu.teacher_view, menu);
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
}
