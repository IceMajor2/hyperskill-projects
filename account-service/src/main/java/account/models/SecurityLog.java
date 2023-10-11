package account.models;

import account.enums.Roles;
import account.enums.SecurityAction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.util.Date;

@Table(name = "logs")
@Entity
@JsonPropertyOrder({"date", "action", "subject", "object", "path"})
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

    public SecurityLog() {
    }

    public SecurityLog(SecurityAction action, String subject, String object, String path) {
        this.date = new Date();
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public SecurityAction getAction() {
        return action;
    }

    public void setAction(SecurityAction action) {
        this.action = action;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
