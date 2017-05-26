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

        @Override
        public StandardTransactionOutput extractGeneralizedOutput() {
            StandardTransactionOutput out = new StandardTransactionOutput();
            out.addContent("default_rubric", this.default_rubric);
            out.addContent("evaluations", this.evaluations);
            out.addContent("name", this.name);
            out.addContent("owner", this.owner);
            out.addContent("users", this.users);

            return out;
        }

    }
}
