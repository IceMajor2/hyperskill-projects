package account.models;

import account.DTO.PaymentDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;
    @Column
    private YearMonth period;
    @Column
    private Long salary;

    public Payment() {}

    public Payment(PaymentDTO paymentDTO, User user) {
        this.user = user;
        this.period = YearMonth.parse(paymentDTO.getPeriod(), DateTimeFormatter.ofPattern("MM-yyyy"));
        this.salary = paymentDTO.getSalary();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
