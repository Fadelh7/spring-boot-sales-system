package com.example.salessystem.service;

import com.example.salessystem.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> list();
    ProductDto create(ProductDto dto);
    ProductDto update(Long id, ProductDto dto);
}
