package account.controllers;

import account.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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
    public ResponseEntity usersInfo() {
        var users = adminService.getUsersList();
        return ResponseEntity.ok(users);
    }
}
