package io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asmateus on 5/04/17.
 */

public class Connector {

    private DBRepresentation manager;
    private SQLiteDatabase db;

    public Connector(Context context) {
        this.manager = new DBRepresentation(context);
        manager.setTableType(DBRepresentation.TYPE_SIGNATURE);
        this.db = manager.getReadableDatabase();
    }

    public ArrayList<HashMap> getContent(String command) {

        // Use raw query provided via SQLCommandGenerator
        Cursor cc = this.db.rawQuery(command, new String[] {});
        Log.d("Connector", command);
        // Parse data
        ArrayList<HashMap> data = new ArrayList<>();
        while(cc.moveToNext()) {
            long id = cc.getLong(cc.getColumnIndexOrThrow(DBRepresentation.Signature._ID));
            String name = cc.getString(cc.getColumnIndexOrThrow(DBRepresentation.Signature.COLUMN_NAME));

            Log.d("Connector", ""+id);
            Log.d("Connector", name);

            HashMap<String, String> h = new HashMap<>();
            h.put(DBRepresentation.Signature._ID, ""+id);
            h.put(DBRepresentation.Signature.COLUMN_NAME, name);
            data.add(h);
        }
        cc.close();

        return data;
    }

    public long setContent(ContentValues v) {
        // Insert the new row, returning the primary key value of the new row
        long id = db.insert(DBRepresentation.Signature.TABLE_NAME, null, v);
        return id;
    }

    public int getAvailableID(int id_type) {
        return 0;
    }
}
