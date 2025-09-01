package com.example.salessystem.service;

import com.example.salessystem.dto.ClientDto;

import java.util.List;

public interface ClientService {
    List<ClientDto> list();
    ClientDto create(ClientDto dto);
    ClientDto update(Long id, ClientDto dto);
}
