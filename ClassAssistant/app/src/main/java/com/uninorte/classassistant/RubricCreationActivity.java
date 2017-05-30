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
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import adapters.RubricAdapter;
import entities.Codes;
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
    public MinRubric rubric;
    private String rubric_id = "";
    private String rubric_owner = "";

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
                createNewCategory();
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

        this.rubric_owner = view_concept.split("/")[0];
        this.rubric_id = view_concept.split("/")[1];
        Log.d("Rubric", view_concept);
        this.rubric = new MinRubric(rubric_id);
        loadRubricInformation();
    }

    private void createNewCategory() {
        if(!this.rubric.checkSumWeights()) {
            InformationTracker tracker = new InformationTracker(InformationTracker.CATEGORY_TRACKER, database, "create");
            tracker.addListener(this);
            trackers.add(tracker);
        }
        else {
            Toast.makeText(this, "Rubric FULL", Toast.LENGTH_LONG).show();
        }
    }

    private void createIntents() {
        this.view_category_intent = new Intent(this, DialogCategory.class);
    }

    public void generateUIElements() {
        adapter = new RubricAdapter(this, this.rubric.getCategories(), view_category_intent);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle_rubric_edit);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadRubricInformation() {
        InformationTracker tracker = new InformationTracker(InformationTracker.RUBRIC_TRACKER_DEEP, database, this.rubric_owner + "/" + this.rubric_id);
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

    private void guessNewCategoryID(HashMap<String, String> hs) {
        // Remove last tracker
        InformationTracker tr = trackers.get(trackers.size()-1);
        tr.unSubscribeFromSource();
        tr.removeListener(-1);
        trackers.remove(tr);

        int max = 0;
        for(String k: hs.keySet()) {
            if(max < Integer.parseInt(hs.get(k).split("_")[1])) {
                max = Integer.parseInt(hs.get(k).split("_")[1]);
            }
        }

        ++max;
        String tail = "";
        if(max < 10) {
            tail += "0";
        }
        tail += max;

        // Make sure no other instance takes this id
        database.getReference().child("categories").child("cat_"+tail).child("id").setValue("cat_"+tail);
        database.getReference().child("categories").child("cat_"+tail).child("item_weight").setValue("");
        database.getReference().child("categories").child("cat_"+tail).child("items").setValue("");
        database.getReference().child("categories").child("cat_"+tail).child("name").setValue("");
        database.getReference().child("categories").child("cat_"+tail).child("weight").setValue("");

        // Create a new category with this id
        MinCategory mc = new MinCategory();
        mc.setId("cat_"+tail);
        mc.setWeight(0);

        // Add this category to rubric
        this.rubric.getCategories().add(mc);

        // Repaint UI
        generateUIElements();
    }

    private void removeAllInformationTrackers() {
        for(InformationTracker tr: this.trackers) {
            tr.unSubscribeFromSource();
            tr.removeListener(-1);
        }

        this.trackers = new ArrayList<>();
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
                case InformationTracker.CATEGORY_TRACKER:
                    guessNewCategoryID(output.getContent());
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Remove all listeners
        removeAllInformationTrackers();

        if(!rubric_title_edit.getText().toString().equals("")) {
            this.rubric.setName(rubric_title_edit.getText().toString());
        }

        if(!this.rubric.getName().equals("")) {
            // Update database
            // First update rubric's database
            String s = "";
            for(MinCategory c: this.rubric.getCategories()) {
                s += c.getId() + ";";
            }

            database.getReference().child("rubrics")
                    .child(rubric_owner).child(this.rubric.getId()).child("name").setValue(this.rubric.getName());
            database.getReference().child("rubrics")
                    .child(rubric_owner).child(this.rubric.getId()).child("categories").setValue(s);

            // Now update each category
            for(MinCategory c: this.rubric.getCategories()) {
                s = "";
                for(String ss: c.getItemsDescriptions()) {
                    s += ss + ";";
                }
                database.getReference().child("categories").child(c.getId()).child("items").setValue(s);

                s = "";
                for(Integer ss: c.getItemsWeights()) {
                    s += ss + ";";
                }
                database.getReference().child("categories").child(c.getId()).child("item_weight").setValue(s);

                database.getReference().child("categories").child(c.getId()).child("id").setValue(c.getId());
                database.getReference().child("categories").child(c.getId()).child("name").setValue(c.getName());
                database.getReference()
                        .child("categories").child(c.getId()).child("weight").setValue(Integer.toString(c.getWeight()));
            }
        }

        super.onBackPressed();
    }


    @Override
    public void onActivityResult(int req_code, int res_code, Intent data) {
        if(res_code == Codes.RESULT_OK) {
            MinCategory cc = (MinCategory) data.getSerializableExtra("category");

            if(rubric.getSumWeights() + cc.getWeight() <= 100) {
                int i;
                for(i = 0; i < this.rubric.getCategories().size(); ++i) {
                    if(this.rubric.getCategories().get(i).getId().equals(cc.getId()))
                        break;
                }

                this.rubric.getCategories().remove(i);
                this.rubric.getCategories().add(cc);

                generateUIElements();
            }
            else {
                Toast.makeText(this, "Category weight overflow", Toast.LENGTH_LONG).show();
            }
        }
    }
}
