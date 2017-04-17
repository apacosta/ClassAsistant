package io;

/**
 * Created by anshy on 9/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBManagerStudent {

    private DBRepresentation dbHelper;

    private Context context;

    private SQLiteDatabase database;
    private String DB;

    public DBManagerStudent(Context c) {
        context = c;
    }

    public DBManagerStudent open() throws SQLException {
        dbHelper = new DBRepresentation(context,DB);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String idstudent,String name, String signt, String eva, String report) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBRepresentation.Student.COLUMN_CUSTOM_ID,idstudent);
        contentValue.put(DBRepresentation.Student.COLUMN_NAME,name);
        contentValue.put(DBRepresentation.Student.COLUMN_SIGNATURES,signt);
        contentValue.put(DBRepresentation.Student.COLUMN_EVALUATIONS,eva);
        contentValue.put(DBRepresentation.Student.COLUMN_REPORTS,report);

        database.insert(DBRepresentation.Student.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBRepresentation.Student._ID, DBRepresentation.Student.COLUMN_CUSTOM_ID, DBRepresentation.Student.COLUMN_NAME, DBRepresentation.Student.COLUMN_SIGNATURES, DBRepresentation.Student.COLUMN_EVALUATIONS, DBRepresentation.Student.COLUMN_REPORTS };
        Cursor cursor = database.query(DBRepresentation.Student.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String idstudent,String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBRepresentation.Student.COLUMN_CUSTOM_ID,idstudent);
        contentValues.put(DBRepresentation.Student.COLUMN_NAME,name);

        int i = database.update(DBRepresentation.Student.TABLE_NAME, contentValues, DBRepresentation.Student._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBRepresentation.Student.TABLE_NAME, DBRepresentation.Student._ID + "=" + _id, null);
    }

}




