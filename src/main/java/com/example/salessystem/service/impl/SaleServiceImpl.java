package com.example.salessystem.service.impl;

import com.example.salessystem.domain.*;
import com.example.salessystem.dto.SaleDtos;
import com.example.salessystem.dto.SaleTransactionDto;
import com.example.salessystem.exception.NotFoundException;
import com.example.salessystem.mapper.Mappers;
import com.example.salessystem.repository.*;
import com.example.salessystem.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import com.example.salessystem.aspect.SaleTransactionUpdateLogger.SaleUpdatedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    public List<SaleDtos.SaleResponse> list() {
        return saleRepository.findAll().stream().map(Mappers::toSaleResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SaleDtos.SaleResponse create(SaleDtos.SaleCreateRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Map<Long, Product> productMap = productRepository.findAllById(
                        request.getTransactions().stream().map(SaleTransactionDto::getProductId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        Sale sale = new Sale();
        sale.setClient(client);
        sale.setSeller(request.getSeller());
        sale.setTransactions(new ArrayList<>());

        BigDecimal total = BigDecimal.ZERO;
        for (SaleTransactionDto t : request.getTransactions()) {
            Product product = productMap.get(t.getProductId());
            if (product == null) throw new NotFoundException("Product not found: " + t.getProductId());
            SaleTransaction st = SaleTransaction.builder()
                    .product(product)
                    .quantity(t.getQuantity())
                    .price(t.getPrice())
                    .sale(sale)
                    .build();
            sale.getTransactions().add(st);
            total = total.add(t.getPrice().multiply(BigDecimal.valueOf(t.getQuantity())));
        }
        sale.setTotal(total);

    Sale saved = saleRepository.save(sale);
        log.info("Created sale {} with {} transactions, total {}", saved.getId(), saved.getTransactions().size(), saved.getTotal());
    publisher.publishEvent(new SaleUpdatedEvent(saved.getId(), saved.getTransactions().size(), saved.getSeller()));
        return Mappers.toSaleResponse(saved);
    }

    @Override
    @Transactional
    public SaleDtos.SaleResponse update(Long id, SaleDtos.SaleUpdateRequest request) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new NotFoundException("Sale not found"));

        Map<Long, Product> productMap = productRepository.findAllById(
                        request.getTransactions().stream().map(SaleTransactionDto::getProductId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        // Replace transactions (simple approach) and recalc total
        sale.getTransactions().clear();
        BigDecimal total = BigDecimal.ZERO;
        for (SaleTransactionDto t : request.getTransactions()) {
            Product product = productMap.get(t.getProductId());
            if (product == null) throw new NotFoundException("Product not found: " + t.getProductId());

            SaleTransaction st = SaleTransaction.builder()
                    .product(product)
                    .quantity(t.getQuantity())
                    .price(t.getPrice())
                    .sale(sale)
                    .build();
            sale.getTransactions().add(st);
            total = total.add(t.getPrice().multiply(BigDecimal.valueOf(t.getQuantity())));
        }
        sale.setSeller(request.getSeller());
        sale.setTotal(total);

    Sale saved = saleRepository.save(sale);
        log.info("Updated sale {}: {} transactions, total {}", saved.getId(), saved.getTransactions().size(), saved.getTotal());
    publisher.publishEvent(new SaleUpdatedEvent(saved.getId(), saved.getTransactions().size(), saved.getSeller()));
        return Mappers.toSaleResponse(saved);
    }
}
