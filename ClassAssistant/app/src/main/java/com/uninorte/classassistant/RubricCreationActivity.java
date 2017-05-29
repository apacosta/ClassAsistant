package com.uninorte.classassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import adapters.RubricAdapter;
import entities.StandardTransactionOutput;
import io.InformationTracker;
import io.TransactionListeners;
import minimum.MinCategory;
import minimum.MinRubric;
import minimum.MinSignature;

public class RubricCreationActivity extends AppCompatActivity implements TransactionListeners {

    // Firebase variables
    private FirebaseDatabase database;
    private ArrayList<InformationTracker> trackers = new ArrayList<>();

    // Rubric variables
    private MinRubric rubric;
    private String rubric_id = "";

    // Activity variables
    private TextView rubric_title;
    private EditText rubric_title_edit;
    private ImageView save_as;

    // Category list variables
    private RubricAdapter adapter;
    private RecyclerView recycler_view;

    // Intents
    private Intent view_category_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubric_creation);
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

        this.rubric_title = (TextView) findViewById(R.id.rubric_edit_name);
        this.rubric_title_edit = (EditText) findViewById(R.id.rubric_edit_field);
        this.save_as = (ImageView) findViewById(R.id.rubric_save_as);

        // INITIALIZATION CODE
        database = FirebaseDatabase.getInstance();

        // Create view Intents
        createIntents();

        // Get intent's extra
        String view_concept = getIntent().getStringExtra("concept");

        if(!view_concept.equals("new")) {
            this.rubric_id = view_concept;

            this.rubric = new MinRubric(rubric_id);
            loadRubricInformation();
        }
        else {
            this.rubric_title.setText("New Rubric");
        }
    }

    private void createIntents() {
        this.view_category_intent = new Intent(this, DialogAddSignature.class);
    }

    private void generateUIElements() {
        adapter = new RubricAdapter(this, this.rubric.getCategories(), view_category_intent);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle_rubric_edit);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadRubricInformation() {
        InformationTracker tracker = new InformationTracker(InformationTracker.RUBRIC_TRACKER_DEEP, database, this.rubric_id);
        tracker.addListener(this);
        trackers.add(tracker);
    }

    private void requestPerCategoryInformation(HashMap<String, String> hs) {
        this.rubric.setName(hs.get("name"));

        this.rubric_title.setText(rubric.getName());
        this.rubric_title_edit.setText(rubric.getName());

        this.rubric.setCategories(new ArrayList<MinCategory>());

        for(String s: hs.get("categories").split(";")) {
            InformationTracker tracker = new InformationTracker(InformationTracker.CATEGORY_TRACKER_DEEP, database, s);
            tracker.addListener(this);
            trackers.add(tracker);
        }
    }

    private void appendCategoriesToRubric(HashMap<String, String> hs) {
        MinCategory mc = new MinCategory();
        mc.setItemsWeights(Arrays.asList(hs.get("item_weight").split(";")));
        mc.setItemsDescriptions(Arrays.asList(hs.get("items").split(";")));
        mc.setName(hs.get("name"));
        mc.setWeight(Integer.parseInt(hs.get("weight")));
        mc.setId(hs.get("id"));

        this.rubric.getCategories().add(mc);

        generateUIElements();
    }

    @Override
    public void manageTransactionResult(StandardTransactionOutput output) {
        if(!output.isNull()) {
            for(String s: output.getContent().keySet()) {
                Log.d("TransactionOutput", output.getContent().get(s));
            }
            switch (output.getResultType()) {
                case InformationTracker.RUBRIC_TRACKER_DEEP:
                    requestPerCategoryInformation(output.getContent());
                    break;
                case InformationTracker.CATEGORY_TRACKER_DEEP:
                    appendCategoriesToRubric(output.getContent());
                    break;
            }
        }
    }
}
