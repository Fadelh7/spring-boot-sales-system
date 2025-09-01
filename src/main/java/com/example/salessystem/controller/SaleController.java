package com.example.salessystem.controller;

import com.example.salessystem.dto.SaleDtos;
import com.example.salessystem.service.SaleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@Tag(name = "Sales")
public class SaleController {
    private final SaleService saleService;

    @GetMapping
    public List<SaleDtos.SaleResponse> list() {
        return saleService.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDtos.SaleResponse create(@Valid @RequestBody SaleDtos.SaleCreateRequest request) {
        return saleService.create(request);
    }

    @PutMapping("/{id}")
    public SaleDtos.SaleResponse update(@PathVariable Long id, @Valid @RequestBody SaleDtos.SaleUpdateRequest request) {
        return saleService.update(id, request);
    }
}
