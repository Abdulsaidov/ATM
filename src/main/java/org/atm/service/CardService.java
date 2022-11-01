package org.atm.service;

import org.atm.model.Account;
import org.atm.model.Card;

import java.time.LocalDate;

public class CardService {
    public static Card addCard(String number, String pin,String holder, String expire) {
        Card card = new Card();
        card.setNumber(number);
        card.setPin(pin);
        card.setHolder(holder);
        int d = 1;
        int m = Integer.parseInt(expire.split("/")[0])+1;
        if (m > 12){
            m = 1;
        }
        int y = Integer.parseInt("20"+expire.split("/")[1]);
        LocalDate date = LocalDate.of(y,m,d);
        card.setExpiresDate(date);
        return card;
    }
    public static Account addAccount(String number, Double balance, String currency) {
        Account account = new Account();
        account.setAccountNumber(number);
        account.setBalance(balance);
        account.setCurrency(currency);
        return account;
    }
}
