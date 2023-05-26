package antifraud.web;

import antifraud.DTO.OperationDTO;
import antifraud.DTO.RoleDTO;
import antifraud.DTO.UserDTO;
import antifraud.model.User;
import antifraud.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_SUPPORT') or hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity listUsers() {
        var users = authService.listUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity createUser(@Valid @RequestBody User user) throws URISyntaxException {
        try {
            UserDTO createdUser = authService.createUser(user);
            return ResponseEntity.created(new URI("/api/auth/" + createdUser.getUsername()))
                    .body(createdUser);
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity deleteUser(@PathVariable String username) {
        System.out.println("initializing");
        try {
            authService.deleteUser(username);
            return ResponseEntity.ok(Map.of("username", username,
                    "status", "Deleted successfully!"));
        } catch (ResponseStatusException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/access")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity access(@RequestBody OperationDTO operationDTO) {
        try {
            UserDTO user = authService.changeLocking(operationDTO.getUsername(),
                    operationDTO.getOperation());
            return ResponseEntity.ok(Map.of("status", "User %s %s!"
                    .formatted(user.getUsername(),
                            operationDTO.getOperation().equals("LOCK") ? "locked" : "unlocked")));
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity role(@RequestBody RoleDTO roleDTO) {
        try {
            UserDTO user = authService.changeRole(roleDTO.getUsername(), roleDTO.getRole());
            return ResponseEntity.ok(Map.of("id", user.getId(),
                    "name", user.getName(),
                    "username", user.getUsername(),
                    "role", user.getRole()));
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }

//    @RequestMapping(value = {"/user", "/user/"}, method = RequestMethod.DELETE)
//    public ResponseEntity dummyDelete() {
//        return new ResponseEntity(HttpStatus.FORBIDDEN);
//    }
//
//    @RequestMapping(value = {"/access/"}, method = RequestMethod.PUT)
//    public ResponseEntity dummyAccess() {
//        return new ResponseEntity(HttpStatus.FORBIDDEN);
//    }
//
//    @RequestMapping(value = {"/role/"}, method = RequestMethod.PUT)
//    public ResponseEntity dummyRole() {
//        return new ResponseEntity(HttpStatus.FORBIDDEN);
//    }
}
