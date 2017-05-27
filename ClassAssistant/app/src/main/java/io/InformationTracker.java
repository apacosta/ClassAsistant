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


    private ArrayList<TransactionListeners> listeners = new ArrayList<>();
    private final DatabaseReference ref;
    private final int tracker_id;

    public InformationTracker(int tracker_id, FirebaseDatabase db, String deep_offset) {
        this.tracker_id = tracker_id;
        String db_path = getTrackerTypeString(tracker_id, deep_offset);
        this.ref = db.getReference(db_path);

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
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
        });
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
                break;
            case SIGNATURE_TRACKER_DEEP:
                rep = snap.getValue(TrackerRepresentation.SignatureRepresentation.class);
                break;
            case EVALUATION_TRACKER:
                break;
            case EVALUATION_TRACKER_DEEP:
                break;
            case STUDENTS_TRACKER:
                break;
            case STUDENTS_TRACKER_DEEP:
                break;
            case LOGS_TRACKER:
                break;
            case LOGS_TRACKER_DEEP:
                break;
            case TEACHERS_TRACKER:
                break;
            case TEACHERS_TRACKER_DEEP:
                break;
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
                type = "signatures";
                break;
            case SIGNATURE_TRACKER_DEEP:
                type = "signatures/" + deepness;
                break;
            case EVALUATION_TRACKER:
                break;
            case EVALUATION_TRACKER_DEEP:
                break;
            case STUDENTS_TRACKER:
                break;
            case STUDENTS_TRACKER_DEEP:
                break;
            case LOGS_TRACKER:
                break;
            case LOGS_TRACKER_DEEP:
                break;
            case TEACHERS_TRACKER:
                break;
            case TEACHERS_TRACKER_DEEP:
                break;
        }

        return type;
    }
}
