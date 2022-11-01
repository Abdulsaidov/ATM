package org.atm.util;

import org.atm.model.Card;

import java.time.LocalDate;

public class isExpired extends OperationAvailability{
    @Override
    public void check(String pin, Card card) {
        if (!card.getExpiresDate().isAfter(LocalDate.now())){
            throw new RuntimeException("Срок действия карты истек");
        }
        checkNext(pin,card);

    }

}
