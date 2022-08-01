package com.ing.kata.services;

import com.ing.kata.Entity.Client;
import com.ing.kata.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;


    public  Client save (Client client){
        return clientRepository.save(client);
    }






}
