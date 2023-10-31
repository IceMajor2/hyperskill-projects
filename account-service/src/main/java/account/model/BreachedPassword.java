package account.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "breached_passwords")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreachedPassword {

    @Id
    private Long id;
    private String password;
}
