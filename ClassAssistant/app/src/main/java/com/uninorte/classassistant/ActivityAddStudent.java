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

import io.DBManagerStudent;
import minimum.MinSignature;


public class ActivityAddStudent extends Activity implements OnClickListener {

    private EditText nameText;
    private Button updateBtn, deleteBtn;
    private EditText idText;
    private MinSignature mins;

    private long _id;

    private DBManagerStudent dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Student");
        mins = (MinSignature) getIntent().getSerializableExtra("asignatura");
        setContentView(R.layout.activity_addstudent);
       // getIntent().getSerializableExtra("materia");

        nameText = (EditText) findViewById(R.id.studentname);

        updateBtn = (Button) findViewById(R.id.btn_update1);
        dbManager = new DBManagerStudent(this);
        dbManager.open();
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update1:
                String name = nameText.getText().toString();

                dbManager.insert(""+mins.getID(),name,"","","");
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

