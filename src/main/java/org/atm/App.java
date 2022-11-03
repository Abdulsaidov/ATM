package org.atm;

import org.atm.service.MenuService;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        try {
            MenuService.welcomePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(ATMService.getStorage().get("balanceRUB"));
//        System.out.println(ATMService.getStorage().get("balanceEUR"));
//        System.out.println(ATMService.getStorage().get("balanceUSD"));
    }
}
