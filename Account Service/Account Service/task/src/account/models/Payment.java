package account.models;

import account.DTO.PaymentDTO;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @DateTimeFormat(pattern = "MM-yyyy")
    private Date period;
    @Column
    private Long salary;

    public Payment() {}

    public Payment(PaymentDTO paymentDTO, User user) throws ParseException {
        this.user = user;
        this.period = new SimpleDateFormat("MM-yyyy").parse(paymentDTO.getPeriod());
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

    public Date getPeriod() throws ParseException {
        return this.period;
        //return new SimpleDateFormat("MM-yyyy").parse(this.period.toString());
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
