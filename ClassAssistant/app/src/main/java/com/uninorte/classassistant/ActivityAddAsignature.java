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

import io.DBManagerSignature;
import minimum.MinSignature;


public class ActivityAddAsignature extends Activity implements OnClickListener {

    private EditText nameText;
    private Button updateBtn, deleteBtn;
    private EditText idText;

    private long _id;

    private DBManagerSignature dbManager;
    private MinSignature min;
    private String method = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addasignature);

        try{
            this.min = (MinSignature) getIntent().getSerializableExtra("materia");
            this.method = getIntent().getStringExtra("method");
        }
        catch(Exception e) {

        }
        if(method.equals("create")) {
            setTitle("Nueva asignatura");
        }
        else {
            setTitle("Cambiar nombre");
        }

        nameText = (EditText) findViewById(R.id.asignaturename);

        updateBtn = (Button) findViewById(R.id.btn_update);
        dbManager = new DBManagerSignature(this);
        dbManager.open();
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String name = nameText.getText().toString();
                if(method != null) {
                    if(method.equals("create")) {
                        dbManager.insert(name,"","","","");
                    }
                    else {
                        dbManager.update(min.getID(), name, "", "", "", "");
                    }
                }


                setResult(1, new Intent());
                finish();
                break;
        }
    }

}

