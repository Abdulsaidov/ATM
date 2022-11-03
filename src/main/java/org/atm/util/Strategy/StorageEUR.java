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

public class StorageEUR implements Storage {


    @Override
    public void fillStorage(BufferedReader scanner, Map<String, Integer> storage) throws IOException {
        List<Integer> list = new ArrayList<>();
        System.out.println("Введите количество 10 EUR купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 20 EUR купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 50 EUR купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        System.out.println("Введите количество 100 EUR купюр");
        list.add(Integer.parseInt(scanner.readLine()));
        storage.put("10EUR", list.get(0));
        storage.put("20EUR", list.get(1));
        storage.put("50EUR", list.get(2));
        storage.put("100EUR", list.get(3));
        storage.put("balanceEUR", 10 * storage.get("10EUR") + 20 * storage.get("20EUR")
                + 50 * storage.get("50EUR") + 100 * storage.get("100EUR"));
        System.out.println("В банкомате: " + storage.get("balanceEUR") + "EUR");
        ATMService.saveConsist();
    }

    @Override
    public boolean withdrawStorage(List<Integer> list, Map<String, Integer> storage) {
        if (availableBanknotes(list, storage)) {
            storage.put("10EUR", storage.get("10EUR") - list.get(3));
            storage.put("20EUR", storage.get("20EUR") - list.get(2));
            storage.put("50EUR", storage.get("50EUR") - list.get(1));
            storage.put("100EUR", storage.get("100EUR") - list.get(0));
            storage.put("balanceEUR", 10 * storage.get("10EUR") + 20 * storage.get("20EUR") +
                    50 * storage.get("50EUR") + 100 * storage.get("100EUR"));
            ATMService.saveConsist();
            return true;
        }
        return false;
    }

    @Override
    public boolean availableBanknotes(List<Integer> list, Map<String, Integer> storage) {
        return storage.get("10EUR") - list.get(0) >= 0 && storage.get("20EUR") - list.get(1) >= 0
                && storage.get("50EUR") - list.get(2) >= 0 && storage.get("100EUR") - list.get(3) >= 0;
    }

    @Override
    public boolean getMoney(double summa, Map<String, Integer> storage) {
        Card card = AdminService.readCard(ATMService.inspectCardNumber());
        if (summa <= storage.get("balanceEUR") &&
                summa <= card.getAccount().get(ATMService.findAccount("EUR")).getBalance()) {
            if (withdrawStorage(Storage.findBankNotes(summa, storage, "EUR", List.of(100, 50, 20, 10)),
                    storage)) {
//                Integer index = null;
//                for (int i = 0; i < card.getAccount().size(); i++) {
//                    if (card.getAccount().get(i).getCurrency().equals("EUR")) {
//                        index = i;
//                    }
//                }
//                assert index != null;
//                double current = card.getAccount().get(index).getBalance();
//                card.getAccount().get(index).setBalance(current - summa);
//                AdminService.updateCard(card);
                System.out.println("Возьмите деньги:" + summa);
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
        return storage.get("balanceEUR");
    }

    @Override
    public double exchange(String from, double summa) {
        CurrencyService.rubRatioCheck();
        if (from.equals("RUB")) {
            return summa / CurrencyService.getRubEur();
        } else if (from.equals("USD")) {
            return summa * CurrencyService.getRubUsd() / CurrencyService.getRubEur();
        } else {
            System.out.println("ввели некорректную валюту");
            return summa;
        }
    }
//    @Override
//    public boolean reduceBalance(double summa, Map<String, Integer> storage) {
//        Card card = AdminService.readCard(ATMService.inspectCardNumber());
//        if (summa <= card.getAccount().get(ATMService.findAccount("EUR")).getBalance()) {
//            Integer index = null;
//            for (int i = 0; i < card.getAccount().size(); i++) {
//                if (card.getAccount().get(i).getCurrency().equals("EUR")) {
//                    index = i;
//                }
//            }
//            //todo: при рефакторинге вынести в отдельный метод
//            assert index != null;
//            double current = card.getAccount().get(index).getBalance();
//            card.getAccount().get(index).setBalance(current - summa);
//            AdminService.updateCard(card);
//            return true;
//        }
//        return false;
//    }
}
