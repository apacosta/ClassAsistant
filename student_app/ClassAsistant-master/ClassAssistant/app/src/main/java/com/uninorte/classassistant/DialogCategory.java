package com.uninorte.classassistant;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapters.CategoryAdapter;
import entities.Codes;
import io.InformationTracker;
import minimum.MinCategory;

public class DialogCategory extends AppCompatActivity {

    private CategoryAdapter adapter;

    private RecyclerView element_list;
    private EditText new_element_description;
    private EditText new_element_weight;
    private EditText new_category_weight;

    private MinCategory category;

    private Intent rename_intent;
    private boolean affected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_dialog_category);
        setTitle("Edit Category");

        this.rename_intent = new Intent(this, DialogAddSignature.class);

        // Get category from serial
        category = (MinCategory) getIntent().getSerializableExtra("category");

        // Bind elements
        element_list = (RecyclerView) findViewById(R.id.list_of_elements_cat);
        new_element_description = (EditText) findViewById(R.id.element_description);
        new_element_weight = (EditText) findViewById(R.id.element_weight);
        new_category_weight = (EditText) findViewById(R.id.category_weight_edit);

        // Load list of elements
        fillList();
    }

    private void fillList() {
        adapter = new CategoryAdapter(this, category);
        element_list.setAdapter(adapter);
        element_list.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addElementToCategory(View view) {
        if(this.category.getSumItemsWeights() < 100) {
            if(new_element_description.getText().toString().equals("") || new_element_weight.getText().toString().equals("")) {
                Toast.makeText(this, "Entry not valid", Toast.LENGTH_LONG).show();
            }
            else {
                this.category.getItemsDescriptions().add(new_element_description.getText().toString());
                this.category.getItemsWeights().add(Integer.parseInt(new_element_weight.getText().toString()));

                affected = true;
                fillList();
            }
        }
        else {
            Toast.makeText(this, "This category is FULL", Toast.LENGTH_LONG).show();
        }
    }

    public void renameCategory(View view) {
        this.startActivityForResult(this.rename_intent, Codes.REQ_ADD_SIGNATURE);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();

        if(!this.new_category_weight.getText().toString().equals("")) {
            this.category.setWeight(Integer.parseInt(this.new_category_weight.getText().toString()));
        }

        i.putExtra("category", this.category);

        if(affected)
            setResult(Codes.RESULT_OK, i);
        else
            setResult(Codes.RESULT_ERROR, i);

        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int reqCode, int resCod, Intent data) {
        if(reqCode == Codes.REQ_ADD_SIGNATURE) {
            if(resCod == Codes.RESULT_OK) {
                this.category.setName(data.getStringExtra("name"));
            }
        }
    }
}
