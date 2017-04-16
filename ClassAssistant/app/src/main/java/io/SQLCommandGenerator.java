package io;

import android.content.ContentValues;

import minimum.MinSignature;

/**
 * Created by asmateus on 5/04/17.
 */

public class SQLCommandGenerator {
    public static String getSignaturesAll() {
        String cmd = "";

        cmd += "SELECT _ID, " + DBRepresentation.Signature.COLUMN_NAME
                + " FROM " + DBRepresentation.Signature.TABLE_NAME;

        return cmd;
    }

    public static String getStudentsFromSignature(long id_signature) {
        return "";
    }

    public static String getEvaluationFromSignature(long id_signature) {
        return "";
    }

    public static ContentValues setNewSignature(MinSignature signature) {
        ContentValues v = new ContentValues();

        v.put(DBRepresentation.Signature.COLUMN_NAME, signature.getName());

        return v;
    }
}
