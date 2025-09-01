package com.example.salessystem.integration;

import com.example.salessystem.domain.Client;
import com.example.salessystem.domain.Product;
import com.example.salessystem.dto.SaleDtos;
import com.example.salessystem.dto.SaleTransactionDto;
import com.example.salessystem.repository.ClientRepository;
import com.example.salessystem.repository.ProductRepository;
import com.example.salessystem.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SalesFlowIT {

    @Autowired
    private SaleService saleService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

    private Client client;
    private Product product;

    @BeforeEach
    void init() {
        client = clientRepository.save(Client.builder().firstName("John").lastName("Doe").mobile("123").build());
        product = productRepository.save(Product.builder().name("Pen").category("Stationery").description("Blue pen").build());
    }

    @Test
    @Transactional
    void create_sale_and_update() {
        SaleTransactionDto t = new SaleTransactionDto();
        t.setProductId(product.getId());
        t.setQuantity(2);
        t.setPrice(new BigDecimal("1.50"));

        SaleDtos.SaleCreateRequest req = new SaleDtos.SaleCreateRequest();
        req.setClientId(client.getId());
        req.setSeller("Alice");
        req.setTransactions(List.of(t));

        var created = saleService.create(req);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getTotal()).isEqualByComparingTo("3.00");

        // update
        t.setQuantity(3);
        SaleDtos.SaleUpdateRequest up = new SaleDtos.SaleUpdateRequest();
        up.setSeller("Bob");
        up.setTransactions(List.of(t));

        var updated = saleService.update(created.getId(), up);
        assertThat(updated.getTotal()).isEqualByComparingTo("4.50");
        assertThat(updated.getSeller()).isEqualTo("Bob");
    }
}
