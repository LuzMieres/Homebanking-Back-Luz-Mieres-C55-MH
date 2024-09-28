package com.mainhub.homebanking.servicesSecurity;

import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientServices clientService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientService.findByEmail(username);
        if (client == null){
            throw new UsernameNotFoundException(username);
        }
        if (client.getEmail().contains("admin")){
            return User
                    .withUsername(username)
                    .password(client.getPassword())
                    .roles("ADMIN")
                    .build();
        }
        return User
                .withUsername(username)
                .password(client.getPassword())
                .roles("CLIENT")
                .build();
    }
}