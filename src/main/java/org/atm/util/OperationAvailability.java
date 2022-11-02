package org.atm.util;

import org.atm.model.Card;

public abstract class OperationAvailability {
    private OperationAvailability next;
    protected void checkNext(String pin, Card card) {
        if (next != null) {
            next.check( pin, card);
        }
    }
    public abstract boolean check(String pin, Card card);

    public static OperationAvailability link(OperationAvailability first, OperationAvailability... chain) {
        OperationAvailability head = first;
        for (OperationAvailability nextInChain: chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }
}
