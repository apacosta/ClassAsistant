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


public class ActivityAddAsignature extends Activity implements View.OnClickListener {

    private EditText nameText;
    private Button updateBtn, deleteBtn;
    private EditText idText;

    private long _id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Asignature");

        setContentView(R.layout.activity_addasignature);

        nameText = (EditText) findViewById(R.id.asignaturename);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_undo);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");

        nameText.setText(name);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String name = nameText.getText().toString();

                this.returnHome();
                break;


        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), ActivitySignature.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}

