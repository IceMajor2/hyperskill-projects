import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;
    private Machine coffeeMachine;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.coffeeMachine = new Machine();
    }

    public void run() {
        while (true) {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            String action = scanner.nextLine();

            if(action.equals("exit")) {
                break;
            }
            if(action.equals("remaining")) {
                System.out.println(coffeeMachine);
                continue;
            }
            if(action.equals("buy")) {
                System.out.println("What do you want to buy? "
                + "1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                String choice = scanner.nextLine();
                if(choice.equals("back")) {
                    continue;
                }
                int intChoice = Integer.valueOf(choice);
                Coffee coffee = Coffee.values()[intChoice - 1];
                String buyResult = coffeeMachine.buy(coffee);
                if(buyResult == null) {
                    System.out.println("I have enough resources, making you a coffee!");
                    continue;
                }
                System.out.println("Sorry, not enough " + buyResult + "!");
            }
            if(action.equals("fill")) {
                System.out.println("Write how many ml of water you want to add:");
                int input = Integer.valueOf(scanner.nextLine());
                coffeeMachine.addWater(input);

                System.out.println("Write how many ml of milk you want to add:");
                input = Integer.valueOf(scanner.nextLine());
                coffeeMachine.addMilk(input);

                System.out.println("Write how many grams of coffee beans you want to add:");
                input = Integer.valueOf(scanner.nextLine());
                coffeeMachine.addBeans(input);

                System.out.println("Write how many disposable cups you want to add:");
                input = Integer.valueOf(scanner.nextLine());
                coffeeMachine.addCups(input);
            }
            if(action.equals("take")) {
                System.out.println("I gave you $" + coffeeMachine.take());
            }
        }
    }
}
