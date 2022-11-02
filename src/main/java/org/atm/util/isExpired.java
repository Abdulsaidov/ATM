package org.atm.util;

import org.atm.model.Card;

import java.time.LocalDate;

public class isExpired extends OperationAvailability {
    @Override
    public boolean check(String pin, Card card) {
        if (!card.getExpiresDate().isAfter(LocalDate.now())) {
            System.out.println("Срок действия карты истек");
            return false;
        }
        checkNext(pin, card);
        return true;
    }
}
