package com.uninorte.classassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import adapters.RubricAdapter;
import entities.Exam;
import io.DBManagerCategories;
import io.DBManagerRubric;
import io.DBManagerRubricTemplate;
import io.DBRepresentation;
import io.SQLCommandGenerator;
import minimum.MinExam;
import minimum.MinRubric;

public class ActivityExam extends AppCompatActivity {

    private Exam exam;
    private ActivityExam ex;
    private TextView tx;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ex = this;
        setContentView(R.layout.activity_exam);

        // Get Serializable from master
        Serializable data = getIntent().getSerializableExtra("Selected_exam");
        exam = Exam.expandIntoExam((MinExam) data);

        // Rename activity
        this.setTitle(exam.getName());
        tx = (TextView) findViewById(R.id.acumexam);
        lv = (ListView) findViewById(R.id.rubriclista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_exam, menu);

        return true;
    }

    public void renameExam(MenuItem item) {
    }

    public void deleteExam(MenuItem item) {
    }

    public void reportExam(MenuItem item) {
    }

    public void addRubric(MenuItem item) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Seleccione la rúbrica: ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);


        /*DBManagerRubricTemplate rut = new DBManagerRubricTemplate(this);
        rut.open();
        rut.insert("holi from misaka", "1,2,3,4,5");
        rut.close();*/

        /*DBManagerCategories catm = new DBManagerCategories(this);
        catm.open();
        catm.insert("elem1,elem2,elem3,elem4,elem5");
        catm.close();

        DBManagerRubric ru = new DBManagerRubric(this);
        ru.open();
        ru.insert(""+2,"20,20,20,20,20;20,20,20,20,20;20,20,20,20,20;20,20,20,20,20;20,20,20,20,20");
        ru.close();*/

        DBManagerRubricTemplate rut = new DBManagerRubricTemplate(this);
        DBManagerRubric ru = new DBManagerRubric(this);
        rut.open();
        ru.open();
        String id = "";
        Cursor c = ru.fetch();
        Cursor c2;
        while(c.moveToNext()) {
            id = c.getString(c.getColumnIndexOrThrow(DBRepresentation.Rubric.COLUMN_TEMPLATE));
            c2 = rut.fetch();
            while (c2.moveToNext()) {

                if(Long.parseLong(id)-1 == c2.getLong(c2.getColumnIndexOrThrow(DBRepresentation.RubricTemplate._ID))) {
                    String id2 = c2.getString(c2.getColumnIndexOrThrow(DBRepresentation.RubricTemplate.COLUMN_NAME));
                    arrayAdapter.add(id2);
                }
            }
        }

        ru.close();
        rut.close();


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                selectionDone(strName);
            }
        });
        builderSingle.show();
    }

    private void selectionDone(String name) {
        // Cargar categorias
        // Cargar elementos
        // Cargar pesos

        String[] categories = new String[] {};
        long tempid = 0;
        ArrayList<String[]> elementos = new ArrayList<>();
        ArrayList<String[]> pesos = new ArrayList<>();

        DBManagerRubricTemplate ru = new DBManagerRubricTemplate(this);
        ru.open();
        Cursor ccat = ru.fetch();
        while(ccat.moveToNext()) {
            if(name.equals(ccat.getString(ccat.getColumnIndexOrThrow(DBRepresentation.RubricTemplate.COLUMN_NAME)))) {
                tempid = ccat.getLong(ccat.getColumnIndexOrThrow(DBRepresentation.RubricTemplate._ID));
                String c = ccat.getString(ccat.getColumnIndexOrThrow(DBRepresentation.RubricTemplate.COLUMN_CATEGORIES));
                categories = c.split(",");
                break;
            }
        }
        ru.close();

        DBManagerCategories man = new DBManagerCategories(this);
        man.open();
        Cursor cc = man.fetch();
        while(cc.moveToNext()) {
            for(String e: categories) {
                if(e.equals(""+cc.getLong(cc.getColumnIndexOrThrow(DBRepresentation.Categories._ID)))) {
                    String c = cc.getString(cc.getColumnIndexOrThrow(DBRepresentation.Categories.COLUMN_ELEMENTS));
                    String[] elem = c.split(",");
                    elementos.add(elem);
                }
            }
        }
        man.close();

        DBManagerRubric rub = new DBManagerRubric(this);
        rub.open();
        Cursor ca = rub.fetch();tempid = 2;
        while(ca.moveToNext()) {
            if(Long.toString(tempid).equals(ca.getString(ca.getColumnIndexOrThrow(DBRepresentation.Rubric.COLUMN_TEMPLATE)))) {
                String c = ca.getString(ca.getColumnIndexOrThrow(DBRepresentation.Rubric.COLUMN_WEIGHTS));
                String[] init = c.split(";");
                for(String e: init) {
                    String[] inner = e.split(",");
                    pesos.add(inner);
                }
            }
        }

        for(String e: categories) {
            Log.d("categories", e);
        }

        for(String[] e: elementos) {
            Log.d("categories", "cat1");
            for(String d: e) {
                Log.d("categories", d);
            }
        }

        float acum = 0;
        for(String[] e: pesos) {
            for(String d: e) {
                acum += Integer.parseInt(d);

            }
        }
        acum = acum/5;
        tx.setText("Sumatoria de pesos: "+ acum + "%");

        fillList(categories, elementos, pesos);
    }

    public void fillList(String[] cat, ArrayList<String[]> elem, ArrayList<String[]> pesos) {
        // Normalize array
        ArrayList<String> total = new ArrayList<>();

        for(int i = 0; i < cat.length; ++i){
            total.add(cat[i]);
            try {
                for (String e : elem.get(i)) {
                    total.add(e);
                }
            }catch (Exception e) {}
        }

        //Declaration part
        ArrayAdapter<String> adapter;
        //arraylist Append
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, total);
        lv.setAdapter(adapter);

    }
}
