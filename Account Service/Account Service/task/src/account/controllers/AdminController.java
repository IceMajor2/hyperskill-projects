package account.controllers;

import account.DTO.RoleDTO;
import account.models.User;
import account.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping(value = {"/user/role", "/user/role/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity changeUserRole(@RequestBody @Valid RoleDTO roleDTO) {
        User user = adminService.changeRole(roleDTO);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(value = {"/user/{user_email}", "/user{user_email}"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity deleteUser(@PathVariable("user_email") String email) {
        User deletedUsr = adminService.deleteUser(email);
        return ResponseEntity.ok(Map.of("user", deletedUsr.getEmail(),
                "status", "Deleted successfully!"));
    }

    @GetMapping(value = {"/user", "/user/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity usersInfo() {
        var users = adminService.getUsersList();
        return ResponseEntity.ok(users);
    }
}
