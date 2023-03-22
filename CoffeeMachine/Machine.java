package machine;

public class Machine {

    private int water;
    private int milk;
    private int beans;
    private int cups;
    private int money;

    public Machine() {
        this.water = 400;
        this.milk = 540;
        this.beans = 120;
        this.cups = 9;
        this.money = 550;
    }

    public String buy(Coffee coffee) {
        if (!(water >= coffee.water())) {
            return "water";
        }
        if (!(milk >= coffee.milk())) {
            return "milk";
        }
        if (!(beans >= coffee.beans())) {
            return "coffee beans";
        }
        if(cups == 0) {
            return "cups";
        }
        water -= coffee.water();
        milk -= coffee.milk();
        beans -= coffee.beans();
        money += coffee.money();
        cups--;
        return null;
    }

    public void addWater(int ml) {
        this.water += ml;
    }

    public void addMilk(int ml) {
        this.milk += ml;
    }

    public void addCups(int amount) {
        this.cups += amount;
    }

    public void addBeans(int g) {
        this.beans += g;
    }

    public int take() {
        int taken = this.money;
        this.money = 0;
        return taken;
    }

    @Override
    public String toString() {
        return "The coffee machine has:\n"
                + water + " ml of water\n"
                + milk + " ml of milk\n"
                + beans + " g of coffee beans\n"
                + cups + " disposable cups\n"
                + "$" + money + " of money";
    }

}
