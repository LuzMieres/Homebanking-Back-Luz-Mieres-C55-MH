package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import com.mainhub.homebanking.models.Client;

public interface AuthService {
    String login(LoginDTO loginDTO);
    Client register(RegisterDTO registerDTO);
    ClientDTO getCurrentClient(String email);
}