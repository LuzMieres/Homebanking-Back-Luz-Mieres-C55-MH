package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.models.Client;

import java.util.List;

public interface ClientServices {
    List<Client> getAllClient();
    List<ClientDTO> getAllClientDTO();
    Client getClientById(Long id);
    ClientDTO getClientDTO(Client client);
    Client findByEmail(String email);
}