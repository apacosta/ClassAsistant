package io;

import entities.StandardTransactionOutput;

/**
 * Created by asmateus on 26/05/17.
 */

public interface TransactionListeners {
    int id = -1;

    void manageTransactionResult(StandardTransactionOutput output);
}
