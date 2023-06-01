package account.controllers;

import account.DTO.RoleDTO;
import account.exceptions.ApiError;
import account.exceptions.RoleNotFoundException;
import account.models.User;
import account.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

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

    @DeleteMapping(value = {"/user/{email}", "/user", "/user/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity deleteUser(@PathVariable String email) {
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleRoleNotFound(
            RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Role not found!";
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ApiError error = new ApiError(status, message, path);
        return ResponseEntity.status(status).body(error);
    }
}
