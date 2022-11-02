package org.atm.service;

import org.atm.model.Card;
import org.atm.util.CurrencyAvailable;
import org.atm.util.OperationAvailability;
import org.atm.util.Pin;
import org.atm.util.isExpired;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ATMService {

    private static Map<String, Integer> storage = new HashMap<>();
    private static final String currency = "RUB";

    static {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/main/resources/storage.dat"))) {
            storage = (Map<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("В банкомате пока нет денег");
//            throw new RuntimeException("cant load storage");
        }
//        System.out.println("В банкомате: " + storage.get("balance") + "RUB");
    }

    public static Integer availableSum() {
        return storage.get("balance");
    }

    private static void saveConsist() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/storage.dat"))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            throw new RuntimeException("cant save storage");
        }
    }

    public static String availableCurrency() {
        return currency;
    }

    public static Map<String, Integer> fillStorage(int tens, int twenties, int fifties, int hundreds) {
        storage.put("10", tens);
        storage.put("20", twenties);
        storage.put("50", fifties);
        storage.put("100", hundreds);
        storage.put("balance", 10 * storage.get("10") + 20 * storage.get("20") + 50 * storage.get("50") + 100 * storage.get("100"));
        saveConsist();
        System.out.println("В банкомате: " + storage.get("balance") + "RUB");
        return storage;
    }

    public static boolean withdrawStorage(int tens, int twenties, int fifties, int hundreds) {
        if (availableBanknotes(tens, twenties, fifties, hundreds)) {
            storage.put("10", storage.get("10") - tens);
            storage.put("20", storage.get("20") - twenties);
            storage.put("50", storage.get("50") - fifties);
            storage.put("100", storage.get("100") - hundreds);
            storage.put("balance", 10 * storage.get("10") + 20 * storage.get("20") + 50 * storage.get("50") + 100 * storage.get("100"));
            saveConsist();
//            System.out.println("В банкомате: " + storage.get("balance") + "RUB");
            return true;
        }
        return false;
    }

    private static boolean availableBanknotes(int tens, int twenties, int fifties, int hundreds) {
        return storage.get("10") - tens >= 0 && storage.get("20") - twenties >= 0
                && storage.get("50") - fifties >= 0 && storage.get("100") - hundreds >= 0;
    }

    public static void getBalance() {
        Card current = AdminService.readCard(inspectCardNumber());
        System.out.println("Здравствуйте, " + current.getHolder() + "\n" +
                "ваш баланс: " + current.getAccount().get(0).getBalance());
    }

    public static void getMoney(Double summa) {
        Double sum = summa;
        Card card = AdminService.readCard(inspectCardNumber());
        if (sum <= ATMService.storage.get("balance") &&
                sum <= card.getAccount().get(0).getBalance()) {
            int hundred = 0;
            int fifty = 0;
            int twenty = 0;
            int ten = 0;

            if (sum != 0) {
                hundred = Math.min((int) (sum / 100),storage.get("100"));
                sum = sum - hundred * 100;
            }
            if (sum != 0) {
                fifty = Math.min((int) (sum / 50),storage.get("50"));
                sum = sum - fifty * 50;
            }
            if (sum != 0) {
                twenty = Math.min((int) (sum / 20),storage.get("20"));
                sum = sum - twenty * 20;
            }
            if (sum != 0) {
                ten = Math.min((int) (sum / 10),storage.get("10"));
                sum = sum - ten * 10;
            }
            if (withdrawStorage(ten, twenty, fifty, hundred)) {
                Double current = card.getAccount().get(0).getBalance();
                card.getAccount().get(0).setBalance(current-summa);
                AdminService.updateCard(card);
                System.out.println("Возьмите деньги:" + summa);
            } else {
                System.out.println("no money honey");
            }
        }
    }

    public static boolean operationAvailable(String pin) {
        OperationAvailability operationAvailability = OperationAvailability.link(new Pin(), new isExpired(), new CurrencyAvailable());
        return operationAvailability.check(pin, AdminService.readCard(inspectCardNumber()));
    }


    /**
     * вообще тут не должно быть хардкода конечно. должна быть реализована логика считывания номера вставленной в банкомат карты
     */
    private static String inspectCardNumber() {
        return "5368 9846 1397 7265";
    }
}