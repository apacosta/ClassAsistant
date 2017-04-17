package io;

/**
 * Created by anshy on 9/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBManagerCategories {

    private DBRepresentation dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManagerCategories (Context c) {
        context = c;
    }

    public DBManagerCategories open() throws SQLException {
        dbHelper = new DBRepresentation(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert( String elem) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBRepresentation.Categories.COLUMN_ELEMENTS,elem);

        database.insert(DBRepresentation.Categories.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBRepresentation.Categories._ID, DBRepresentation.Categories.COLUMN_ELEMENTS};
        Cursor cursor = database.query(DBRepresentation.Categories.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String elem) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBRepresentation.Categories.COLUMN_ELEMENTS,elem);


        int i = database.update(DBRepresentation.Categories.TABLE_NAME, contentValues, DBRepresentation.Categories._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBRepresentation.Categories.TABLE_NAME, DBRepresentation.Categories._ID + "=" + _id, null);
    }

}
