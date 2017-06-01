package io;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import entities.StandardTransactionOutput;

/**
 * Created by asmateus on 26/05/17.
 */

public class InformationTracker {
    public static final int SIGNATURE_TRACKER = 0;
    public static final int SIGNATURE_TRACKER_DEEP = 1;
    public static final int EVALUATION_TRACKER = 2;
    public static final int EVALUATION_TRACKER_DEEP = 3;
    public static final int STUDENTS_TRACKER = 4;
    public static final int STUDENTS_TRACKER_DEEP = 5;
    public static final int LOGS_TRACKER = 6;
    public static final int LOGS_TRACKER_DEEP = 7;
    public static final int TEACHERS_TRACKER = 8;
    public static final int TEACHERS_TRACKER_DEEP = 9;
    public static final int STUDENTS_SCORE_TRACKER = 10;
    public static final int RUBRIC_TRACKER = 11;
    public static final int RUBRIC_TRACKER_DEEP = 12;
    public static final int CATEGORY_TRACKER = 13;
    public static final int CATEGORY_TRACKER_DEEP = 14;
    public static final int PETITIONS_TRACKER = 15;


    private ArrayList<TransactionListeners> listeners = new ArrayList<>();
    private final DatabaseReference ref;
    private final int tracker_id;
    private String special_info = "";
    private ValueEventListener ev;

    public InformationTracker(int tracker_id, FirebaseDatabase db, String deep_offset) {
        this.tracker_id = tracker_id;
        String db_path = getTrackerTypeString(tracker_id, deep_offset);
        this.ref = db.getReference(db_path);

        // Attach a listener to read the data at our posts reference
        this.ev = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StandardTransactionOutput output = processSnapshot(dataSnapshot, InformationTracker.this.tracker_id);


                for(TransactionListeners l: listeners) {
                    l.manageTransactionResult(output);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("posts", "The read failed: " + databaseError.getCode());
            }
        };

