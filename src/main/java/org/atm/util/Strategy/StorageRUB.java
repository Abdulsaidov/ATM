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

public class StorageRUB implements Storage {

    @Override
    public void fillStorage(BufferedReader scanner, Map<String, Integer> storage) throws IOException {
        List<Integer> list = new ArrayList<>();
        System.out.println("Введите количество 100 рублевых купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 200 рублевых купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 500 рублевых купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 1000 рублевых купюр");
        list.add(Integer.parseInt(scanner.readLine()));


        storage.put("100RUB", list.get(0));
        storage.put("200RUB", list.get(1));
        storage.put("500RUB", list.get(2));
        storage.put("1000RUB", list.get(3));
        storage.put("balanceRUB", 100 * storage.get("100RUB") + 200 * storage.get("200RUB")
                + 500 * storage.get("500RUB") + 1000 * storage.get("1000RUB"));
        System.out.println("В банкомате: " + storage.get("balanceRUB") + "RUB");
    }

    @Override
    public boolean withdrawStorage(List<Integer> list, Map<String, Integer> storage) {
        if (availableBanknotes(list, storage)) {
            storage.put("100RUB", storage.get("100RUB") - list.get(3));
            storage.put("200RUB", storage.get("200RUB") - list.get(2));
            storage.put("500RUB", storage.get("500RUB") - list.get(1));
            storage.put("1000RUB", storage.get("1000RUB") - list.get(0));
            storage.put("balanceRUB", 100 * storage.get("100RUB") + 200 * storage.get("200RUB")
                    + 500 * storage.get("500RUB") + 1000 * storage.get("1000RUB"));
            ATMService.saveConsist();
            return true;
        }
        return false;
    }

    @Override
    public boolean availableBanknotes(List<Integer> list, Map<String, Integer> storage) {
        return storage.get("100RUB") - list.get(3) >= 0 && storage.get("200RUB") - list.get(2) >= 0
                && storage.get("500RUB") - list.get(1) >= 0 && storage.get("1000RUB") - list.get(0) >= 0;
    }

    @Override
    public boolean getMoney(double summa, Map<String, Integer> storage) {

        Card card = AdminService.readCard(ATMService.inspectCardNumber());
        if (summa <= storage.get("balanceRUB")) {
            if (withdrawStorage(Storage.findBankNotes(summa, storage, "RUB", List.of(1000, 500, 200, 100)),
                    storage)) {
                System.out.println("Возьмите деньги: " + summa + " RUB");
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
        return storage.get("balanceRUB");
    }

    @Override
    public double exchange(String from, double summa) {
        CurrencyService.rubRatioCheck();
        if (from.equals("EUR")) {
            return summa * CurrencyService.getRubEur();
        } else if (from.equals("USD")) {
            return summa * CurrencyService.getRubUsd();
        } else {
            System.out.println("ввели некорректную валюту");
            return summa;
        }
    }
    @Override
    public boolean availableForGetMoney(double summa, Map<String, Integer> storage) {
        Card card = AdminService.readCard(ATMService.inspectCardNumber());
        return summa <= card.getAccount().get(ATMService.findAccount("RUB")).getBalance();
    }
}
