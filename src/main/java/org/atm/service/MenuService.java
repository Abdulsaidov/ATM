package org.atm.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuService {
    private static final String WELCOME = "Добро пожаловать в супербанк!" + "\n" +
            "-----------------------------------------------\n" +
            "для продолжения работы нажмите: " + "\n" +
            "1 -> для админки" + "\n" +
            "2 -> чтобы воспользоваться банкоматом" + "\n" +
            "0 -> для того чтоб завершить работу\n"+
            "-----------------------------------------------";

    private static final String ADMIN = "для продолжения работы нажмите: " + "\n" +
            "-----------------------------------------------\n" +
            "1 -> чтобы добавить новую карту" + "\n" +
            "2 -> чтобы внести деньги в банкомат" + "\n" +
            "0 -> для того чтоб вернуться в предыдущее меню\n" +
            "-----------------------------------------------";
    private static final String USER = "для продолжения работы нажмите: " + "\n" +
            "-----------------------------------------------\n" +
            "1 -> чтобы узнать баланс" + "\n" +
            "2 -> чтобы снять деньги" + "\n" +
            "0 -> для того чтоб вернуться в предыдущее меню\n"+
            "-----------------------------------------------";

    public static void welcomePage() {
        try (BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println(WELCOME);
            String choose = "";
            while (!choose.equals("0")) {
                choose = scanner.readLine();
                if (choose.equals("1")) {
                    // admins side
                    adminPage(scanner);
                } else if (choose.equals("2")) {
                    // users side
                    userPage(scanner);
                } else {
                    System.out.println("Главное меню Введите 1,2 или 0");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Exception welcomePage");
        }
    }

    private static void adminPage(BufferedReader scanner) throws IOException {
        System.out.println(ADMIN);
        String admin = "";
        while (!admin.equals("0")) {
            admin = scanner.readLine();
            if (admin.equals("1")) {
                System.out.println("Введите номер карты");
                String cardNumber = scanner.readLine();
                AdminService.writeCard(cardNumber);
                System.out.println("Карта успешно добавлена" + "\n" + "Нажмите 0 , чтобы продолжить");
            } else if (admin.equals("2")) {
                System.out.println("Введите количество 10 рублевых купюр");
                int tens = Integer.parseInt(scanner.readLine());
                System.out.println("Введите количество 20 рублевых купюр");
                int twenty = Integer.parseInt(scanner.readLine());
                System.out.println("Введите количество 50 рублевых купюр");
                int fifty = Integer.parseInt(scanner.readLine());
                System.out.println("Введите количество 100 рублевых купюр");
                int hundred = Integer.parseInt(scanner.readLine());
                ATMService.fillStorage(tens, twenty, fifty, hundred);
                System.out.println("Деньги успешно добавлены в банкомат" + "\n" + "Нажмите 0 , чтобы продолжить");
            } else if (admin.equals("0")) {
                System.out.println();
            } else {
                System.out.println("Admin : Введите 1,2 или 0");
            }
        }
        System.out.println(WELCOME);
    }

    private static void userPage(BufferedReader scanner) throws IOException {
        String pin;
        for (int i = 0; i < 3; i++) {
            System.out.println("Введите pin code");
            pin = scanner.readLine();
            if (i<2 && !ATMService.operationAvailable(pin)) {
                System.out.println("Вы ввели неверный pin " +
                        "\nещё попыток: " + (2 - i));
            } else if (i==2 && !ATMService.operationAvailable(pin)) {
                System.out.println("Вы ввели неверный pin и будете возвращены в главное меню"+"\n");
                welcomePage();
            } else {
                break;
            }
        }

        System.out.println(USER);
        String user = "";
        while (!user.equals("0")) {
            user = scanner.readLine();
            if (user.equals("1")) {
                ATMService.getBalance();
                System.out.println("\n" + "Нажмите 0 , чтобы продолжить");
            } else if (user.equals("2")) {
                System.out.println("Введите сумму, которую вы хотите снять");
                double summa = Double.parseDouble(scanner.readLine());
                ATMService.getMoney(summa);
                System.out.println("\n" + "Нажмите 0 , чтобы продолжить");
            } else if (user.equals("0")) {
                System.out.println();
            } else {
                System.out.println("User Введите 1,2 или 0");
            }
        }
        System.out.println(WELCOME);
    }
}
