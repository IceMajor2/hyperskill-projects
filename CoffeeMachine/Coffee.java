package machine;

public enum Coffee {
    ESPRESSO(250, 0, 16, 4), LATTE(350, 75, 20, 7), CAPPUCINO(200, 100, 12, 6);

    private final int water;
    private final int milk;
    private final int beans;
    private final int money;

    Coffee(int water, int milk, int beans, int money) {
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.money = money;
    }

    public int beans() {
        return beans;
    }

    public int milk() {
        return milk;
    }

    public int water() {
        return water;
    }

    public int money() {
        return money;
    }
}
