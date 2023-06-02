package account.models;

import account.DTO.UserDTO;
import account.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "users")
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

    public User() {}

    public List<Roles> getRoles() {
        roles.sort(Comparator.naturalOrder());
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public void addRole(Roles role) {
        this.roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return roles.contains(Roles.ROLE_ADMINISTRATOR);
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
}
