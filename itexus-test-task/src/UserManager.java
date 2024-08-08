import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager {
    private static final String FILENAME = "users.txt";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^375[0-9]{9}$");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<User> users = loadUsers();

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Редактировать пользователя");
            System.out.println("3. Удалить пользователя");
            System.out.println("4. Получить информацию о пользователе");
            System.out.println("5. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser(scanner, users);
                    break;
                case 2:
                    editUser(scanner, users);
                    break;
                case 3:
                    deleteUser(scanner, users);
                    break;
                case 4:
                    getUser(scanner, users);
                    break;
                case 5:
                    saveUsers(users);
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Некорректный выбор, попробуйте еще раз.");
            }
        }
    }

    private static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try {
            File file = new File(FILENAME);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        String firstName = parts[0];
                        String lastName = parts[1];
                        String email = parts[2];
                        List<String> roles = Arrays.asList(parts[3].split("\\|"));
                        List<String> phones = Arrays.asList(parts[4].split("\\|"));
                        users.add(new User(firstName, lastName, email, roles, phones));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return users;
    }

    private static void saveUsers(List<User> users) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
            for (User user : users) {
                StringBuilder rolesSb = new StringBuilder();
                for (String role : user.getRoles()) {
                    rolesSb.append(role).append("|");
                }
                rolesSb.deleteCharAt(rolesSb.length() - 1);

                StringBuilder phonesSb = new StringBuilder();
                for (String phone : user.getPhones()) {
                    phonesSb.append(phone).append("|");
                }
                phonesSb.deleteCharAt(phonesSb.length() - 1);

                writer.write(user.getFirstName() + "," + user.getLastName() + "," + user.getEmail() + "," + rolesSb + "," + phonesSb);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    private static void createUser(Scanner scanner, List<User> users) {
        System.out.print("Введите имя: ");
        String firstName = scanner.nextLine();
        System.out.print("Введите фамилию: ");
        String lastName = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        do {
            if (isValidEmail(email)) {
            } else {
                System.out.println("Неверный формат email.");
                System.out.print("Введите email: ");
                email = scanner.nextLine();
            }
        }while (!isValidEmail(email));

        int numRoles;
        List<String> roles = new ArrayList<>();
        do {
            System.out.print("Введите роль: 1 - admin, 2 - user : ");
            numRoles = scanner.nextInt();
            scanner.nextLine();
            if (numRoles == 1 || numRoles == 2) {
                if (numRoles == 1){
                    roles.add("admin");
                }if (numRoles == 2){
                    roles.add("user");
                }
            } else{
                    System.out.print("неверный ввод роли");
                }

        } while (numRoles < 1 || numRoles > 2);

        int numPhones;
        List<String> phones = new ArrayList<>();
        do {
            System.out.print("Введите количество телефонов (1-3): ");
            numPhones = scanner.nextInt();
            scanner.nextLine();
            if (numPhones < 1 || numPhones > 3) {
                System.out.println("Количество телефонов должно быть от 1 до 3.");
            } else {
                for (int i = 0; i < numPhones; i++) {
                    System.out.print("Введите телефон " + (i + 1) + " в формате 375*********: ");
                    String phone = scanner.nextLine();
                    if (isValidPhone(phone)) {
                        phones.add(phone);
                    } else {
                        System.out.println("Неверный формат телефона.");
                        i--;
                    }
                }
            }
        } while (numPhones < 1 || numPhones > 3);

        User user = new User(firstName, lastName, email, roles, phones);
        users.add(user);
        System.out.println("Пользователь успешно создан.");
    }

    private static void editUser(Scanner scanner, List<User> users) {
        System.out.print("Введите email пользователя, которого нужно отредактировать: ");
        String email = scanner.nextLine();
        boolean found = false;
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                found = true;
                System.out.print("Введите новое имя (текущее: " + user.getFirstName() + "): ");
                String newFirstName = scanner.nextLine();
                System.out.print("Введите новую фамилию (текущая: " + user.getLastName() + "): ");
                String newLastName = scanner.nextLine();
                System.out.print("Введите новый email (текущий: " + user.getEmail() + "): ");
                String newEmail = scanner.nextLine();

                do {
                    if (isValidEmail(newEmail)) {
                    } else {
                        System.out.println("Неверный формат email.");
                        System.out.print("Введите новый email: ");
                        newEmail = scanner.nextLine();
                    }
                }while (!isValidEmail(newEmail));

                int numRoles;
                List<String> newRoles = new ArrayList<>();
                do {
                    System.out.print("Введите новую роль: 1 - admin, 2 - user : (текущая: " + user.getRoles() + "): ");
                    numRoles = scanner.nextInt();
                    scanner.nextLine();
                    if (numRoles == 1 || numRoles == 2) {
                        if (numRoles == 1){
                            newRoles.add("admin");
                        }if (numRoles == 2){
                            newRoles.add("user");
                        }
                    } else{
                        System.out.print("неверный ввод роли");
                    }

                } while (numRoles < 1 || numRoles > 2);

                int numPhones;
                List<String> newPhones = new ArrayList<>();
                do {
                    System.out.print("Введите новое количество телефонов (1-3) (текущее: " + user.getPhones().size() + "): ");
                    numPhones = scanner.nextInt();
                    scanner.nextLine();
                    if (numPhones < 1 || numPhones > 3) {
                        System.out.println("Количество телефонов должно быть от 1 до 3.");
                    } else {
                        for (int i = 0; i < numPhones; i++) {
                            System.out.print("Введите новый телефон " + (i + 1) + " в формате 375*********: ");
                            String newPhone = scanner.nextLine();
                            if (isValidPhone(newPhone)) {
                                newPhones.add(newPhone);
                            } else {
                                System.out.println("Неверный формат телефона.");
                                i--;
                            }
                        }
                    }
                } while (numPhones < 1 || numPhones > 3);

                user.setFirstName(newFirstName);
                user.setLastName(newLastName);
                user.setEmail(newEmail);
                user.setRoles(newRoles);
                user.setPhones(newPhones);
                System.out.println("Пользователь успешно отредактирован.");
                break;
            }
        }
        if (!found) {
            System.out.println("Пользователь с таким email не найден.");
        }
    }

    private static void deleteUser(Scanner scanner, List<User> users) {
        System.out.print("Введите email пользователя, которого нужно удалить: ");
        String email = scanner.nextLine();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) {
                found = true;
                users.remove(i);
                System.out.println("Пользователь успешно удален.");
                break;
            }
        }
        if (!found) {
            System.out.println("Пользователь с таким email не найден.");
        }
    }

    private static void getUser(Scanner scanner, List<User> users) {
        System.out.print("Введите email пользователя: ");
        String email = scanner.nextLine();
        boolean found = false;
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                found = true;
                System.out.println(user);
                break;
            }
        }
        if (!found) {
            System.out.println("Пользователь с таким email не найден.");
        }
    }

    private static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPhone(String phone) {
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        return matcher.matches();
    }
}