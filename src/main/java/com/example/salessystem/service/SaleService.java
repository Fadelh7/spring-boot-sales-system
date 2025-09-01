package com.example.salessystem.service;

import com.example.salessystem.dto.SaleDtos;

import java.util.List;

public interface SaleService {
    List<SaleDtos.SaleResponse> list();
    SaleDtos.SaleResponse create(SaleDtos.SaleCreateRequest request);
    SaleDtos.SaleResponse update(Long id, SaleDtos.SaleUpdateRequest request);
}
