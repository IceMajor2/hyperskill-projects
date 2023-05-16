package antifraud;

public class Transaction {

    private Long amount;

    public Transaction() {}

    public Transaction(Long amount) {
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
