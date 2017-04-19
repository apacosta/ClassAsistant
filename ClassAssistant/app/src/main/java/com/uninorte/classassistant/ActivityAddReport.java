package com.uninorte.classassistant;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.DBManagerReport;
import io.DBManagerSignature;
import io.DBRepresentation;
import minimum.MinSignature;

/**
 * Created by asmateus on 19/04/17.
 */

public class ActivityAddReport extends Activity implements View.OnClickListener {
    private TextView nameText;
    private Button updateBtn, deleteBtn;
    private EditText idText;

    private long _id;

    private DBManagerReport dbManager;
    private MinSignature min;
    private String method = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addreport);

        try{
            this.min = (MinSignature) getIntent().getSerializableExtra("materia");
            this.method = getIntent().getStringExtra("method");
        }
        catch(Exception e) {

        }

        nameText = (TextView) findViewById(R.id.reportcontent);
        dbManager = new DBManagerReport(this);
        dbManager.open();
        updateBtn = (Button) findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(this);

        if(method.equals("edit")) {
            setTitle("Crear Reporte");
            updateBtn.setText("Enviar");
        }
        else {
            setTitle("Ver reporte");

            String content = "";
            Cursor c = dbManager.fetch();
            while(c.moveToNext()) {
                String t = c.getString(c.getColumnIndexOrThrow(DBRepresentation.Report.COLUMN_TARGET));
                if (t.equals(min.getName())) {
                    content += c.getString(c.getColumnIndexOrThrow(DBRepresentation.Report.COLUMN_CONTENT));
                    content += "\n";

                }
            }
            nameText.setText(content);

            updateBtn.setText("Cerrar");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String name = nameText.getText().toString();
                if(method != null) {
                    if(method.equals("edit")) {
                        dbManager.insert("sig", min.getName(), name);
                    }
                    else {
                    }
                }
                setResult(1, new Intent());
                finish();
                break;
        }
    }
}