        this.ref.addValueEventListener(ev);
    }

    public void unSubscribeFromSource() {
        this.ref.removeEventListener(ev);
    }

    public void addListener(TransactionListeners l) {
        this.listeners.add(l);
    }

    public void removeListener(int id) {
        int i; boolean c = false;
        for(i = 0; i < this.listeners.size(); ++i) {
            if(this.listeners.get(i).id == id) {
                c = true;
                break;
            }
        }
        if(c) {
            this.listeners.remove(i);
        }
    }

    private StandardTransactionOutput processSnapshot(DataSnapshot snap, int id) {
        TrackerRepresentation.GeneralizedRepresentation rep = null;

        switch(id) {
            case SIGNATURE_TRACKER:
                StandardTransactionOutput sig_tr_o = new StandardTransactionOutput();
                sig_tr_o.setResultType(SIGNATURE_TRACKER);
                sig_tr_o.getContent().put("special_methods", this.special_info);

                int con = 0;
                for(DataSnapshot d: snap.getChildren()) {
                    sig_tr_o.getContent().put(Integer.toString(con), d.getKey());
                    ++con;
                }

                return sig_tr_o;
            case SIGNATURE_TRACKER_DEEP:
                rep = snap.getValue(TrackerRepresentation.SignatureRepresentation.class);
                break;
            case EVALUATION_TRACKER:
                break;
            case EVALUATION_TRACKER_DEEP:
                rep = snap.getValue(TrackerRepresentation.EvaluationRepresentation.class);
                break;
            case STUDENTS_TRACKER:
                break;
            case STUDENTS_TRACKER_DEEP:
                rep = snap.getValue(TrackerRepresentation.StudentRepresentation.class);
                break;
            case LOGS_TRACKER:
                break;
            case LOGS_TRACKER_DEEP:
                break;
            case TEACHERS_TRACKER:
                break;
            case TEACHERS_TRACKER_DEEP:
                rep = snap.getValue(TrackerRepresentation.TeacherRepresentation.class);
                break;
            case STUDENTS_SCORE_TRACKER:
                StandardTransactionOutput o = new StandardTransactionOutput();

                String[] std = special_info.split(";");
                String sig = std[0];
                String acum;

                o.setResultType(STUDENTS_SCORE_TRACKER);
                o.getContent().put("sig_target", sig);

                for(DataSnapshot d: snap.getChildren()) {
                    for(int i = 1; i < std.length; ++i) {
                        if(d.getKey().equals(std[i])) {
                            acum = "";
                            for(DataSnapshot e: d.getChildren()) {
                                if(e.getKey().split("_")[0].equals(sig)) {
                                    acum += e.getValue() + "-";
                                }
                            }
                            acum = acum.substring(0, acum.length()-1);
                            acum += "@";
                            for(DataSnapshot e: d.getChildren()) {
                                if(e.getKey().split("_")[0].equals(sig)) {
                                    acum += e.getKey() + "-";
                                }
                            }
                            o.getContent().put(std[i], acum);
                        }
                    }
                }
                return o;
            case RUBRIC_TRACKER:
                StandardTransactionOutput rub_o = new StandardTransactionOutput();
                rub_o.setResultType(RUBRIC_TRACKER);

                for(DataSnapshot d: snap.getChildren()) {
                    for(DataSnapshot de: d.getChildren()) {
                        if(de.getKey().equals("name")) {
                            rub_o.getContent().put(d.getKey(), de.getValue().toString());
                        }
                    }
                }
                return rub_o;
            case RUBRIC_TRACKER_DEEP:
                rep = snap.getValue(TrackerRepresentation.RubricRepresentation.class);
                break;
            case CATEGORY_TRACKER:
                StandardTransactionOutput cat_o = new StandardTransactionOutput();
                cat_o.setResultType(CATEGORY_TRACKER);

                int i = 0;
                for(DataSnapshot d: snap.getChildren()) {
                    cat_o.getContent().put(Integer.toString(i), d.getKey());
                }

                return cat_o;
            case CATEGORY_TRACKER_DEEP:
                rep = snap.getValue(TrackerRepresentation.CategoryRepresentation.class);
                break;
            case PETITIONS_TRACKER:
                StandardTransactionOutput temp;
                rep = snap.getValue(TrackerRepresentation.StudentRepresentation.class);
                if(rep != null) {
                    temp = rep.extractGeneralizedOutput();
                    temp.setResultType(PETITIONS_TRACKER);
                }
                else {
                    temp = StandardTransactionOutput.nullTransactionOutput();
                }
                return temp;
        }

        if(rep != null) {
            return rep.extractGeneralizedOutput();
        }
        else {
            return StandardTransactionOutput.nullTransactionOutput();
        }
    }

    private String getTrackerTypeString(int id, String deepness) {
        String type = "";

        switch (id) {
            case SIGNATURE_TRACKER:
                Log.d("Processing", "HOla, i am Processing");
                type = "signatures";
                special_info = deepness;
                break;
            case SIGNATURE_TRACKER_DEEP:
                type = "signatures/" + deepness;
                break;
            case EVALUATION_TRACKER:
                break;
            case EVALUATION_TRACKER_DEEP:
                type = "evaluations/" + deepness;
                break;
            case STUDENTS_TRACKER:
                break;
            case STUDENTS_TRACKER_DEEP:
                type = "students/" + deepness;
                break;
            case LOGS_TRACKER:
                break;
            case LOGS_TRACKER_DEEP:
                break;
            case TEACHERS_TRACKER:
                break;
            case TEACHERS_TRACKER_DEEP:
                type = "teachers/" + deepness;
                break;
            case STUDENTS_SCORE_TRACKER:
                type = "eval-results";
                special_info = deepness;
                break;
            case RUBRIC_TRACKER:
                type = "rubrics/" + deepness;
                break;
            case RUBRIC_TRACKER_DEEP:
                type = "rubrics/" + deepness;
                break;
            case CATEGORY_TRACKER:
                type = "categories";
                special_info = deepness;
                break;
            case CATEGORY_TRACKER_DEEP:
                type = "categories/" + deepness;
                break;
            case PETITIONS_TRACKER:
                type = "students/" + deepness;
                break;
        }

        return type;
    }
}
