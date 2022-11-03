package org.atm.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Double.compare(account.balance, balance) == 0 && accountNumber.equals(account.accountNumber) && currency.equals(account.currency) && Objects.equals(card, account.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, currency, balance, card);
    }
}
