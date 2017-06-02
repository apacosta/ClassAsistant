package io;

/**
 * Created by anshy on 9/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBManagerRubricTemplate {

    private DBRepresentation dbHelper;

    private Context context;

    private SQLiteDatabase database;


    public DBManagerRubricTemplate(Context c) {
        context = c;
    }

    public DBManagerRubricTemplate open() throws SQLException {
        dbHelper = new DBRepresentation(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert( String name, String categories) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBRepresentation.RubricTemplate.COLUMN_NAME,name);
        contentValue.put(DBRepresentation.RubricTemplate.COLUMN_CATEGORIES,categories);



        database.insert(DBRepresentation.RubricTemplate.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBRepresentation.RubricTemplate._ID, DBRepresentation.RubricTemplate.COLUMN_NAME, DBRepresentation.RubricTemplate.COLUMN_CATEGORIES };
        Cursor cursor = database.query(DBRepresentation.RubricTemplate.TABLE_NAME, columns, null, null, null, null, null);

        return cursor;
    }

    public int update(long _id, String name, String categories) {
        ContentValues contentValues = new ContentValues();


        contentValues.put(DBRepresentation.RubricTemplate.COLUMN_NAME,name);
        contentValues.put(DBRepresentation.RubricTemplate.COLUMN_CATEGORIES,categories);

        int i = database.update(DBRepresentation.RubricTemplate.TABLE_NAME, contentValues, DBRepresentation.RubricTemplate._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBRepresentation.RubricTemplate.TABLE_NAME, DBRepresentation.RubricTemplate._ID + "=" + _id, null);
    }

}

