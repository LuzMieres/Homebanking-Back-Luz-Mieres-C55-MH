package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.services.AuthService;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.servicesSecurity.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private AuthService authService;

    // Endpoint para iniciar sesión y generar un token JWT.
    // AuthController.java
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.login(loginDTO);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            // Asegúrate de devolver un mensaje con la respuesta de error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            // Maneja otros errores y devuelve un mensaje de error adecuado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }


    // Endpoint para registrar un nuevo cliente.
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        try {
            Client client = authService.register(registerDTO);
            return ResponseEntity.ok("Registration successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // El objeto Authentication proporciona información sobre el usuario actualmente autenticado, como su nombre de usuario, roles, y otros atributos.
    @GetMapping("/current")
    public ResponseEntity<?> getClient(Authentication authentication) {
        // Retorna los detalles del cliente en la respuesta.
        return ResponseEntity.ok(new ClientDTO(clientRepository.findByEmail(authentication.getName())));
    }
}
