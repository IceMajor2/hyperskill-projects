package antifraud.model;

import antifraud.DTO.TransactionDTO;
import antifraud.Enum.Region;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
    private String ip;
    private String number;
    @Enumerated(EnumType.STRING)
    private Region region;
    private LocalDateTime date;

    public Transaction() {
    }

    public Transaction(Long id, Long amount, String ip, String number, Region region, LocalDateTime date) {
        this.id = id;
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
    }

    public Transaction(TransactionDTO transactionDTO) {
        this.amount = transactionDTO.getAmount();
        this.number = transactionDTO.getNumber();
        this.ip = transactionDTO.getIp();
        this.region = Region.valueOf(transactionDTO.getRegion());
        this.date = (LocalDateTime) DateTimeFormatter.ofPattern("yyy-MM-ddTHH:mm:ss").parse(transactionDTO.getDate());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
