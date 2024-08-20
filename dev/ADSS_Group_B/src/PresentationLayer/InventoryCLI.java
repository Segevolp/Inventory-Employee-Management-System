package PresentationLayer;

import ServiceLayer.Inventory.*;
import ServiceLayer.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InventoryCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final InventoryService inventoryService;

    static {
        try {
            inventoryService = new InventoryService(
                    msg -> itsSunday(),
                    msg -> System.out.println(msg)
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
    }

    public static void MainManagerMenu(){
        boolean exit = false;
        while (!exit) {
            printMainManagerMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    productMenu();
                    break;
                case "2":
                    itemMenu();
                    break;
                case "3":
                    discountMenu();
                    break;
                case "4":
                    orderMenu();
                    break;
                case "5":
                    reportMenu();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void MainEmployeeMenu(){
        boolean exit = false;
        while (!exit) {
            printMainEmployeeMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    productMenu();
                    break;
                case "2":
                    itemMenu();
                    break;
                case "3":
                    reportMenu();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void printMainManagerMenu() {
        System.out.println("\nInventory Management System");
        System.out.println("1. Products");
        System.out.println("2. Items");
        System.out.println("3. Discounts");
        System.out.println("4. Orders");
        System.out.println("5. Reports");
        System.out.println("0. Back to last menu");
        System.out.print("Enter your choice: ");
    }

    private static void printMainEmployeeMenu(){
        System.out.println("\nInventory Management System");
        System.out.println("1. Products");
        System.out.println("2. Items");
        System.out.println("3. Reports");
        System.out.println("0. Back to last menu");
        System.out.print("Enter your choice: ");
    }

    private static void productMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nProduct Management");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. Get All Products");
            System.out.println("0. Back to last menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addProduct();
                    break;
                case "2":
                    removeProduct();
                    break;
                case "3":
                    getAllProducts();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void itemMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nItem Management");
            System.out.println("1. Add Item");
            System.out.println("2. Remove Item");
            System.out.println("3. Move Item to Shelf");
            System.out.println("4. Move Item to Storage");
            System.out.println("5. Report Defective Item");
            System.out.println("6. Get All Items From Product Makat");
            System.out.println("0. Back to last menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addItem();
                    break;
                case "2":
                    removeItem();
                    break;
                case "3":
                    moveItemToShelf();
                    break;
                case "4":
                    moveItemToStorage();
                    break;
                case "5":
                    reportDefective();
                    break;
                case "6":
                    getAllItems();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void discountMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nDiscount Management");
            System.out.println("1. Add Discount from Supplier");
            System.out.println("2. Add Discount from Store");
            System.out.println("3. Remove Discount from Supplier");
            System.out.println("4. Remove Discount from Store");
            System.out.println("5. Get Current Discounts");
            System.out.println("0. Back to last menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addDiscountFromSupplier();
                    break;
                case "2":
                    addDiscountFromStore();
                    break;
                case "3":
                    removeDiscountFromSupplier();
                    break;
                case "4":
                    removeDiscountFromStore();
                    break;
                case "5":
                    displayResponse(inventoryService.getDiscounts());
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void orderMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nOrder Management");
            System.out.println("1. Add Order");
            System.out.println("2. Update Order");
            System.out.println("3. Delete Order");
            System.out.println("4. Get All Orders");
            System.out.println("0. Back to last menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addOrder();
                    break;
                case "2":
                    updateOrder();
                    break;
                case "3":
                    deleteOrder();
                    break;
                case "4":
                    getOrders();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void reportMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nReport Management");
            System.out.println("1. Make Report");
            System.out.println("0. Back to last menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    makeReport();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void itsSunday() {
        System.out.println("It's Sunday today, do you wish to make a report?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        String choice = scanner.nextLine();
        if (Objects.equals(choice, "1")) {
            makeReport();
        }
    }

    private static int promptInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }


    private static double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static boolean promptBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Boolean.parseBoolean(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter true or false.");
            }
        }
    }

    private static Date promptDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return dateFormat.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
    }

    private static void addItem() {
        int makat = promptInt("Enter makat: ");
        int sn = promptInt("Enter serial number: ");
        Date expiredDate = promptDate("Enter expiration date (yyyy-MM-dd): ");
        boolean inStorage = promptBoolean("Is in storage (true/false): ");

        displayResponse(inventoryService.addItem(makat, sn, expiredDate, inStorage));
    }

    private static void removeItem() {
        int makat = promptInt("Enter makat: ");
        int sn = promptInt("Enter serial number: ");

        displayResponse(inventoryService.removeItem(makat, sn));
    }

    private static void addProduct() {
        int makat = promptInt("Enter makat: ");
        int minimalAmount = promptInt("Enter minimal amount: ");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter place: ");
        String place = scanner.nextLine();
        System.out.print("Enter manufacturer: ");
        String manufacturer = scanner.nextLine();
        double costPrice = promptDouble("Enter cost price: ");
        double currentPrice = promptDouble("Enter current price: ");
        System.out.print("Enter categories (comma separated): ");
        String[] categories = scanner.nextLine().split(",");
        int popularity = promptInt("Enter popularity: ");

        displayResponse(inventoryService.addProduct(makat, minimalAmount, name, place, manufacturer, costPrice, currentPrice, categories, popularity));
    }

    private static void removeProduct() {
        int makat = promptInt("Enter makat: ");
        System.out.print("Enter categories (comma separated): ");
        String[] categories = scanner.nextLine().split(",");

        displayResponse(inventoryService.removeProduct(makat, categories));
    }

    private static void moveItemToShelf() {
        int makat = promptInt("Enter makat: ");
        int amount = promptInt("Enter amount: ");

        displayResponse(inventoryService.moveItemToShelf(makat, amount));
    }

    private static void moveItemToStorage() {
        int makat = promptInt("Enter makat: ");
        int amount = promptInt("Enter amount: ");

        displayResponse(inventoryService.moveItemToStorage(makat, amount));
    }

    private static void reportDefective() {
        int makat = promptInt("Enter makat: ");
        int amount = promptInt("Enter amount: ");

        displayResponse(inventoryService.reportDefective(makat, amount));
    }

    private static void makeReport() {
        UserInput userInput = getUserInput();
        if (userInput == null) {
            System.out.println("Invalid choice.");
            return;
        }

        displayResponse(inventoryService.makeReport(userInput.catNames, userInput.prodMakats));
    }

    private static void addDiscountFromSupplier() {
        // Print current products and categories for reference
        System.out.println("Current Products:");
        displayResponse(inventoryService.getAllProducts());

        UserInput userInput = getUserInput();
        if (userInput == null) {
            System.out.println("Invalid choice.");
            return;
        }

        int percentage = promptInt("Enter percentage: ");

        displayResponse(inventoryService.addDiscountFromSupplier(userInput.prodMakats, userInput.catNames, percentage));
    }

    private static void addDiscountFromStore() {
        // Print current products and categories for reference
        System.out.println("Current Products:");
        displayResponse(inventoryService.getAllProducts());

        UserInput userInput = getUserInput();
        if (userInput == null) {
            System.out.println("Invalid choice.");
            return;
        }

        int percentage = promptInt("Enter percentage: ");

        displayResponse(inventoryService.addDiscountFromStore(userInput.prodMakats, userInput.catNames, percentage));
    }

    private static void removeDiscountFromSupplier() {
        // Print current discounts for reference
        System.out.println("Current Discounts:");
        displayResponse(inventoryService.getDiscounts());

        int orderId = promptInt("Enter discount ID: ");

        displayResponse(inventoryService.removeDiscountFromSupplier(orderId));
    }

    private static void removeDiscountFromStore() {
        // Print current discounts for reference
        System.out.println("Current Discounts:");
        displayResponse(inventoryService.getDiscounts());

        int orderId = promptInt("Enter discount ID: ");

        displayResponse(inventoryService.removeDiscountFromStore(orderId));
    }

    public static void loadInitialData() {
        inventoryService.loadInitialData();
    }

    private static void getAllProducts() {
        displayResponse(inventoryService.getAllProducts());
    }

    private static void getAllItems() {
        int makat = promptInt("Enter makat: ");
        displayResponse(inventoryService.getAllItems(makat));
    }

    private static void addOrder() {
        System.out.println("Current Products: ");
        int makat = promptInt("Enter makat: ");
        int amount = promptInt("Enter amount: ");
        int dayOfMonth = promptInt("Enter day of the month: ");

        displayResponse(inventoryService.addOrder(makat, amount, dayOfMonth));
    }

    private static void updateOrder() {
        System.out.println("Current orders:");
        getOrders();
        int orderId = promptInt("Enter order ID: ");
        int newAmount = promptInt("Enter new amount: ");

        displayResponse(inventoryService.updateOrder(orderId, newAmount));
    }

    private static void deleteOrder() {
        System.out.println("Current orders:");
        getOrders();
        int orderId = promptInt("Enter order ID: ");

        displayResponse(inventoryService.deleteOrder(orderId));
    }

    private static void getOrders() {
        displayResponse(inventoryService.getOrders());
    }

    private static UserInput getUserInput() {
        System.out.println("1. Provide product makats");
        System.out.println("2. Provide category names");
        System.out.println("3. Provide both product makats and category names");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        List<Integer> prodMakats = null;
        List<String[]> catNames = null;

        switch (choice) {
            case "1":
                prodMakats = promptMakats();
                break;
            case "2":
                catNames = promptCategoryNames();
                break;
            case "3":
                prodMakats = promptMakats();
                catNames = promptCategoryNames();
                break;
            default:
                return null;
        }
        return new UserInput(prodMakats, catNames);
    }

    private static List<Integer> promptMakats() {
        System.out.print("Enter product makats (comma separated): ");
        String[] makatsStr = scanner.nextLine().split(",");
        List<Integer> makats = new LinkedList<>();
        for (String makatStr : makatsStr) {
            try {
                makats.add(Integer.parseInt(makatStr.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid makat: " + makatStr);
            }
        }
        return makats;
    }

    private static List<String[]> promptCategoryNames() {
        System.out.print("Enter category names (comma separated): ");
        String[] categories = scanner.nextLine().split(",");
        List<String[]> catNames = new LinkedList<>();
        for (String category : categories) {
            catNames.add(category.trim().split(" "));
        }
        return catNames;
    }

    private static void displayResponse(Response response) {
        if (response.getErrorMsg() == null) {
            System.out.println(response.getReturnValue());
        } else {
            System.out.println("Operation failed: " + response.getErrorMsg());
        }
    }

    private static class UserInput {
        List<Integer> prodMakats;
        List<String[]> catNames;

        UserInput(List<Integer> prodMakats, List<String[]> catNames) {
            this.prodMakats = prodMakats;
            this.catNames = catNames;
        }
    }
}