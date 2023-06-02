package account.models;

import jakarta.persistence.*;

@Entity
@Table(name = "breached_passwords")
public class BreachedPassword {

    @Id
    private Long id;
    private String password;

    public BreachedPassword() {}

    public BreachedPassword(Long id, String password) {
        this.password = password;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
