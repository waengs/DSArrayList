import java.util.ArrayList;
import java.util.Scanner;

class User {
    String username;
    String password;
    boolean isFirstPurchase;

    User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isFirstPurchase = true; // Indicates that the user has not made a purchase yet
    }
}

class Drink {
    String name;
    double price;
    int purchaseCount;
    ArrayList<String> toppings; // To track all available toppings
    ArrayList<Integer> toppingCounts; // To track the number of times each topping is added

    Drink(String name, double price) {
        this.name = name;
        this.price = price;
        this.purchaseCount = 0;
        this.toppings = new ArrayList<>();
        this.toppingCounts = new ArrayList<>();
    }

    void addTopping(String topping) {
        if (!toppings.contains(topping)) {
            toppings.add(topping);
            toppingCounts.add(0);
        }
    }

    void addPurchase(String topping) {
        purchaseCount++;
        int index = toppings.indexOf(topping);
        if (index != -1) {
            toppingCounts.set(index, toppingCounts.get(index) + 1);
        }
    }

    ArrayList<String> getSortedToppings() {
        ArrayList<String> sortedToppings = new ArrayList<>(toppings);
        sortedToppings.sort((t1, t2) -> Integer.compare(toppingCounts.get(toppings.indexOf(t2)), toppingCounts.get(toppings.indexOf(t1))));
        return sortedToppings;
    }

    String getMostPopularTopping() {
        int maxIndex = 0;
        for (int i = 1; i < toppingCounts.size(); i++) {
            if (toppingCounts.get(i) > toppingCounts.get(maxIndex)) {
                maxIndex = i;
            }
        }
        return toppings.size() > 0 ? toppings.get(maxIndex) : "None";
    }
}

class CartItem {
    Drink drink;
    String topping;
    double finalPrice;

    CartItem(Drink drink, String topping, double finalPrice) {
        this.drink = drink;
        this.topping = topping;
        this.finalPrice = finalPrice;
    }
}

public class Main {
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Drink> drinks = new ArrayList<>();
    private static ArrayList<CartItem> cart = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initialize drinks menu
        Drink coke = new Drink("Coke", 1.50);
        coke.addTopping("Lemon");
        coke.addTopping("Ice");

        Drink pepsi = new Drink("Pepsi", 1.45);
        pepsi.addTopping("Lemon");
        pepsi.addTopping("Ice");

        Drink sprite = new Drink("Sprite", 1.40);
        sprite.addTopping("Mint");
        sprite.addTopping("Ice");

        Drink fanta = new Drink("Fanta", 1.55);
        fanta.addTopping("Orange Slice");
        fanta.addTopping("Ice");

        drinks.add(coke);
        drinks.add(pepsi);
        drinks.add(sprite);
        drinks.add(fanta);

        while (true) {
            System.out.println("Welcome! Please choose an option:");
            System.out.println("1. Sign In");
            System.out.println("2. Sign Up");
            System.out.println("3. Continue as Guest");
            System.out.println("4. Quit");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    signIn();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    showMenu(null);
                    break;
                case 4:
                    System.out.println("Thank you for visiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void signIn() {
        System.out.println("Sign In:");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                System.out.println("Sign In successful! Welcome " + username);
                showMenu(user);
                return;
            }
        }
        System.out.println("Invalid username or password. Please try again.");
    }

    private static void signUp() {
        System.out.println("Sign Up:");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        users.add(new User(username, password));
        System.out.println("Sign Up successful! You can now sign in.");
    }

    private static void showMenu(User user) {
        while (true) {
            System.out.println("Menu:");
            for (int i = 0; i < drinks.size(); i++) {
                Drink drink = drinks.get(i);
                System.out.print((i + 1) + ". " + drink.name + " - $" + drink.price +
                        " (Purchased: " + drink.purchaseCount + ", Most popular topping: " + drink.getMostPopularTopping() + ")");
                System.out.print(" Toppings: ");
                for (String topping : drink.getSortedToppings()) {
                    System.out.print(topping + " (" + drink.toppingCounts.get(drink.toppings.indexOf(topping)) + ") ");
                }
                System.out.println();
            }
            System.out.println("0. View Cart and Checkout");
            System.out.println("Please choose a drink by number (or 0 to view cart and checkout):");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 0) {
                viewCartAndCheckout(user);
                break;
            }

            if (choice < 1 || choice > drinks.size()) {
                System.out.println("Invalid choice. Please try again.");
            } else {
                Drink selectedDrink = drinks.get(choice - 1);
                System.out.println("Available toppings for " + selectedDrink.name + ":");
                for (int i = 0; i < selectedDrink.toppings.size(); i++) {
                    System.out.println((i + 1) + ". " + selectedDrink.toppings.get(i));
                }
                System.out.print("Enter the number of a topping (or 0 for none): ");
                int toppingChoice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                String topping = "None";
                double toppingPrice = 0;
                if (toppingChoice > 0 && toppingChoice <= selectedDrink.toppings.size()) {
                    topping = selectedDrink.toppings.get(toppingChoice - 1);
                    toppingPrice = 1.0; // Each topping costs $1
                }

                double finalPrice = selectedDrink.price + toppingPrice;
                cart.add(new CartItem(selectedDrink, topping, finalPrice));
                System.out.println("Added to cart!");
            }
        }
    }

    private static void viewCartAndCheckout(User user) {
        System.out.println("Cart:");
        double total = 0;
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            System.out.println((i + 1) + ". " + item.drink.name + " with " + item.topping + " - $" + String.format("%.2f", item.finalPrice));
            total += item.finalPrice;
        }

        double discount = 0;
        if (user != null) {
            if (user.isFirstPurchase) {
                discount = 0.69; // 69% discount for the first purchase
                user.isFirstPurchase = false;
            } else {
                discount = 0.06; // 6% discount for subsequent purchases
            }
        }

        double discountedTotal = total * (1 - discount);
        System.out.println("Original Total: $" + String.format("%.2f", total));
        System.out.println("Discount applied: " + (discount * 100) + "%");
        System.out.println("Final Total: $" + String.format("%.2f", discountedTotal));
        System.out.println("Do you want to checkout? (yes/no)");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("yes")) {
            for (CartItem item : cart) {
                item.drink.addPurchase(item.topping);
            }
            cart.clear();
            System.out.println("Thank you for your purchase!");
        } else {
            System.out.println("Returning to menu.");
        }
    }
}
