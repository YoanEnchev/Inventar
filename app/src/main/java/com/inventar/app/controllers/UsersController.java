package com.inventar.app.controllers;

import com.inventar.app.models.User;
import com.inventar.app.repositories.RoleRepository;
import com.inventar.app.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.List;

// Don't bother returning view.
// Return data instead.
@RestController
public class UsersController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UsersController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/admin/create-mol")
    public ResponseEntity createMol(
            @RequestParam(required = true, name = "username") String username,
            @RequestParam(required = true, name = "password") String password
    ) {

        try {
            List<User> users = this.userRepository.findByUsername(username);

            if (!users.isEmpty()) {
                throw new Exception("User already exists");
            }

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            // For now the only users that are created have role with id = 2.
            User user = new User(username, bCryptPasswordEncoder.encode(password), this.roleRepository.getById(2L));
            this.userRepository.save(user);

            return ResponseEntity.ok("ok");
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(
            @RequestParam(required = true, name = "username") String username,
            @RequestParam(required = true, name = "password") String password
    ) {
        try {
            List<User> users = this.userRepository.findByUsername(username);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if(users.isEmpty()) {
                throw new Exception("Inexisting user.");
            }

            User user = users.get(0);

            // If username does not exist or password mismatch.
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new Exception("Invalid credentials.");
            }

            return ResponseEntity.ok(new JSONObject()
                    .put("role", user.getRole().getName())
                    .put("username", user.getUsername())
                    .put("api-auth-token", passwordEncoder.encode(user.getId() + ""))
                    .toString()
                );
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
