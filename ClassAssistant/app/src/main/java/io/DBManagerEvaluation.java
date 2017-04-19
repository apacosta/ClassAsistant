package io;

/**
 * Created by anshy on 9/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBManagerEvaluation {

    private DBRepresentation dbHelper;

    private Context context;

    private SQLiteDatabase database;


    public DBManagerEvaluation(Context c) {
        context = c;
    }

    public DBManagerEvaluation open() throws SQLException {
        dbHelper = new DBRepresentation(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name,String id, String rubric, String results) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBRepresentation.Evaluation.COLUMN_NAME,name);
        contentValue.put(DBRepresentation.Evaluation.COLUMN_CUSTOM_ID,id);
        contentValue.put(DBRepresentation.Evaluation.COLUMN_RUBRIC,rubric);
        contentValue.put(DBRepresentation.Evaluation.COLUMN_RESULTS_STUDENTS,results);


        database.insert(DBRepresentation.Evaluation.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBRepresentation.Evaluation._ID, DBRepresentation.Evaluation.COLUMN_NAME,DBRepresentation.Evaluation.COLUMN_CUSTOM_ID, DBRepresentation.Evaluation.COLUMN_RUBRIC, DBRepresentation.Evaluation.COLUMN_RESULTS_STUDENTS };
        Cursor cursor = database.query(DBRepresentation.Evaluation.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public int update(long _id, String name,String id, String rubric, String results) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBRepresentation.Evaluation.COLUMN_NAME,name);
        contentValues.put(DBRepresentation.Evaluation.COLUMN_CUSTOM_ID,id);
        contentValues.put(DBRepresentation.Evaluation.COLUMN_RUBRIC,rubric);
        contentValues.put(DBRepresentation.Evaluation.COLUMN_RESULTS_STUDENTS,results);

        int i = database.update(DBRepresentation.Evaluation.TABLE_NAME, contentValues, DBRepresentation.Evaluation._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBRepresentation.Evaluation.TABLE_NAME, DBRepresentation.Evaluation._ID + "=" + _id, null);
    }

}




