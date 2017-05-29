package io;

import entities.StandardTransactionOutput;

/**
 * Created by asmateus on 26/05/17.
 */

public class TrackerRepresentation {

    public interface GeneralizedRepresentation {
        StandardTransactionOutput extractGeneralizedOutput();
    }

    public static class SignatureRepresentation implements GeneralizedRepresentation {
        public String default_rubric;
        public String evaluations;
        public String name;
        public String owner;
        public String users;
        public String id;

        @Override
        public StandardTransactionOutput extractGeneralizedOutput() {
            StandardTransactionOutput out = new StandardTransactionOutput();
            out.addContent("id", this.id);
            out.addContent("default_rubric", this.default_rubric);
            out.addContent("evaluations", this.evaluations);
            out.addContent("name", this.name);
            out.addContent("owner", this.owner);
            out.addContent("users", this.users);

            out.addContent("result_type", "" + InformationTracker.SIGNATURE_TRACKER_DEEP);

            return out;
        }
    }

    public static class TeacherRepresentation implements GeneralizedRepresentation {
        public String courses;
        public String email;
        public String name;
        public String last_online;

        @Override
        public StandardTransactionOutput extractGeneralizedOutput() {
            StandardTransactionOutput out = new StandardTransactionOutput();
            out.addContent("courses", this.courses);
            out.addContent("email", this.email);
            out.addContent("name", this.name);
            out.addContent("last_online", this.last_online);

            out.addContent("result_type", "" + InformationTracker.TEACHERS_TRACKER_DEEP);

            return out;
        }
    }
}
