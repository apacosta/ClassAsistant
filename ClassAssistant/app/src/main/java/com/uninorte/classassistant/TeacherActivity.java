package com.uninorte.classassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

    // Rubric list variables
    private Menu rubrics_menu_list;
    private ArrayList<String> rubrics_ids = new ArrayList<>();

    // Floating button variables
    private String signature_on_creation_name = "";

    // Activity trackers
    private ArrayList<InformationTracker> trackers = new ArrayList<>();

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = null;
    private FirebaseDatabase database;
    private String username;

    // intents
    private Intent signature_intent;
    private Intent add_signature_intent;
    private Intent modify_create_rubric;

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
                createNewSignature();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Get navigation drawer menu and add onClick listener
        Menu menu = navigationView.getMenu();
        rubrics_menu_list = menu.addSubMenu("Rubrics");

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
        this.add_signature_intent = new Intent(this, DialogAddSignature.class);
        this.modify_create_rubric = new Intent(this, RubricCreationActivity.class);
    }

    private void fillSignatureList() {
        view_adapter = new SignatureAdapter(this, signatures_data, signature_intent);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle_teacher);
        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillRubricList(HashMap<String, String> hs) {
        for(String k: hs.keySet()) {
            if(!rubrics_ids.contains(k)) {
                rubrics_menu_list.add(0, rubrics_ids.size(), 0, hs.get(k));
                rubrics_ids.add(k);
            }
        }
    }

    private void requestSignaturesInfo(String[] sig_ids) {
        this.signatures_data = new ArrayList<>();
        for(String s: sig_ids) {
            InformationTracker tracker = new InformationTracker(InformationTracker.SIGNATURE_TRACKER_DEEP, database, s);
            tracker.addListener(this);
            this.trackers.add(tracker);
        }
    }

    private void requestRubricInfo() {
        InformationTracker tracker = new InformationTracker(InformationTracker.RUBRIC_TRACKER, database, username);
        tracker.addListener(this);
        this.trackers.add(tracker);
    }

    private void requestGeneralScoreData(String id, String students) {
        InformationTracker tracker = new InformationTracker(InformationTracker.STUDENTS_SCORE_TRACKER, database, id+";"+students);
        tracker.addListener(this);
        this.trackers.add(tracker);
    }

    private void updateSignatureInfo(HashMap<String, String> hs) {
        for(MinSignature s: this.signatures_data) {
            if(s.getID().equals(hs.get("sig_target"))) {
                int exam_weight = 0;
                ArrayList<Double> results = new ArrayList<>();
                double exam_result;
                double temp;
                double total_sig_per_std;
                String[] r;

                Set<String> k = hs.keySet();
                k.remove("sig_target");

                // Example
                // "50: 40_ 50,4.6;50,4.8: 60_40,4.7;60,5"

                for(String v: k) {
                    String[] res = hs.get(v).split("@")[0].split("-");
                    total_sig_per_std = 0.0;
                    for(int i = 0; i < res.length; ++i) {
                        exam_weight = Integer.parseInt(res[i].split(":")[0]);
                        exam_result = 0.0;
                        String[] rubric_vals = res[i].split(":");
                        String[] rub_elements;
                        String[] element;
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
                    results.add(total_sig_per_std);
                }

                // Get average
                exam_result = 0.0;
                for(Double e: results) {
                    exam_result += e;
                }

                if(results.size() != 0)
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

    private void createNewSignature() {
        this.startActivityForResult(this.add_signature_intent, Codes.REQ_ADD_SIGNATURE);
    }

    private void selectNextPosibleId(Set<String> keys, HashMap<String, String> out) {

        // Remove listener completly
        InformationTracker tr = this.trackers.get(this.trackers.size()-1);
        tr.unSubscribeFromSource();
        tr.removeListener(-1);
        this.trackers.remove(tr);

        int max = 0;
        for(String k: keys) {
            if(max < Integer.parseInt(out.get(k).substring(2))) {
                max = Integer.parseInt(out.get(k).substring(2));
            }
        }
        ++max;
        String tail = "";
        if(max < 10) {
            tail += "0";
        }

        tail += max;

        // Create min_signature
        database.getReference().child("signatures").child("pr"+tail).child("default_rubric").setValue("");
        database.getReference().child("signatures").child("pr"+tail).child("evaluations").setValue("");
        database.getReference().child("signatures").child("pr"+tail).child("id").setValue("pr"+tail);
        database.getReference().child("signatures").child("pr"+tail).child("name").setValue(signature_on_creation_name);
        database.getReference().child("signatures").child("pr"+tail).child("owner").setValue(user.getEmail());
        database.getReference().child("signatures").child("pr"+tail).child("users").setValue("");
        database.getReference().child("signatures").child("pr"+tail).child("petitions").setValue("");

        // Append min_signature to the teacher
        database.getReference().child("teachers").child(username).child("courses").setValue(courses_id_stamps + "pr"+tail+";");
    }

    public void removeAllInformationTrackers() {
        for(InformationTracker tr: this.trackers) {
            tr.unSubscribeFromSource();
            tr.removeListener(-1);
        }

        this.trackers = new ArrayList<>();
    }


    @Override
    public void manageTransactionResult(StandardTransactionOutput output) {
        if(!output.isNull()) {

            int result_type = output.getResultType();
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
                        requestRubricInfo();
                    }
                    break;

                case InformationTracker.SIGNATURE_TRACKER:
                    if(output.getContent().get("special_methods").equals("create")) {
                        Set<String> keys = output.getContent().keySet();
                        keys.remove("special_methods");
                        selectNextPosibleId(keys, output.getContent());
                    }
                    break;

                case InformationTracker.SIGNATURE_TRACKER_DEEP:
                    MinSignature ms = new MinSignature();
                    ms.setName(output.getContent().get("name"));
                    ms.setID(output.getContent().get("id"));
                    ms.setStudents(output.getContent().get("users"));
                    ms.setEvaluations(output.getContent().get("evaluations"));
                    ms.setOwner(output.getContent().get("owner"));
                    ms.setDefaultRubric(output.getContent().get("default_rubric"));
                    ms.setPetitions(output.getContent().get("petitions"));

                    if(output.getContent().get("users").split(";")[0].equals(""))
                        ms.setNumStudents(0);
                    else
                        ms.setNumStudents(output.getContent().get("users").split(";").length);
                    this.signatures_data.add(ms);

                    requestGeneralScoreData(output.getContent().get("id"), output.getContent().get("users"));
                    break;

                case InformationTracker.STUDENTS_SCORE_TRACKER:
                    updateSignatureInfo(output.getContent());
                    fillSignatureList();
                    break;
                case InformationTracker.RUBRIC_TRACKER:
                    fillRubricList(output.getContent());
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resCod, Intent data) {
        if(reqCode == Codes.REQ_ADD_SIGNATURE) {
            if(resCod == Codes.RESULT_OK) {
                this.signature_on_creation_name = data.getStringExtra("name");
                InformationTracker tracker = new InformationTracker(InformationTracker.SIGNATURE_TRACKER, database, "create");
                tracker.addListener(this);
                this.trackers.add(tracker);
            }
        }
        else if(reqCode == Codes.REQ_EVALUATION) {
            if(resCod == Codes.RESULT_OK) {
                String res_id = data.getStringExtra("id");
                if(!res_id.isEmpty()) {
                    for(MinSignature s: signatures_data) {
                        if(s.getID().equals(res_id)) {
                            requestGeneralScoreData(res_id, s.getStudents());
                            break;
                        }
                    }
                }
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
        if(id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        removeAllInformationTrackers();

        if(id == R.id.new_rubric) {
            // Select next rubric
            int max = 0;
            for(String e: rubrics_ids) {
                if(max < Integer.parseInt(e.split("_")[1])) {
                    max = Integer.parseInt(e.split("_")[1]);
                }
            }
            ++max;

            String tail = "";
            if(max < 10) {
                tail += "0";
            }
            tail += max;

            this.modify_create_rubric.putExtra("concept", this.username + "/rub_" + tail);
            Log.d("NavClick", "Procediendo a crear nueva rúbrica");
        }
        else {
            this.modify_create_rubric.putExtra("concept", this.username + "/" + this.rubrics_ids.get(id));
            Log.d("NavClick", this.rubrics_ids.get(id));
        }

        startActivity(this.modify_create_rubric);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
