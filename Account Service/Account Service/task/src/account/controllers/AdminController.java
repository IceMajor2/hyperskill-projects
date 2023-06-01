package account.controllers;

import account.DTO.RoleDTO;
import account.DTO.UserActionDTO;
import account.exceptions.ApiError;
import account.models.User;
import account.services.AdminService;
import account.services.SecurityLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private SecurityLogService securityLogService;

    @PutMapping(value = {"/user/role", "/user/role/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity changeUserRole(@RequestBody @Valid RoleDTO roleDTO) {
        User user = adminService.changeRole(roleDTO);
        securityLogService.saveRoleChangedLog(user, roleDTO);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(value = {"/user/{email}", "/user", "/user/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity deleteUser(@AuthenticationPrincipal UserDetails details,
                                     @PathVariable String email) {
        User deletedUsr = adminService.deleteUser(email);
        securityLogService.saveAccountDeletedLog(details, deletedUsr);
        return ResponseEntity.ok(Map.of("user", deletedUsr.getEmail(),
                "status", "Deleted successfully!"));
    }

    @GetMapping(value = {"/user", "/user/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity usersInfo() {
        var users = adminService.getUsersList();
        return ResponseEntity.ok(users);
    }

    @PutMapping(value = {"/user/access", "/user/access/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity lockUnlockUser(@AuthenticationPrincipal UserDetails details,
                                         @RequestBody @Valid UserActionDTO userActionDTO) {
        User user = adminService.lockUnlockUser(userActionDTO);
        securityLogService.saveAccountLockLog(details, user, userActionDTO);
        return ResponseEntity.ok(Map.of("status", "User %s %s!"
                .formatted(user.getEmail(),
                        userActionDTO.getOperation().toString().toLowerCase() + "ed")));
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
