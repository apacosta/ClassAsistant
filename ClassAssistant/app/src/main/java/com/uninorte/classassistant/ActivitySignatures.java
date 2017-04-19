package com.uninorte.classassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import adapters.SignatureAdapter;
import entities.Codes;
import io.Connector;
import io.DBRepresentation;
import io.SQLCommandGenerator;
import minimum.MinSignature;

public class ActivitySignatures extends AppCompatActivity {

    private List<MinSignature> signatures_data = new ArrayList<>();

    private SignatureAdapter view_adapter;
    private RecyclerView recycler_view;

    private Intent signature_intent;
    private Intent rubric_intent;
    private Intent add_asignature;


    private Connector cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signatures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.cc = new Connector(this, DBRepresentation.TYPE_SIGNATURE);

        // Set intents for Signature and Rubric
        this.signature_intent = new Intent(this, ActivitySignature.class);
        this.rubric_intent = new Intent(this, ActivityRubric.class);
        this.add_asignature = new Intent(this,ActivityAddAsignature.class);
        this.add_asignature.putExtra("method", "create");

        // Fill RecycleView with found data
        this.fillSignatureList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new empty signature

                startActivityForResult(add_asignature, Codes.REQ_ADD_SIGNATURE);
            }
        });
    }

    private void fillSignatureList() {
        // Initialize Signature List with the content of the database
        this.signatures_data = MinSignature.dbParse(cc.getContent(SQLCommandGenerator.getSignaturesAll()));

        view_adapter = new SignatureAdapter(this, signatures_data, signature_intent);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle);
        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
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
}
