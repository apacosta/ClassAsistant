package com.uninorte.classassistant;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import entities.Codes;

public class DialogJoinSignature extends Activity implements View.OnClickListener {

    private TextView nameText;
    private Button updateBtn;
    private Button ignoreBtn;

    private int index;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_join_signature);

        name = getIntent().getStringExtra("name");
        index = getIntent().getIntExtra("index", 0);

        nameText = (TextView) findViewById(R.id.student_name_request);
        nameText.setText(name);

        updateBtn = (Button) findViewById(R.id.accept_request);
        ignoreBtn = (Button) findViewById(R.id.ignore_request);
        updateBtn.setOnClickListener(this);
        ignoreBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        i.putExtra("index", index);

        switch (v.getId()) {
            case R.id.accept_request:
                setResult(Codes.RESULT_OK, i);
                finish();
                break;
            case R.id.ignore_request:
                setResult(Codes.RESULT_ERROR, i);
                finish();
                break;
        }
    }
}
