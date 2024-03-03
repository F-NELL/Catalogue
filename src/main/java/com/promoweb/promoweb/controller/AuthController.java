package com.promoweb.promoweb.controller;

import com.promoweb.promoweb.security.JWTGenerator;
import com.promoweb.promoweb.model.*;
import com.promoweb.promoweb.repository.RoleRepository;
import com.promoweb.promoweb.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Mercadona-auth", description = "Les authentifications Mercadona")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    // Authentifier un utilisateur
    @PostMapping("/login")
    @Operation(operationId = "login", summary = "login (Authentifier un utilisateur)")
    public ResponseEntity<AuthResponse> login(@RequestBody Logins logins){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logins.getUsername(),
                        logins.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    // Enregistrer de nouveaux utilisateurs, cette fonctionnalité n'est pas à disposition des utilisateurs mais pourrait le devenir s'il y a une demande
    @Operation(operationId = "register", summary = "register (Enregistrement de nouveaux utilisateurs, cette fonctionnalité n'est pas à disposition des utilisateurs mais pourrait le devenir s'il y a une demande)")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Register register) {
        if (userRepository.existsByUsername(register.getUsername())) {
            return new ResponseEntity<>("Oups, il faut trouvé un identifiant plus original, celui-ci est déjà pris", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(register.getUsername());
        user.setPassword(passwordEncoder.encode((register.getPassword())));

        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);

        return new ResponseEntity<>("Nouvel utilisateur enregistré!", HttpStatus.OK);
    }
}