package com.uninorte.classassistant;


/**
 * Created by anshy on 9/04/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import io.DBManagerEvaluation;
import minimum.MinSignature;


public class ActivityAddEvaluation extends Activity implements OnClickListener {

    private EditText nameText;
    private Button updateBtn, deleteBtn;
    private EditText idText;
    private MinSignature mins;
    private long _id;

    private DBManagerEvaluation dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Evaluation");
        mins = (MinSignature) getIntent().getSerializableExtra("asignatura");
        setContentView(R.layout.activity_addevaluation);

        nameText = (EditText) findViewById(R.id.evaluationname);

        updateBtn = (Button) findViewById(R.id.btn_update2);
        dbManager = new DBManagerEvaluation(this);
        dbManager.open();
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update2:
                String name = nameText.getText().toString();

                dbManager.insert(name,""+mins.getID(),"","");
                setResult(RESULT_OK, new Intent());
                finish();
                break;




        }
    }

   public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), SignatureActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

}

