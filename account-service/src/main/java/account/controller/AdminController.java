package account.controller;

import account.dto.RoleDTO;
import account.dto.UserActionDTO;
import account.exception.ApiError;
import account.model.User;
import account.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping(value = {"/user/role", "/user/role/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public User changeUserRole(@AuthenticationPrincipal UserDetails details,
                               @RequestBody @Valid RoleDTO roleDTO) {
        return adminService.changeRole(details, roleDTO);
    }

    @DeleteMapping(value = {"/user/{email}", "/user", "/user/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public Map<String, String> deleteUser(@AuthenticationPrincipal UserDetails details,
                                          @PathVariable String email) {
        User deletedUsr = adminService.deleteUser(details, email);
        return Map.of("user", deletedUsr.getEmail(), "status", "Deleted successfully!");
    }

    @GetMapping(value = {"/user", "/user/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public List<User> usersInfo() {
        return adminService.getUsersList();
    }

    @PutMapping(value = {"/user/access", "/user/access/"})
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public Map<String, String> lockUnlockUser(@AuthenticationPrincipal UserDetails details,
                                              @RequestBody @Valid UserActionDTO userActionDTO) {
        User user = adminService.lockUnlockUser(details, userActionDTO);
        return Map.of("status", "User %s %s!".formatted(user.getEmail(),
                userActionDTO.getOperation().toString().toLowerCase() + "ed"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleRoleNotFound(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Role not found!";
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ApiError error = new ApiError(status, message, path);
        return ResponseEntity.status(status).body(error);
    }
}
