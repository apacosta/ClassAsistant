package io;

/**
 * Created by anshy on 9/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBManagerSignature {

    private DBRepresentation dbHelper;

    private Context context;

    private SQLiteDatabase database;


    public DBManagerSignature(Context c) {
        context = c;
    }

    public DBManagerSignature open() throws SQLException {
        dbHelper = new DBRepresentation(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String eva, String students, String reports, String grubric) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBRepresentation.Signature.COLUMN_NAME,name);
        contentValue.put(DBRepresentation.Signature.COLUMN_EVALUATIONS,eva);
        contentValue.put(DBRepresentation.Signature.COLUMN_STUDENTS,students);
        contentValue.put(DBRepresentation.Signature.COLUMN_REPORTS,reports);
        contentValue.put(DBRepresentation.Signature.COLUMN_GLOBAL_RUBRIC,grubric);

        database.insert(DBRepresentation.Signature.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBRepresentation.Signature._ID, DBRepresentation.Signature.COLUMN_NAME, DBRepresentation.Signature.COLUMN_EVALUATIONS, DBRepresentation.Signature.COLUMN_STUDENTS, DBRepresentation.Signature.COLUMN_REPORTS, DBRepresentation.Signature.COLUMN_GLOBAL_RUBRIC };
        Cursor cursor = database.query(DBRepresentation.Signature.TABLE_NAME, columns, null, null, null, null, null);

        return cursor;
    }

    public int update(long _id,String name, String eva, String students, String reports, String grubric) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBRepresentation.Signature.COLUMN_NAME,name);
        contentValues.put(DBRepresentation.Signature.COLUMN_EVALUATIONS,eva);
        contentValues.put(DBRepresentation.Signature.COLUMN_STUDENTS,students);
        contentValues.put(DBRepresentation.Signature.COLUMN_REPORTS,reports);
        contentValues.put(DBRepresentation.Signature.COLUMN_GLOBAL_RUBRIC,grubric);

        int i = database.update(DBRepresentation.Signature.TABLE_NAME, contentValues, DBRepresentation.Signature._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBRepresentation.Signature.TABLE_NAME, DBRepresentation.Signature._ID + "=" + _id, null);
    }

}



