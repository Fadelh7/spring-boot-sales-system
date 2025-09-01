package com.example.salessystem.service.impl;

import com.example.salessystem.domain.Client;
import com.example.salessystem.dto.ClientDto;
import com.example.salessystem.exception.NotFoundException;
import com.example.salessystem.mapper.Mappers;
import com.example.salessystem.repository.ClientRepository;
import com.example.salessystem.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public List<ClientDto> list() {
        return clientRepository.findAll().stream().map(Mappers::toDto).collect(Collectors.toList());
    }

    @Override
    public ClientDto create(ClientDto dto) {
        Client c = Mappers.toEntity(dto);
        c.setId(null);
        return Mappers.toDto(clientRepository.save(c));
    }

    @Override
    public ClientDto update(Long id, ClientDto dto) {
        Client c = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
        c.setFirstName(dto.getFirstName());
        c.setLastName(dto.getLastName());
        c.setMobile(dto.getMobile());
        return Mappers.toDto(clientRepository.save(c));
    }
}
