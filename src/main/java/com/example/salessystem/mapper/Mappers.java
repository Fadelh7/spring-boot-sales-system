package com.example.salessystem.mapper;

import com.example.salessystem.domain.*;
import com.example.salessystem.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Mappers {
    private Mappers() {}

    public static ProductDto toDto(Product p) {
        if (p == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setCategory(p.getCategory());
        return dto;
    }

    public static Product toEntity(ProductDto dto) {
        if (dto == null) return null;
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .build();
    }

    public static ClientDto toDto(Client c) {
        if (c == null) return null;
        ClientDto dto = new ClientDto();
        dto.setId(c.getId());
        dto.setFirstName(c.getFirstName());
        dto.setLastName(c.getLastName());
        dto.setMobile(c.getMobile());
        return dto;
    }

    public static Client toEntity(ClientDto dto) {
        if (dto == null) return null;
        return Client.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .mobile(dto.getMobile())
                .build();
    }

    public static SaleDtos.SaleResponse toSaleResponse(Sale sale) {
        if (sale == null) return null;
        SaleDtos.SaleResponse res = new SaleDtos.SaleResponse();
        res.setId(sale.getId());
        res.setCreationDate(sale.getCreationDate());
        res.setClientId(sale.getClient() != null ? sale.getClient().getId() : null);
        res.setSeller(sale.getSeller());
        res.setTotal(sale.getTotal());
        res.setTransactions(toTransactionResponses(sale.getTransactions()));
        return res;
    }

    private static List<SaleDtos.SaleTransactionResponse> toTransactionResponses(List<SaleTransaction> txs) {
        if (txs == null) return List.of();
        return txs.stream().map(Mappers::toTransactionResponse).collect(Collectors.toList());
    }

    private static SaleDtos.SaleTransactionResponse toTransactionResponse(SaleTransaction t) {
        SaleDtos.SaleTransactionResponse r = new SaleDtos.SaleTransactionResponse();
        r.setId(t.getId());
        r.setProductId(t.getProduct() != null ? t.getProduct().getId() : null);
        r.setQuantity(t.getQuantity());
        r.setPrice(t.getPrice());
        r.setLineTotal(t.getPrice() != null && t.getQuantity() != null ? t.getPrice().multiply(BigDecimal.valueOf(t.getQuantity())) : BigDecimal.ZERO);
        return r;
    }
}
