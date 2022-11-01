package org.atm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
}
