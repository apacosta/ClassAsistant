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

    public Connector(Context context, int table_type) {
        this.manager = new DBRepresentation(context);
        manager.setTableType(table_type);
        this.db = manager.getReadableDatabase();
    }

    public ArrayList<HashMap> getContent(SQLPacket pkg) {

        // Use raw query provided via SQLCommandGenerator
        Cursor cc = this.db.rawQuery(pkg.cmd, new String[] {});
        Log.d("Connector", pkg.cmd);
        // Parse data
        ArrayList<HashMap> data = new ArrayList<>();
        while(cc.moveToNext()) {

            HashMap<String, String> h = new HashMap<>();
            for(int i = 0; i < pkg.fields.size(); ++i) {
                String d = "";
                switch (pkg.types.get(i)) {
                    case SQLPacket.TYPE_INT:
                        d = Integer.toString(cc.getInt(cc.getColumnIndexOrThrow(pkg.fields.get(i))));
                        break;
                    case SQLPacket.TYPE_LONG:
                        d = Long.toString(cc.getLong(cc.getColumnIndexOrThrow(pkg.fields.get(i))));
                        break;
                    case SQLPacket.TYPE_STRING:
                        d = cc.getString(cc.getColumnIndexOrThrow(pkg.fields.get(i)));
                        break;
                }

                h.put(pkg.fields.get(i), d);

            }

            data.add(h);
        }
        cc.close();

        return data;
    }

    public long setContent(ContentValues v, String table) {
        // Insert the new row, returning the primary key value of the new row
        long id = db.insert(table, null, v);
        return id;
    }

    public int getAvailableID(int id_type) {
        return 0;
    }
}
