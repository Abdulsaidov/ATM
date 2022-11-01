package org.atm.util;

import org.atm.model.Card;
import org.atm.service.ATMService;


public class CurrencyAvailable extends OperationAvailability{
    @Override
    public void check(String pin, Card card) {
       if (!card.getAccount().get(0).getCurrency().equals(ATMService.availableCurrency()))
       {throw new RuntimeException("Нет подходящей валюты");}
       checkNext(pin, card);
    }

}
