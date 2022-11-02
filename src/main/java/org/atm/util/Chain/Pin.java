package org.atm.util.Chain;

import org.atm.model.Card;

public class Pin extends OperationAvailability {
    @Override
    public boolean check(String pin, Card card) {
        if (!auth(pin, card.getPin())) {
            return false;
        }
        checkNext(pin, card);
        return true;
    }

    private static boolean auth(String pin, String targetPin) {
        return pin.equals(targetPin);
    }
}
