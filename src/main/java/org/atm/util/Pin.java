package org.atm.util;

import org.atm.model.Card;

public class Pin extends OperationAvailability{
    @Override
    public void check(String pin, Card card) {
        if (!auth(pin, card.getPin())){
            throw new RuntimeException("неверный pin");
        }
        checkNext(pin,card);
    }

    private static boolean auth(String pin, String targetPin){
        return pin.equals(targetPin);
    }
}
