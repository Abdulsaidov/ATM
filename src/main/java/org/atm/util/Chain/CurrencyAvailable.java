package org.atm.util.Chain;

import org.atm.model.Card;
import org.atm.service.ATMService;


public class CurrencyAvailable extends OperationAvailability {
    @Override
    public boolean check(String pin, Card card) {
//        if (!card.getAccount().get(0).getCurrency().equals(ATMService.availableCurrency())) {
//            System.out.println("Нет подходящей валюты");
//            return false;
//        }
        checkNext(pin, card);
        return true;
    }
}
