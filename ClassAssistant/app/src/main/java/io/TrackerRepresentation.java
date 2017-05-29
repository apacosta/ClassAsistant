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

            out.setResultType(InformationTracker.SIGNATURE_TRACKER_DEEP);

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

            out.setResultType(InformationTracker.TEACHERS_TRACKER_DEEP);

            return out;
        }
    }

    public static class RubricRepresentation implements GeneralizedRepresentation {
        public String categories;
        public String name;

        @Override
        public StandardTransactionOutput extractGeneralizedOutput() {
            StandardTransactionOutput out = new StandardTransactionOutput();
            out.addContent("categories", this.categories);
            out.addContent("name", this.name);

            out.setResultType(InformationTracker.RUBRIC_TRACKER_DEEP);

            return out;
        }
    }

    public static class CategoryRepresentation implements GeneralizedRepresentation {
        public String item_weight;
        public String items;
        public String name;
        public String weight;
        public String id;

        @Override
        public StandardTransactionOutput extractGeneralizedOutput() {
            StandardTransactionOutput out = new StandardTransactionOutput();
            out.addContent("item_weight", this.item_weight);
            out.addContent("items", this.items);
            out.addContent("name", this.name);
            out.addContent("weight", this.weight);
            out.addContent("id", this.id);

            out.setResultType(InformationTracker.CATEGORY_TRACKER_DEEP);

            return out;
        }
    }
}
