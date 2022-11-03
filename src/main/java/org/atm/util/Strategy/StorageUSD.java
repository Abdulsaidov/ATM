package org.atm.util.Strategy;

import org.atm.model.Card;
import org.atm.service.ATMService;
import org.atm.service.AdminService;
import org.atm.service.CurrencyService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StorageUSD implements Storage {


    @Override
    public void fillStorage(BufferedReader scanner, Map<String, Integer> storage) throws IOException {
        List<Integer> list = new ArrayList<>();
        System.out.println("Введите количество 10 USD купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 20 USD купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 50 USD купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 100 USD купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        storage.put("10USD", list.get(0));
        storage.put("20USD", list.get(1));
        storage.put("50USD", list.get(2));
        storage.put("100USD", list.get(3));
        storage.put("balanceUSD", 10 * storage.get("10USD") + 20 * storage.get("20USD")
                + 50 * storage.get("50USD") + 100 * storage.get("100USD"));
        System.out.println("В банкомате: " + storage.get("balanceUSD") + "USD");
        ATMService.saveConsist();
    }

    @Override
    public boolean withdrawStorage(List<Integer> list, Map<String, Integer> storage) {
        if (availableBanknotes(list, storage)) {
            storage.put("10USD", storage.get("10USD") - list.get(3));
            storage.put("20USD", storage.get("20USD") - list.get(2));
            storage.put("50USD", storage.get("50USD") - list.get(1));
            storage.put("100USD", storage.get("100USD") - list.get(0));
            storage.put("balanceUSD", 10 * storage.get("10USD") + 20 * storage.get("20USD") +
                    50 * storage.get("50USD") + 100 * storage.get("100USD"));
            ATMService.saveConsist();
            return true;
        }
        return false;
    }

    @Override
    public boolean availableBanknotes(List<Integer> list, Map<String, Integer> storage) {
        return storage.get("10USD") - list.get(3) >= 0 && storage.get("20USD") - list.get(2) >= 0
                && storage.get("50USD") - list.get(1) >= 0 && storage.get("100USD") - list.get(0) >= 0;
    }

    @Override
    public boolean getMoney(double summa, Map<String, Integer> storage) {

        if (summa <= storage.get("balanceUSD")) {
            if (withdrawStorage(Storage.findBankNotes(summa, storage, "USD", List.of(100, 50, 20, 10)),
                    storage)) {
                System.out.println("Возьмите деньги: " + summa + " USD");
                return true;
            } else {
                System.out.println("no money honey");
                return false;
            }
        } else {
            System.out.println("На счете недостаточно средств");
            return false;
        }
    }

    @Override
    public Integer availableSum(Map<String, Integer> storage) {
        return storage.get("balanceUSD");
    }

    @Override
    public double exchange(String from, double summa) {
        CurrencyService.rubRatioCheck();
        if (from.equals("RUB")) {
            return summa / CurrencyService.getRubUsd();
        } else if (from.equals("USD")) {
            return summa * CurrencyService.getRubEur() / CurrencyService.getRubUsd();
        } else {
            System.out.println("ввели некорректную валюту");
            return summa;
        }
    }
    @Override
    public boolean availableForGetMoney(double summa, Map<String, Integer> storage) {
        Card card = AdminService.readCard(ATMService.inspectCardNumber());
        return summa <= card.getAccount().get(ATMService.findAccount("USD")).getBalance();
    }
}
