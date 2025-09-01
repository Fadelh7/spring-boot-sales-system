package com.example.salessystem.service.impl;

import com.example.salessystem.domain.Product;
import com.example.salessystem.dto.ProductDto;
import com.example.salessystem.exception.NotFoundException;
import com.example.salessystem.mapper.Mappers;
import com.example.salessystem.repository.ProductRepository;
import com.example.salessystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> list() {
        return productRepository.findAll().stream().map(Mappers::toDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto create(ProductDto dto) {
        Product p = Mappers.toEntity(dto);
        p.setId(null);
        return Mappers.toDto(productRepository.save(p));
    }

    @Override
    public ProductDto update(Long id, ProductDto dto) {
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setCategory(dto.getCategory());
        return Mappers.toDto(productRepository.save(p));
    }
}
