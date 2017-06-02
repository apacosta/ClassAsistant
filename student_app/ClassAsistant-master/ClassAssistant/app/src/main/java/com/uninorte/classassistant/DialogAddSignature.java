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

import entities.Codes;
import io.DBManagerSignature;
import minimum.MinSignature;


public class DialogAddSignature extends Activity implements OnClickListener {

    private EditText nameText;
    private Button updateBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addasignature);

        nameText = (EditText) findViewById(R.id.asignaturename);

        updateBtn = (Button) findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String name = nameText.getText().toString();
                if(name.isEmpty()) {
                    setResult(Codes.RESULT_ERROR, new Intent());
                }
                else {
                    Intent i = new Intent();
                    i.putExtra("name", name);
                    setResult(Codes.RESULT_OK, i);
                }

                finish();
                break;
        }
    }

}

