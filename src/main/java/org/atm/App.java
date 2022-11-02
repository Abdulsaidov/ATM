package org.atm;

import org.atm.service.ATMService;
import org.atm.service.MenuService;

public class App {

    public static void main(String[] args) {
        MenuService.welcomePage();
//        System.out.println(ATMService.getStorage().get("balanceRUB"));
//        System.out.println(ATMService.getStorage().get("balanceEUR"));
//        System.out.println(ATMService.getStorage().get("balanceUSD"));
    }
}
