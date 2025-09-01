package com.example.salessystem.service;

import com.example.salessystem.domain.Product;
import com.example.salessystem.dto.ProductDto;
import com.example.salessystem.repository.ProductRepository;
import com.example.salessystem.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    private ProductRepository repo;
    private ProductService service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(ProductRepository.class);
        service = new ProductServiceImpl(repo);
    }

    @Test
    void create_saves_and_returns_dto() {
        ProductDto dto = new ProductDto();
        dto.setName("Phone");
        dto.setDescription("Smartphone");
        dto.setCategory("Electronics");

        when(repo.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProductDto out = service.create(dto);
        assertThat(out.getId()).isEqualTo(1L);
        assertThat(out.getName()).isEqualTo("Phone");
    }
}
