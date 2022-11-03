package org.atm.util.Strategy;

import org.atm.model.Card;
import org.atm.service.ATMService;
import org.atm.service.AdminService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Storage {
    void fillStorage(BufferedReader scanner, Map<String, Integer> storage) throws IOException;

    boolean withdrawStorage(List<Integer> list, Map<String, Integer> storage);

    boolean availableBanknotes(List<Integer> list, Map<String, Integer> storage);

    boolean getMoney(double summa, Map<String, Integer> storage);
    boolean availableForGetMoney(double summa, Map<String, Integer> storage);


    Integer availableSum(Map<String, Integer> storage);

    double exchange(String from, double summa);

    static List<Integer> findBankNotes(double summa, Map<String, Integer> storage, String currency, List<Integer> list) {
        double sum = summa;
        Card card = AdminService.readCard(ATMService.inspectCardNumber());
        List<Integer> withdrawList = new ArrayList<>(list.size());
        if (sum <= storage.get("balance" + currency)) {
            for (int i = 0; i < list.size(); i++) {
                if (sum != 0) {
                    withdrawList.add(Math.min((int) (sum / list.get(i)), storage.get("" + list.get(list.size()-i-1) + currency)));
                    sum = sum - withdrawList.get(i) * list.get(i);
                }
                else {
                    withdrawList.add(0);
                }
            }
        }
        return withdrawList;
    }
}

