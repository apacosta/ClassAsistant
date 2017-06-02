package entities;

import java.util.HashMap;

/**
 * Created by asmateus on 26/05/17.
 */

public class StandardTransactionOutput {
    private final HashMap<String, String> content = new HashMap<>();
    private int status = 0;
    private int type = 0;


    public void addContent(String key, String value) {
        this.content.put(key, value);
    }

    public HashMap<String, String> getContent() {
        return this.content;
    }

    public static StandardTransactionOutput nullTransactionOutput() {
        StandardTransactionOutput out = new StandardTransactionOutput();
        out.status = -1;

        return out;
    }

    public boolean isNull() {
        if(this.status == -1)
            return true;
        else
            return false;
    }

    public int getResultType() {
        return this.type;
    }

    public void setResultType(int type) {
        this.type = type;
    }
}
