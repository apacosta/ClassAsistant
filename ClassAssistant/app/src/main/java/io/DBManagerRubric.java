package io;

/**
 * Created by anshy on 9/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBManagerRubric {

    private DBRepresentation dbHelper;

    private Context context;

    private SQLiteDatabase database;
    private String DB;

    public DBManagerRubric(Context c) {
        context = c;
    }

    public DBManagerRubric open() throws SQLException {
        dbHelper = new DBRepresentation(context,DB);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert( String rtemplate, String rweigths) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBRepresentation.Rubric.COLUMN_TEMPLATE,rtemplate);
        contentValue.put(DBRepresentation.Rubric.COLUMN_WEIGHTS,rweigths);



        database.insert(DBRepresentation.Rubric.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBRepresentation.Rubric._ID, DBRepresentation.Rubric.COLUMN_TEMPLATE, DBRepresentation.Rubric.COLUMN_WEIGHTS };
        Cursor cursor = database.query(DBRepresentation.Rubric.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String rtemplate, String rweigths) {
        ContentValues contentValues = new ContentValues();


        contentValues.put(DBRepresentation.Rubric.COLUMN_TEMPLATE,rtemplate);
        contentValues.put(DBRepresentation.Rubric.COLUMN_WEIGHTS,rweigths);

        int i = database.update(DBRepresentation.Rubric.TABLE_NAME, contentValues, DBRepresentation.Rubric._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBRepresentation.Rubric.TABLE_NAME, DBRepresentation.Rubric._ID + "=" + _id, null);
    }

}
