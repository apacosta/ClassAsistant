package com.uninorte.classassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Signatures extends AppCompatActivity {

    private List<MinSignature> data = new ArrayList<>();
    private ViewAdapter view_adapter;
    private RecyclerView recycler_view;
    private Intent single_signature_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signatures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        single_signature_intent = new Intent(this, SingleSignature.class);
        list_demo();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void list_demo() {
        String[] names = {
                "FÍSICA CUÁNTICA", "PDI", "CONTROL",
                "ALGORITMOS Y COMPLEJIDAD", "CULTURA ORIENTAL", "KARATE DO",
                "INTELIGENCIA ARTIFICIAL", "ESTRUCTURAS DISCRETAS",
                "BASES DE DATOS", "ELECTRÓNICA I", "ELECTRÓNICA II",
                "ELECTRÓNICA III", "REDES DE COMPUTADORES", "SISTEMAS OPERATIVOS",
                "COMUNICACIONES", "FÍSICA ELECTRICIDAD", "MECÁNICA CLÁSICA",
                "MECÁNICA DE FLUIDOS", "TERMODINÁMICA", "KUNG FU", "MEDIOS DE TRANSMISIÓN",
                "ÓPTICA", "NEON GENESIS EVANGELION"
        };

        for(String e: names) {
            MinSignature f = new MinSignature();
            f.setTitle(e);
            data.add(f);
        }

        view_adapter = new ViewAdapter(this, data, single_signature_intent);

        recycler_view = (RecyclerView) this.findViewById(R.id.recycle);
        recycler_view.setAdapter(view_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signatures, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
