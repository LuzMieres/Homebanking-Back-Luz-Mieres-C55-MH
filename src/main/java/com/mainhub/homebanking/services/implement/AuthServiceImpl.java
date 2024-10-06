package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.CardRepository;
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
import java.util.regex.Pattern;

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

    @Autowired
    private CardRepository cardRepository;

    // Ajustes en los patrones de validación
    private final Pattern namePattern = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúñÑ][A-Za-zÁÉÍÓÚáéíóúñÑ ]*$"); // No permite que empiece con espacio
    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$"); // No permite que empiece con espacio
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,}$");

    @Override
    public String login(LoginDTO loginDTO) {
        // Validaciones de email y contraseña
        validateEmail(loginDTO.email());
        validatePassword(loginDTO.password());

        // Verificar si el cliente existe
        Client existingClient = clientService.findByEmail(loginDTO.email());
        if (existingClient == null) {
            throw new IllegalArgumentException("Email not registered");
        }

        // Intentar autenticación
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect password"); // Mensaje específico para contraseña incorrecta
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());
        return jwtUtilService.generateToken(userDetails);
    }

    @Override
    public Client register(RegisterDTO registerDTO) {
        // Validar todos los campos
        validateName(registerDTO.firstName(), "First name");
        validateName(registerDTO.lastName(), "Last name");
        validateEmail(registerDTO.email());
        validatePassword(registerDTO.password());

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

        // Crear y asociar la tarjeta de débito SILVER
        Card debitCardSilver = new Card();
        debitCardSilver.setClient(client); // Asociar la tarjeta al cliente
        debitCardSilver.setType(CardType.DEBIT);
        debitCardSilver.setColor(CardColor.SILVER);
        debitCardSilver.setNumber(debitCardSilver.generateCardNumber()); // Genera el número de tarjeta
        debitCardSilver.setCvv(debitCardSilver.generateCVV());
        debitCardSilver.setCardHolder(client.getFirstName() + " " + client.getLastName());// Genera el CVV
        debitCardSilver.setFromDate(LocalDate.now());
        debitCardSilver.setThruDate(LocalDate.now().plusYears(5)); // Por ejemplo, válida por 5 años
        // Guarda la tarjeta en el repositorio
        cardRepository.save(debitCardSilver);

        // Generar la cuenta para el nuevo cliente
        Account account = new Account();
        account.setClient(client);
        account.setNumber(utilMetod.generateAccountNumber());
        account.setCreationDate(LocalDate.now());
        account.addCard(debitCardSilver);
        accountRepository.save(account);

        clientRepository.save(client);

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

    // Métodos de validación
    private void validateName(String name, String fieldName) {
        if (name == null || name.trim().isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty or start with a space");
        }
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException(fieldName + " is not valid, only letters are allowed");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty or start with a space");
        }
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format, must contain @ and .com");
        }
    }

    private void validatePassword(String password) {
        if (!passwordPattern.matcher(password).matches()) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain one uppercase letter, one lowercase letter, one number, and one special character.");
        }
    }
}
