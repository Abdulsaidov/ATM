package org.atm.service;


import org.atm.model.Account;
import org.atm.model.Card;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    public static void updateCard(Card card){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(card.getNumber() + ".dat"))){
            oos.writeObject(card);
        }catch(IOException e){
            throw new RuntimeException("can't update");
        }
    }
    public static void writeCard(String cardNumber) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cardNumber + ".dat"));
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
//            String number, Double balance, String currency
            List<Account> accountList = new ArrayList<>();
            accountList.add(createAccount(br));
            System.out.println("Добавить еще счет? да/нет");
            String more = br.readLine();
            if (more.equals("да")) {
                accountList.add(createAccount(br));
            }
            else{
                System.out.println("Введите данные карты");
            }
            Card card = createCard(br);
            card.setAccount(accountList);
            oos.writeObject(card);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Card readCard(String cardNumber) {
        Card card;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cardNumber + ".dat"))) {
            card = (Card) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return card;
    }

    private static Account createAccount(BufferedReader br) throws IOException {
//            String number, Double balance, String currency
        System.out.println("accountNumber");
        String accountNumber = br.readLine();
        System.out.println("balance");
        Double balance = Double.valueOf(br.readLine());
        System.out.println("currency");
        String currency = br.readLine();
        return CardService.addAccount(accountNumber, balance, currency);
    }

    private static Card createCard(BufferedReader br) throws IOException {
        System.out.println("cardNumber");
        String cardNumber = br.readLine();
        System.out.println("pin");
        String pin = br.readLine();
        System.out.println("holder");
        String holder = br.readLine();
        System.out.println("expire");
        String expire = br.readLine();
        return CardService.addCard(cardNumber, pin, holder,expire);
    }

}
