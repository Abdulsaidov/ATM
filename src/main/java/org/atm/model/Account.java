package org.atm.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Account implements Serializable {
    private String accountNumber;
    private String currency;
    private double balance;

    private Card card;

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                '}';
    }
}
