package io;

/**
 * Created by anshy on 9/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBManagerReport {

    private DBRepresentation dbHelper;

    private Context context;

    private SQLiteDatabase database;


    public DBManagerReport(Context c) {
        context = c;
    }

    public DBManagerReport open() throws SQLException {
        dbHelper = new DBRepresentation(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String type, String target, String content) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBRepresentation.Report.COLUMN_TYPE,type);
        contentValue.put(DBRepresentation.Report.COLUMN_TARGET,target);
        contentValue.put(DBRepresentation.Report.COLUMN_CONTENT,content);


        database.insert(DBRepresentation.Report.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBRepresentation.Report._ID, DBRepresentation.Report.COLUMN_TYPE, DBRepresentation.Report.COLUMN_TARGET, DBRepresentation.Report.COLUMN_CONTENT };
        Cursor cursor = database.query(DBRepresentation.Report.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String type, String target, String content) {
        ContentValues contentValues = new ContentValues();


        contentValues.put(DBRepresentation.Report.COLUMN_TYPE,type);
        contentValues.put(DBRepresentation.Report.COLUMN_TARGET,target);
        contentValues.put(DBRepresentation.Report.COLUMN_CONTENT,content);

        int i = database.update(DBRepresentation.Report.TABLE_NAME, contentValues, DBRepresentation.Report._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBRepresentation.Report.TABLE_NAME, DBRepresentation.Report._ID + "=" + _id, null);
    }

}

