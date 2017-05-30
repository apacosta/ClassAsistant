package io;

import android.content.ContentValues;

import minimum.MinEvaluation;
import minimum.MinSignature;
import minimum.MinStudent;

/**
 * Created by asmateus on 5/04/17.
 */

public class SQLCommandGenerator {
    public static SQLPacket getSignaturesAll() {
        SQLPacket pkg = new SQLPacket();

        pkg.cmd += "SELECT _ID, " + DBRepresentation.Signature.COLUMN_NAME
                + " FROM " + DBRepresentation.Signature.TABLE_NAME;

        pkg.fields.add(DBRepresentation.Signature._ID);
        pkg.fields.add(DBRepresentation.Signature.COLUMN_NAME);

        pkg.types.add(SQLPacket.TYPE_LONG);
        pkg.types.add(SQLPacket.TYPE_STRING);

        return pkg;
    }

    public static SQLPacket getStudentsFromSignature(long id_signature) {
        SQLPacket pkg = new SQLPacket();

        pkg.cmd += "SELECT " + DBRepresentation.Student.TABLE_NAME + "._ID,"
                + DBRepresentation.Student.TABLE_NAME + "." + DBRepresentation.Student.COLUMN_NAME
                + " FROM " + DBRepresentation.Student.TABLE_NAME
                + " WHERE " + DBRepresentation.Student.TABLE_NAME + "." + DBRepresentation.Student.COLUMN_CUSTOM_ID + " = " + id_signature;


        pkg.fields.add(DBRepresentation.Student._ID);
        pkg.fields.add(DBRepresentation.Student.COLUMN_NAME);

        pkg.types.add(SQLPacket.TYPE_LONG);
        pkg.types.add(SQLPacket.TYPE_STRING);

        return pkg;
    }

    public static SQLPacket getEvaluationFromSignature(long id_signature) {
        SQLPacket pkg = new SQLPacket();

        pkg.cmd += "SELECT " + DBRepresentation.Evaluation.TABLE_NAME + "._ID,"
                + DBRepresentation.Evaluation.TABLE_NAME + "." + DBRepresentation.Evaluation.COLUMN_NAME
                + " FROM " + DBRepresentation.Evaluation.TABLE_NAME
                + " WHERE " + DBRepresentation.Evaluation.TABLE_NAME + "." + DBRepresentation.Evaluation.COLUMN_CUSTOM_ID + " = " + id_signature;

        pkg.fields.add(DBRepresentation.Evaluation._ID);
        pkg.fields.add(DBRepresentation.Evaluation.COLUMN_NAME);

        pkg.types.add(SQLPacket.TYPE_LONG);
        pkg.types.add(SQLPacket.TYPE_STRING);

        return pkg;
    }

    public static ContentValues setNewSignature(MinSignature signature) {
        ContentValues v = new ContentValues();

        v.put(DBRepresentation.Signature.COLUMN_NAME, signature.getName());

        return v;
    }

    public static ContentValues setNewStudent(MinStudent student) {
        ContentValues v = new ContentValues();
        v.put(DBRepresentation.Student.COLUMN_NAME, student.getName());

        return v;
    }

    public static ContentValues setNewEvaluation(MinEvaluation exam) {
        ContentValues v = new ContentValues();

        v.put(DBRepresentation.Evaluation.COLUMN_NAME, exam.getName());

        return v;
    }
}
