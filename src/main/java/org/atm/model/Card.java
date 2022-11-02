package org.atm.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Card implements Serializable {
    private String number;
    private String pin;
    private String holder;
    private LocalDate expiresDate;
    private List<Account> account;

    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", pin='" + pin + '\'' +
                ", holder='" + holder + '\'' +
                ", expiresDate=" + expiresDate +
                ", account=" + account +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return number.equals(card.number) && pin.equals(card.pin) && holder.equals(card.holder) && expiresDate.equals(card.expiresDate) && Objects.equals(account, card.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, pin, holder, expiresDate, account);
    }
}
