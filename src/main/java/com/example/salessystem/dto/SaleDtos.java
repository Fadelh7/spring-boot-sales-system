package com.example.salessystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class SaleDtos {
    private SaleDtos() {}

    @Data
    public static class SaleCreateRequest {
        @NotNull
        private Long clientId;
        @NotBlank
        @Size(max = 100)
        private String seller;
        @Valid
        @NotNull
        private List<SaleTransactionDto> transactions;
    }

    @Data
    public static class SaleUpdateRequest {
        @Valid
        @NotNull
        private List<SaleTransactionDto> transactions;
        @NotBlank
        @Size(max = 100)
        private String seller;
    }

    @Data
    public static class SaleResponse {
        @Schema(readOnly = true)
        private Long id;
        private Instant creationDate;
        private Long clientId;
        private String seller;
        private BigDecimal total;
        private List<SaleTransactionResponse> transactions;
    }

    @Data
    public static class SaleTransactionResponse {
        private Long id;
        private Long productId;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal lineTotal;
    }
}
