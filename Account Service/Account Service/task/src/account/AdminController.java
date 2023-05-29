package account;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PutMapping("/user/role")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public void changeUserRole() {

    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public void deleteUser() {

    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public void usersInfo() {

    }
}
