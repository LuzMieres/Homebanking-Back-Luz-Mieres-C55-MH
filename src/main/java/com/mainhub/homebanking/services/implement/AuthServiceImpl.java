package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.AuthService;
import com.mainhub.homebanking.services.ClientServices;
import com.mainhub.homebanking.servicesSecurity.JwtUtilService;
import com.mainhub.homebanking.utils.UtilMetod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthServiceImpl implements AuthService {

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
    private UtilMetod utilMetod;

    @Autowired
    private ClientServices clientService;

    @Override
    public String login(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());
        return jwtUtilService.generateToken(userDetails);
    }

    @Override
    public Client register(RegisterDTO registerDTO) {
        // Verificar si el cliente ya existe
        Client existingClient = clientService.findByEmail(registerDTO.email());
        if (existingClient != null) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Crear nuevo cliente
        Client client = new Client(
                registerDTO.firstName(),
                registerDTO.lastName(),
                registerDTO.email(),
                passwordEncoder.encode(registerDTO.password()));

        clientRepository.save(client);

        // Generar la cuenta para el nuevo cliente
        Account account = new Account();
        account.setClient(client);
        account.setNumber(utilMetod.generateAccountNumber());
        account.setCreationDate(LocalDate.now());
        accountRepository.save(account);

        return client;
    }

    @Override
    public ClientDTO getCurrentClient(String email) {
        Client client = clientService.findByEmail(email);
        if (client == null) {
            throw new IllegalArgumentException("Client with email " + email + " not found");
        }
        return new ClientDTO(client);
    }
}