package org.atm.service;

import org.atm.util.Strategy.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuService {
    private static final String WELCOME = "Добро пожаловать в супербанк!" + "\n" +
            "-----------------------------------------------\n" +
            "для продолжения работы нажмите: " + "\n" +
            "1 -> для админки" + "\n" +
            "2 -> чтобы воспользоваться банкоматом" + "\n" +
            "0 -> для того чтоб завершить работу\n" +
            "-----------------------------------------------";

    private static final String ADMIN = "для продолжения работы нажмите: " + "\n" +
            "-----------------------------------------------\n" +
            "1 -> чтобы добавить новую карту" + "\n" +
            "2 -> чтобы внести деньги в банкомат" + "\n" +
            "3 -> чтобы внести узнать доступные суммы" + "\n" +
            "0 -> для того чтоб вернуться в предыдущее меню\n" +
            "-----------------------------------------------";
    private static final String USER = "для продолжения работы нажмите: " + "\n" +
            "-----------------------------------------------\n" +
            "1 -> чтобы узнать баланс" + "\n" +
            "2 -> чтобы снять деньги" + "\n" +
            "0 -> для того чтоб вернуться в предыдущее меню\n" +
            "-----------------------------------------------";

    public static void welcomePage() throws IOException {
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
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
            } else if (choose.equals("0")) {
                System.out.println("Всего хорошего!");
            } else {
                System.out.println("Главное меню Введите 1,2 или 0");
            }
        }
    }

    private static void adminPage(BufferedReader scanner) throws IOException {
        System.out.println(ADMIN);
        String admin = "";
        while (!admin.equals("0")) {
            admin = scanner.readLine();
            if (admin.equals("1")) {
                System.out.println("Введите номер карты");
                AdminService.writeCard(scanner.readLine());
                System.out.println("Карта успешно добавлена" + "\n" + "Нажмите 0 , чтобы продолжить");
            } else if (admin.equals("2")) {
                //todo : выносить логику в метод
                System.out.println("Введите валюту RUB,USD или EUR");
                Storage strategy = ATMService.defineStorage(scanner.readLine());
                if (strategy != null) {
                    strategy.fillStorage(scanner, ATMService.getStorage());
                    System.out.println("Деньги успешно добавлены в банкомат" + "\n" + "Нажмите 0 , чтобы продолжить");
                    ATMService.saveConsist();
                } else {
                    System.out.println("Валюта введена некорректно");
                    adminPage(scanner);
                }
            } else if (admin.equals("3")) {
                ATMService.availableSum();
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
            if (i < 2 && !ATMService.operationAvailable(pin)) {
                System.out.println("Вы ввели неверный pin " +
                        "\nещё попыток: " + (2 - i));
            } else if (i == 2 && !ATMService.operationAvailable(pin)) {
                System.out.println("Вы ввели неверный pin и будете возвращены в главное меню" + "\n");
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
                ATMService.showAccounts();
                System.out.println("Введите необходимую цифру для выбора счета или 0, чтобы выйти");
                int accountIndex = Integer.parseInt(scanner.readLine());
                if (accountIndex == 0) {
                    userPage(scanner);
                }
                ATMService.getBalance(accountIndex);
                System.out.println("\n" + "Нажмите 0 , чтобы продолжить");
            } else if (user.equals("2")) {
                System.out.println("Введите желаемую валюту: RUB, USD, EUR");
                String currency = scanner.readLine();
                Storage strategy = ATMService.defineStorage(currency); // определили стратегию валютного стораджа
                if (strategy != null) {
                    System.out.println("Введите сумму, которую вы хотите снять");
                    double summa = Double.parseDouble(scanner.readLine());
                    if (!strategy.availableForGetMoney(summa, ATMService.getStorage())) { // узнали что денег на соответствующем счете нет
                        System.out.println("На текущем счете недостаточно средств\n" +
                                "желаете снять с другого?\n" +
                                " конвертация будет осуществлена по текущему курсу ЦБ" + "\n" +
                                "да/нет?");
                        String answer = scanner.readLine();
                        if (answer.equals("да")) {
                            System.out.println("Желаете снять " + summa + " " + currency + " с другого счета?" + "\n"); // сумма и валюта оригинальные
                            ATMService.showAccounts();
                            System.out.println("Введите необходимую цифру для выбора счета или 0, чтобы выйти");
                            int accountIndex = Integer.parseInt(scanner.readLine());
                            if (accountIndex == 0) {
                                userPage(scanner);
                            }
                            //todo здесь правильный обмен, но сумма выдается не с того стораджа
                            Storage alternativeStrategy = ATMService.defineStorage(ATMService.getCurrency(accountIndex));
                            if(alternativeStrategy != null
                                    && alternativeStrategy.availableForGetMoney(alternativeStrategy.exchange(currency,summa),ATMService.getStorage())){

                                strategy.getMoney(summa, ATMService.getStorage()); // снимает деньги со стораджа валюты
                                CardService.updateBalance(alternativeStrategy.exchange(currency, summa), ATMService.getCurrency(accountIndex));
                            }

                        } else {
                            System.out.println("Вы не выбрали да");
                            userPage(scanner);
                        }
                    }
                    CardService.updateBalance(summa, currency);
                    System.out.println("\n" + "Нажмите 0 , чтобы продолжить");
                } else {
                    System.out.println("Валюта введена некорректно");
                    userPage(scanner);
                }
            } else if (user.equals("0")) {
                System.out.println();
            } else {
                System.out.println("User Введите 1,2 или 0");
            }
        }
        System.out.println(WELCOME);
    }
}
