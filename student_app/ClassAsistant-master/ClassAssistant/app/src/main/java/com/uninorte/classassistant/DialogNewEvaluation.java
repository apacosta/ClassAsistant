package com.uninorte.classassistant;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import entities.Codes;

public class DialogNewEvaluation extends Activity implements View.OnClickListener {

    private EditText nameText;
    private EditText weightText;
    private Button updateBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_new_evaluation);

        nameText = (EditText) findViewById(R.id.new_evaluation_name);
        weightText = (EditText) findViewById(R.id.new_evaluation_weight);

        updateBtn = (Button) findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String name = nameText.getText().toString();
                if(name.isEmpty() || weightText.getText().toString().isEmpty()) {
                    setResult(Codes.RESULT_ERROR, new Intent());
                }
                else {
                    Intent i = new Intent();
                    i.putExtra("name", name);
                    i.putExtra("weight", weightText.getText().toString());
                    setResult(Codes.RESULT_OK, i);
                }

                finish();
                break;
        }
    }

}
