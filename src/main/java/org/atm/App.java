package org.atm;

import org.atm.service.ATMService;
import org.atm.service.AdminService;

import java.util.Scanner;


public class App {
    private static final String WELCOME = "Добро пожаловать в супербанк!" + "\n" +
            "для продолжения работы нажмите: " + "\n" +
            "1 - для админки" + "\n" +
            "2 - чтобы воспользоваться банкоматом" + "\n" +
            "0 - для того чтоб завершить работу";
    private static final String ADMIN = "для продолжения работы нажмите: " + "\n" +
            "1 - чтобы добавить новую карту" + "\n" +
            "2 - чтобы внести деньги в банкомат" + "\n" +
            "0 - для того чтоб вернуться в предыдущее меню";
    private static final String USER ="для продолжения работы нажмите: " + "\n" +
            "1 - чтобы узнать баланс" + "\n" +
            "2 - чтобы снять деньги" + "\n" +
            "0 - для того чтоб вернуться в предыдущее меню";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        setWelcome(scanner);

    }
    private static void setWelcome(Scanner scanner) {
        System.out.println(WELCOME);
        String choose = "";
        while (!choose.equals("0")) {
            choose = scanner.next();
            if (choose.equals("1")) {
                // admins side
                setAdmin(scanner);
            } else if (choose.equals("2")) {
                // users side
                setUser(scanner);
            } else {
                System.out.println("Главное меню Введите 1,2 или 0");
            }
        }
    }
    private static void setAdmin(Scanner scanner) {
        System.out.println(ADMIN);
        String admin = "";
        while (!admin.equals("0")) {
            admin = scanner.next();
            if (admin.equals("1")) {
                System.out.println("Введите номер карты");
                String cardNumber = scanner.next();
                AdminService.writeCard(cardNumber);
                System.out.println("Карта успешно добавлена \"\\n\" + \"Нажмите 0 , чтобы продолжить\"");
            } else if (admin.equals("2")) {
                System.out.println("Введите количество 10 рублевых купюр");
                int tens = scanner.nextInt();
                System.out.println("Введите количество 20 рублевых купюр");
                int twenty = scanner.nextInt();
                System.out.println("Введите количество 50 рублевых купюр");
                int fifty = scanner.nextInt();
                System.out.println("Введите количество 100 рублевых купюр");
                int hundred = scanner.nextInt();
                ATMService.fillStorage(tens, twenty, fifty, hundred);
                System.out.println("Деньги успешно добавлены в банкомат"+"\n" + "Нажмите 0 , чтобы продолжить");
            } else if (admin.equals("0")) {
//                        choose = "";
                System.out.println("\n");
            } else {
                System.out.println("Admin : Введите 1,2 или 0");
            }
        }
        System.out.println("admin " + WELCOME);
    }
    private static void setUser(Scanner scanner) {
        System.out.println("Введите pin code");
        String pin = scanner.next();
        ATMService.operationAvailable(pin);
        System.out.println(USER);
        String user = "";
        while (!user.equals("0")) {
            user = scanner.next();
            if (user.equals("1")) {
                ATMService.getBalance();
                System.out.println("\n" + "Нажмите 0 , чтобы продолжить");
            } else if (user.equals("2")) {
                System.out.println("Введите сумму, которую вы хотите снять");
                double summa = scanner.nextDouble();
                ATMService.getMoney(summa);
                System.out.println("\n" + "Нажмите 0 , чтобы продолжить");
            } else if (user.equals("0")) {
                System.out.println("\n");
            }else {
                System.out.println("Юзер Введите 1,2 или 0");
            }
        }
        System.out.println("user " + WELCOME);
    }
}
