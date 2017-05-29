package com.uninorte.classassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import adapters.SignatureAdapter;
import entities.Codes;
import entities.StandardTransactionOutput;
import io.Connector;
import io.DBRepresentation;
import io.InformationTracker;
import io.SQLCommandGenerator;
import io.TransactionListeners;
import minimum.MinSignature;

public class OldActivitySignatures extends AppCompatActivity implements TransactionListeners {

    private List<MinSignature> signatures_data = new ArrayList<>();

    private SignatureAdapter view_adapter;
    private RecyclerView recycler_view;

    private Intent signature_intent;
    private Intent rubric_intent;
    private Intent add_asignature;


    private Connector cc;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = null;
    private FirebaseDatabase database;
    private boolean account_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signatures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize login activity if no session is active
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                Log.d("Hello", "Hello world");
                if (user != null) {
                    // User is signed in
                    Log.d("Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                    account_status = true;

                    Log.d("Account", user.getEmail());

                    database = FirebaseDatabase.getInstance();
                    loadActivityView();
                } else {
                    // User is signed out
                    Log.d("Firebase", "onAuthStateChanged:signed_out");
                    account_status = false;
                    proceedToLogin();
                }
                // ...
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new empty signature

                startActivityForResult(add_asignature, Codes.REQ_ADD_SIGNATURE);
            }
        });
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
        * */

        InformationTracker tracker = new InformationTracker(InformationTracker.SIGNATURE_TRACKER_DEEP, database, "pr01");
        tracker.addListener(this);

        // Set intents for Signature and Rubric
        //this.signature_intent = new Intent(this, SignatureActivity.class);
        //this.rubric_intent = new Intent(this, ActivityRubric.class);
        //this.add_asignature = new Intent(this,DialogAddSignature.class);
        //this.add_asignature.putExtra("method", "create");

        // Fill RecycleView with found data
        //this.fillSignatureList();

    }

    private void fillSignatureList() {
        // Initialize Signature List with the content of the database
        /*this.signatures_data = MinSignature.dbParse(cc.getContent(SQLCommandGenerator.getSignaturesAll()));

        view_adapter = new SignatureAdapter(this, signatures_data, signature_intent);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle);
        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));*/
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
    public void onActivityResult(int req_code, int res_code, Intent data){

        this.fillSignatureList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signatures, menu);
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

    @Override
    public void manageTransactionResult(StandardTransactionOutput output) {
        for(String s: output.getContent().keySet()) {
            Log.d("TransactionOutput", output.getContent().get(s));
        }
    }
}
