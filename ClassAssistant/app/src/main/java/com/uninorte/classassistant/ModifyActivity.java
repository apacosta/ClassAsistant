package com.uninorte.classassistant;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import io.DBManagerSignature;

public class ModifyActivity extends Activity implements OnClickListener {

    private EditText titleText;
    private Button updateBtn, deleteBtn;


    private long _id;

    private DBManagerSignature dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modificar Datos");

        setContentView(R.layout.activity_addasignature);

        dbManager = new DBManagerSignature(this);
        dbManager.open();

        titleText = (EditText) findViewById(R.id.asignaturename);


        updateBtn = (Button) findViewById(R.id.btn_update);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");


        _id = Long.parseLong(id);

        titleText.setText(name);


        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String title = titleText.getText().toString();


                dbManager.update(_id, title,"","","","");
                this.returnHome();
                break;


        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), ActivitySignatures.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
