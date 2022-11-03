package org.atm.service;

import org.atm.model.Account;
import org.atm.model.Card;
import org.atm.util.Chain.CurrencyAvailable;
import org.atm.util.Chain.OperationAvailability;
import org.atm.util.Chain.Pin;
import org.atm.util.Chain.isExpired;
import org.atm.util.Strategy.Storage;
import org.atm.util.Strategy.StorageEUR;
import org.atm.util.Strategy.StorageRUB;
import org.atm.util.Strategy.StorageUSD;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMService {

    private static Map<String, Integer> storage = new HashMap<>();

    public static Map<String, Integer> getStorage() {
        return storage;
    }

    static {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/main/resources/storage.dat"))) {
            storage = (Map<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("В банкомате пока нет денег");
        }
    }

    public static void availableSum() {
        System.out.println(storage.get("balanceRUB") + " RUB");
        System.out.println(storage.get("balanceUSD") + " USD");
        System.out.println(storage.get("balanceEUR") + " EUR");
        System.out.println("\n" + "Нажмите 0 , чтобы продолжить");
    }

    public static void saveConsist() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/storage.dat"))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            throw new RuntimeException("cant save storage");
        }
    }

    public static Storage defineStorage(String currency) {
        Storage current;
        if (currency.equals("RUB")) {
            current = new StorageRUB();
        } else if (currency.equals("USD")) {
            current = new StorageUSD();
        } else if (currency.equals("EUR")) {
            current = new StorageEUR();
        } else {
            current = null;
        }
        return current;
    }

    public static void getBalance(int i) {
        Card current = AdminService.readCard(inspectCardNumber());
        String formattedBalance = new DecimalFormat("#0.00").format(current.getAccount().get(i - 1).getBalance());
        System.out.println("Здравствуйте, " + current.getHolder() + "\n" +
                "ваш баланс: " + formattedBalance + " " + current.getAccount().get(i-1).getCurrency()) ;
    }

    public static String getCurrency(int i) {
        Card current = AdminService.readCard(inspectCardNumber());
        return current.getAccount().get(i - 1).getCurrency();
    }

    public static void getMoney(Double summa, String currency) {

    }

    public static boolean operationAvailable(String pin) {
        OperationAvailability operationAvailability = OperationAvailability.link(new Pin(), new isExpired(), new CurrencyAvailable());
        return operationAvailability.check(pin, AdminService.readCard(inspectCardNumber()));
    }

    /**
     * вообще тут не должно быть хардкода конечно. должна быть реализована логика считывания номера вставленной в банкомат карты
     */
    public static String inspectCardNumber() {
        return "5368 9846 1397 7265";
    }

    public static void showAccounts() {
        Card card = AdminService.readCard(inspectCardNumber());
        List<Account> accountList = card.getAccount();
        for (Account a : accountList) {
            System.out.println(accountList.indexOf(a) + 1 + "-> " + a.getAccountNumber() + " " + a.getCurrency());
        }
    }

    public static int findAccount(String currency) {
        Card card = AdminService.readCard(inspectCardNumber());
        List<Account> accountList = card.getAccount();
        for (Account a : accountList) {
            if (a.getCurrency().equals(currency)) {
                return accountList.indexOf(a);
            }
        }
        return -1;
    }
}