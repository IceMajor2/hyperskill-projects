package account.model;

import account.enumerated.SecurityAction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Table(name = "logs")
@Entity
@JsonPropertyOrder({"date", "action", "subject", "object", "path"})
@NoArgsConstructor
@Getter
@Setter
public class SecurityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Enumerated(EnumType.STRING)
    private SecurityAction action;
    private String subject;
    private String object;
    private String path;

    public SecurityLog(SecurityAction action, String subject, String object, String path) {
        this.date = new Date();
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }
}
