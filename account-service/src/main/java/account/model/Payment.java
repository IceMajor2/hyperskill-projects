package account.model;

import account.dto.PaymentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Column
    @Temporal(TemporalType.DATE)
    private Date period;

    @Column
    private Long salary;

    public Payment(PaymentDTO paymentDTO, User user) throws ParseException {
        this.user = user;
        this.period = new SimpleDateFormat("MM-yyyy").parse(paymentDTO.getPeriod());
        this.salary = paymentDTO.getSalary();
    }

    public Date getPeriod() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(this.period.toString());
        } catch (ParseException exception) {
            return this.period;
        }
    }
}
