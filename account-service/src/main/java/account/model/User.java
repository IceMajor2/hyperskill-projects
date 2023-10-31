package account.model;

import account.dto.UserDTO;
import account.enumerated.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    @JsonProperty("lastname")
    private String lastName;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @NotEmpty
    @JsonIgnore
    private String password;

    @NotEmpty
    @JsonProperty("roles")
    private List<Roles> roles;

    @JsonIgnore
    private boolean accountNonLocked = true;

    public User(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.lastName = userDTO.getLastname();
        this.email = userDTO.getEmail().toLowerCase();
        this.password = userDTO.getPassword();
        this.roles = new ArrayList<>();
    }

    @JsonIgnore
    public boolean isAdmin() {
        return roles.contains(Roles.ROLE_ADMINISTRATOR);
    }

    public List<Roles> getRoles() {
        roles.sort(Comparator.naturalOrder());
        return roles;
    }

    public void addRole(Roles role) {
        this.roles.add(role);
    }

    public boolean hasRole(Roles role) {
        return this.roles.contains(role);
    }

    public int rolesCount() {
        return this.roles.size();
    }

    public void removeRole(Roles role) {
        this.roles.remove(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return accountNonLocked == user.accountNonLocked && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, email, password, roles, accountNonLocked);
    }
}
